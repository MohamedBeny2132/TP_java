package com.example.blockchain;

public interface ConsensusStrategy {
    boolean validate(Block block);
    String getName();
}
