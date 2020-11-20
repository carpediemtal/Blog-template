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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LinkedListTransform {
    public static void showStage() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("LinkedList");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/chain.png")));

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

        Scene scene = new Scene(vBox, 400, 250);
        scene.getStylesheets().add(Main.class.getResource("/css/jfoenix-components.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static String transform(String text) {
        String[] nodes = text.split("->");
        StringBuilder codes = new StringBuilder();
        int index = 1;
        for (String node : nodes) {
            String line = String.format("ListNode node%d = new ListNode(%s);\n", index, node);
            codes.append(line);
            index++;
        }
        for (int i = 1; i < index - 1; i++) {
            String line = String.format("node%d.next = node%d;\n", i, i + 1);
            codes.append(line);
        }
        return codes.toString();
    }
}
