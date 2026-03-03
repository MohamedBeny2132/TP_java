package com.example.blockchain;

public class ApplicationBlockChain {
    public static void main(String[] args) {
        System.out.println("=== Simulation Blockchain Billetterie (Expert) ===");

        // Initialisation avec difficulté 4 (4 zéros au début du hash)
        Blockchain ticketChain = new Blockchain(4);

        String event = "Concert_Summer_2026";
        String artist = "Daft Punk (Reunion)";

        System.out.println("\n--- Étape 1: Achat initial ---");
        ticketChain.addBlock(event, artist, "VALIDE", "Alice");

        System.out.println("\n--- Étape 2: Première revente ---");
        ticketChain.addBlock(event, artist, "REVENTE", "Bob");

        System.out.println("\n--- Étape 3: Deuxième revente ---");
        ticketChain.addBlock(event, artist, "REVENTE", "Charlie");

        System.out.println("\n--- Étape 4: Utilisation du ticket ---");
        ticketChain.addBlock(event, artist, "UTILISÉ", "Charlie");

        System.out.println("\n--- Étape 5: Tentative de réutilisation (Fraude) ---");
        // Dans une vraie blockchain, on vérifierait le statut avant d'ajouter.
        // Ici on montre que l'historique est immuable.
        ticketChain.addBlock(event, artist, "INVALIDE (Double usage)", "Charlie");

        System.out.println("\nAffichage de l'historique complet du ticket :");
        ticketChain.displayChain();

        System.out.println("Vérification d'intégrité globale : " + (ticketChain.isChainValid() ? "OK" : "CORROMPUE"));

        System.out.println("\nSauvegarde des données...");
        ticketChain.exportAsJson();

        System.out.println("\n=== Fin de la simulation Billetterie ===");
    }
}
