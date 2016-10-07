package com.main.controller;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.CachingUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
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

import com.main.database.LoginHandler;
import com.main.database.TastePreferencesDbOps;
import com.main.form.Profile;
import com.main.form.Question;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Recommendations {
	
	private static HashMap<Integer, String> idVsTags = new HashMap<Integer, String>();
	private static HashMap<String, Integer> tagVsId = new HashMap<String, Integer>();

	private static Question extractQuestionInformation(JSONObject jsonPost) {
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
	
	public static List<Question> doSearchQuestion(String query)
	{

			Client client=null;
				try {
					client = TransportClient.builder().build()
							.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("pi.asu.edu"), 9300));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
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
	
	public static List<Question> interestBasedUserBasedRecommendations(int userId) throws IOException, TasteException
	{
		createDictionary();
		
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("127.0.0.1");
		dataSource.setUser("root");
		dataSource.setPassword("hp1615");
		dataSource.setDatabaseName("adaptive_web");
		ConnectionPoolDataSource ds = new ConnectionPoolDataSource(dataSource);
		DataModel model = new MySQLJDBCDataModel(ds,
				"TASTE_PREFERENCES_SMALL", "user_id", "item_id", "preference", null);
		//File file = new File("/home/deepesh/workspace/readcsv/newUserBased.csv");
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//File file = new File(classLoader.getResource("newUserBased.csv").getPath());
		//DataModel model = new FileDataModel(file);
		/*UserSimilarity similarity = new LogLikelihoodSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.84, similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);*/
		
		UserSimilarity similarity = new CachingUserSimilarity(new LogLikelihoodSimilarity(model), model);
		UserNeighborhood neighborhood = new CachingUserNeighborhood(new NearestNUserNeighborhood(10, similarity, model), model);
		Recommender recommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
		
		
		List<String> tags = new ArrayList<String>();
		
		StringBuilder tagString = new StringBuilder("");
		
		
//		File file = new File("/home/deepesh/workspace/readcsv/newUserBased.csv");
//		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		//File file = new File(classLoader.getResource("newUserBased.csv").getPath());
//		DataModel model = new FileDataModel(file);
//		UserSimilarity similarity = new LogLikelihoodSimilarity(model);
//		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.84, similarity, model);
//		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
//		List<String> tags = new ArrayList<String>();
//		
//		StringBuilder tagString = new StringBuilder("");
		
		//get a user id!
		try
		{	
			List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
			for (RecommendedItem recommendation : recommendations) 
			{
				String tag = extractTagForId((int)recommendation.getItemID());
				
				tagString.append(tag + ",");
				tags.add(tag);
			}
		}
		catch(NoSuchUserException e)
		{
			System.out.println("No such user exception - LoginServlet");
		}
		return doSearchQuestion(tagString + "");
	}
	
	public static void createDictionary()
	{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			File file = new File(classLoader.getResource("tag_vs_id.csv").getPath());
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) 
			{
				String[] tag_Id = line.split(cvsSplitBy);

				if(idVsTags.get(Integer.parseInt(tag_Id[1])) == null)
				{
					try{
						idVsTags.put(Integer.parseInt(tag_Id[1]), tag_Id[0]);
						tagVsId.put(tag_Id[0], Integer.parseInt(tag_Id[1]));
					}
					catch(NumberFormatException e)
					{
						e.printStackTrace();
					}

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static List<Question> interestBasedRecommendedQuestions(int userId) throws IOException, TasteException, SQLException
	{
		createDictionary();
		StringBuilder tagString = new StringBuilder();
		//get tag ids from database
		//Profile p = LoginHandler.getRecords(emailId);
		
		String tagItems = TastePreferencesDbOps.getUserPreferences(userId);
		
		for(String tagNumStr : tagItems.split(","))
		{
			int tag = -1;
			try
			{
				tag = Integer.parseInt(tagNumStr);
				tagString.append(extractTagForId(tag) + ",");
			}
			catch(NumberFormatException e)
			{
				System.out.println("Error in tag: " + tagNumStr);
			}
		}
		return doSearchQuestion(tagString + "");
	}

	
	public static String extractTagForId(int id)
	{
		return idVsTags.get(id);
	}
	
	
	public static HashMap<Integer, String> getDictionaryIdVsTag()
	{
		return idVsTags;
	}
	
	public static HashMap<String, Integer> getDictionaryTagVsId()
	{
		return tagVsId;
	}

}
