package com.example.blockchain;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BlockchainExporter {

    public void exportAsJson(List<Block> chain, String filename) {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(chain);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
            System.out.println("Export reussi : " + filename);
        } catch (IOException e) {
            System.err.println("Erreur export : " + e.getMessage());
        }
    }
}
