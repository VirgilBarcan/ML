package ml.classifier.decisiontree;

import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Discretizer;
import ml.classifier.decisiontree.purityfunction.Entropy;
import ml.classifier.decisiontree.tree.ID3;
import ml.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is used to test the implementation for the DecisionTree
 * Created by virgil on 29.10.2015.
 */
public class DecisionTreeMain {

    public static void main(String[] args) {

        //DataLoader dataLoader = new DataLoader();
        //Dataset dataset = dataLoader.loadDatasetFromCsv("input.csv", ",");

        //ID3 id3 = new ID3(dataset, "Y");
        //id3.setPurityFunction(new Entropy());
        //id3.showTree();

        DataLoader dataLoader = new DataLoader();
        Dataset dataset = dataLoader.loadDatasetFromCsv("input.csv", ",", "Y");

        List<String> continuousAttributeNames = new ArrayList<>();
        continuousAttributeNames.add("A");
        Discretizer discretizer = new Discretizer(dataset, continuousAttributeNames);
        List<String> outputClasses = new ArrayList<>();
        outputClasses.add("0");
        outputClasses.add("1");

        long startTime = System.currentTimeMillis();
        Dataset discretizedDataset = discretizer.discretize(outputClasses);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Discretize took: " + duration + " ms");
        //System.out.println(discretizedDataset.toString());
    }
}
