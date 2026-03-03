package com.example.blockchain;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class BlockchainViewer extends Application {

    private TextArea detailsArea;

    @Override
    public void start(Stage primaryStage) {
        Blockchain blockchain = buildDemoBlockchain();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label title = new Label("Blockchain Viewer");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#e94560"));
        title.setPadding(new Insets(15));
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setStyle("-fx-background-color: #16213e; -fx-alignment: center;");
        root.setTop(title);

        HBox chainBox = new HBox();
        chainBox.setAlignment(Pos.CENTER_LEFT);
        chainBox.setPadding(new Insets(20));
        chainBox.setSpacing(0);

        List<Block> chain = blockchain.getChain();
        for (int i = 0; i < chain.size(); i++) {
            chainBox.getChildren().add(createBlockNode(chain.get(i), false));
            if (i < chain.size() - 1) {
                chainBox.getChildren().add(createArrow());
            }
        }

        List<Block> pending = blockchain.getPendingBlocks();
        if (!pending.isEmpty()) {
            chainBox.getChildren().add(createPendingArrow());
            for (int i = 0; i < pending.size(); i++) {
                chainBox.getChildren().add(createBlockNode(pending.get(i), true));
                if (i < pending.size() - 1) {
                    chainBox.getChildren().add(createPendingArrow());
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(chainBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");
        scrollPane.setPrefHeight(300);
        root.setCenter(scrollPane);

        detailsArea = new TextArea("Cliquez sur un bloc pour voir ses details...");
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(200);
        detailsArea.setFont(Font.font("Consolas", 14));
        detailsArea
                .setStyle("-fx-control-inner-background: #16213e; -fx-text-fill: #e0e0e0; -fx-border-color: #e94560;");

        Label infoBar = new Label("  Chaine: " + chain.size() + " blocs | Pending: " + pending.size()
                + " | Integrite: " + (blockchain.isChainValid() ? "VALIDE" : "CORROMPUE"));
        infoBar.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        infoBar.setTextFill(Color.web("#0f3460"));
        infoBar.setStyle("-fx-background-color: #e94560; -fx-padding: 8;");
        infoBar.setMaxWidth(Double.MAX_VALUE);

        VBox bottomBox = new VBox(infoBar, detailsArea);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 1000, 550);
        primaryStage.setTitle("Blockchain Viewer - TP Billetterie");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createBlockNode(Block block, boolean isPending) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setPrefWidth(160);
        box.setPrefHeight(180);

        String bgColor = isPending ? "#533a1e" : "#16213e";
        String borderColor = isPending ? "#e9a560" : "#0f3460";

        box.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10;"
                + "-fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 2;"
                + "-fx-cursor: hand;");

        Label indexLabel = new Label("Bloc #" + block.getIndex());
        indexLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
        indexLabel.setTextFill(isPending ? Color.web("#e9a560") : Color.web("#e94560"));

        Label ownerLabel = new Label(block.getOwner());
        ownerLabel.setFont(Font.font("Consolas", 13));
        ownerLabel.setTextFill(Color.WHITE);

        Label statusLabel = new Label(block.getStatus());
        statusLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 11));
        statusLabel.setTextFill(isPending ? Color.web("#e9a560") : Color.web("#53d769"));

        String shortHash = block.getHash().substring(0, 10) + "...";
        Label hashLabel = new Label(shortHash);
        hashLabel.setFont(Font.font("Consolas", 10));
        hashLabel.setTextFill(Color.GRAY);

        Label validatorLabel = new Label(
                block.getValidator() != null ? block.getValidator() : "N/A");
        validatorLabel.setFont(Font.font("Consolas", 10));
        validatorLabel.setTextFill(Color.web("#7ec8e3"));

        box.getChildren().addAll(indexLabel, ownerLabel, statusLabel, hashLabel, validatorLabel);

        box.setOnMouseClicked(e -> showBlockDetails(block, isPending));
        box.setOnMouseEntered(
                e -> box.setStyle(box.getStyle() + "-fx-effect: dropshadow(gaussian, #e94560, 15, 0, 0, 0);"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10;"
                + "-fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 2;"
                + "-fx-cursor: hand;"));

        return box;
    }

    private StackPane createArrow() {
        Polygon arrow = new Polygon(0, 8, 20, 8, 20, 0, 30, 12, 20, 24, 20, 16, 0, 16);
        arrow.setFill(Color.web("#0f3460"));
        StackPane pane = new StackPane(arrow);
        pane.setPadding(new Insets(0, 5, 0, 5));
        return pane;
    }

    private StackPane createPendingArrow() {
        Polygon arrow = new Polygon(0, 8, 20, 8, 20, 0, 30, 12, 20, 24, 20, 16, 0, 16);
        arrow.setFill(Color.web("#e9a560"));
        arrow.setOpacity(0.5);
        StackPane pane = new StackPane(arrow);
        pane.setPadding(new Insets(0, 5, 0, 5));
        return pane;
    }

    private void showBlockDetails(Block block, boolean isPending) {
        StringBuilder sb = new StringBuilder();
        sb.append(isPending ? "=== BLOC EN ATTENTE ===" : "=== BLOC VALIDE ===").append("\n\n");
        sb.append("Index         : ").append(block.getIndex()).append("\n");
        sb.append("Event         : ").append(block.getEventId()).append("\n");
        sb.append("Artiste       : ").append(block.getArtist()).append("\n");
        sb.append("Proprietaire  : ").append(block.getOwner()).append("\n");
        sb.append("Statut        : ").append(block.getStatus()).append("\n");
        sb.append("Timestamp     : ").append(block.getTimestamp()).append("\n");
        sb.append("Validateur    : ").append(block.getValidator() != null ? block.getValidator() : "Aucun")
                .append("\n");
        sb.append("Nonce         : ").append(block.getNonce()).append("\n");
        sb.append("Hash          : ").append(block.getHash()).append("\n");
        sb.append("Previous Hash : ").append(block.getPreviousHash()).append("\n");
        if (block.getSignature() != null) {
            sb.append("Signature     : ").append(block.getSignature()).append("\n");
        }
        detailsArea.setText(sb.toString());
    }

    private Blockchain buildDemoBlockchain() {
        Blockchain bc = new Blockchain(new PoAStrategy());
        bc.addBlock("Techno_Parade_2026", "Amelie Lens", "VALIDE", "Alice");

        bc.setStrategy(new PoWStrategy(4));
        bc.addBlock("Techno_Parade_2026", "Amelie Lens", "REVENTE", "Bob");

        bc.setStrategy(new PoSStrategy());
        bc.addBlock("Techno_Parade_2026", "Amelie Lens", "REVENTE", "Charlie");

        PBFTStrategy pbft = new PBFTStrategy();
        bc.setStrategy(pbft);
        bc.addBlock("Techno_Parade_2026", "Amelie Lens", "UTILISE", "Charlie");

        return bc;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
