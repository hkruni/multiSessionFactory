package com.cmdi.yjs.service;



import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmdi.yjs.dao1.UserDAO;
import com.cmdi.yjs.po.User;


@Service("userService")
@Transactional
public class UserService {
	
	@Autowired(required=true)
	private UserDAO userDAO;
	
	public List<User> getUser(String username){
		
		List<User> list = userDAO.getUser(username);
		
		return list;
	}
	
	public List<User> getUserByName(String username ){
		List<User> user = userDAO.getUserByName(username);
//		for(User u : user){
//			System.out.println("name " + u.getUsername() + " password " + u.getPassword());
//		}
		return user;
	}
	
	public User addUser(String username,String password){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userDAO.addUser(user);
		return user;
	}
	
}
