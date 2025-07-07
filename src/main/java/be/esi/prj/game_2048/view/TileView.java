package be.esi.prj.game_2048.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour une tuile du jeu
 */
public class TileView extends StackPane {
    private final Label valueLabel;
    private int value;

    public TileView() {
        this.getStyleClass().add("tile");
        // Réduire la taille des tuiles
        this.setMinSize(70, 70);
        this.setMaxSize(70, 70);
        this.setPrefSize(70, 70);

        valueLabel = new Label();
        valueLabel.getStyleClass().add("tile-label");
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 22));

        this.getChildren().add(valueLabel);
        this.setAlignment(Pos.CENTER);

        setValue(0); // Initialisation avec une tuile vide
    }

    public void setValue(int value) {
        this.value = value;

        // Mise à jour des styles
        getStyleClass().removeIf(style -> style.startsWith("tile-"));
        getStyleClass().add("tile");

        if (value > 0) {
            getStyleClass().add("tile-" + value);
            valueLabel.setText(String.valueOf(value));

            // Ajuste la taille de la police en fonction du nombre de chiffres
            if (value > 999) {
                valueLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            } else if (value > 99) {
                valueLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            } else {
                valueLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
            }
        } else {
            valueLabel.setText("");
        }
    }

    public int getValue() {
        return value;
    }
}