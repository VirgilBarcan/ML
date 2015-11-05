package ml.classifier.decisiontree.purityfunction;

import ml.classifier.decisiontree.tree.ConfusionMatrix;

/**
 * This interface models a purity function
 * A purity function is a function that selects data (Entropy, InformationGain, GiniIndex)
 * Created by virgil on 29.10.2015.
 */
public interface PurityFunction {

    /**
     * This function calculates the value of the purity function for the given variables
     * It is implemented in the classes that extend this class, as only they know how to calculate each function
     * It doesn't need the attributes as the confusionMatrix is saved for the exact two attributes used
     * @param confusionMatrix the confusion matrix
     * @return the value of the purity function for the variables
     */
    double calculate(ConfusionMatrix confusionMatrix);

}
