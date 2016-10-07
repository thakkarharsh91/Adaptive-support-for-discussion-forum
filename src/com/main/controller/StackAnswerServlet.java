package com.main.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.main.form.Answer;
import com.main.form.Question;


/**
 * Servlet implementation class AnswerServlet
 */
@WebServlet("/StackAnswerServlet")
public class StackAnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String answersURL = "https://api.stackexchange.com/2.2/questions/id/answers?order=desc&sort=activity&site=stackoverflow&filter=!-*f(6rzfcVz5";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StackAnswerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String questionText = request.getParameter("question");
		String[] tags = request.getParameter("tags").replace("[", "").replace("]", "").split(",");
		List<Answer> stackanswers = getAnswers(answersURL.replace("id", id));
		StringBuilder tagString = new StringBuilder();
		for(String eachTag : tags)
		{
			tagString.append(eachTag + ",");
		}
		List<Question> recommendedQuestions = Recommendations.doSearchQuestion(tagString+"");
		request.setAttribute("RelatedQuestions", recommendedQuestions);
		request.setAttribute("answers", stackanswers);
		request.setAttribute("questionText", questionText);
		request.setAttribute("questionId", id);
		request.setAttribute("tags", tags);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/question.jsp");
		dispatcher.forward(request, response);
	}

	public static List<Answer> getAnswers(String url) throws IOException {
		List<Answer> answers = new ArrayList<Answer>();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
		JSONArray array = json.getJSONArray("items");
		for(int i=0;i<array.length();i++) {
			JSONObject jsonPost = (JSONObject) array.get(i);
			Answer answer = extractAnswer(jsonPost);
			answers.add(answer);
		}
		return answers;
	}
	
	
	
	public static Answer extractAnswer(JSONObject jsonPost) {
		Answer answer = new Answer();
		answer.setId(jsonPost.getInt("answer_id"));
		answer.setBody(jsonPost.getString("body"));
		return answer;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
