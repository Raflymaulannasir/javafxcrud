/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package javafxloginregcrudfrom;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author SDNCangri
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private BorderPane login_form;

    @FXML
    private Button si_createAccountBtn;

    @FXML
    private Button si_loginBtn;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private BorderPane signup_form;

    @FXML
    private Button su_loginAccountBtn;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button su_signupBtn;

    @FXML
    private TextField su_username;
    
    
    //lets create database dan account
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
 
    // Penambahan metode untuk mendapatkan koneksi ke database

    // Metode untuk menangani login
    public void loginAccount() {
        String sql = "SELECT Username, password FROM admin WHERE username = ? and password = ?";

        // Hubungkan ke database
        connect = database.connect();

        try {
            Alert alert;
            if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    // Jika username dan password benar
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();

		    // Sembunyikan formulir login
                    si_loginBtn.getScene().getWindow().hide();
		    
		    Parent root = FXMLLoader.load(getClass().getResource("crudForm.fxml"));

                    // Tampilkan formulir CRUD
                    
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Jika username atau password salah
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Tutup koneksi dan set objek null
            // Pastikan untuk menutup koneksi setelah digunakan
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Metode untuk mendaftarkan akun baru
    public void registerAccount() {
        String sql = "INSERT INTO admin (username, password) VALUES(?, ?)";

        // Hubungkan ke database
        connect = database.connect();

        try {
            Alert alert;
            if (su_username.getText().isEmpty() || su_password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                // Lakukan pengecekan apakah username sudah ada
                String checkData = "SELECT username FROM admin WHERE username = ?";
                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, su_username.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    if (su_password.getText().length() < 8) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid Password, at least 8 characters needed");
                        alert.showAndWait();
                    } else {
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, su_username.getText());
                        prepare.setString(2, su_password.getText());

                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully create a new Account!");
                        alert.showAndWait();

                        login_form.setVisible(true);
                        signup_form.setVisible(false);

                        su_username.setText("");
                        su_password.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Tutup koneksi dan set objek null
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Metode untuk beralih antara formulir login dan formulir pendaftaran
    public void switchForm(ActionEvent event) {
        if (event.getSource() == su_loginAccountBtn) {
            login_form.setVisible(true);
            signup_form.setVisible(false);
        } else if (event.getSource() == si_createAccountBtn) {
            login_form.setVisible(false);
            signup_form.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inisialisasi, jika diperlukan
    }
}