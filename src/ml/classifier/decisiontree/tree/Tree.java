package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Instance;
import ml.classifier.decisiontree.purityfunction.PurityFunction;

/**
 * This class models a Tree
 * A tree is given by its root node
 * Once the tree is created, it can be used to evaluate a test instance
 * Created by virgil on 29.10.2015.
 */
public abstract class Tree {

    private Node root;
    private PurityFunction purityFunction;

    /**
     * Evaluate the new instance using this tree
     * @param observation the new observation
     * @return the value of the evaluation (the predicted class)
     */
    abstract public String evaluate(Instance observation);

    /**
     * Show the tree
     */
    abstract public void showTree();

    /**
     * Get the root of the Tree
     * @return the root of the Tree
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Set the root of the Tree
     * @param root the new root of the Tree
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Get the purity function used by the tree to select the best attribute for a node
     * @return the purity function
     */
    public PurityFunction getPurityFunction() {
        return purityFunction;
    }

    /**
     * Set the purity function used by the tree to select the best attribute for a node
     * @param purityFunction the new purity function
     */
    public void setPurityFunction(PurityFunction purityFunction) {
        this.purityFunction = purityFunction;
    }
}
