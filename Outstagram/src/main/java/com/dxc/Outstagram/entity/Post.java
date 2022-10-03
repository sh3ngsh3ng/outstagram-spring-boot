package com.dxc.Outstagram.entity;


import java.time.Instant;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="post")
@EntityListeners(AuditingEntityListener.class)
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="post_id")
	private int post_id;
	@Column(name="post_type")
	private String post_type;
	@Column(name="caption")
	private String caption;
	@Column(name="content")
	private String content;
	@Column(name="views")
	private int views;
		
	// constructors
	Post() {}
		
	public Post(String post_type, String caption, String content) {
		super();
		this.post_type = post_type;
		this.caption = caption;
		this.content = content;
		this.views = 0;
	}

	
	// Relationship
	@ManyToOne
	@JoinColumn(name="account_id", nullable=false)
	private Account account;
	
	@ManyToMany(mappedBy="likedPosts")
	Set <Account> likes;
	
	
	// Audit Fields
	@CreatedDate
	private Instant createdDate;
	
//	@CreatedBy
//	private String createdBy;
	
	
	// setters and getters
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	


	public Account getAccount() {
		return account;
	}


	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Post [post_id=" + post_id + ", post_type=" + post_type + ", caption=" + caption + ", content=" + content
				+ ", views=" + views + ", account=" + account + ", createdDate=" + createdDate + "]";
	}

	

	
	
	
}
