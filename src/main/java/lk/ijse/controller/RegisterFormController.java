package lk.ijse.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dto.RegisterDto;
import lk.ijse.model.RegistrationModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtUserName;

    RegistrationModel registrationModel = new RegistrationModel();

    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), rootNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void clearFields() {
        txtUserName.setText("");
        txtPhoneNumber.setText("");
        txtPassword.setText("");
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String name = txtUserName.getText();
        int phone = Integer.parseInt(txtPhoneNumber.getText());
        String pw = txtPassword.getText();

        var dto = new RegisterDto(name,phone,pw);
        try {
            boolean checkDuplicates = registrationModel.check(phone);
            if (checkDuplicates) {
                new Alert(Alert.AlertType.ERROR, "This Phone Number is Already Registered").showAndWait();
                return;
            }
            boolean isRegistered = registrationModel.save(dto);
            if (isRegistered) {
                clearFields();
                txtUserName.requestFocus();
                new Alert(Alert.AlertType.INFORMATION,"Account has been Created").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
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

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    @FXML
    void txtGoToPhoneNumberOnAction(ActionEvent event) {
        txtPhoneNumber.requestFocus();
    }

    @FXML
    void txtGoToPwOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtGoToRegisterOnAction(ActionEvent event) {
        btnRegisterOnAction(new ActionEvent());
    }
}
