package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Attribute;
import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Instance;
import ml.classifier.decisiontree.purityfunction.Entropy;
import ml.classifier.decisiontree.purityfunction.PurityFunction;
import ml.utils.Pair;

import java.util.List;

/**
 * This class models an ID3 decision tree
 * It extends the Tree class
 * Created by virgil on 30.10.2015.
 */
public class ID3 extends Tree {

    /**
     * The ID3 Constructor
     * @param dataset the dataset from which the tree is created
     * @param outcomeAttributeName the outcome attribute name
     */
    public ID3(Dataset dataset, String outcomeAttributeName) {
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

        Instance instance = dataset.getObservations().get(0); //this is just to gain access to the list of attributes
        for (Attribute attribute : instance.getAttributes()) {
            if (labelName.equals(attribute.getAttributeName()))
                continue;
            ConfusionMatrix confusionMatrix = new ConfusionMatrix(dataset, attribute.getAttributeName(), labelName);
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
            String label = dataset.getMajorityValueForAttribute(labelName);
            node = new TerminalNode(label);
            node.setDataset(dataset);
        }
        else {
            node = new InnerNode();
            //Get all the possible values for the attribute and create decisions (new Nodes)
            List<String> allAttributeValues = dataset.getAllDistinctValuesForAttribute(attributeName);

            for (String attributeValue : allAttributeValues) {
                Attribute attribute = new Attribute(attributeName, attributeValue);

                //We need to split the data to select only those instances that have the attribute
                Dataset splitDataset = Dataset.splitDatasetByAttribute(dataset, attribute);

                Node decisionNode = createNode(splitDataset, labelName);

                ((InnerNode)node).addDecision(new Pair<Attribute, Node>(attribute, decisionNode));
            }
            node.setLabel(attributeName);
            node.setDataset(dataset);
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
        return null;
    }

    /**
     * Show the tree
     */
    @Override
    public void showTree() {
        String indent = "";
        getRoot().showNode(indent);
    }
}
