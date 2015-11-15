package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Dataset;

/**
 * This class models a Node from the decision tree
 * Created by virgil on 29.10.2015.
 */
public abstract class Node {

    private String label;
    private Dataset dataset;
    private ConfusionMatrix confusionMatrix;

    private Double purityFunctionValue;

    /**
     * Get the label of this Node
     * @return the label of this Node
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Set the label for this Node
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the dataset for the current node
     * @return the dataset for the current node
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * Set the dataset for the current node
     * @param dataset the new dataset
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Get the node's confusionMatrix
     * @return the node's confusionMatrix
     */
    public ConfusionMatrix getConfusionMatrix() {
        return this.confusionMatrix;
    }

    /**
     * Set the node's confusionMatrix
     * @param confusionMatrix the new confusionMatrix of the node
     */
    public void setConfusionMatrix(ConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

    public Double getPurityFunctionValue() {
        return purityFunctionValue;
    }

    public void setPurityFunctionValue(Double purityFunctionValue) {
        this.purityFunctionValue = purityFunctionValue;
    }

    /**
     * Check if the Node is terminal
     * @return true if the Node is terminal, false otherwise
     */
    public boolean isTerminal() {
        //System.out.println("Node.isTerminal: " + this);
        if (this instanceof TerminalNode)
            return true;
        return false;
    }

    /**
     * Show the current node with an indent
     * @param indent the indent
     * @param nodeLevel the node level
     */
    abstract public void showNode(String indent, int nodeLevel);

}
