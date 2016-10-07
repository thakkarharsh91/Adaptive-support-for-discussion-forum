package com.main.form;

import java.util.List;


public class Question {
	private int id;
	private String title;
	private String body;
	private User askedBy;
	private boolean isAnswered;
	private int votes;
	private List<String> tags;
	private List<Answer> answers;
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public User getAskedBy() {
		return askedBy;
	}
	public void setAskedBy(User askedBy) {
		this.askedBy = askedBy;
	}
	
	public boolean isAnswered() {
		return isAnswered;
	}
	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}
	
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getShortBody() {
		String[] arr = body.split("<p>");
		String[] arr2 = arr[1].split("</p>");
		return arr2[0];
	}
}