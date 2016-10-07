package com.main.form;

public class Answer{
	private String body;
	private User answeredBy;
	private int votes;
	private boolean isAccepted;
	private int id;
	
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public User getAnsweredBy() {
		return answeredBy;
	}
	public void setAnsweredBy(User answeredBy) {
		this.answeredBy = answeredBy;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	public boolean isAccepted() {
		return isAccepted;
	}
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}