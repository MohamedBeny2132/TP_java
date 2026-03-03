package com.example.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;
    private final List<Block> pendingBlocks;
    private ConsensusStrategy strategy;

    public Blockchain(ConsensusStrategy strategy) {
        this.chain = new ArrayList<>();
        this.pendingBlocks = new ArrayList<>();
        this.strategy = strategy;

        Block genesis = new Block(0, "GENESIS", "System", "INITIAL", "System", "0");
        strategy.validate(genesis);
        chain.add(genesis);
    }

    public void addBlock(String eventId, String artist, String status, String owner) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(
                chain.size() + pendingBlocks.size(),
                eventId, artist, status, owner,
                lastBlock.getHash());

        System.out.println("\nValidation du bloc " + newBlock.getIndex() + " via " + strategy.getName() + "...");

        if (strategy.validate(newBlock)) {
            chain.add(newBlock);
            System.out.println("Bloc " + newBlock.getIndex() + " ajouté à la chaîne.");
        } else {
            pendingBlocks.add(newBlock);
            System.out
                    .println("Bloc " + newBlock.getIndex() + " mis en attente (" + pendingBlocks.size() + " pending).");
        }
    }

    public void retryPendingBlocks() {
        System.out.println("\n--- Relance des blocs en attente ---");
        Iterator<Block> it = pendingBlocks.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            Block lastBlock = chain.get(chain.size() - 1);
            block.setPreviousHash(lastBlock.getHash());
            block.setIndex(chain.size());
            block.setHash(block.calculateHash());

            if (strategy.validate(block)) {
                chain.add(block);
                it.remove();
                System.out.println("Bloc " + block.getIndex() + " validé et ajouté !");
            } else {
                System.out.println("Bloc " + block.getIndex() + " toujours en attente.");
            }
        }
    }

    public void setStrategy(ConsensusStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);
            if (!current.getHash().equals(current.calculateHash()))
                return false;
            if (!current.getPreviousHash().equals(previous.getHash()))
                return false;
        }
        return true;
    }

    public void displayChain() {
        System.out.println("\n=== BLOCKCHAIN ===");
        for (Block block : chain) {
            System.out.println(block);
        }
        if (!pendingBlocks.isEmpty()) {
            System.out.println("\n=== BLOCS EN ATTENTE ===");
            for (Block block : pendingBlocks) {
                System.out.println(block);
            }
        }
    }

    public List<Block> getChain() {
        return Collections.unmodifiableList(chain);
    }

    public List<Block> getPendingBlocks() {
        return Collections.unmodifiableList(pendingBlocks);
    }
}
