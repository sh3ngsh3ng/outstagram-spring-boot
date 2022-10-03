package com.dxc.Outstagram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dxc.Outstagram.entity.Account;
import com.dxc.Outstagram.entity.User;
import com.dxc.Outstagram.repository.AccountRepository;

@Component
public class AccountServices {
	@Autowired 
	private AccountRepository ar;
	
	
	public Account findAccountByUserId(int userId) {
		return ar.findAccountByUserId(userId);
	}
	
	public Account createAccountForUser(User u) {
		return ar.save(new Account(u, "normal"));
	}
	
	public int checkIfPostIsLiked(int accountId, int postId) {
		return ar.checkIfPostLiked(accountId, postId);
	}
	
}
