package com.main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.main.controller.Recommendations;
import com.main.form.Profile;

public class TastePreferencesDbOps {
	static DatabaseAccess sql;
	static {
		sql = new DatabaseAccess();
	}

	public static boolean insertUserInterestsIntoDb(Profile profile) {
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			int userId = (int) profile.getUser_id();
			/*
			 * //get the record of user ResultSet rs =
			 * sql.getUserTastePreference(userId); ArrayList<String>
			 * tagsPresentInDb = new ArrayList<String>(); try { while(rs.next())
			 * //fetch record from db. {
			 * tagsPresentInDb.add(rs.getString("ITEM_ID"));
			 * //profile.setArea_Of_Interest(rs.getString("INTEREST")); } }
			 * catch (SQLException e) { System.out.println(
			 * "Problem in extracting a user from db - TastePreference"); }
			 */

			String[] tags = profile.getArea_Of_Interest().split(",");

			for (String eachTag : tags) {
				int tagId = Integer.parseInt(eachTag);
				sql.insertTastePreferenceBatch(userId, tagId, 3, false);
			}
			// execute all the prepared Statements
			// sql.executeTastePreferenceBatch();
			return true;
		} catch (Exception e) {
			System.out.println("Problems executing Taste Preferences - Taste Preferences");
		}
		return false;
	}

	public static boolean updateUserInterestsIntoDb(int userId, List<String> newTags, int weight)// we
																									// can
																									// work
																									// through
																									// just
																									// string
																									// too!
	{
		boolean flag = true;
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		HashMap<Integer, Integer> tagsMapPresentInDb = new HashMap<Integer, Integer>();
		try {
			ResultSet rs = sql.getUserTastePreference(userId);
			while (rs.next()) // fetch record from db.
			{
				tagsMapPresentInDb.put(Integer.parseInt(rs.getString("ITEM_ID")),
						Integer.parseInt(rs.getString("PREFERENCE")));
			}
		} catch (SQLException e) {
			flag = false;
			System.out.println("Problem in extracting a user from db - TastePreference");
		}

		Set<Integer> tagSetPresentInDb = tagsMapPresentInDb.keySet();
		Recommendations.createDictionary();
		HashMap<String, Integer> tagsVsId = Recommendations.getDictionaryTagVsId();

		try {
			for (String eachNewTag : newTags) {
				int itemId = -1;
				try {
					itemId = tagsVsId.get(eachNewTag.trim());
				} catch (NullPointerException e) {
				}
				if (itemId != -1) {
					if (tagSetPresentInDb.contains(tagsVsId.get(eachNewTag.trim()))) {
						sql.insertTastePreferenceBatch(userId, itemId, tagsMapPresentInDb.get(itemId) + weight, true);
					} else {
						sql.insertTastePreferenceBatch(userId, itemId, weight, false);
					}
				}
			}
		}

		catch (Exception e) {
			flag = false;
			System.out.println(
					"Tag not found at all or problems in updating databse with new tags - TastePreferencesDbOps");
		}

		return flag;
	}

	public static String getUserPreferences(int userId) throws SQLException {
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		StringBuilder tags = new StringBuilder();
		ResultSet rs = sql.getUserTastePreference(userId);

		while (rs.next()) {
			tags.append(rs.getString("ITEM_ID") + ",");
		}

		return tags + "";
	}

	public static boolean updateProfileTastePreference(int userId, String newTags) throws SQLException {
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try
		{
			ResultSet rs = sql.getAreaOfInterest(userId);
	
			List<String> interestFromDb = null;
			while (rs.next()) {
				interestFromDb = Arrays.asList(rs.getString("INTEREST").split(","));
			}
	
			rs = sql.getUserTastePreference(userId);
			HashMap<Integer, Integer> tagsFromDb = new HashMap<Integer, Integer>();
	
			while (rs.next()) {
				tagsFromDb.put(Integer.parseInt(rs.getString("ITEM_ID")), Integer.parseInt(rs.getString("PREFERENCE")));
			}
	
			// delete user
			sql.deleteUser(userId);
	
			// insert new tags
			Set<Integer> tagsDb = tagsFromDb.keySet();
			Recommendations.createDictionary();
			HashMap<String, Integer> tagsVsId = Recommendations.getDictionaryTagVsId();
			HashMap<Integer, String> idVsTag = Recommendations.getDictionaryIdVsTag();
	
			String[] newTagArray = newTags.split(",");
	
			for (Integer eachTagId : tagsDb) {
				if (!interestFromDb.contains(""+eachTagId)) {
					sql.insertTastePreferenceBatch(userId, eachTagId, 3, false);
				}
			}
	
			for (String eachNewTag : newTagArray) {
	
				if (interestFromDb.contains(eachNewTag)) 
				{
					int a = tagsFromDb.get(Integer.parseInt(eachNewTag));
					 int b = Integer.parseInt(eachNewTag);
					sql.insertTastePreferenceBatch(userId, b , a , false);
				} 
				else 
				{
					sql.insertTastePreferenceBatch(userId, Integer.parseInt(eachNewTag), 3, false);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println();
		}

		return true;
	}

}
