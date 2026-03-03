package com.example.blockchain;

import java.util.Arrays;
import java.util.List;

public class PoAStrategy implements ConsensusStrategy {
    private final List<String> authorizedAdmins = Arrays.asList(
            "Admin_Spectacle", "SuperUser", "Organisateur");

    @Override
    public boolean validate(Block block) {
        String signer = authorizedAdmins.get(0);

        System.out.println("[POA] Verification d'autorite...");
        System.out.println("  Admins autorises : " + authorizedAdmins);
        System.out.println("  Signataire       : " + signer);

        if (authorizedAdmins.contains(signer)) {
            System.out.println("  Signature acceptee.");
            block.setValidator(signer);
            block.setSignature("SIG_" + signer.hashCode());
            block.setHash(block.calculateHash());
            return true;
        } else {
            System.err.println("  REJETE : Signataire non autorise.");
            return false;
        }
    }

    @Override
    public String getName() {
        return "Proof of Authority";
    }
}
