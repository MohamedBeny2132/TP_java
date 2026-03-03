package com.example.blockchain;

public class PoWStrategy implements ConsensusStrategy {
    private final int difficulty;

    public PoWStrategy(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean validate(Block block) {
        System.out.println("[POW] Minage en cours (Difficulte: " + difficulty + ")...");
        String target = new String(new char[difficulty]).replace('\0', '0');

        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(block.calculateHash());
        }

        System.out.println("Block Mine ! Hash: " + block.getHash());
        return true;
    }

    @Override
    public String getName() {
        return "Proof of Work";
    }
}
