package ml.classifier.decisiontree.instance;

import java.util.*;

/**
 * This class is used to discretise continuous values for a given attribute
 * It contains the dataset in which the continuous values are found and the name of the attribute to discretise
 * Created by virgil on 04.11.2015.
 */
public class Discretizer {

    private Dataset dataset;
    private List<String> attributeNames;

    private Map<String, Map<Double, String>> mapContinuousToDiscrete;

    /**
     * The Discretiser constructor
     * @param dataset the dataset that contains the continuous values
     * @param attributeNames the names of the attributes to be discretized
     */
    public Discretizer(Dataset dataset, List<String> attributeNames) {
        this.dataset = dataset;
        this.attributeNames = attributeNames;
        this.mapContinuousToDiscrete = new HashMap<>();
    }

    /**
     * Get the names of the attributes to be discretized
     * @return the names of the attributes to be discretized
     */
    public List<String> getAttributeNames() {
        return attributeNames;
    }

    /**
     * Set the names of the attributes to be discretized
     * @param attributeNames the new list of attribute names
     */
    public void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    /**
     * Get the dataset in which the continuous valued attribute is discretized
     * @return the dataset in which the continuous valued attribute is discretized
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * Set the dataset to be discretized
     * @param dataset the new dataset
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Get the mapping between the continuous values and the discrete ones for every continuous attribute
     * @return the mapping between the continuous and discrete values for every continuous attribute
     */
    public Map<String, Map<Double, String>> getMapContinuousToDiscrete() {
        return mapContinuousToDiscrete;
    }

    /**
     * Get all the values for the attributes, transform them in numeric values and then sort
     * (We also need to store their outcomes and be careful to match them also after the sort)
     *
     * @param noOfOutputClasses the number of output classes wanted
     * @return the new dataset, with discretized values for the given attribute
     */
    public Dataset discretize(int noOfOutputClasses) {
        List<String> outputClasses = new ArrayList<>();

        //Build outputClasses as values of 0 to noOfOutputClasses-1
        for (int i = 0; i < noOfOutputClasses; ++i) {
            outputClasses.add(i + "");
        }

        return this.discretize(outputClasses);
    }

    /**
     * Get all the values for the attributes, transform them in numeric values and then sort
     * (We also need to store their outcomes and be careful to match them also after the sort)
     *
     * @param outputClasses the list of possible output classes
     * @return the new dataset, with discretized values for the given attribute
     */
    public Dataset discretize(List<String> outputClasses) {
        Dataset discretizedDataset = new Dataset(this.dataset);

        int noOfOutputClasses = outputClasses.size();

        for (String attributeName : attributeNames) {

            //Obtain the String values for the given attribute
            List<String> attributeValuesAsStrings = dataset.getAllValuesForAttribute(attributeName);

            //Obtain the outcomes for the values
            List<String> outcomeValues = dataset.getOutcomeValues();

            //Obtain the double values in order to sort them
            List<Double> attributeValues = transformStringsListToDoublesList(attributeValuesAsStrings);

            //BE CAREFUL
            //Map each attributeValue to an outcomeValue
            Map<Double, String> attributeOutcomeMap = mapAttributeValuesToOutcomes(attributeValues, outcomeValues);

            //Sort the values (in increasing order, "place them on the real numbers axis")
            Collections.sort(attributeValues);

            //Find the split points
            List<Double> splitPoints = getSplitPoints(attributeValues, attributeOutcomeMap);

            //Get the best split for the current attribute values
            List<Double> bestSplit = new ArrayList<>();
            bestSplit = testAllSplits(noOfOutputClasses, attributeValues, attributeOutcomeMap, splitPoints, outputClasses);

            //Edit the continuous attribute in the dataset to have values according to the decision surface
            Map<Double, String> attributeClasses = getAttributeClasses(attributeOutcomeMap, bestSplit, outputClasses);
            mapContinuousToDiscrete.put(attributeName, attributeClasses);

            //Go through the dataset and modify the value of the attribute
            for (Instance observation : discretizedDataset.getObservations()) {
                for (Attribute attribute : observation.getAttributes()) {
                    if (attribute.getAttributeName().equals(attributeName)) {
                        Double value = Double.parseDouble(attribute.getAttributeValue());
                        if (attributeClasses.containsKey(value)) {
                            attribute.setAttributeValue(attributeClasses.get(value));
                            break;
                        }
                    }
                }
            }
        }

        return discretizedDataset;
    }

