package com.friendtracking.model;

import java.sql.Timestamp;

public class Tag {

	private int id;
	private int remote_id;
	private int activity_id;
	private double longitude;
	private double latitude;
	private Timestamp created_at;

	public Tag() {}
	
	public Tag(int id, int remote_id, int activity_id, double longitude,
			double latitude, Timestamp created_at) {
		super();
		this.id = id;
		this.remote_id = remote_id;
		this.activity_id = activity_id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRemote_id() {
		return remote_id;
	}

	public void setRemote_id(int remote_id) {
		this.remote_id = remote_id;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

}
