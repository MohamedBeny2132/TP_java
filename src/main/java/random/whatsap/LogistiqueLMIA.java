package random.whatsap;

import org.tribuo.*;
import org.tribuo.classification.*;
import org.tribuo.classification.evaluation.*;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.columnar.*;
import org.tribuo.data.columnar.processors.field.*;
import org.tribuo.data.columnar.processors.response.FieldResponseProcessor;
import org.tribuo.data.csv.CSVDataSource;
import org.tribuo.evaluation.TrainTestSplitter;
import org.tribuo.impl.ArrayExample;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LogistiqueLMIA {
    private static final String fileName = "data.csv";
    private static final String newFileName = "data_converted.csv";
    private static final String modelFile = "livraison_regressor.ser";

    private static final Path input = Paths.get("src", "main", "resources", fileName);
    private static final Path output = Paths.get("src", "main", "resources", newFileName);
    private static final Path MODEL_PATH = Paths.get("src", "main", "resources", modelFile);

    public static void main(String[] args) throws Exception {
        HeureDepartPreprocessor.convertPreprocessor(input, output);
        if (!Files.exists(output))
            return;

        LabelFactory labelFactory = new LabelFactory();

        LinkedHashMap<String, FieldProcessor> fieldProcessors = new LinkedHashMap<>();
        fieldProcessors.put("heure_decimal", new DoubleFieldProcessor("heure_decimal"));
        fieldProcessors.put("distance_km", new DoubleFieldProcessor("distance_km"));
        fieldProcessors.put("pluie", new IdentityProcessor("pluie"));
        fieldProcessors.put("jour_semaine", new IdentityProcessor("jour_semaine"));
        fieldProcessors.put("vehicule_type", new IdentityProcessor("vehicule_type"));

        FieldResponseProcessor<Label> responseProcessor = new FieldResponseProcessor<>("retard", "non", labelFactory);
        RowProcessor<Label> rowProcessor = new RowProcessor<>(responseProcessor, fieldProcessors);

        CSVDataSource<Label> dataSource = new CSVDataSource<>(output, rowProcessor, true);

        TrainTestSplitter<Label> splitter = new TrainTestSplitter<>(dataSource, 0.8, 42L);
        MutableDataset<Label> train = new MutableDataset<>(splitter.getTrain());
        MutableDataset<Label> test = new MutableDataset<>(splitter.getTest());

        LogisticRegressionTrainer trainer = new LogisticRegressionTrainer();
        Model<Label> model = trainer.train(train);

        LabelEvaluator evaluator = new LabelEvaluator();
        LabelEvaluation evaluation = evaluator.evaluate(model, test);
        System.out.println(evaluation.toString());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MODEL_PATH.toFile()))) {
            oos.writeObject(model);
        }

        Example<Label> predictionExample = new ArrayExample<>(new Label("unknown"));
        predictionExample.add(new Feature("distance_km", 120.0));
        predictionExample.add(new Feature("heure_decimal", 8.0));
        predictionExample.add(new Feature("pluie@non", 1.0));
        predictionExample.add(new Feature("jour_semaine@mercredi", 1.0));
        predictionExample.add(new Feature("vehicule_type@camionnette", 1.0));

        Prediction<Label> prediction = model.predict(predictionExample);
        System.out.println("\nPrediction Delay: " + prediction.getOutput().getLabel());
        System.out.println("Confidence: " + String.format("%.2f%%", prediction.getOutput().getScore() * 100));
    }
}
