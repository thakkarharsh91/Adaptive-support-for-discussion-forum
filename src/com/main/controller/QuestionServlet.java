package com.main.controller;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.main.form.Question;

/**
 * Servlet implementation class QuestionServlet
 */
@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String relatedQuestionsURL = "https://api.stackexchange.com/2.2/questions/ids/related?order=desc&sort=activity&site=stackoverflow";
	private StringBuilder ids = new StringBuilder("1");
	/**
	 * Default constructor. 
	 */
	public QuestionServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String searchitem = request.getParameter("searchitem");
			Client client=null;
			try {
				client = TransportClient.builder().build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("pi.asu.edu"), 9300));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			List<Question> questions = doSearchQuestion(client,searchitem);
			for(Question question:questions) {
				ids.append(";"+question.getId());
			}
			
			List<String> searchedQuestionTagsString = new ArrayList<String>();
			for(Question eachQuestion : questions)
			{
				for(String eachTag :  eachQuestion.getTags())
				{
					if(!searchedQuestionTagsString.contains(eachTag))
						searchedQuestionTagsString.add(eachTag);
				}
			}
			
			
			int userId = -1; 
			try{
				userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
			}catch(NumberFormatException e)
			{
			}
			int weight = 1; //weight for all the questions searched is 1.
			
			List<Question> filteredQuestions = null;
			boolean tagsUpdate = false;
			if(userId != -1)
			{
			 tagsUpdate = TastePreferencesDbOps.updateUserInterestsIntoDb(userId, searchedQuestionTagsString, weight);
			//get filtered content 
			
			 filteredQuestions = Recommendations.interestBasedUserBasedRecommendations(userId);
			}
			 
			if(tagsUpdate && filteredQuestions != null)
			{
				request.setAttribute("filteredques", filteredQuestions);
			}
			 
			//List<Question> relatedQuestions = getQuestions(relatedQuestionsURL.replace("ids", ids.toString()));
			//request.setAttribute("relatedQuestions", relatedQuestions);
			request.setAttribute("questionText", searchitem);
			request.setAttribute("questions", questions);
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/search.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Question> doSearchQuestion(Client client,String query){
		//querybuilder for title
		//for this method you will have to send the whole query which would be a string.
		MatchQueryBuilder Title = matchQuery("title", query)
				.cutoffFrequency((float) 0.001)
				.boost(4);
		//querybuilder for Body
		MatchQueryBuilder Body = matchQuery("body", query)
				.cutoffFrequency((float) 0.001);


		//BooleanQuery
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

		boolQuery.should(Body)
		.should(Title);

		//Prepare Search on this Query
		//This will return top 5 

		SearchResponse search= client.prepareSearch("java")
				.setTypes("questions")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery)
				.setSize(5)
				//.setExplain(true)
				.execute()
				.actionGet();
		JSONObject json = new JSONObject(search.getHits());
		JSONArray array = json.getJSONArray("hits");
		List<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonPost = (JSONObject) array.get(i);
			Question question = extractQuestionInformation(jsonPost);
			questions.add(question);
		}
		return questions;

	}
	
	private Question extractQuestionInformation(JSONObject jsonPost) {
		Question question = new Question();
		question.setId(jsonPost.getJSONObject("source").getInt("Id"));
		question.setTitle(jsonPost.getJSONObject("source").getString("title"));
		question.setBody(jsonPost.getJSONObject("source").getString("Body"));
		JSONArray arrjson = jsonPost.getJSONObject("source").getJSONArray("Tags");	
		List<String> list = new ArrayList<String>();
		for (int i=0; i<arrjson.length(); i++) {
		    list.add( arrjson.getString(i).trim().replace("<", "").replace(">", "") );
		}
		question.setTags(list);
		return question;
	}
	
	private List<Question> getQuestions(String url) throws IOException {
		List<Question> questions = new ArrayList<Question>();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
		JSONArray array = json.getJSONArray("items");
		for(int i=0;i<array.length();i++) {
			JSONObject jsonPost = (JSONObject) array.get(i);
			Question question = extractQuestion(jsonPost);
			questions.add(question);
		}
		return questions;
	}
	
	private Question extractQuestion(JSONObject jsonPost) {
		Question question = new Question();
		question.setId(jsonPost.getInt("question_id"));
		ids.append(";" + question.getId());
		question.setTitle(jsonPost.getString("title"));
		question.setAnswered(jsonPost.getBoolean("is_answered"));
		//question.setBody(jsonPost.getString("body"));
		question.setVotes(jsonPost.getInt("score"));
		JSONArray arrjson = jsonPost.getJSONArray("tags");
		List<String> list = new ArrayList<String>();
		for (int i=0; i<arrjson.length(); i++) {
		    list.add( arrjson.getString(i) );
		}
		question.setTags(list);
		return question;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
