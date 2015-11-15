package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Attribute;
import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Discretizer;
import ml.classifier.decisiontree.instance.Instance;
import ml.classifier.decisiontree.purityfunction.PurityFunction;
import ml.utils.Pair;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * This class models an Extended ID3 decision tree
 * It extends the Tree class
 * Created by virgil on 05.11.2015.
 */
public class ExtendedID3 extends Tree {

    private List<String> outputClasses;

    /**
     * The ID3 Constructor
     * @param dataset the dataset from which the tree is created
     * @param outcomeAttributeName the outcome attribute name
     * @param purityFunction the purity function used to separate the values
     */
    public ExtendedID3(Dataset dataset, String outcomeAttributeName, PurityFunction purityFunction) {
        setPurityFunction(purityFunction);
        createTree(dataset, outcomeAttributeName);
    }

    /**
     * The ID3 Constructor
     * @param dataset the dataset from which the tree is created
     * @param outcomeAttributeName the outcome attribute name
     * @param purityFunction the purity function used to separate the values
     */
    public ExtendedID3(Dataset dataset, String outcomeAttributeName, PurityFunction purityFunction, List<String> outputClasses) {
        setPurityFunction(purityFunction);
        this.outputClasses = outputClasses;
        createTree(dataset, outcomeAttributeName);
    }

    /**
     * Create the tree knowing the dataset and the outcome attribute
     * @param dataset the set of observations
     * @param outcomeAttributeName the outcome attribute
     */
    private void createTree(Dataset dataset, String outcomeAttributeName) {
        Node root = createNode(dataset, outcomeAttributeName);
        this.setRoot(root);
    }

    /**
     * Create a new node in the tree
     * @param dataset the simplified dataset (with respect to the attribute)
     * @param labelName the label
     * @return the new node
     */
    private Node createNode(Dataset dataset, String labelName){
        Node node;

        //System.out.println(labelName + " " + dataset.toString());

        Double minimumEntropy = Double.MAX_VALUE;
        String attributeName = "";
        boolean foundAttribute = false;
        boolean singleLabel = false;

        //Discretize the dataset
        Discretizer discretizer = new Discretizer(dataset, dataset.getContinuousValuedAttributes());
        //TODO: Find another way such that not only binary classification is possible
        Dataset discretizedDataset = discretizer.discretize(this.outputClasses);

        Instance instance = discretizedDataset.getObservations().get(0); //this is just to gain access to the list of attributes
        for (Attribute attribute : instance.getAttributes()) {
            if (labelName.equals(attribute.getAttributeName()))
                continue;
            ConfusionMatrix confusionMatrix = new ConfusionMatrix(discretizedDataset, attribute.getAttributeName(), labelName);
            if (confusionMatrix.isUseless())
                continue;
            Double currentEntropy = getPurityFunction().calculate(confusionMatrix);
            foundAttribute = true;
            if (currentEntropy < minimumEntropy) {
                minimumEntropy = currentEntropy;
                attributeName = attribute.getAttributeName();
                singleLabel = confusionMatrix.isSameLabel();
            }
        }
        if (foundAttribute == false || singleLabel == true) {
            //Get the value that has the biggest count for the attribute
            //TODO: Get also the value of the split threshold in order to update the label to something like: Attribute < threshold (Edits needed in Discretizer)
            String label = discretizedDataset.getMajorityValueForAttribute(labelName);
            node = new TerminalNode(label);
            node.setDataset(dataset);
            node.setPurityFunctionValue(minimumEntropy);
        }
        else {
            //Hard Pre-pruning done in order to have a small tree
            if (minimumEntropy < .10) {

                node = new InnerNode();
                //Get all the possible values for the attribute and create decisions (new Nodes)
                List<String> allAttributeValues = discretizedDataset.getAllDistinctValuesForAttribute(attributeName);

                for (String attributeValue : allAttributeValues) {
                    Attribute attribute = new Attribute(attributeName, attributeValue);

                    //We need to split the data to select only those instances that have the attribute
                    //Split by discretizedDataset, but send the original database to the next node such that it will chose its best split point in the continuous data
                    Dataset splitDataset = Dataset.splitDiscretizedDatasetByAttribute(discretizedDataset, dataset, attribute);

                    Node decisionNode = createNode(splitDataset, labelName);

                    ((InnerNode) node).addDecision(new Pair<Attribute, Node>(attribute, decisionNode));
                }
                node.setLabel(attributeName);
                node.setDataset(dataset);
                node.setPurityFunctionValue(minimumEntropy);
            }
            else {
                //Get the value that has the biggest count for the attribute
                //TODO: Get also the value of the split threshold in order to update the label to something like: Attribute < threshold (Edits needed in Discretizer)
                String label = discretizedDataset.getMajorityValueForAttribute(labelName);
                node = new TerminalNode(label);
                node.setDataset(dataset);
                node.setPurityFunctionValue(minimumEntropy);
            }
        }

        return node;
    }

    /**
     * Evaluate the new instance using this tree
     * @param observation the new observation
     * @return the value of the evaluation (the predicted class)
     */
    @Override
    public String evaluate(Instance observation) {
        System.out.println("ExtendedID3.evaluate");
        Node node = getRoot();
        while( node.isTerminal() == false ) {
            //System.out.println("ExtendedID3.evaluate: node = " + node);
            for( Pair<Attribute, Node> pair: ((InnerNode)node).getDecisions()) {
                //System.out.println("ExtendedID3.evaluate: pair = " + pair);
                Attribute attribute = pair.getFirst();
                Attribute observationAttribute = observation.getAttributeByName( attribute.getAttributeName() );
                //TODO: This doesn't work in the continuous variable case
                if( attribute.equals( observationAttribute ) ) {
                    node = pair.getSecond();
                    break;
                }
            }
        }
        return node.getLabel();
    }

    /**
     * Show the tree
     */
    @Override
    public void showTree() {
        String indent = "";
        getRoot().showNode(indent, 1);
    }
}
