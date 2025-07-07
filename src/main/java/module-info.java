module be.esi.prj.game_2048 {
    requires javafx.controls;
    requires javafx.fxml;
    // autres requires...

    // Pour permettre l'accès à la classe principale
    exports be.esi.prj.game_2048 to javafx.graphics;

    // Pour permettre l'accès par réflexion à vos contrôleurs
    opens be.esi.prj.game_2048.controller to javafx.fxml;
    opens be.esi.prj.game_2048 to javafx.fxml;

    // autres exports si nécessaire
}