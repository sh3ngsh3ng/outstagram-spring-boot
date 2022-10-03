package com.dxc.Outstagram.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="account")
@EntityListeners(AuditingEntityListener.class)
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="account_id")
	private int account_id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id", referencedColumnName="user_id")
//	@CreatedBy
	private User user;
	
	@Column(name="account_type")
	private String account_type;
	
	
	// constructors
	Account(){}

	public Account(User user, String account_type) {
		super();
		this.user = user;
		this.account_type = account_type;
	}
	
	// Audit Fields
	@CreatedDate
	private Instant createdDate;

	
//	@Column(name="modify_by")
//	private User modify_by;

	
	


	// Relationship
	@OneToMany(mappedBy="account",cascade= CascadeType.ALL)
	private List<Post> post;

	
	@ManyToMany(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(
		name="likes", 
		joinColumns = @JoinColumn(name="account_id"), 
		inverseJoinColumns=@JoinColumn(name="post_id"))
	Set <Post> likedPosts;

	// getters and setters
	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}



	// to string
	@Override
	public String toString() {
		return "Account [account_id=" + account_id + ", account_type=" + account_type + "]";
	}
	
	public void addLikes(Post p) {
		if (likedPosts == null) {
			likedPosts = new HashSet<>();
		}
		likedPosts.add(p);
	}
	
	
	
}
