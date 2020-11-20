package eternal.fire.utils;

import com.jfoenix.controls.JFXButton;
import eternal.fire.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BracketsTransform {
    public static void showStage() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Brackets");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/brackets.png")));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);


        TextArea textArea1 = new TextArea();
        TextArea textArea2 = new TextArea();
        JFXButton transformButton = new JFXButton("Transform");

        textArea1.setWrapText(true);
        textArea2.setWrapText(true);
        transformButton.getStyleClass().add("button-raised");
        transformButton.setOnAction(event -> {
            textArea2.setText(transform(textArea1.getText()));
            /* Auto copy */
            var content = new ClipboardContent();
            content.putString(textArea2.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });


        vBox.getChildren().addAll(textArea1, transformButton, textArea2);

        Scene scene = new Scene(vBox, 400, 200);
        scene.getStylesheets().add(Main.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.setOnKeyPressed(keyEvent->{
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static String transform(String s) {
        return s.replaceAll("\\[", "{").replaceAll("]", "}");
    }
}
