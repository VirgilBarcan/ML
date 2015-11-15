package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Attribute;
import ml.utils.Pair;

/**
 * This class models a TerminalNode
 * A TerminalNode is a Node that contains a decision
 * Created by virgil on 29.10.2015.
 */
public class TerminalNode extends Node {

    /**
     * The TerminalNode default constructor
     */
    public TerminalNode() {
        this.setLabel("");
        this.setDataset(null);
    }

    /**
     * The TerminalNode constructor
     * @param label
     */
    public TerminalNode(String label) {
        this.setLabel(label);
        this.setDataset(null);
    }

    /**
     * Show the current node with an indent
     * @param indent the indent
     * @param nodeLevel the node level
     */
    @Override
    public void showNode(String indent, int nodeLevel) {
        //System.out.println(indent + "|- " + this.getLabel() + " terminal");
        System.out.println(indent + nodeLevel + ") " + "Decision: " + this.getLabel() + "  entropy = " + this.getPurityFunctionValue() + " *");
    }

    @Override
    public String toString() {
        String returnValue = "Decision: " + this.getLabel() + "  entropy = " + this.getPurityFunctionValue() + " *";

        return returnValue;
    }
}
