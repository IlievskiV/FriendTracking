package com.friendtracking.database;

import java.util.List;

import com.friendtracking.model.Tag;

public interface TagInterface {

	public boolean addTag(Tag tag);
	
	public List<Tag> getTagsByActivity(long activity_id);
	
	public List<Tag> getUnpublishedTags();
	
	public boolean updateTags(List<Tag> tags);
	
	public boolean updateTag(Tag tag);
	
	public List<Tag> getAllTags();
}
