package eternal.fire;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {
    private static final Set<KeyCode> set = new HashSet<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* stackPane-->vBox-->(hBox1, hBox2, flowPane3) */
        StackPane stackPane = new StackPane();

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(15));

        HBox hBox1 = new HBox();
        hBox1.setSpacing(20);
        hBox1.setPadding(new Insets(15));

        HBox hBox2 = new HBox();
        hBox2.setSpacing(60);
        hBox2.setPadding(new Insets(15));

        FlowPane flowPane3 = new FlowPane();
        flowPane3.setPadding(new Insets(20));
        flowPane3.setHgap(10);
        flowPane3.setVgap(10);


        /*--------------------------------------*/
        Label fileNameLabel = new Label("File Name");
        fileNameLabel.setFont(Font.font("Consolas", 15));
        JFXTextField fileNameInput = new JFXTextField("LeetCode");
        fileNameInput.setPrefWidth(100);

        /*--------------------------------------*/
        Label pathLabel = new Label("Path");
        pathLabel.setFont(Font.font("Consolas", 15));
        JFXTextField pathInput = new JFXTextField("C:\\Users\\67460\\Documents\\blog\\source\\_posts");
        pathInput.setPrefWidth(300);

        /*--------------------------------------*/
        JFXButton browseButton = new JFXButton("Browse");
        browseButton.getStyleClass().add("button-raised");
        JFXButton openPathButton = new JFXButton("Open Path");
        openPathButton.getStyleClass().add("button-raised");
        openPathButton.setStyle("-fx-background-color: #1A91DA");
        JFXButton executeButton = new JFXButton("Execute");
        executeButton.getStyleClass().add("button-raised");
        executeButton.setStyle("-fx-background-color: #0F9D58");
        JFXButton exitButton = new JFXButton("Exit");
        exitButton.getStyleClass().add("button-raised");
        exitButton.setStyle("-fx-background-color: rgb(113, 118, 114);");


        /* Dialog: stackPane-->(head-->{HBox}, body-->{VBox-->(HBox,null)) */
        Text dialogHeadText = new Text();
        Text dialogBodyText = new Text();
        JFXButton acceptButton = new JFXButton("Accept");
        acceptButton.setTextFill(Color.BLUE);

        HBox dialogHeaderHBox = new HBox();
        dialogHeaderHBox.setAlignment(Pos.CENTER);
        dialogHeaderHBox.getChildren().add(dialogHeadText);

        VBox dialogBodyVBox = new VBox();
        dialogBodyVBox.setAlignment(Pos.CENTER_RIGHT);

        HBox dialogBodyTextHBox = new HBox();
        dialogBodyTextHBox.setAlignment(Pos.CENTER);
        dialogBodyTextHBox.getChildren().add(dialogBodyText);
        dialogBodyVBox.getChildren().addAll(dialogBodyTextHBox, acceptButton);

        JFXDialog jfxDialog = new JFXDialog();
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(dialogHeaderHBox);
        dialogLayout.setBody(dialogBodyVBox);
        dialogLayout.setAlignment(Pos.CENTER);
        jfxDialog.setContent(dialogLayout);
        acceptButton.setOnAction(event -> jfxDialog.close());


        /*--------------------------------------*/
        openPathButton.setOnAction(event -> {
            try {
                Desktop.getDesktop().open(new File(pathInput.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        browseButton.setOnAction(event -> {
            File dir = chooseDirectory(primaryStage);
            if (dir != null) {
                pathInput.setText(dir.toString());
            }
        });
        executeButton.setOnAction(event -> {
            try {
                if (createTemplate(pathInput.getText(), fileNameInput.getText())) {
                    dialogHeadText.setText("Done");
                    dialogHeadText.setFill(Color.GREEN);
                    dialogHeadText.setFont(Font.font(17));
                    dialogBodyText.setText("Create template successfully!");
                } else {
                    dialogHeadText.setText("Failed");
                    dialogHeadText.setFill(Color.RED);
                    dialogBodyText.setFill(Color.RED);
                    dialogHeadText.setFont(Font.font(17));
                    dialogBodyText.setText("Create template failed, it may because the file has already existed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            jfxDialog.show(stackPane);
        });
        exitButton.setOnAction(event -> primaryStage.close());


        /*--------------------------------------*/
        hBox1.getChildren().addAll(fileNameLabel, fileNameInput);
        hBox2.getChildren().addAll(pathLabel, pathInput);
        flowPane3.getChildren().addAll(openPathButton, browseButton, executeButton, exitButton);
        vBox.getChildren().addAll(hBox1, hBox2, flowPane3);
        stackPane.getChildren().add(vBox);

        /*--------------------------------------*/
        Scene scene = new Scene(stackPane, 500, 300);
        scene.getStylesheets().add(Main.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode key = keyEvent.getCode();
            set.add(key);
            if (key == KeyCode.ESCAPE) {
                exitButton.fire();
            }
            if (set.contains(KeyCode.ALT)) {
                switch (key) {
                    case B -> browseButton.fire();
                    case E -> executeButton.fire();
                    case O -> openPathButton.fire();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/LeetCodeIcon.png")));
        primaryStage.setTitle("LeetCode Template");
        primaryStage.show();
    }

    /**
     * create a new window to let user choose a directory
     *
     * @param primaryStage javafx primaryStage
     * @return the directory(File) been chosen
     */
    private File chooseDirectory(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        return directoryChooser.showDialog(primaryStage);
    }

    /**
     * @param name file name such as LeetCode-233
     * @return return true if the file is created successfully and false if the file has already existed
     */
    private boolean createTemplate(String path, String name) throws IOException {
        File file = new File(path + "/" + name + ".md");
        if (file.exists()) {
            return false;
        }

        // build the content
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String content = readStringFromFile();
        content = content.replaceFirst("%s", name);
        content = content.replaceFirst("%s", date);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
        writer.write(content);
        writer.flush();
        return true;
    }

    private String readStringFromFile() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/template"), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
