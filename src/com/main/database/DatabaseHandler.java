package com.main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.main.form.Profile;

public class DatabaseHandler {
	
	static DatabaseAccess sql;

	static { 
		sql = new DatabaseAccess();
	}
	
	public ResultSet checkDatabaseHandler(String userName) {

		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		try {
			return (ResultSet)sql.readLoginDataBase(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean insertDatabaseHandler(Profile profile) {
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			return sql.insertLoginDataBase(profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ResultSet getDatabaseHandler(String userName)
	{
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		try {
			return (ResultSet)sql.readUserDatabase(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateDatabaseHandler(Profile person) 
	{
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			return sql.updateUserDatabase(person);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
