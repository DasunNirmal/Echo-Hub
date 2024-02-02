package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dto.RegisterDto;
import lk.ijse.model.RegistrationModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    RegistrationModel registrationModel = new RegistrationModel();

    private static ArrayList<DataOutputStream> clientHandlersList = new ArrayList<>();


    public void initialize() {
//        startServer();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String name = txtUserName.getText();
        String pw = txtPassword.getText();

        try {
            boolean isValid = registrationModel.isValidUser(name,pw);
            if (isValid) {

                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/server_form.fxml"));
                AnchorPane anchorPane = loader.load();

                // Create a new scene with the loaded anchorPane
                Scene scene = new Scene(anchorPane);

                // Get the current stage
                Stage stage = new Stage();

                // Set the new scene to the current stage
                stage.setScene(scene);

                // Customize the stage properties
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.setTitle("Echo Room");
                stage.show();

                // Retrieve user information
                RegisterDto userDto = registrationModel.getUserInfo(name);

                // Access the controller from the FXMLLoader
                MessageFormController messageFormController = loader.getController();

                // Pass user information to the controller
                messageFormController.setUser(userDto);
                txtUserName.setText("");
                txtPassword.setText("");

            } else {
                new Alert(Alert.AlertType.ERROR,"User Name And Password Did Not Matched try again").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(3001);
                Socket socket;
                while (true) {
                    System.out.println("Waiting for clients...");
                    socket = serverSocket.accept();
                    System.out.println("Accepted...");
                    ClientHandler clients = new ClientHandler(socket,clientHandlersList);
                    new Thread(clients).start();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void hypForgotPwOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/forgotPassword_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Forgot Password");
    }

    @FXML
    void hypSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/register_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Register");
    }

    @FXML
    void txtGoToPwOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtGoToLoginOnAction(ActionEvent event) {
        btnLoginOnAction(new ActionEvent());
    }
}
