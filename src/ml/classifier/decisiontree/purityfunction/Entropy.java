package ml.classifier.decisiontree.purityfunction;

import ml.classifier.decisiontree.tree.ConfusionMatrix;

/**
 * This class models the Entropy function
 * It calculates the conditional entropy of two variables
 * Created by virgil on 29.10.2015.
 */
public class Entropy implements PurityFunction {

    /**
     * This function calculates the value of the purity function for the given variables
     * It is implemented in the classes that extend this class, as only they know how to calculate each function
     * It doesn't need the attributes as the confusionMatrix is saved for the exact two attributes used
     * @param confusionMatrix the confusion matrix
     * @return the value of the purity function for the variables
     */
    @Override
    public double calculate(ConfusionMatrix confusionMatrix) {
        //H(Y|A) = Sum((totalRow_i/total) * H(Y|A=val_i))
        //for each row in the confusion matrix we calculate H(Y|A=val_i) = getConditionalCount(val_i, j)/rowTotal, j in the list of all columns

        double entropy = 0.0;

        for (String row : confusionMatrix.getRowValues()) {
            double rowEntropy = 0.0;
            for (String column : confusionMatrix.getColumnValues()) {
                if (confusionMatrix.getCountAt(row, column) != 0) {
                    rowEntropy += (confusionMatrix.getCountAt(row, column) + 0.0) / (confusionMatrix.getRowTotal(row) + 0.0) * (Math.log((confusionMatrix.getRowTotal(row) + 0.0) / (confusionMatrix.getCountAt(row, column) + 0.0)) / Math.log(2));
                }
                else
                    rowEntropy += 0;
            }
            entropy += rowEntropy * (confusionMatrix.getRowTotal(row) + 0.0) / (confusionMatrix.getTotalCount() + 0.0);
        }
        return entropy;
    }

}
