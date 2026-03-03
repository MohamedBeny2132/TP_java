package com.example.blockchain;

import java.security.MessageDigest;
import java.time.Instant;

public class Block {
    public int index;
    public String timestamp, data, previousHash, hash;

    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = index + timestamp + data + previousHash;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
