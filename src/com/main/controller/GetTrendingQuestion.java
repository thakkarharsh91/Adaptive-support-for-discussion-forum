package com.main.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.main.form.Question;

public class GetTrendingQuestion {
	private static String URL = "https://api.stackexchange.com/2.2/questions?order=desc&sort=hot&tagged=java&site=stackoverflow&filter=!9YdnSIN18";
	private static StringBuilder ids = new StringBuilder("1");

	public static List<Question> getQuestions() throws IOException {
		List<Question> questions = new ArrayList<Question>();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(URL);
		HttpResponse response = client.execute(request);
		JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
		JSONArray array = json.getJSONArray("items");
		for(int i=0;i<10;i++) {
			JSONObject jsonPost = (JSONObject) array.get(i);
			Question question = extractQuestion(jsonPost);
			questions.add(question);
		}
		return questions;
	}
	
	public static Question extractQuestion(JSONObject jsonPost) {
		Question question = new Question();
		question.setId(jsonPost.getInt("question_id"));
		ids.append(";" + question.getId());
		question.setTitle(jsonPost.getString("title"));
		question.setAnswered(jsonPost.getBoolean("is_answered"));
		question.setBody(jsonPost.getString("body"));
		JSONArray arrjson = jsonPost.getJSONArray("tags");
		List<String> list = new ArrayList<String>();
		for (int i=0; i<arrjson.length(); i++) {
		    list.add( arrjson.getString(i) );
		}
		question.setTags(list);
		
		question.setVotes(jsonPost.getInt("score"));
		return question;
	}

	
	
}
