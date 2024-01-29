package lk.ijse.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dto.RegisterDto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class MessageFormController {

    @FXML
    private Label lblTime;

    @FXML
    private Label lblUserName;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtMassageSend;

    private  RegisterDto userDto;

    @FXML
    private VBox vBox;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    String updated = "";


    public void initialize() {
        setDateAndTime();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rootNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3001);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String messageTyp = dataInputStream.readUTF();

                    if (messageTyp.equals("TEXT")) {
                        String message = dataInputStream.readUTF();

                        Platform.runLater(() -> {
                            if (updated.equals("done")) {
                                Label label = new Label(message);
                                label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
                                label.setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(10), new Insets(10))));
                                BorderPane borderPane = new BorderPane();
                                borderPane.setRight(label);
                                vBox.getChildren().add(borderPane);
                                updated = "";
                            }else {
                                Label label = new Label(message);
                                label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");
                                label.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(10))));
                                vBox.getChildren().add(label);
                            }
                        });
                    }
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.INFORMATION, "Connection Closed").show();
            }
        }).start();
    }

    @FXML
    void txtMessageSendOnAction(ActionEvent event) {
        String sender = lblUserName.getText();
        String message = txtMassageSend.getText().trim(); // Trim to remove leading/trailing spaces

        try {
            dataOutputStream.writeUTF("TEXT");
            dataOutputStream.writeUTF(sender +"\n"+message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updated = "done";
    }

    public void setUser(RegisterDto registerDto) {
        this.userDto = registerDto;
        loadUserName();
    }

    private void loadUserName() {
        if (userDto != null) {
            String userName = userDto.getUser_name();
            lblUserName.setText(userName);
        }
    }

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void setDateAndTime(){
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                String timeNow = LocalTime.now().format(formatter);
                lblTime.setText(timeNow);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }
}
