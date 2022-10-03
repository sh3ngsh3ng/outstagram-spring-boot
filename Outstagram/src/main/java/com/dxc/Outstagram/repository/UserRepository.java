package com.dxc.Outstagram.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dxc.Outstagram.classes.Login;
import com.dxc.Outstagram.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	// validate user login
	// validation DOES NOT account for multiple username
	// i.e. if more than 1 same username, login might error
//	default boolean validateUser(Login lg) {
//		String username = lg.username;
//		String password = lg.password;
//		
//		User u = findUserByUsername(username);
//		if (password.equals(u.getPassword())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	@Query(value = "select * from USER where username=:username", nativeQuery=true)
	User findUserByUsername(@Param("username") String username);

}
