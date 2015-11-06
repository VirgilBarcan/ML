package ml.classifier.decisiontree.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class models a Dataset
 * A Dataset consists of a list of instances called observations
 * A Dataset has to know what is its outcome attribute
 * Created by virgil on 29.10.2015.
 */
public class Dataset {

    private List<Instance> observations;
    private String outcomeAttributeName;

    //TODO: Do something in order to not need this list of continuousValuedAttributes
    private List<String> continuousValuedAttributes;

    /**
     * The Dataset default constructor
     */
    public Dataset() {
        observations = new ArrayList<>();
        outcomeAttributeName = "";
    }

    /**
     * The Dataset constructor
     * @param observations the list of observations
     * @param outcomeAttributeName the outcome attribute name
     */
    public Dataset(List<Instance> observations, String outcomeAttributeName) {
        this.observations = observations;
        this.outcomeAttributeName = outcomeAttributeName;
    }

    /**
     * The Dataset constructor
     * Takes a dataset and creates a clone of it
     * @param datasetToCopy the dataset to be "cloned"
     */
    public Dataset(Dataset datasetToCopy) {
        this.observations = new ArrayList<>();

        for (Instance observation : datasetToCopy.getObservations()) {
            List<Attribute> attributes = new ArrayList<>();
            for (Attribute attribute : observation.getAttributes()) {
                attributes.add(new Attribute(new String(attribute.getAttributeName()), new String(attribute.getAttributeValue())));
            }
            this.observations.add(new Instance(attributes));
        }

        this.outcomeAttributeName = new String(datasetToCopy.getOutcomeAttributeName());
    }

    /**
     * Get the list of observations
     * @return the list of observations
     */
    public List<Instance> getObservations() {
        return observations;
    }

    /**
     * Set the list of observations
     * @param observations the new observations
     */
    public void setObservations(List<Instance> observations) {
        this.observations = observations;
    }

    /**
     * Set the outcome attribute name
     * @param outcomeAttributeName the new outcome attribute name
     */
    public void setOutcomeAttributeName(String outcomeAttributeName) {
        this.outcomeAttributeName = outcomeAttributeName;
    }

    /**
     * Get the outcome attribute name
     * @return the outcome attribute name
     */
    public String getOutcomeAttributeName() {
        return this.outcomeAttributeName;
    }

    /**
     * Add a new observation to the list of observations
     * @param observation the new observation to be added
     */
    public void addObservation(Instance observation) {
        this.observations.add(observation);
    }

    /**
     * Get the number of observations
     * @return the number of observations
     */
    public int getObservationsCount() {
        return this.observations.size();
    }

    /**
     * Get the value that has the biggest count from all values of the attribute named attributeName
     * @param attributeName the name of the attribute for which the search is done
     * @return the value that appears most times
     */
    public String getMajorityValueForAttribute(String attributeName) {
        String result = "";

        //Build a map to hold pairs value - count foreach value of the attribute
        Map<String, Integer> valueCounts = new HashMap<>();

        for (Instance observation : this.observations) {
            Attribute attribute = observation.getAttributeByName(attributeName);

            if (attribute != null) {
                String attributeValue = attribute.getAttributeValue();
                Integer count = valueCounts.get(attributeValue);
                count = (count != null ? count : 0);
                ++count;
                valueCounts.put(attributeValue, count);
            }
        }

        int maxCount = -1;
        //Go through the new map and find its maximum count
        for (String attributeValue : valueCounts.keySet()) {
            if (valueCounts.get(attributeValue) > maxCount) {
                maxCount = valueCounts.get(attributeValue);
                result = attributeValue;
            }
        }

        return result;
    }

    /**
     * Get the list of all distinct values for the attribute named attributeName
     * @param attributeName the name of the attribute for which the list is done
     * @return the list of all values
     */
    public List<String> getAllDistinctValuesForAttribute(String attributeName) {
        List<String> result = new ArrayList<>();

        for (Instance observation : observations) {
            Attribute attribute = observation.getAttributeByName(attributeName);

            if (attribute != null) {
                String attributeValue = attribute.getAttributeValue();

                if (!result.contains(attributeValue))
                    result.add(attributeValue);
            }
        }

        return result;
    }

    /**
     * Get the list of all values for the attribute named attributeName
     * @param attributeName the name of the attribute for which the list is done
     * @return the list of all values
     */
    public List<String> getAllValuesForAttribute(String attributeName) {
        List<String> result = new ArrayList<>();

        for (Instance observation : observations) {
            Attribute attribute = observation.getAttributeByName(attributeName);

            if (attribute != null) {
                String attributeValue = attribute.getAttributeValue();
                result.add(attributeValue);
            }
        }

        return result;
    }

    /**
     * Get the list of the outcome values
     * @return the list of outcome values
     */
    public List<String> getOutcomeValues() {
        return this.getAllValuesForAttribute(this.outcomeAttributeName);
    }

    /**
     * Split the dataset by an attribute
     * Choose from the given dataset only the instances that contain the given attribute value
     * @param dataset the dataset to be split
     * @param attribute the split attribute
     * @return the new dataset, containing only the instances that have the wanted attribute value
     */
    public static Dataset splitDatasetByAttribute(Dataset dataset, Attribute attribute) {
        Dataset resultDataset = new Dataset();

        for (Instance observation : dataset.getObservations()) {
            for (Attribute observationAttribute : observation.getAttributes())
                if (observationAttribute.equals(attribute))
                    resultDataset.addObservation(observation);
        }

        return resultDataset;
    }

    /**
     * Split the discretized dataset by an attribute, but return a new Dataset filled with the continuous values
     * Choose from the given dataset only the instances that contain the given attribute value
     * @param discretizedDataset the dataset to be split
     * @param dataset the dataset of continuous values
     * @param attribute the split attribute
     * @return the new dataset, containing only the instances that have the wanted attribute value
     */
    public static Dataset splitDiscretizedDatasetByAttribute(Dataset discretizedDataset, Dataset dataset, Attribute attribute) {
        Dataset resultDataset = new Dataset();

        for (int instanceIndex = 0; instanceIndex < dataset.getObservations().size(); ++instanceIndex) {
            Instance observationDiscrete = discretizedDataset.getObservations().get(instanceIndex);
            Instance observationContinuous = dataset.getObservations().get(instanceIndex);
            for (Attribute observationAttribute : observationDiscrete.getAttributes())
                if (observationAttribute.equals(attribute))
                    resultDataset.addObservation(observationContinuous);
        }

        resultDataset.setContinuousValuedAttributes(dataset.getContinuousValuedAttributes());
        resultDataset.setOutcomeAttributeName(dataset.getOutcomeAttributeName());

        return resultDataset;
    }

    /**
     * Get the list of continuous valued attributes (just the name of the attributes)
     * @return the list of continuous valued attributes (their names)
     */
    public List<String> getContinuousValuedAttributes() {
        return continuousValuedAttributes;
    }

    /**
     * Set the list of continuous valued attributes (their names)
     * @param continuousValuedAttributes the new list of continuous valued attributes
     */
    public void setContinuousValuedAttributes(List<String> continuousValuedAttributes) {
        this.continuousValuedAttributes = continuousValuedAttributes;
    }

    @Override
    public String toString() {
        String returnString = "Dataset{observations=\n";
        for (Instance observation : observations)
            returnString += observation.toString() + "\n ";
        returnString += "},\n outcomeAttributeName=" + outcomeAttributeName + "}";
        return returnString;
    }

}
