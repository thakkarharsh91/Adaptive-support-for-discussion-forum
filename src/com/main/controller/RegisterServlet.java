package com.main.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Random r = new Random();
			String userId = 10000000 + r.nextInt(90000000) + ""; // use this id universally! 
			
			String firstName = (String) request.getParameter("firstname");
			String lastName = (String) request.getParameter("lastname");
			String email = (String) request.getParameter("email");
			String password = (String) request.getParameter("password");
			String[] values = request.getParameterValues("interest");
			//saveUserInterestIntoCSV(userId, values);
			
			Profile profile = new Profile();
			profile.setUser_id(Integer.parseInt(userId));
			profile.setFirstName(firstName);
			profile.setLastName(lastName);
			profile.setEmail(email);
			profile.setPassword(password);
			String interest = "";
			for(int i =0;i<values.length;i++){
				interest = interest + values[i]+ ",";
			}
			profile.setArea_Of_Interest(interest);
			boolean result = LoginHandler.insertRecords(profile);
			TastePreferencesDbOps.insertUserInterestsIntoDb(profile);
			if(result){
				request.getSession().setAttribute("userId", userId);
				request.getSession().setAttribute("EMAIL", email);
				request.setAttribute("errormessage", "Registration successful");
				getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
			}
			else{
				request.getSession().setAttribute("EMAIL", null);
				request.setAttribute("errormessage", "Either the emailID or the username is already present in the database. Please try with other credentials.");
				getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void saveUserInterestIntoCSV(String userId, String[] values) throws IOException
//	{
//		File file = new File("/home/deepesh/workspace/readcsv/newUserBased.csv");
//		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		//File file = new File(classLoader.getResource("newUserBased.csv").getPath());
//		BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
//		
//		try
//		{
//			for(String value: values)
//			{
//				writer.write(userId + "," + value + "," + "3" + "\n"); //default score 0.5?
//			}
//		}catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		finally
//		{
//			writer.close();
//		}
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
