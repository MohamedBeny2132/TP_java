package com.example.blockchain;

import java.util.*;

public class PoSStrategy implements ConsensusStrategy {
    private final Map<String, Integer> stakes = new LinkedHashMap<>();

    public PoSStrategy() {
        stakes.put("Alice", 100);
        stakes.put("Bob", 50);
        stakes.put("Charlie", 10);
        stakes.put("David", 30);
    }

    @Override
    public boolean validate(Block block) {
        System.out.println("[POS] Selection d'un validateur par stake...");

        int totalStake = 0;
        for (Map.Entry<String, Integer> entry : stakes.entrySet()) {
            totalStake += entry.getValue();
            System.out.println("     " + entry.getKey() + " : " + entry.getValue() + " tokens");
        }
        System.out.println("     Total stakes : " + totalStake + " tokens");

        Random random = new Random();
        int randomPoint = random.nextInt(totalStake);
        int cumulative = 0;
        String selectedValidator = null;

        for (Map.Entry<String, Integer> entry : stakes.entrySet()) {
            cumulative += entry.getValue();
            if (randomPoint < cumulative) {
                selectedValidator = entry.getKey();
                break;
            }
        }

        double probability = (stakes.get(selectedValidator) * 100.0) / totalStake;
        System.out.println("  Validateur selectionne : " + selectedValidator
                + " (Probabilite: " + String.format("%.1f", probability) + "%)");

        block.setValidator(selectedValidator);
        block.setHash(block.calculateHash());
        return true;
    }

    @Override
    public String getName() {
        return "Proof of Stake";
    }
}
