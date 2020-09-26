package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox(20);
        HBox hBox1 = new HBox(10);
        HBox hBox2 = new HBox(10);
        HBox hBox3 = new HBox(10);
        vBox.getChildren().addAll(hBox1, hBox2, hBox3);

        /*  HBox-1  */
        Label nameLabel = new Label("File Name:");
        TextField nameTextField = new TextField("LeetCode");
        nameTextField.setMinWidth(250);
        nameLabel.getStyleClass().add("text-primary");

        /*  HBox-2  */
        Label pathLabel = new Label("Path:");
        pathLabel.getStyleClass().add("text-primary");
        TextField pathTextField = new TextField("C:\\Users\\67460\\Documents\\blog\\source\\_posts");
        pathTextField.setMinWidth(250);
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(actionEvent -> pathTextField.setText(chooseDirectory(primaryStage).toString()));
        browseButton.getStyleClass().addAll("btn", "btn-outline-primary");

        /*  HBox-3  */
        Label infoLabel = new Label();
        // Set red color for info label
        infoLabel.setTextFill(Paint.valueOf("#FF0000"));
        infoLabel.getStyleClass().addAll("lbl", "lbl-warning");
        infoLabel.setVisible(false);

        Button executeButton = new Button("Execute");
        executeButton.setOnAction(actionEvent -> {
            try {
                if (createTemplate(pathTextField.getText(), nameTextField.getText())) {
                    infoLabel.setText("Done!");
                } else {
                    infoLabel.setText("The file has already existed!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                infoLabel.setVisible(true);
            }
        });
        executeButton.getStyleClass().addAll("btn", "btn-primary");
        Button openPathButton = new Button("Open");
        openPathButton.setOnAction(actionEvent -> {
            try {
                Desktop.getDesktop().open(new File("C:\\Users\\67460\\Documents\\blog\\source\\_posts"));
                infoLabel.setText("Open path, done!");
            } catch (IOException e) {
                infoLabel.setText("Open path failed");
                e.printStackTrace();
            }finally {
                infoLabel.setVisible(true);
            }
        });
        openPathButton.getStyleClass().setAll("btn", "btn-info");
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(actionEvent -> primaryStage.close());
        cancelButton.getStyleClass().setAll("btn", "btn-danger");

        hBox1.getChildren().addAll(nameLabel, nameTextField);
        hBox2.getChildren().addAll(pathLabel, pathTextField, browseButton);
        hBox3.getChildren().addAll(infoLabel, executeButton, openPathButton, cancelButton);

        hBox1.setPadding(new Insets(20, 20, 10, 20));
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPadding(new Insets(20));
        hBox2.setAlignment(Pos.CENTER);
        hBox3.setPadding(new Insets(20));
        hBox3.setAlignment(Pos.CENTER_RIGHT);

        primaryStage.setTitle("LeetCode template");
        var image = new Image("LeetCodeIcon.png");
        primaryStage.getIcons().add(image);

        Scene scene = new Scene(vBox, 430, 240);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param name file name such as LeetCode-233
     * @return return true if the file is created successfully and false if the file has already existed
     * @throws IOException the operation to file may cause IOException
     */
    private static boolean createTemplate(String path, String name) throws IOException {
        File file = new File(path + "/" + name + ".md");
        if (file.exists()) {
            return false;
        }

        // build the content
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String content = String.format("""
                ---
                title: %s
                date: %s
                tags:
                categories: LeetCode
                ---
                                
                ## 题目
                                
                                
                                
                <!-- more -->
                                
                ## 结果
                                
                                
                                
                ## 代码
                                
                                
                                
                ## 复杂度
                                
                时间复杂度：O（）
                                
                空间复杂度：O（）
                """, name, date);

        // Write the content to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        return true;
    }

    private File chooseDirectory(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        return directoryChooser.showDialog(primaryStage);
    }
}
