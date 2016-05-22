import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cmdi.yjs.service.UserService;

public class PersonRedisTest {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		UserService service = (UserService) context.getBean("userService");
		service.add("kaka", "22222222", "cluo", 28);
	}
}