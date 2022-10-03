package com.dxc.Outstagram.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.entity.Account;
import com.dxc.Outstagram.entity.Post;
import com.dxc.Outstagram.entity.User;
import com.dxc.Outstagram.repository.AccountRepository;
import com.dxc.Outstagram.repository.PostRepository;

@Component
public class PostServices {
	@Autowired
	private PostRepository pr;
	
	@Autowired
	private AccountRepository ar;
	
	// Function: Delete the post
	// Note: 1) Delete media from local system
	// 2) Delete post from DB
	public void deleteMedia(User u, int postId) {
		Account a = ar.findAccountByUserId(u.getUser_id());
		// Get post by same userid and valid accountid
		Post p = pr.getOnePostByUser(a.getAccount_id(), postId);
		String mediaDir = p.getContent();
		// Deleting file from local system
		File file = new File(mediaDir);
		file.delete();
		System.out.println("Media deleted from local system");
		// Delete post from db
		pr.delete(p);
		System.out.println("Post successfully deleted");
	}
	
	public String getEncodedImage(int postId) throws IOException {
		// Get post by postid
		Post p = pr.findById(postId);
		// Get filepath from post
		String filePath = p.getContent();
		// Read and encode the image to base64
		byte[] image = Files.readAllBytes(Paths.get(filePath));
		String encodedImage = Base64.getEncoder().encodeToString(image);
		return encodedImage;
	}
	
	// refactor
	public void addImage(Post post, Account account, String postDir) {
		// link account to post
		post.setAccount(account);
		// set post dir
		post.setContent(postDir);
		// save post
		pr.save(post);
	}
	
	
	// update post by id
	public void updatePostById(Account a, int postId, Post newPost) {
		// get old post of user
		Post oldPost = pr.getOnePostByUser(a.getAccount_id(), postId);
		// set new captions
		oldPost.setCaption(newPost.getCaption());
		// save updated post
		pr.save(oldPost);
	}
	
	
	// admin delete post
	public void deletePostById(int postId) {
		pr.deleteById(postId);
	}
	
	// admin update post
	public void updatePostByIdAdmin(int postId, Post newPost) {
		Post p = pr.findById(postId);
		p.setCaption(newPost.getCaption());
		pr.save(p);
	}
	
	// add post view count + 1
	public void addOnePostView(int postId) {
		Post p = pr.findById(postId);
		p.setViews(p.getViews() + 1);
		pr.save(p);
	}
	
}
