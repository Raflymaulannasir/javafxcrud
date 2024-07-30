/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxloginregcrudfrom;

/**
 *
 * @author SDNCangri
 */
public class studentData {
	
	private Integer studentNumber;
	private String fullName;
	private String year;
	private String course;
	private String gender;
	
	public studentData(Integer studentNumber, String fullName, String year, String course, String gender){
		this.studentNumber = studentNumber;
		this.fullName = fullName;
		this.year = year;
		this.course = course;
		this.gender = gender;
	}
	
	
	public Integer getStudentNumber(){
		return studentNumber;
	}
	public String getFullName(){
		return fullName;
	}
	public String getyear(){
		return year;
	}
	public String getCourse(){
		return course;
	}
	public String getGender(){
		return gender;
	}
}
