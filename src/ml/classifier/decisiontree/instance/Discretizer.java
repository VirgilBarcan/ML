package ml.classifier.decisiontree.instance;

import ml.utils.Pair;

import java.util.*;

/**
 * This class is used to discretise continuous values for a given attribute
 * It contains the dataset in which the continuous values are found and the name of the attribute to discretise
 * Created by virgil on 04.11.2015.
 */
public class Discretizer {

    private Dataset dataset;
    private List<String> attributeNames;

    private List<Double> bestSplit;

    private Map<String, List<Pair<Double, String>>> mapContinuousToDiscrete;

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
    public Map<String, List<Pair<Double, String>>> getMapContinuousToDiscrete() {
        return mapContinuousToDiscrete;
    }

    /**
     * Get the best split points
     * @return the list of points in the best split
     */
    public List<Double> getBestSplit() {
        return bestSplit;
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
            List<Pair<Double, String>> attributeOutcomeMap = mapAttributeValuesToOutcomes(attributeValues, outcomeValues);

            //Sort the values (in increasing order, "place them on the real numbers axis")
            Collections.sort(attributeValues);

            //Sort
            attributeOutcomeMap.sort(new Comparator<Pair<Double, String>>() {
                @Override
                public int compare(Pair<Double, String> o1, Pair<Double, String> o2) {
                    return o1.getFirst().compareTo(o2.getFirst());
                }
            });

            //Find the split points
            List<Double> splitPoints = getSplitPoints(attributeValues, attributeOutcomeMap);

            //Get the best split for the current attribute values
            bestSplit = new ArrayList<>();
            bestSplit = testAllSplits(noOfOutputClasses, attributeValues, attributeOutcomeMap, splitPoints, outputClasses);

            //Edit the continuous attribute in the dataset to have values according to the decision surface
            List<Pair<Double, String>> attributeClasses = getAttributeClasses(attributeOutcomeMap, bestSplit, outputClasses);
            mapContinuousToDiscrete.put(attributeName, attributeClasses);

            //Go through the dataset and modify the value of the attribute
            for (int instanceIndex = 0; instanceIndex < discretizedDataset.getObservations().size(); ++instanceIndex) {
                Instance observation = discretizedDataset.getObservations().get(instanceIndex);
                for (Attribute attribute : observation.getAttributes()) {
                    if (attribute.getAttributeName().equals(attributeName)) {
                        attribute.setAttributeValue(attributeClasses.get(instanceIndex).getSecond());
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
    private List<Double> testAllSplits(int noOfOutputClasses, List<Double> attributeValues, List<Pair<Double, String>> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
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
    private List<Double> getSplitPoints(List<Double> attributeValues, List<Pair<Double, String>> attributeOutcomeMap) {
        List<Double> splitPoints = new ArrayList<>();

        //compare each value i with its predecessor, i in {1, ..., n-1}, where n = attributeValues.size()
        for (int i = 1; i < attributeOutcomeMap.size(); ++i) {
            Pair<Double, String> currentPair = attributeOutcomeMap.get(i);
            Pair<Double, String> lastPair = attributeOutcomeMap.get(i - 1);

            if (!currentPair.getSecond().equals(lastPair.getSecond())) {
                //new split point: add the average of the values as the split point (because we want to simulate that the value isn't suddenly changing)
                splitPoints.add((currentPair.getFirst() + lastPair.getFirst()) / 2);
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
    private int countMisclassifiedValues(List<Double> attributeValues, List<Pair<Double, String>> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
        int misclassifiedValues = 0;

        List<Pair<Double, String>> attributeClasses = getAttributeClasses(attributeOutcomeMap, splitPoints, outputClasses);

        for (int i = 0; i < attributeOutcomeMap.size(); ++i) {
            if (!attributeOutcomeMap.get(i).getSecond().equals(attributeClasses.get(i).getSecond())) {
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
    private List<Pair<Double, String>> getAttributeClasses(List<Pair<Double, String>> attributeOutcomeMap, List<Double> splitPoints, List<String> outputClasses) {
        List<Pair<Double, String>> decisionSurface = new ArrayList<>();

        int splitPointIndex = 0;
        int splitPointsCount = splitPoints.size();
        String currentDecision;

        //Get first decision
        currentDecision = outputClasses.get(0);

        for (int i = 0; i < attributeOutcomeMap.size(); ++i) {
            Double key = attributeOutcomeMap.get(i).getFirst();
            if (splitPointIndex < splitPointsCount) {
                if (key < splitPoints.get(splitPointIndex)) {
                    decisionSurface.add(new Pair(key, currentDecision));
                } else {
                    ++splitPointIndex;
                    currentDecision = outputClasses.get(splitPointIndex);
                    decisionSurface.add(new Pair(key, currentDecision));
                }
            }
            else {
                decisionSurface.add(new Pair(key, currentDecision));
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
    private List<Pair<Double, String>> mapAttributeValuesToOutcomes(List<Double> attributeValues, List<String> outcomeValues) {
        List<Pair<Double, String>> attributeOutcomeMap = new ArrayList<>();

        //attributeValues has the same size as outcomeValues
        for (int i = 0; i < attributeValues.size(); ++i) {
            attributeOutcomeMap.add(new Pair(attributeValues.get(i), outcomeValues.get(i)));
        }

        return attributeOutcomeMap;
    }
}
