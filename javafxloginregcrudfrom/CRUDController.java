/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxloginregcrudfrom;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author SDNCangri
 */
public class CRUDController implements Initializable{

    @FXML
    private Button crud_addBtn;

    @FXML
    private Button crud_clearBtn;

    @FXML
    private TableColumn<studentData, String> crud_col_course;

    @FXML
    private TableColumn<studentData, String> crud_col_fullName;

    @FXML
    private TableColumn<studentData, String> crud_col_gender;

    @FXML
    private TableColumn<studentData, String> crud_col_studentNumber;

    @FXML
    private TableColumn<studentData, String> crud_col_year;

    @FXML
    private ComboBox<?> crud_course;

    @FXML
    private Button crud_deleteBtn;

    @FXML
    private TextField crud_fullName;

    @FXML
    private ComboBox<?> crud_gender;

    @FXML
    private TextField crud_studentNumber;

    @FXML
    private TableView<studentData> crud_tableView;

    @FXML
    private Button crud_updateBtn;

    @FXML
    private ComboBox<?> crud_year;
    
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    private Alert alert;
    
    public void studentAddBtn() {
	    
    connect = database.connect();

    try {
        // Validasi input
        if (crud_studentNumber.getText().isEmpty() 
		|| crud_fullName.getText().isEmpty()
                || crud_year.getSelectionModel().isEmpty() 
		|| crud_course.getSelectionModel().isEmpty()
                || crud_gender.getSelectionModel().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkData = "SELECT student_number FROM student_info WHERE student_number = ?";
            prepare = connect.prepareStatement(checkData);
            prepare.setString(1, crud_studentNumber.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                // Jika nomor siswa sudah ada, tampilkan pesan kesalahan
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Student Number: " + crud_studentNumber.getText() + " is already taken");
                alert.showAndWait();
            } else {
                // Jika nomor siswa belum ada, lakukan penyisipan ke dalam database
                String insertData = "INSERT INTO student_info (student_number, full_name, year, course, gender, date)"
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, crud_studentNumber.getText());
                prepare.setString(2, crud_fullName.getText());
                prepare.setString(3, (String) crud_year.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String) crud_course.getSelectionModel().getSelectedItem());
                prepare.setString(5, (String) crud_gender.getSelectionModel().getSelectedItem());

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(6, String.valueOf(sqlDate));
		
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Message");
		alert.setHeaderText(null);
		alert.setContentText("Successfully Added!");
		alert.showAndWait();

                prepare.executeUpdate();

                // Perbarui tampilan di crud_tableView setelah penyisipan data baru
                studentShowData();
                // Bersihkan input setelah penyisipan data
                studentClearBtn();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQLException appropriately, maybe show an alert
    } catch (Exception e) {
        e.printStackTrace();
        // Handle other exceptions appropriately
    } finally {
        // Tutup sumber daya (Resultset, PreparedStatement, Connection) di blok finally jika belum ditutup di tempat lain.
        try {
            if (result != null) {
                result.close();
            }
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    public void studentUpdateBtn() {
	    
	    connect = database.connect();
	    
	    try{
		    
	    if (crud_studentNumber.getText().isEmpty()){
	    alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
	    alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Student Number: "
	    + crud_studentNumber.getText()+ "?");
            Optional<ButtonType> option = alert.showAndWait();
	    
	    if(option.get().equals(ButtonType.OK)){
		    String updateData = "UPDATE student_info SET"
			+ " full_name = '" + crud_fullName.getText()
			+ "', year = '" + crud_year.getSelectionModel().getSelectedItem()
			+ "', course = '" + crud_course.getSelectionModel().getSelectedItem()
			+ "', gender = '" + crud_gender.getSelectionModel().getSelectedItem()
			+ "' WHERE student_number = " + crud_studentNumber.getText();
		
		prepare = connect.prepareStatement(updateData);
		prepare.executeUpdate();
		
		alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Update!");
            alert.showAndWait();
		
		// Perbarui tampilan di crud_tableView setelah penyisipan data baru
                studentShowData();
		
                // Bersihkan input setelah penyisipan data
                studentClearBtn();
		
	}else{
	    alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Cancelled.");
            alert.showAndWait();
	    }
		    
	}
	    
	    }catch (Exception e){
		    e.printStackTrace();
	}
    }
    
    public void studentDeleteBtn(){
	connect = database.connect();
	    
	    try{
		    
	    if (crud_studentNumber.getText().isEmpty()){
	    alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
	    alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Student Number: "
	    + crud_studentNumber.getText()+ "?");
            Optional<ButtonType> option = alert.showAndWait();
	    
	    if(option.get().equals(ButtonType.OK)){
		    String deleteData = "DELETE FROM student_info WHERE student_number = "
			+ crud_studentNumber.getText();
		
		prepare = connect.prepareStatement(deleteData);
		prepare.executeUpdate();
		
		alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Delete!");
            alert.showAndWait();
		
		// Perbarui tampilan di crud_tableView setelah penyisipan data baru
                studentShowData();
                // Bersihkan input setelah penyisipan data
                studentClearBtn();
		
	}else{
	    alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Cancelled.");
            alert.showAndWait();
	    }
		    
	}
	    
	    }catch (Exception e){
		    e.printStackTrace();
	}
    }
    
    public void studentClearBtn(){
	    crud_studentNumber.setText("");
	    crud_fullName.setText("");
	    crud_year.getSelectionModel().clearSelection();
	    crud_course.getSelectionModel().clearSelection();
	    crud_gender.getSelectionModel().clearSelection();
    }
    
    private String[] yearList = {"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
    
    public void studentYearList(){
	    List<String> yList = new ArrayList<>();
	    
	    for(String data: yearList){
		    yList.add(data);
	    }
	    
	    ObservableList listData = FXCollections.observableArrayList(yList);
	    crud_year.setItems(listData);
    }
    
    private String[] courseList = {"Tehnik Informatika", "Sistem Informasi"};
    public void studentCourseList(){
	    List<String> cList = new ArrayList<>();
	    
	    for(String data: courseList){
		    cList.add(data);
	    }
	    
	    ObservableList listData = FXCollections.observableArrayList(cList);
	    crud_course.setItems(listData);
    }
    
    private String[] genderList = {"Male", "Female", "Others"};
    public void studentGenderList(){
	    List<String> gList = new ArrayList<>();
	    
	    for(String data: genderList){
		    gList.add(data);
	    }
	    
	    ObservableList listData = FXCollections.observableArrayList(gList);
	    crud_gender.setItems(listData);
    }
    
	// private ObservableList<studentData> listData;
    
    public ObservableList<studentData> studentListData(){
	    
	ObservableList<studentData> listData = FXCollections.observableArrayList();
	
	String selectData = "SELECT * FROM student_info";
	
	connect = database.connect();
	
	try{
	
		prepare = connect.prepareStatement(selectData);
		result = prepare.executeQuery();
		
		studentData sData;
		 System.out.println("ini adalah listdata===================================");
		 System.out.println(listData);
		while(result.next()){
			sData = new studentData(result.getInt("student_number"),
				result.getString("full_name"),
				result.getString("year"),
				result.getString("course"),
				result.getString("gender"));
		
			listData.add(sData);

		}
		
	}catch (Exception e) {
		    e.printStackTrace();
		    System.out.println(listData);
	    }
	    return listData;
	}
	   
	private ObservableList<studentData> studentData;
	public void studentShowData(){
		
		connect = database.connect();
		
	    studentData = studentListData();
		
		crud_col_studentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
		crud_col_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		crud_col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
		crud_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
		crud_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		

		crud_tableView.setItems(studentData);
	}
	
	public void studentSelectData(){
		studentData sData = crud_tableView.getSelectionModel().getSelectedItem();
		int num = crud_tableView.getSelectionModel().getSelectedIndex();
		
		if((num - 1) < - 1) return;
		
		crud_studentNumber.setText(String.valueOf(sData.getStudentNumber()));
		crud_fullName.setText(sData.getFullName());		
		
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		studentYearList();
		studentCourseList();
		studentGenderList();
		studentShowData();
	
	}
	
}
