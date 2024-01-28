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

public class ForgotPasswordFormController {
    @FXML
    private AnchorPane ForgotPwPane;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtUserName;

    RegistrationModel registrationModel = new RegistrationModel();

    public void initialize() {
        txtUserName.setDisable(true);
        txtPassword.setDisable(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), ForgotPwPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void clearFields() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtPhoneNumber.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtUserName.getText();
        int phoneNumber = Integer.parseInt(txtPhoneNumber.getText());
        String pw = txtPassword.getText();

        var dto = new RegisterDto(name,phoneNumber,pw);
        try {
            boolean isUpdated = registrationModel.update(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION,"Your Password has been Updated").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void imgBackOnAction(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.ForgotPwPane.getScene().getWindow();
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
    void txtGoToRegisterOnAction(ActionEvent event) {
        btnSaveOnAction(new ActionEvent());
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String phoneNumber = txtPhoneNumber.getText();
        try {
            RegisterDto registerDto = registrationModel.search(phoneNumber);
            if (registerDto != null) {
                txtUserName.setText(registerDto.getUser_name());
                txtPhoneNumber.setText(String.valueOf(registerDto.getPhone_number()));
                txtUserName.setDisable(false);
                txtPassword.setDisable(false);
                txtUserName.setEditable(false);
                txtPassword.requestFocus();
            } else {
                new Alert(Alert.AlertType.INFORMATION,"User is not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
}