    /**
     * Test all possible splits and calculate the impurity function, choosing the best split
     * @param noOfOutputClasses the number of output classes wanted (that is, the number of split points chosen)
     * @param attributeValues the attribute values
     * @param attributeOutcomeMap the map of attribute values - outcomes
     * @param splitPoints the list of split points
     * @param outputClasses the list of possible output classes (the possible values taken by the attribute after the split)
     * @return the best possible split
     */
    private List<Double> testAllSplits(int noOfOutputClasses, List<Double> attributeValues, Map<Double, String> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
        List<Double> bestSplit = new ArrayList<>();

        int minMisclassifiedValues = Integer.MAX_VALUE;
        int misclassifiedValues;

        //TODO: implement for noOfOutputClasses = 2 for now, more to be done
        Double bestSplitPoint = 0.0;

        for (Double splitPoint : splitPoints) {
            List<Double> chosenSplitPoints = new ArrayList<>();
            chosenSplitPoints.add(splitPoint);

            misclassifiedValues = countMisclassifiedValues(attributeValues, attributeOutcomeMap, chosenSplitPoints, outputClasses);

            if (misclassifiedValues < minMisclassifiedValues) {
                minMisclassifiedValues = misclassifiedValues;
                bestSplitPoint = splitPoint;
            }
        }

        bestSplit.add(bestSplitPoint);
        return bestSplit;
    }

    /**
     * Get the split points of the given attributeValues (the attributeValues have to be sorted)
     * A split point is a point where the values change their output from one value to another
     * @param attributeValues the attribute values
     * @param attributeOutcomeMap the map of attribute values - outcomes
     * @return the list of split points
     */
    private List<Double> getSplitPoints(List<Double> attributeValues, Map<Double, String> attributeOutcomeMap) {
        List<Double> splitPoints = new ArrayList<>();

        //compare each value i with its predecessor, i in {1, ..., n-1}, where n = attributeValues.size()
        for (int i = 1; i < attributeValues.size(); ++i) {
            Double currentValue = attributeValues.get(i);
            Double lastValue = attributeValues.get(i - 1);
            if (!attributeOutcomeMap.get(currentValue).equals(attributeOutcomeMap.get(lastValue))) {
                //new split point: add the average of the values as the split point (because we want to simulate that the value isn't suddenly changing)
                splitPoints.add((attributeValues.get(i) + attributeValues.get(i - 1)) / 2);
            }
        }

        return splitPoints;
    }

    /**
     * Count the misclassified values after the new split
     * @param attributeValues the attribute values
     * @param attributeOutcomeMap the map of attribute values - outcomes
     * @param splitPoints the split points
     * @param outputClasses the possible output classes, ordered
     * @return the number of misclassified values after the split
     */
    private int countMisclassifiedValues(List<Double> attributeValues, Map<Double, String> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
        int misclassifiedValues = 0;

        Map<Double, String> attributeClasses = getAttributeClasses(attributeOutcomeMap, splitPoints, outputClasses);

        for (int i = 0; i < attributeValues.size(); ++i) {
            Double currentValue = attributeValues.get(i);
            if (!attributeOutcomeMap.get(currentValue).equals(attributeClasses.get(currentValue))) {
                ++misclassifiedValues;
            }
        }

        return misclassifiedValues;
    }

    /**
     * Create the attribute classes of the current attribute values, considering the split point
     * A class shows to what we map a value (one of the outputClasses)
     * @param attributeOutcomeMap the map of attribute values - outcomes
     * @param splitPoints the split points
     * @param outputClasses the possible output classes
     * @return the decision surface
     */
    private Map<Double, String> getAttributeClasses(Map<Double, String> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
        Map<Double, String> decisionSurface = new TreeMap<>();

        int splitPointIndex = 0;
        int splitPointsCount = splitPoints.size();
        String currentDecision;

        //Get first decision
        currentDecision = outputClasses.get(0);

        for (Double key : attributeOutcomeMap.keySet()) {
            if (splitPointIndex < splitPointsCount) {
                if (key < splitPoints.get(splitPointIndex)) {
                    decisionSurface.put(key, currentDecision);
                } else {
                    ++splitPointIndex;
                    currentDecision = outputClasses.get(splitPointIndex);
                    decisionSurface.put(key, currentDecision);
                }
            }
            else {
                decisionSurface.put(key, currentDecision);
            }
        }

        return decisionSurface;
    }

    /**
     * Transform the list of String values to a list of Double values
     * @param attributeValuesAsStrings the list of String values
     * @return the list of Double values
     */
    private List<Double> transformStringsListToDoublesList(List<String> attributeValuesAsStrings) {
        List<Double> attributeValues = new ArrayList<>();

        for (String attributeValueAsString : attributeValuesAsStrings) {
            attributeValues.add(Double.parseDouble(attributeValueAsString));
        }

        return attributeValues;
    }

    /**
     * Map the attributeValues to their outcomeValues
     * Each attributeValue was part of an Instance, and, being a training data instance, it was labeled
     * We want to map the attributeValue to its outcomeValue
     *
     * !attributeValues has the same size as outcomeValues!
     *
     * @param attributeValues the attributeValues
     * @param outcomeValues the outcomeValues
     * @return the map of attributeValues to outcomeValues
     */
    private Map<Double, String> mapAttributeValuesToOutcomes(List<Double> attributeValues, List<String> outcomeValues) {
        Map<Double, String> attributeOutcomeMap = new TreeMap<>();

        //attributeValues has the same size as outcomeValues
        for (int i = 0; i < attributeValues.size(); ++i) {
            attributeOutcomeMap.put(attributeValues.get(i), outcomeValues.get(i));
        }

        return attributeOutcomeMap;
    }
}
