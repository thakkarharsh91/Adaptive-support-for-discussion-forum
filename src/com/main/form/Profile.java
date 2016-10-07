package com.main.form;


public class Profile {
	private int user_id;
	private String firstName;
	private String lastName;
	private String email;
	private String area_Of_Interest;
	private String password;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getArea_Of_Interest() {
		return area_Of_Interest;
	}
	public void setArea_Of_Interest(String area_Of_Interest) {
		this.area_Of_Interest = area_Of_Interest;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
