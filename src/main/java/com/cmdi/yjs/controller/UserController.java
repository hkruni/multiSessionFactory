package com.cmdi.yjs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdi.yjs.po.Student;
import com.cmdi.yjs.po.User;
import com.cmdi.yjs.service.UserService;
import com.cmdi.yjs.service2.StudentService;
import com.cmdi.yjs.util.ObjectByteArrayUtils;

@Controller
public class UserController {

	private static Log log = LogFactory.getLog(UserController.class);

	@Autowired(required = true)
	private UserService userService;

	@Autowired(required = true)
	private StudentService studentService;

	@Autowired(required = true)
	private RedisTemplate redisTemplate;

	@RequestMapping(value = "/testPost", method = RequestMethod.POST)
	@ResponseBody
	public Map testPost(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		Map map = new HashMap();
		map.put("name", username);
		map.put("value", password);

		return map;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Map test() {

		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {

				connection.select(2);
				byte[] object = connection.get("person:100".getBytes());
				try {
					Object value = ObjectByteArrayUtils.toObject(object);
					System.out.println(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

		});

		Map map = new HashMap();
		map.put("name", "tencent");
		map.put("value", "weixin");

		return map;

	}

	@RequestMapping(value = "/testresult", method = RequestMethod.GET)
	@ResponseBody
	public Map testresult() {
		List<User> userList = userService.getUserByName("hukai");
		Student student = studentService.getStudentById(1);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userList", userList);
		map.put("student", student);

		return map;

	}

	/**
	 * 
	 * @param tid
	 * @param rid
	 * @param jsoncallback
	 * @return
	 */
	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	@ResponseBody
	public Map GetItemSiteList(@RequestParam("username") String username) {

		List<User> list = userService.getUser(username);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("totalCount", 1);
		map.put("root", list);

		return map;

	}

	/**
	 * 
	 * @param tid
	 * @param rid
	 * @param jsoncallback
	 * @return
	 */
	@RequestMapping(value = "/getUserByName", method = RequestMethod.GET)
	@ResponseBody
	public Map getUserByName(@RequestParam("username") String username) {

		List<User> user = userService.getUserByName(username);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("root", user);

		return map;

	}

	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	@ResponseBody
	public Map addUser(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		Map<String, Object> map = new HashMap<String, Object>();

		return map;

	}

}
