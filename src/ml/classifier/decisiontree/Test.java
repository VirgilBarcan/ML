package ml.classifier.decisiontree;

import ml.classifier.decisiontree.instance.Dataset;
import ml.classifier.decisiontree.instance.Instance;
import ml.classifier.decisiontree.purityfunction.Entropy;
import ml.classifier.decisiontree.tree.ExtendedID3;
import ml.classifier.decisiontree.tree.ID3;
import ml.classifier.decisiontree.tree.Tree;
import ml.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virgil on 07.11.2015.
 */
public class Test {

    public static void main(String[] args) {

        /*
        DataLoader dataLoader = new DataLoader();

        Dataset trainDataset = dataLoader.loadDatasetFromCsv("covtype-train_discrete.csv", ",", "Cover_Type");
        Tree tree = new ID3(trainDataset, "Cover_Type", new Entropy());
        tree.showTree();

        Dataset testDataset = dataLoader.loadDatasetFromCsv("covtype-test_discrete.csv", ",", "Cover_Type");

        Double hit = .0, count = .0;
        for( Instance observation: testDataset.getObservations() ) {
            String actualLabel = observation.getAttributeByName( "Cover_Type" ).getAttributeValue();
            String predictedLabel = tree.evaluate(observation);

            //System.out.println(actualLabel + " " + predictedLabel);

            count = count + 1.0;
            if( actualLabel.equals( predictedLabel ) )
                hit = hit + 1.0;
        }
        System.out.println("\nThe probability of getting the right label is: " + (hit / count));

        */

        //*
        DataLoader dataLoader = new DataLoader();
        Dataset trainDataset = dataLoader.loadDatasetFromCsv("covtype-train4.csv", ",", "Cover_Type");

        List<String> continuousAttributeNames = new ArrayList<>();
        continuousAttributeNames.add("Elevation");
        //continuousAttributeNames.add("Aspect");
        //continuousAttributeNames.add("Slope");
        //continuousAttributeNames.add("Horizontal_Distance_To_Hydrology");
        //continuousAttributeNames.add("Vertical_Distance_To_Hydrology");
        //continuousAttributeNames.add("Horizontal_Distance_To_Roadways");
        //continuousAttributeNames.add("Hillshade_9am");
        //continuousAttributeNames.add("Hillshade_Noon");
        //continuousAttributeNames.add("Hillshade_3pm");
        //continuousAttributeNames.add("Horizontal_Distance_To_Fire_Points");

        trainDataset.setContinuousValuedAttributes(continuousAttributeNames);

        List<String> outputClasses = new ArrayList<>();
        outputClasses.add("2");
        outputClasses.add("1");

        ExtendedID3 extendedID3 = new ExtendedID3(trainDataset, "Cover_Type", new Entropy(), outputClasses);
        extendedID3.showTree();

        //*
        Dataset testDataset = dataLoader.loadDatasetFromCsv("covtype-test.csv", ",");
        System.out.println("Evaluating...");

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

        }
        System.out.println(hit / count);
        //*/
    }

}
