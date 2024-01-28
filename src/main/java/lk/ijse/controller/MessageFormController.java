package lk.ijse.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    private Socket socket;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;


    public void initialize() throws IOException {
        setDateAndTime();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rootNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        socket = new Socket("localhost",3003);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
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

    @FXML
    void txtMessageSendOnAction(ActionEvent event) throws IOException {
        String msg = txtMassageSend.getText();
        dataOutputStream.writeUTF(msg);
        dataOutputStream.flush();
        System.out.println(msg);
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
