package com.dxc.Outstagram.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dxc.Outstagram.entity.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
	public Post findById(int id);
	
//	@Modifying
//	@Transactional
	@Query(value="select * from POST where account_id=:accountId AND post_id=:postId", nativeQuery=true)
	Post getOnePostByUser(@Param("accountId") int accountId, @Param("postId") int postId);
	
	@Query(value="select * from POST where account_id=:accountId", nativeQuery=true)
	ArrayList<Post> getAllPostByUser(@Param("accountId") int accountId);
	
}
