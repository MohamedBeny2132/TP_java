package com.example.blockchain;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        // Genesis block
        chain.add(new Block(0, "Départ chaîne logistique", "0"));
    }

    public void addBlock(String data) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, lastBlock.hash);
        chain.add(newBlock);
    }

    public void displayChain() {
        for (Block block : chain) {
            System.out.println("Index : " + block.index);
            System.out.println("Horodatage : " + block.timestamp);
            System.out.println("Données : " + block.data);
            System.out.println("Hash préc. : " + block.previousHash);
            System.out.println("Hash : " + block.hash);
            System.out.println("-------------------------------------");
        }
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Verify current hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Hash actuel invalide pour le bloc " + i);
                return false;
            }

            // Verify chain link
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Lien rompu entre le bloc " + (i - 1) + " et " + i);
                return false;
            }
        }
        return true;
    }

    public void exportAsJson() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(chain);
        try (FileWriter writer = new FileWriter("blockchain.json")) {
            writer.write(json);
            System.out.println("Blockchain exportée dans blockchain.json");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export JSON : " + e.getMessage());
        }
    }
}
