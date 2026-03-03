package com.example.blockchain;

public class ApplicationBlockChain {
    public static void main(String[] args) {
        System.out.println("=== Simulation Blockchain Logistique ===");

        Blockchain blockchain = new Blockchain();

        System.out.println("Ajout d'événements...");
        blockchain.addBlock("Marchandise prise en charge par le transporteur");
        blockchain.addBlock("Passage en douane validé");
        blockchain.addBlock("Colis livré au client final");

        System.out.println("\nAffichage de la chaîne :");
        blockchain.displayChain();

        System.out.println("Vérification de l'intégrité : " + (blockchain.isChainValid() ? "VALIDE" : "CORROMPUE"));

        System.out.println("\nExportation des données...");
        blockchain.exportAsJson();

        System.out.println("\n=== Fin de la simulation ===");
    }
}
