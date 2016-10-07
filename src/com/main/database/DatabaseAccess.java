package com.main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.main.form.Profile;

public class DatabaseAccess {
	private static final Logger LOG = Logger.getLogger(DatabaseAccess.class);
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	
	public void getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager
				.getConnection("jdbc:mysql://127.0.0.1:3306/adaptive_web?"
						+ "user=root&password=hp1615");
	}
	
	public ResultSet readLoginDataBase(String emailAddress){
		try {						
			preparedStatement = connect.prepareStatement("SELECT * FROM adaptive_web.USERS where EMAIL=?");    
			preparedStatement.setString(1, emailAddress);    
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet;

		} catch(Exception e){
			LOG.error("Issue while getting the login information "+e.getMessage());
		}
		return null;
	}

	public boolean insertLoginDataBase(Profile profile) {
		boolean flag = true;
		try{
			preparedStatement = connect
					.prepareStatement("INSERT into adaptive_web.USERS(USER_ID,FIRST_NAME,LAST_NAME,EMAIL,PASSWORD,INTEREST)VALUES(?,?,?,?,?,?); ");
			preparedStatement.setString(1, profile.getUser_id() + "");
			preparedStatement.setString(2, profile.getFirstName());
			preparedStatement.setString(3, profile.getLastName());
			preparedStatement.setString(4, profile.getEmail());
			preparedStatement.setString(5, profile.getPassword());
			preparedStatement.setString(6, profile.getArea_Of_Interest());
			preparedStatement.executeUpdate();
		}
		catch(Exception e){
			LOG.error("Issue while getting the login information "+e.getMessage());
			flag = false;
		}
		return flag;
	}
	
	public ResultSet readUserDatabase(String emailAddress){
		try {						
			preparedStatement = connect.prepareStatement("SELECT * FROM adaptive_web.USERS where EMAIL=?");    
			preparedStatement.setString(1, emailAddress);    
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch(Exception e){
			LOG.error("Issue while getting the login information "+e.getMessage());
		}
		return null;
	}
	
	public ResultSet getAreaOfInterest(int userId)
	{
		ResultSet rs = null;
		try
		{
			preparedStatement = connect.prepareStatement("SELECT INTEREST FROM adaptive_web.USERS WHERE USER_ID = '"+userId+"'");
			rs = preparedStatement.executeQuery();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean updateUserDatabase(Profile profile)
	{
		boolean flag = true;
		try
		{
			preparedStatement = connect.prepareStatement("UPDATE adaptive_web.USERS SET "
					+ "FIRST_NAME=\'"+profile.getFirstName()+"\', "
					+ "LAST_NAME=\'"+profile.getFirstName()+"\', "
					+ "PASSWORD=\'"+profile.getPassword()+"\', "
					+ "INTEREST=\'"+profile.getArea_Of_Interest()+"\' "
					+"WHERE EMAIL= \'"+profile.getEmail()+"\'"
					);
			preparedStatement.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public ResultSet getUserTastePreference(int userId)
	{
		try {						
			preparedStatement = connect.prepareStatement("SELECT * FROM adaptive_web.TASTE_PREFERENCES_SMALL where USER_ID=?");    
			preparedStatement.setString(1, userId+"");    
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch(Exception e){
			LOG.error("Issue while getting the taste preference");
		}
		return null;
	}
	
	public boolean insertTastePreferenceBatch(int userId, int tagId, float preference, boolean recordPresent)
	{
		boolean flag = true;
		try{
			if(recordPresent == false)
			{
				preparedStatement = connect
						.prepareStatement("INSERT into adaptive_web.TASTE_PREFERENCES_SMALL(USER_ID,ITEM_ID,PREFERENCE)VALUES(?,?,?); ");
				preparedStatement.setString(1, userId+"");
				preparedStatement.setString(2, tagId + "");
				preparedStatement.setString(3, preference+"");
			}
			else if(recordPresent)
			{
				preparedStatement = connect.prepareStatement("UPDATE adaptive_web.TASTE_PREFERENCES_SMALL SET "
						+ "PREFERENCE=\'"+preference+"\' "
						+ "WHERE USER_ID= \'"+userId+"\' AND ITEM_ID=\'"+ tagId+ "\'"
						);
			}
			preparedStatement.executeUpdate();
		}
		catch(Exception e){
			LOG.error("Issue while creating batch of Taste Preferences - Database access");
			flag = false;
		}
		return flag;
	}

	
	public boolean deleteUser(int userId) throws SQLException
	{

		/*preparedStatement = connect
				.prepareStatement("Select * from adaptive_web.TASTE_PREFERENCES_SMALL WHERE USER_ID=(?); ");
		preparedStatement.setString(1, userId+"");
		preparedStatement.setString(1, userId+"");
		ResultSet rs = preparedStatement.executeQuery();
		*/
		
		try
		{
			preparedStatement = connect
					.prepareStatement("DELETE FROM adaptive_web.TASTE_PREFERENCES_SMALL WHERE USER_ID=?; ");
			preparedStatement.setString(1, userId+"");
			preparedStatement.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Hard time deleting user");
			return false;
		}
		return true;
	}
	
	public int getCount(int tagId) throws SQLException
	{
		preparedStatement = connect.prepareStatement("SELECT COUNT(PREFERENCE) AS count FROM TASTE_PREFERENCES_SMALL WHERE ITEM_ID="+tagId);
		ResultSet rs = preparedStatement.executeQuery();
		return Integer.parseInt(rs.getString("count"));
		
	}
	
	
	//SELECT COUNT(PREFERENCE) FROM TASTE_PREFERENCE_SMALL WHERE PREFERENCE='0'
	/*public boolean addTastePreference(int userId, String tag, int weight)
	{
		preparedStatement = connect
				.prepareStatement("INSERT into adaptive_web.TASTE_PREFERENCES_SMALL(USER_ID,ITEM_ID,PREFERENCE)VALUES(?,?,?); ");
		preparedStatement.setString(1, userId+"");
		preparedStatement.setString(2, tagId + "");
		preparedStatement.setString(3, preference+"");
		
		
		return false;
	}*/
	
}
