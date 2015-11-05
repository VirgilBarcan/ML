package ml.classifier.decisiontree.tree;

import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Instance;
import ml.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class models a ConfusionMatrix for two variables
 * The confusion matrix is used to calculate how many of the actual attribute's values are correctly/incorrectly predicted
 *
 * A confusion matrix looks like this
 *
 *                           Predicted            Total
 *                        0             1
 *  Actual        A1     nA1           pA1       totalA1
 * Attribute      B1     nB1           pB1       totalB1
 *                C1     nC1           pC1       totalC1
 *                      totalO        total1      total
 * The predicted values are not necessarily binary
 *
 * Attribute and Predicted are row/column labels
 * A1, B1, C1, 0, 1 are row/column values
 *
 * The matrix is stored as a map of pairs: ((A1, 0), nA1), ((A1, 1), pA1), ...
 * The totals are stored in additional maps, each row/column name with its total
 * The total count of instances is stored in a variable
 * The labels for the row/column are stored in two strings
 * Created by virgil on 29.10.2015.
 */
public class ConfusionMatrix {

    private Map<Pair<String, String>, Integer> matrix;
    private Map<String, Integer> columnTotal;
    private Map<String, Integer> rowTotal;
    private int totalCount;
    private String columnLabel;
    private String rowLabel;

    /**
     * The ConfusionMatrix constructor
     * @param rowLabel the row label
     * @param columnLabel the column label
     */
    public ConfusionMatrix(String rowLabel, String columnLabel) {
        this.rowLabel = rowLabel;
        this.columnLabel = columnLabel;

        this.matrix = new HashMap<>();
        this.columnTotal = new HashMap<>();
        this.rowTotal = new HashMap<>();
        this.totalCount = 0;
    }

    /**
     * The ConfusionMatrix constructor
     * The row and column labels are needed in order to know which attributes to select from the dataset
     * @param dataset the dataset
     * @param rowLabel the row label
     * @param columnLabel the column label
     */
    public ConfusionMatrix(Dataset dataset, String rowLabel, String columnLabel) {
        this(rowLabel, columnLabel);

        this.setDataset(dataset);
    }

    /**
     * Set the dataset
     * Create the matrix and the totals from the dataset
     * @param dataset the dataset
     */
    public void setDataset(Dataset dataset) {
        for (Instance observation : dataset.getObservations()) {
            String rowValue = observation.getAttributeByName(rowLabel).getAttributeValue();
            String columnValue = observation.getAttributeByName(columnLabel).getAttributeValue();

            this.addValueToMatrix(rowValue, columnValue);
            this.addValueToRowTotal(rowValue);
            this.addValueToColumnTotal(columnValue);
        }

        this.totalCount = dataset.getObservationsCount();
    }

    /**
     * Add value to the matrix
     * If the value doesn't exist, then add it now
     * If the value exists, then increment it (the matrix stores counts, so we increment at each pair)
     * @param rowValue the row value
     * @param columnValue the column value
     */
    public void addValueToMatrix(String rowValue, String columnValue) {
        Pair<String, String> key = new Pair<>(rowValue, columnValue);

        Integer value = matrix.get(key);
        value = (value == null ? 0 : value);
        ++value;

        matrix.put(key, value);
    }

    /**
     * Add value to the row total
     * If the value doesn't exist for the given row value, then add it now
     * If the value exists, increment it (each new value has to be counted in the total)
     * @param rowValue the row value
     */
    public void addValueToRowTotal(String rowValue) {
        Integer value = rowTotal.get(rowValue);

        value = (value == null ? 0 : value);
        ++value;

        rowTotal.put(rowValue, value);
    }

    /**
     * Add value to the column total
     * If the value doesn't exist for the given column value, then add it now
     * If the value exists, increment it (each new value has to be counted in the total)
     * @param columnValue the column value
     */
    public void addValueToColumnTotal(String columnValue) {
        Integer value = columnTotal.get(columnValue);

        value = (value == null ? 0 : value);
        ++value;

        columnTotal.put(columnValue, value);
    }

    /**
     * Get the column total
     * @param column the name of the column for which the total is asked
     * @return the value of the total count for the wanted column
     */
    public Integer getColumnTotal(String column) {
        return columnTotal.get(column);
    }

    /**
     * Get the row total
     * @param row the name of the row for which the total is asked
     * @return the value of the total count for the wanted row
     */
    public Integer getRowTotal(String row) {
        return rowTotal.get(row);
    }

    /**
     * Return the total number of instances
     * @return the total count of instances
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * Return the column label
     * @return the column label
     */
    public String getColumnLabel() {
        return columnLabel;
    }

    /**
     * Set the column label
     * @param columnLabel the new column label
     */
    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    /**
     * Get the row label
     * @return the row label
     */
    public String getRowLabel() {
        return rowLabel;
    }

    /**
     * Set the row label
     * @param rowLabel the new row label
     */
    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    /**
     * Get the list of row values
     * @return the list of row values
     */
    public List<String> getRowValues() {
        List<String> rowValues = new ArrayList<>();

        for (Pair<String, String> key : matrix.keySet()) {
             if (!rowValues.contains(key.getFirst())) {
                 rowValues.add(key.getFirst());
             }
        }

        return rowValues;
    }

    /**
     * Get the list of column values
     * @return the list of column values
     */
    public List<String> getColumnValues() {
        List<String> columnValues = new ArrayList<>();

        for (Pair<String, String> key : matrix.keySet()) {
            if (!columnValues.contains(key.getSecond())) {
                columnValues.add(key.getSecond());
            }
        }

        return columnValues;
    }

    /**
     * Get the value at (row, column) from the matrix
     * @param row the row
     * @param column the column
     * @return the return value, if it exists, otherwise 0
     */
    public Integer getCountAt(String row, String column) {
        Pair<String, String> key = new Pair<>(row, column);

        return matrix.getOrDefault(key, 0);
    }

    public boolean isUseless() {
        int count = 0;
        for( Map.Entry<String, Integer> entry: rowTotal.entrySet() ) {
            if( entry.getValue() > 0 )
                count = count + 1;
        }
        return ( count == 1 );
    }

    public boolean isSameLabel() {
        int count = 0;
        for( Map.Entry<String, Integer> entry: columnTotal.entrySet() ) {
            if( entry.getValue() > 0 )
                count = count + 1;
        }
        return ( count == 1 );
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        List<String> rowValues = getRowValues();
        List<String> columnValues = getColumnValues();
        for (int i = 0; i < rowValues.size(); ++i) {
            String row = rowValues.get(i);
            for (int j = 0; j < columnValues.size(); ++j) {
                String column = columnValues.get(j);
                Integer count = getCountAt(row, column);
                returnString.append(count + " ");
            }
            returnString.append(", ");
        }

        return "ConfusionMatrix{" +
                "matrix=" + returnString.toString() +
                "columnTotal=" + columnTotal +
                ", rowTotal=" + rowTotal +
                ", totalCount=" + totalCount +
                ", columnLabel='" + columnLabel + '\'' +
                ", rowLabel='" + rowLabel + '\'' +
                '}';
    }
}
