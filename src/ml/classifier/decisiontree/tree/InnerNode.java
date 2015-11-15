package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Attribute;
import ml.classifier.decisiontree.instance.Dataset;
import ml.utils.Pair;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models an InnerNode
 * An InnerNode is a Node that is not a terminal one (which means it splits the dataset for a chosen Attribute)
 * It contains the decisions for the chosen split attribute
 * A decision is a pair Attribute Node; an Attribute splits the data by its value, creating a new node (which is either a terminal one, or an inner one, continuing the split)
 * Created by virgil on 29.10.2015.
 */
public class InnerNode extends Node {

    private List<Pair<Attribute, Node>> decisions;

    /**
     * The InnerNode default constructor
     */
    public InnerNode() {
        this.setLabel("");
        this.setDataset(null);
        this.decisions = new ArrayList<>();
    }

    /**
     * The InnerNode constructor
     * @param label the label of the node
     */
    public InnerNode(String label) {
        this.setLabel(label);
        this.setDataset(null);
        this.decisions = new ArrayList<>();
    }

    /**
     * Get the list of decisions for this node
     * @return the list of decisions
     */
    public List<Pair<Attribute, Node>> getDecisions() {
        return this.decisions;
    }

    /**
     * Set the list of decisions
     * @param decisions the new list of decisions for the node
     */
    public void setDecisions(List<Pair<Attribute, Node>> decisions) {
        this.decisions = decisions;
    }

    /**
     * Add a new decision to the decisions list
     * @param decision the new decision to be added to the list
     */
    public void addDecision(Pair<Attribute, Node> decision) {
        this.decisions.add(decision);
    }

    /**
     * Add a new decision to the decisions list
     * @param attribute the attribute to be added
     * @param node the node
     */
    public void addDecision(Attribute attribute, Node node) {
        this.decisions.add(new Pair<Attribute, Node>(attribute, node));
    }

    /**
     * Show the current node with an indent
     * @param indent the indent
     * @param nodeLevel the node level
     */
    @Override
    public void showNode(String indent, int nodeLevel) {
        //System.out.println(indent + "|- " + this.getLabel() + " " + "inner");

        for (Pair<Attribute, Node> decision : decisions) {
            Node node = decision.getSecond();
            System.out.println(indent + nodeLevel + ") " + decision.getFirst().getAttributeName() + "=" + decision.getFirst().getAttributeValue() + "  entropy = " + decision.getSecond().getPurityFunctionValue());
            node.showNode(indent + "  ", nodeLevel + 1);
        }
    }

    @Override
    public String toString() {
        String returnValue = "InnerNode:\nDecisions:\n";

        for (Pair<Attribute, Node> decision : decisions) {
            returnValue += decision.getFirst().getAttributeName() +
                    "=" + decision.getFirst().getAttributeValue() +
                    "  entropy = " + decision.getSecond().getPurityFunctionValue() + " ";
        }

        return returnValue;
    }
}
