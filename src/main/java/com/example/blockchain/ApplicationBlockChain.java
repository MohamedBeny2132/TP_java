package com.example.blockchain;

public class ApplicationBlockChain {
    public static void main(String[] args) {
        System.out.println("=== Blockchain Billetterie ===\n");

        Blockchain ticketChain = new Blockchain(new PoAStrategy());

        String event = "Techno_Parade_2026";
        String artist = "Amelie Lens";

        System.out.println("\n--- 1. Emission du ticket (PoA) ---");
        ticketChain.addBlock(event, artist, "VALIDE", "Alice");

        System.out.println("\n--- 2. Revente [PoW - Difficulte 4] ---");
        ticketChain.setStrategy(new PoWStrategy(4));
        ticketChain.addBlock(event, artist, "REVENTE", "Bob");

        System.out.println("\n--- 3. Revente [PoS - Selection par Stake] ---");
        ticketChain.setStrategy(new PoSStrategy());
        ticketChain.addBlock(event, artist, "REVENTE", "Charlie");

        PBFTStrategy pbft = new PBFTStrategy();
        ticketChain.setStrategy(pbft);

        System.out.println("\n--- 4. Utilisation [PBFT] - Aucun vote ---");
        ticketChain.addBlock(event, artist, "UTILISE", "Charlie");

        System.out.println("\n--- Les noeuds votent un par un ---");
        pbft.submitVote("Terminal_Nord", true);
        pbft.submitVote("Serveur_Central", true);

        System.out.println("\n--- Tentative de retry (2 votes) ---");
        ticketChain.retryPendingBlocks();

        System.out.println("\n--- Un 3eme noeud vote ---");
        pbft.submitVote("Terminal_Sud", true);

        System.out.println("\n--- Retry avec 3 votes (Quorum atteint !) ---");
        ticketChain.retryPendingBlocks();

        ticketChain.displayChain();
        System.out.println("\nIntegrite : " + (ticketChain.isChainValid() ? "VALIDE" : "CORROMPUE"));

        BlockchainExporter exporter = new BlockchainExporter();
        exporter.exportAsJson(ticketChain.getChain(), "blockchain_solid.json");

        System.out.println("\n=== Fin de la simulation ===");
    }
}
