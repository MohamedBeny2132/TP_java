package com.example.blockchain;

import java.security.MessageDigest;
import java.time.Instant;

public class Block {
    private int index;
    private String timestamp;
    private String previousHash;
    private String hash;
    private int nonce;
    private String validator;
    private String signature;

    private String eventId;
    private String artist;
    private String status;
    private String owner;

    public Block(int index, String eventId, String artist, String status, String owner, String previousHash) {
        this.index = index;
        this.timestamp = Instant.now().toString();
        this.eventId = eventId;
        this.artist = artist;
        this.status = status;
        this.owner = owner;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = index + timestamp + eventId + artist + status + owner
                    + previousHash + nonce
                    + (validator != null ? validator : "")
                    + (signature != null ? signature : "");
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

    public int getIndex() {
        return index;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getNonce() {
        return nonce;
    }

    public String getValidator() {
        return validator;
    }

    public String getSignature() {
        return signature;
    }

    public String getEventId() {
        return eventId;
    }

    public String getArtist() {
        return artist;
    }

    public String getStatus() {
        return status;
    }

    public String getOwner() {
        return owner;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index       : ").append(index).append("\n");
        sb.append("Artiste     : ").append(artist).append("\n");
        sb.append("Proprietaire: ").append(owner).append("\n");
        sb.append("Statut      : ").append(status).append("\n");
        sb.append("Hash        : ").append(hash).append("\n");
        if (validator != null)
            sb.append("Validateur  : ").append(validator).append("\n");
        if (nonce > 0)
            sb.append("Nonce       : ").append(nonce).append("\n");
        sb.append("-------------------------------------");
        return sb.toString();
    }
}
