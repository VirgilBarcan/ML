package ml.classifier.decisiontree.instance;

/**
 * This class models an Attribute
 * An Attribute is part of an Instance of the dataset
 * An Attribute has an attributeName and an attributeValue
 * Created by virgil on 29.10.2015.
 */
public class Attribute {

    private String attributeName;
    private String attributeValue;

    /**
     * The Attribute default constructor
     */
    public Attribute() {
        this.attributeName = "";
        this.attributeValue = "";
    }

    /**
     * The Attribute constructor
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     */
    public Attribute(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /**
     * Get the name of the attribute
     * @return the name of the attribute
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set the name of the attribute
     * @param attributeName the new name of the attribute
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Get the value of the attribute
     * @return the value of the attribute
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Set the value of the attribute
     * @param attributeValue the new value of the attribute
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * Check if an Attribute is equal with this one
     * @param other the other attribute
     * @return true if the attributes are equal, false otherwise
     */
    public boolean equals(Attribute other) {
        if (attributeName.equals(other.getAttributeName()) == false)
            return false;
        if (attributeValue.compareTo(other.getAttributeValue()) != 0)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = attributeName != null ? attributeName.hashCode() : 0;
        result = 31 * result + (attributeValue != null ? attributeValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }
}
