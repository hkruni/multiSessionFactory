package com.cmdi.yjs.dao2;

import org.apache.ibatis.annotations.Param;

import com.cmdi.yjs.po.Student;

public interface StudentDAO {
	
	public Student getStudenByID(@Param(value = "id") Integer id);
}
