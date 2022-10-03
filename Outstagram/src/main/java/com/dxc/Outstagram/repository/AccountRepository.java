package com.dxc.Outstagram.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dxc.Outstagram.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
	@Query(value="select * from ACCOUNT where user_id=:userid", nativeQuery=true)
	Account findAccountByUserId(@Param("userid") int userId);
	
	@Query(value="select exists(select * from LIKES where account_id=:accountid AND post_id=:postid)", nativeQuery=true)
	int checkIfPostLiked(@Param("accountid") int accountId, @Param("postid") int postId);
	
}
