package com.cmdi.yjs.session;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.cmdi.yjs.util.ObjectByteArrayUtils;
import com.cmdi.yjs.util.StringUtils;



/**
 * @author xiaoqingli
 *
 */
public class SessionService {

	private static Log log = LogFactory.getLog(SessionService.class);

	private static SessionService instance = null;

	JedisPool masterPool = null;
	JedisPool slavePool = null;

	private int expire = 3600;
	private String password = "";

	public static synchronized SessionService getInstance() {
		if (instance == null) {
			instance = new SessionService();
		}
		return instance;
	}

	private SessionService() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream infile = cl.getResourceAsStream("redis.properties");
		Properties props = new Properties();
		String masterIp = "";
		int masterPort = 0;
		String slaveIp = "";
		int slavePort = 0;
		try {
			props.load(infile);
			masterIp = props.getProperty("redis-server-master-ip");
			masterPort = Integer.parseInt(props
					.getProperty("redis-server-master-port"));
			slaveIp = props.getProperty("redis-server-slave-ip");
			slavePort = Integer.parseInt(props
					.getProperty("redis-server-slave-port"));
			expire = Integer.parseInt(props.getProperty("expire"));
			password = props.getProperty("password");
		} catch (Exception e) {
			log.error(e, e.getCause());
		}

		createMasterPool(masterIp, masterPort);
		createSlavePool(slaveIp, slavePort);

	}

	private void createSlavePool(String slaveIp, int slavePort) {
		JedisPoolConfig config = new JedisPoolConfig();
		slavePool = new JedisPool(config, slaveIp, slavePort);

	}

	private void createMasterPool(String masterIp, int masterPort) {
		JedisPoolConfig config = new JedisPoolConfig();
		masterPool = new JedisPool(config, masterIp, masterPort);
	}

	protected void finalize() {
		if (this.masterPool != null) {
			this.masterPool.destroy();
		}
		if (this.slavePool != null) {
			this.slavePool.destroy();
		}
	}

	public Object hget(String sid, String key) {
		
		MyJedis myJedis = new MyJedis(masterPool, slavePool);
		try {
			Jedis jedis = myJedis.getJedis();
			jedis.auth(password);
			Object value = ObjectByteArrayUtils.toObject(jedis.hget(sid.getBytes(), key.getBytes()));
			jedis.expire(sid.getBytes(), expire);
			log.info("hget key:" + key + " for sid:"+sid);
			return value;
		} catch(Exception e) {
			log.error(StringUtils.printStackTrace(e));
		} finally {
			myJedis.returnJedis();
		}
		
		return null;
	}

	public void del(String sid) {
		MyJedis myJedis = new MyJedis(masterPool, slavePool);
		try {
			Jedis jedis = myJedis.getJedis();
			jedis.auth(password);
			jedis.del(sid.getBytes());
			log.info("del sid:" + sid);
		} finally {
			myJedis.returnJedis();
		}
	}

	public void hdel(String sid, String key) {
		MyJedis myJedis = new MyJedis(masterPool, slavePool);
		try {
			Jedis jedis = myJedis.getJedis();
			jedis.auth(password);
			jedis.hdel(sid.getBytes(), key.getBytes());
			jedis.expire(sid.getBytes(), expire);
			log.info("hdel key:" + key + " for sid:"+sid);
		} finally {
			myJedis.returnJedis();
		}
	}

	public void hset(String sid, String key, Object value) {
		MyJedis myJedis = new MyJedis(masterPool, slavePool);
		try {
			Jedis jedis = myJedis.getJedis();
			jedis.auth(password);
			jedis.hset(sid.getBytes(), key.getBytes(), ObjectByteArrayUtils.toBytes(value));
			jedis.expire(sid, expire);
			log.info("hset key:" + key + " for sid:"+sid);
		}  catch(Exception e) {
			log.error(StringUtils.printStackTrace(e));
		} finally {
			myJedis.returnJedis();
		}

	}

	public Set<String> hkeys(String sid) {
		Set<byte[]> keys = null;
		Set<String> stringKeys = new HashSet<String>();
		MyJedis myJedis = new MyJedis(masterPool, slavePool);
		try {
			Jedis jedis = myJedis.getJedis();
			jedis.auth(password);
			keys = jedis.hkeys(sid.getBytes());
			for(byte[] key : keys) {
				stringKeys.add(String.valueOf(key));
			}
		} finally {
			myJedis.returnJedis();
		}
		
		return stringKeys;
	}

	public static class MyJedis {
		private Jedis jedis;
		private JedisPool pool;

		private JedisPool masterPool;
		private JedisPool slavePool;

		public MyJedis(JedisPool masterPool, JedisPool slavePool) {
			this.masterPool = masterPool;
			this.slavePool = slavePool;
		}

		public Jedis getJedis() {
			try {
				jedis = masterPool.getResource();
				pool = masterPool;
			} catch (Exception e) {
				log.error("Master redis is crash, switch to slave");
				jedis = slavePool.getResource();
				pool = slavePool;
			}

			return jedis;
		}

		public void returnJedis() {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}
}
