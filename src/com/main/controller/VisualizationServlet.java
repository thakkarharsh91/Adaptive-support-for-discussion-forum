package com.main.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.main.database.DatabaseAccess;

public class VisualizationServlet extends HttpServlet {
	
	
	private static HashMap<Integer, Integer> tagVsPreferenceForAUser = null;
	private static HashMap<Integer, Integer> tagVsPreferenceCount = null;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String,Integer> map = new HashMap<String,Integer>();
		
		DatabaseAccess sql = new DatabaseAccess();
		try {
			sql.getConnection();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
		
		
		ResultSet rs = null;
		try {
		rs = sql.getUserTastePreference(userId);
		}
		catch(Exception e)
		{
			System.out.println("here");
		}
		
		try {
			while(rs.next())
			{
				map.put(rs.getString("ITEM_ID") , Integer.parseInt( rs.getString("PREFERENCE")));
			}
		} catch (SQLException e) 
		{	
			System.out.println("Error in db!");
		}
		
		try {
			socialVisual(userId);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("map", map);
		request.setAttribute("piemap", tagVsPreferenceCount);
		
		
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/visualization.jsp");
		dispatcher.forward(request, response);

	}
	
	
	public static void socialVisual(int userId) throws ClassNotFoundException, SQLException
	{
		DatabaseAccess sql = new DatabaseAccess();
		
		sql.getConnection();
		
		tagVsPreferenceCount = new HashMap<Integer, Integer>();
		
		
		
	}

}
