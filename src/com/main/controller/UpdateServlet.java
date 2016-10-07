package com.main.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.main.database.LoginHandler;
import com.main.form.Profile;

/**
 * Servlet implementation class ActionController
 */
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String email = (String) request.getSession().getAttribute("EMAIL");
			Profile profile = LoginHandler.getRecords(email);
			request.setAttribute("FIRSTNAME", profile.getFirstName());
			request.setAttribute("LASTNAME", profile.getLastName());
			request.setAttribute("EMAIL", profile.getEmail());
			String[] interests = profile.getArea_Of_Interest().split(",");
			for(String opt : interests)
			{
				int optVal = Integer.parseInt(opt);
				request.setAttribute("opt"+optVal, "selected");
			}
			request.setAttribute("INTEREST", profile.getArea_Of_Interest());
		    getServletContext().getRequestDispatcher("/update.jsp").forward(request, response);
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
