import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.cmdi.yjs.po.Person;
import com.cmdi.yjs.po.User;
import com.cmdi.yjs.redis.SerializeUtil;
import com.cmdi.yjs.util.ObjectByteArrayUtils;

public class PersonRedisTest {
	
    private static Jedis jedis=null;
    private static JedisPool masterPool;
    
    /**
     * 初始化Jedis对象
     * 
     * @throws Exception
     */

    
    /**
     * 序列化写对象, 将Person对象写入Redis中
     * 
     * 我们到命令行窗口中读取该对象，看看有没有写入成功：
      * redis 127.0.0.1:6379> get person:100
     * "\xac\xed\x00\x05sr\x00\x15alanland.redis.Person\x05\xf4\x8d9A\xf4`\xb0\x02\x00\x02I\x00\x02idL\x00\x04namet\x00\x12Ljava/lang/String;xp\x00\x00\x00dt\x00\x04alan"
     * 可以取到序列化之后的值。
     * @throws Exception 
     */
    
    
    public static void main(String []args) throws Exception{
    	JedisPoolConfig config = new JedisPoolConfig();
    	masterPool = new JedisPool(config, "127.0.0.1", 6379);
    	jedis = masterPool.getResource();
    	 jedis.select(2);
    	System.out.println(jedis);
    	Set<String> set = jedis.keys("*");
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String str = it.next();
			System.out.println("有一个 ：" + str);
			System.out.println(jedis.get(str.getBytes()));
		}
    	
    	 byte []b1 = "person:100".getBytes();
    	 byte []b2 = "person:101".getBytes();
    	 System.out.println(b1 + " ..." + b2);
    	
//    	 jedis.set(b1, SerializeUtil.serialize(new Person(100, "zhangsan")));
//         jedis.set(b2, SerializeUtil.serialize(new Person(101, "bruce")));
    	 
         
    	 byte []b11 = "person:100".getBytes();
    	 byte []b22 = "person:101".getBytes();
    	 System.out.println(b11 + " ..." + b22);
         byte[] data100= jedis.get(b11);
         System.out.println(data100);
         
 		 List<User> list=new ArrayList<User>();
		 list.addAll((List<User>)ObjectByteArrayUtils.toObject(data100));
		 User person100 = list.get(0);
         System.out.println(String.format("person:100->id=%s,name=%s", person100.getId(), person100.getUsername()));
         masterPool.returnResource(jedis);
         
//         byte[] data101= jedis.get(b22);
//         User person101 = (User) SerializeUtil.unserialize(data101);
//         System.out.println(String.format("person:101->id=%s,name=%s", person101.getId(), person101.getUsername()));
    }

}