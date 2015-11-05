package ml.classifier.decisiontree.instance;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models an Instance of data
 * An instance of data represents a row in the dataset
 * A row in the dataset is actually an array of (attribute name, attribute value)
 * Created by virgil on 29.10.2015.
 */
public class Instance {

    private List<Attribute> attributes;

    /**
     * The Instance default constructor
     * Initializes the member variable
     */
    public Instance() {
        attributes = new ArrayList<>();
    }

    /**
     * The Instance constructor
     * @param attributes the list of attributes that give the current Instance
     */
    public Instance(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the list of attributes for this Instance
     * @return the list of attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Set the list of attributes for this Instance
     * @param attributes the new list of attributes
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Add a new Attribute to the list of attributes
     * @param attribute the new attribute to add
     */
    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    /**
     * Get the Attribute with the given attributeName
     * @param attributeName the name of the searched attribute
     * @return the attribute with this name, if it exists, null otherwise
     */
    public Attribute getAttributeByName(String attributeName) {
        Attribute result = null;

        for (Attribute attribute : attributes) {{
            if (attribute.getAttributeName().equals(attributeName)) {
                result = attribute;
                break;
            }
        }}

        return  result;
    }

    @Override
    public String toString() {
        String returnString = "Instance{ attributes=";

        for (Attribute attribute : attributes) {
            returnString += attribute.toString() + " ";
        }
        returnString += "}";

        return returnString;
    }
}
