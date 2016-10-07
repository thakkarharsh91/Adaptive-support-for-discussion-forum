package com.main.controller;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mahout.cf.taste.common.TasteException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONArray;
import org.json.JSONObject;

import com.main.database.TastePreferencesDbOps;
import com.main.form.Answer;
import com.main.form.Question;


/**
 * Servlet implementation class AnswerServlet
 */
@WebServlet("/AnswerServlet")
public class AnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String relatedQuestionsURL = "https://api.stackexchange.com/2.2/questions/ids/related?order=desc&sort=activity&site=stackoverflow";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnswerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int question_id = Integer.parseInt(request.getParameter("id"));
		
		String questionText = request.getParameter("question");
		Client client=null;
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("pi.asu.edu"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		List<Answer> answers = doSearchAnswer(client,String.valueOf(question_id));
		StringBuilder tagString = new StringBuilder();
		
		Question question = GetQuestion.doSearchQuestion(String.valueOf(question_id));
		for(String eachTag : question.getTags())
		{
			tagString.append(eachTag + ",");
		}
		
		List<String> tags = question.getTags();
		
		List<Question> recommendedQuestions = Recommendations.doSearchQuestion(tagString+"");
		//List<Question> recommendedQuestionsWithNewInteractions = new ArrayList<Question>();
		List<Question> filteredQuestions = null;
		
		int userId = -1; 
		try
		{
			userId = Integer.parseInt((String)request.getSession().getAttribute("userId"));
		}
		catch(NumberFormatException e)
		{
		}
		//update user interactions
		if(userId != -1)
		{
			int weight = 3; //here, the user has clicked, increase the weight by 3.
			boolean userInteractionsRecorded = TastePreferencesDbOps.updateUserInterestsIntoDb(userId, tags, weight);
			//tags based recommendation which includes history of user + currently added tags.
			String newTags = null;
			
			if(userInteractionsRecorded)
			{
				try 
				{
					newTags = TastePreferencesDbOps.getUserPreferences(userId);
				}
				catch (SQLException e) 
				{
					System.out.println("Problem in extracting update tags - AnswerServlet");
				}
				if(newTags != null)
				{
					//recommendedQuestionsWithNewInteractions = Recommendations.doSearchQuestion(newTags); //direct from elastic search
					try {
						filteredQuestions = Recommendations.interestBasedUserBasedRecommendations(userId);
					} catch (TasteException e) {
						System.out.println("Problem in recommending filtered tags");
					}
				}
			}
		}
		
		//filtered questions
		request.setAttribute("filteredQuestions", filteredQuestions);	
		//recommended questions
		//request.setAttribute("recommendedQuestionsWithNewInteractions", recommendedQuestionsWithNewInteractions);
		request.setAttribute("RelatedQuestions", recommendedQuestions);
		request.setAttribute("questionText", "nostackanswer");
		request.setAttribute("answers", answers);
		request.setAttribute("mainquestion", question);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/question.jsp");
		dispatcher.forward(request, response);
	}

	public List<Answer> doSearchAnswer(Client client, String Id){

		//match the id of the parent document this is the routing that would be necessary
		//for this method you will also have to send in the parent index(Remember that)

		MatchQueryBuilder id = matchQuery("Id", Id);

		//Build boolean query builder
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.hasParentQuery("questions", id));

		//search the id in parent
		SearchResponse search = client.prepareSearch("java")
				.setTypes("Answers")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery)
				//.setExplain(true)
				.execute()
				.actionGet();
		JSONObject json = new JSONObject(search.getHits());
		JSONArray array = json.getJSONArray("hits");
		List<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonPost = (JSONObject) array.get(i);
			Answer answer = new Answer();
			answer.setBody(jsonPost.getJSONObject("source").getString("Body"));
			answer.setId(jsonPost.getJSONObject("source").getInt("Id"));
			answers.add(answer);
		}
		return answers;

	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
