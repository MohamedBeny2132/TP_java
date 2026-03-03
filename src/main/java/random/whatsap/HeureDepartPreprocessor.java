package random.whatsap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HeureDepartPreprocessor {
    public static void convertPreprocessor(Path input, Path output) {
        try (BufferedReader reader = Files.newBufferedReader(input);
                BufferedWriter writer = Files.newBufferedWriter(output)) {

            String line = reader.readLine();
            if (line != null) {
                if (!line.contains("heure_decimal")) {
                    writer.write(line + ",heure_decimal");
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    try {
                        double decimalTime = convertTimeToDecimal(parts[1]);
                        writer.write(line + "," + String.format("%.2f", decimalTime).replace(",", "."));
                    } catch (Exception e) {
                        writer.write(line + ",0.0");
                    }
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error preprocessing dataset: " + e.getMessage());
        }
    }

    private static double convertTimeToDecimal(String timeStr) {
        String[] h_m = timeStr.split(":");
        int hours = Integer.parseInt(h_m[0].trim());
        int minutes = Integer.parseInt(h_m[1].trim());
        return hours + (minutes / 60.0);
    }
}
