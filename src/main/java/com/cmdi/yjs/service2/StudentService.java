package com.cmdi.yjs.service2;



import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmdi.yjs.dao1.UserDAO;
import com.cmdi.yjs.dao2.StudentDAO;
import com.cmdi.yjs.po.Student;
import com.cmdi.yjs.po.User;


@Service("studentService")
@Transactional
public class StudentService {
	
	@Autowired(required=true)
	private StudentDAO studentDAO;
	
	public Student getStudentById(Integer id){
		
		Student student = studentDAO.getStudenByID(id);
		
		return student;
	}
	

	
}
