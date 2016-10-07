package com.main.controller;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.main.database.LoginHandler;
import com.main.database.TastePreferencesDbOps;
import com.main.form.Profile;

/**
 * Servlet implementation class ActionController
 */
public class UpdateDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateDataServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
			String firstName = (String) request.getParameter("firstname");
			String lastName = (String) request.getParameter("lastname");
			String email = (String) request.getParameter("email");
			String password = (String) request.getParameter("password");
			String[] values = request.getParameterValues("interest");
			Profile profile = new Profile();
			profile.setFirstName(firstName);
			profile.setLastName(lastName);
			profile.setEmail(email);
			profile.setPassword(password);
			String interest = "";
			for(int i =0;i<values.length;i++){
				interest = interest + values[i]+ ",";
			}
			profile.setArea_Of_Interest(interest);
			
			//delete from TastePreferences
			//ResultSet rs = TastePreferencesDbOps.getUserPreferences(userId);
			TastePreferencesDbOps.updateProfileTastePreference(userId, interest);
			
			LoginHandler.updateMethod(profile);
		    getServletContext().getRequestDispatcher("/home").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
