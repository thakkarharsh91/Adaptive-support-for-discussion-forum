package com.main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.main.form.Profile;

public class LoginHandler {
	
	public static boolean checkRecords(String emailAddress,String password){
		ResultSet rs = null;
		String pass = "";
		try {
			DatabaseHandler handler = new DatabaseHandler();
			rs = handler.checkDatabaseHandler(emailAddress);
			if(rs!=null){
				if(rs.next()){
					pass = rs.getString("PASSWORD");
					if(pass.equals(password))
						return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getUserID(String emailAddress){
		ResultSet rs = null;
		try {
			DatabaseHandler handler = new DatabaseHandler();
			rs = handler.checkDatabaseHandler(emailAddress);
			if(rs!=null){
				if(rs.next()){
					return rs.getString("USER_ID");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean insertRecords(Profile profile) {
		boolean flag = false;
		DatabaseHandler handler = new DatabaseHandler();
		flag = handler.insertDatabaseHandler(profile);
		return flag;
	}
	
	public static Profile getRecords(String emailAddress)
	{
		Profile profile = new Profile();
		ResultSet rs = null;
		DatabaseHandler handler = new DatabaseHandler();
		rs = handler.getDatabaseHandler(emailAddress);
		try {
			while(rs.next())
			{
				profile.setFirstName(rs.getString("FIRST_NAME"));
				profile.setLastName(rs.getString("LAST_NAME"));
				profile.setEmail(rs.getString("EMAIL"));
				profile.setArea_Of_Interest(rs.getString("INTEREST"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return profile;
	}
	
	public static boolean updateMethod(Profile person) {
		boolean flag = false;
		DatabaseHandler handler = new DatabaseHandler();
		flag = handler.updateDatabaseHandler(person);
		return flag;
	}

}
