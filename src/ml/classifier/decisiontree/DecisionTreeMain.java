package ml.classifier.decisiontree;

import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Discretizer;
import ml.classifier.decisiontree.instance.Instance;
import ml.classifier.decisiontree.purityfunction.Entropy;
import ml.classifier.decisiontree.tree.*;
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
        /*
        DataLoader dataLoader = new DataLoader();
        Dataset dataset = dataLoader.loadDatasetFromCsv("input.csv", ",");

        ID3 id3 = new ID3(dataset, "Y", new Entropy());
        id3.setPurityFunction(new Entropy());
        id3.showTree();
        */

        /*
        DataLoader dataLoader = new DataLoader();
        Dataset dataset = dataLoader.loadDatasetFromCsv("input.csv", ",", "Y");

        List<String> continuousAttributeNames = new ArrayList<>();
        continuousAttributeNames.add("A");

        dataset.setContinuousValuedAttributes(continuousAttributeNames);

        List<String> outputClasses = new ArrayList<>();
        outputClasses.add("0");
        outputClasses.add("1");

        ExtendedID3 extendedID3 = new ExtendedID3(dataset, "Y", new Entropy(), outputClasses);
        extendedID3.showTree();
        */

        /*
        DataLoader dataLoader = new DataLoader();
        Dataset dataset = dataLoader.loadDatasetFromCsv("covtype-train4.csv", ",", "Cover_Type");

        List<String> continuousAttributeNames = new ArrayList<>();
        continuousAttributeNames.add("Elevation");

        dataset.setContinuousValuedAttributes(continuousAttributeNames);
        Discretizer discretizer = new Discretizer(dataset, continuousAttributeNames);
        List<String> outputClasses = new ArrayList<>();
        outputClasses.add("2");
        outputClasses.add("1");
        Dataset discretizedDataset = discretizer.discretize(outputClasses);
        */

        //*
        DataLoader dataLoader = new DataLoader();
        Dataset dataset = dataLoader.loadDatasetFromCsv("covtype-train4.csv", ",", "Cover_Type");

        List<String> continuousAttributeNames = new ArrayList<>();
        continuousAttributeNames.add("Elevation");
        dataset.setContinuousValuedAttributes(continuousAttributeNames);

        List<String> outputClasses = new ArrayList<>();
        outputClasses.add("2");
        outputClasses.add("1");

        ExtendedID3 extendedID3 = new ExtendedID3(dataset, "Cover_Type", new Entropy(), outputClasses);
        extendedID3.showTree();

        Dataset testDataset = dataLoader.loadDatasetFromCsv("covtype-test.csv", ",");
        System.out.println("Evaluating...");

        Node innerNode = new InnerNode();
        Node terminalNode= new TerminalNode();

        System.out.println(innerNode.isTerminal() + " " + terminalNode.isTerminal());

        Double hit = .0, count = .0;

        int checked = 0;
        for( Instance observation: testDataset.getObservations() ) {
            ++checked;
            String actualLabel = observation.getAttributeByName( "Cover_Type" ).getAttributeValue();
            String predictedLabel = extendedID3.evaluate( observation );
            count = count + 1.0;
            if( actualLabel.equals( predictedLabel ) )
                hit = hit + 1.0;
            System.out.println("Count = " + count + "\nhit = " + hit);
            if (checked == 100) {
                System.out.println("Count = " + count + "\nhit = " + hit);
                checked = 0;
            }
        }
        System.out.println(hit / count);
        //*/
    }
}
