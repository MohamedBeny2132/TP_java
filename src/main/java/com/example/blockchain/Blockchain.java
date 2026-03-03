package com.example.blockchain;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;
    private final int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;

        // Genesis block for a concert
        Block genesis = new Block(0, "GENESIS", "Ecoalils", "INITIAL", "System", "0");
        genesis.mineBlock(difficulty);
        chain.add(genesis);
    }

    public void addBlock(String eventId, String artist, String status, String owner) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), eventId, artist, status, owner, lastBlock.hash);

        System.out.println("Minage du bloc " + newBlock.index + " en cours...");
        newBlock.mineBlock(difficulty);

        chain.add(newBlock);
    }

    public void displayChain() {
        for (Block block : chain) {
            System.out.println("Index       : " + block.index);
            System.out.println("Artiste     : " + block.artist);
            System.out.println("Propriétaire: " + block.owner);
            System.out.println("Statut      : " + block.status);
            System.out.println("Hash        : " + block.hash);
            System.out.println("Nonce       : " + block.nonce);
            System.out.println("-------------------------------------");
        }
    }

    public boolean isChainValid() {
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Verify current hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Erreur: Hash corrompu pour le bloc " + i);
                return false;
            }

            // Verify chain link
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Erreur: Lien rompu entre " + (i - 1) + " et " + i);
                return false;
            }

            // Verify Proof of Work
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("Erreur: Le bloc " + i + " n'a pas été miné correctement.");
                return false;
            }
        }
        return true;
    }

    public void exportAsJson() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(chain);
        try (FileWriter writer = new FileWriter("blockchain_tickets.json")) {
            writer.write(json);
            System.out.println("Blockchain exportée dans blockchain_tickets.json");
        } catch (IOException e) {
            System.err.println("Erreur export: " + e.getMessage());
        }
    }
}
