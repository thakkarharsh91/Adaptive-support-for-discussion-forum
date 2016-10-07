package com.main.controller;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

import com.main.form.Question;

public class GetQuestion {
	public static Question doSearchQuestion(String Id){
		
		Client client=null;
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("pi.asu.edu"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		//querybuilder for title
		//for this method you will have to send the whole query which would be a string.
		MatchQueryBuilder id = matchQuery("Id", Id);


		//BooleanQuery
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.should(id);
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
		JSONObject jsonPost = (JSONObject) array.get(0);
		Question questions = extractQuestionInformation(jsonPost);
		return questions;

	}
	
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
}
