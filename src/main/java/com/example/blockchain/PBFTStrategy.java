package com.example.blockchain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PBFTStrategy implements ConsensusStrategy {
    private final List<String> nodes;
    private final Map<String, Boolean> votes = new LinkedHashMap<>();
    private Block currentBlock;

    public PBFTStrategy() {
        this.nodes = new ArrayList<>();
        this.nodes.add("Terminal_Nord");
        this.nodes.add("Terminal_Sud");
        this.nodes.add("Serveur_Central");
        this.nodes.add("Verificateur_Mobile");
    }

    public void submitVote(String nodeName, boolean approve) {
        if (!nodes.contains(nodeName)) {
            System.err.println("  [REJETE] " + nodeName + " n'est pas un noeud autorise.");
            return;
        }
        if (votes.containsKey(nodeName)) {
            System.out.println("  [INFO] " + nodeName + " a deja vote.");
            return;
        }
        votes.put(nodeName, approve);
        String label = approve ? "OUI" : "NON";
        System.out.println("  [VOTE] " + nodeName + " vote " + label);
    }

    public boolean isQuorumReached() {
        int votesRequired = (int) Math.ceil(2.0 * nodes.size() / 3.0);
        long positiveVotes = votes.values().stream().filter(v -> v).count();
        return positiveVotes >= votesRequired;
    }

    public void resetVotes() {
        votes.clear();
        currentBlock = null;
    }

    @Override
    public boolean validate(Block block) {
        this.currentBlock = block;
        int votesRequired = (int) Math.ceil(2.0 * nodes.size() / 3.0);

        System.out.println("[PBFT] Bloc " + block.getIndex() + " soumis au reseau.");
        System.out.println("  Noeuds    : " + nodes);
        System.out.println("  Quorum    : " + votesRequired + "/" + nodes.size());
        System.out.println("  Votes     : " + votes.size());

        long positiveVotes = votes.values().stream().filter(v -> v).count();

        if (positiveVotes >= votesRequired) {
            System.out.println("  Consensus atteint (" + positiveVotes + "/" + nodes.size() + ")");
            block.setValidator("Reseau_PBFT (" + positiveVotes + "/" + nodes.size() + ")");
            block.setHash(block.calculateHash());
            resetVotes();
            return true;
        } else {
            System.out.println("  En attente de votes (" + positiveVotes + "/" + votesRequired + " requis)");
            return false;
        }
    }

    @Override
    public String getName() {
        return "PBFT (Byzantine Fault Tolerance)";
    }
}
