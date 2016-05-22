package com.cmdi.yjs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmdi.yjs.dao1.UserDAO;
import com.cmdi.yjs.dao2.StudentDAO;
import com.cmdi.yjs.po.Student;
import com.cmdi.yjs.po.User;

@Service("userService")
@Transactional
public class UserService {

	@Autowired(required = true)
	private UserDAO userDAO;

	@Autowired(required = true)
	private StudentDAO studentDAO;

	public List<User> getUser(String username) {

		List<User> list = userDAO.getUser();

		return list;
	}

	public List<User> getUserByName(String username) {
		List<User> user = userDAO.getUserByName(username);
		for (User u : user) {
			System.out.println("name " + u.getUsername() + " password "
					+ u.getPassword());
		}
		return user;
	}

	public User add(String username, String password, String name, Integer age) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		Student student = new Student();
		student.setName(name);
		student.setAge(age);
		userDAO.addUser(user);
		int x = 1 / 0;
		studentDAO.addStudent(student);
		// List<User> list = userDAO.getUser();
		// User u = list.get(1);
		// System.out.println(u.getUsername());

		return user;
	}
}
