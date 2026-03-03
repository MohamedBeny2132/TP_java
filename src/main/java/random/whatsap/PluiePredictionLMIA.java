package random.whatsap;

import org.tribuo.*;
import org.tribuo.classification.*;
import org.tribuo.classification.dtree.CARTClassificationTrainer;
import org.tribuo.classification.evaluation.*;
import org.tribuo.data.columnar.*;
import org.tribuo.data.columnar.processors.field.*;
import org.tribuo.data.columnar.processors.response.FieldResponseProcessor;
import org.tribuo.data.csv.CSVDataSource;
import org.tribuo.evaluation.TrainTestSplitter;
import org.tribuo.impl.ArrayExample;

import java.nio.file.*;
import java.util.*;

public class PluiePredictionLMIA {
    private static final String fileName = "data.csv";
    private static final String newFileName = "data_converted.csv";
    private static final Path input = Paths.get("src", "main", "resources", fileName);
    private static final Path output = Paths.get("src", "main", "resources", newFileName);

    public static void main(String[] args) throws Exception {
        System.out.println("=== Bonus: Rain Prediction Model (Decision Tree) ===");

        HeureDepartPreprocessor.convertPreprocessor(input, output);
        if (!Files.exists(output))
            return;

        LabelFactory labelFactory = new LabelFactory();

        LinkedHashMap<String, FieldProcessor> fieldProcessors = new LinkedHashMap<>();
        fieldProcessors.put("jour_semaine", new IdentityProcessor("jour_semaine"));
        fieldProcessors.put("retard", new IdentityProcessor("retard"));

        FieldResponseProcessor<Label> responseProcessor = new FieldResponseProcessor<>("pluie", "non", labelFactory);
        RowProcessor<Label> rowProcessor = new RowProcessor<>(responseProcessor, fieldProcessors);

        CSVDataSource<Label> dataSource = new CSVDataSource<>(output, rowProcessor, true);

        TrainTestSplitter<Label> splitter = new TrainTestSplitter<>(dataSource, 0.8, 42L);
        MutableDataset<Label> train = new MutableDataset<>(splitter.getTrain());
        MutableDataset<Label> test = new MutableDataset<>(splitter.getTest());

        CARTClassificationTrainer trainer = new CARTClassificationTrainer();
        Model<Label> model = trainer.train(train);

        LabelEvaluator evaluator = new LabelEvaluator();
        LabelEvaluation evaluation = evaluator.evaluate(model, test);
        System.out.println(evaluation.toString());

        Example<Label> predictionExample = new ArrayExample<>(new Label("unknown"));
        predictionExample.add(new Feature("jour_semaine@vendredi", 1.0));
        predictionExample.add(new Feature("retard@oui", 1.0));

        Prediction<Label> prediction = model.predict(predictionExample);
        System.out.println("\n--- Bonus Prediction (Input: Vendredi + Retard) ---");
        System.out.println("Is it raining? " + prediction.getOutput().getLabel());
        System.out.println("Confidence: " + String.format("%.2f%%", prediction.getOutput().getScore() * 100));
    }
}
