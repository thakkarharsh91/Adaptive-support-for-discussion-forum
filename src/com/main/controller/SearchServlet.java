package com.main.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mahout.cf.taste.common.TasteException;

import com.main.form.Question;


/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		List<Question> filteredQuestions = null;
		List<Question> recommendedQuestions = null;
		
		
		try {
			String a = (String) request.getSession().getAttribute("userId");
			String emailId = (String) request.getSession().getAttribute("EMAIL");
			
			//System.out.println(" Search Servlet" + (String) request.getSession().getAttribute("user_id"));
			if(a != null){
				filteredQuestions = Recommendations.interestBasedUserBasedRecommendations(Integer.parseInt(a));
				recommendedQuestions = Recommendations.interestBasedRecommendedQuestions(Integer.parseInt(a));
				request.setAttribute("questions", recommendedQuestions);
				request.setAttribute("filteredques", filteredQuestions);
			}else{
				List<Question> questions= GetTrendingQuestion.getQuestions();
				request.setAttribute("questions", questions);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
