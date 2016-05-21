package com.cmdi.yjs.redis;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.cmdi.yjs.po.User;
import com.cmdi.yjs.util.ObjectByteArrayUtils;
import com.cmdi.yjs.util.StringUtils;


/**
 * @author hukai 自定义mybatis缓存，访问Redis
 * 
 */
public class RedisCache implements Cache {

	private static Log log = LogFactory.getLog(RedisCache.class);

	private int DATABASE = 1;//访问的具体数据库

	private JedisPool masterPool;
	private String password;

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private String id;

	public RedisCache(String id) {
		this.id = id;

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream infile = cl.getResourceAsStream("redis.properties");
		Properties props = new Properties();
		String masterIp = "";
		int masterPort = 0;
		try {
			props.load(infile);
			masterIp = props.getProperty("redis.host");
			masterPort = Integer.parseInt(props.getProperty("redis.port"));
			password = props.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//创建jedis 连接池
		JedisPoolConfig config = new JedisPoolConfig();
		masterPool = new JedisPool(config, masterIp, masterPort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#clear()
	 */
	@Override
	public void clear() {
		System.out.println("clear");
		Jedis jedis = null;
		try {
			jedis = masterPool.getResource();
			
			if(password != null && !password.equals(""))
				jedis.auth(password);
			jedis.select(DATABASE);
			jedis.flushDB();
			log.info("clear all");
		} finally {
			masterPool.returnResource(jedis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#getObject(java.lang.Object)
	 */
	@Override
	public Object getObject(Object key) {
		Jedis jedis = null;
		Object value = null;
		try {
			jedis = masterPool.getResource();
			if(password != null && !password.equals(""))
				jedis.auth(password);
			jedis.select(DATABASE);
			
			value = ObjectByteArrayUtils.toObject(jedis.get(key.toString().getBytes()));
			log.info("get key = " + key);
		} catch (Exception e) {
			System.out.println(StringUtils.printStackTrace(e));
			log.error("can not get object for " + key);
		} finally {
			masterPool.returnResource(jedis);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#getReadWriteLock()
	 */
	@Override
	public ReadWriteLock getReadWriteLock() {
		return this.readWriteLock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#getSize()
	 */
	@Override
	public int getSize() {
		Jedis jedis = null;
		long size = 0;
		try {
			jedis = masterPool.getResource();
			jedis.auth(password);
			jedis.select(DATABASE);
			size = jedis.dbSize();

			log.info("get size");
		} catch (Exception e) {
			log.error("can not get size");
		} finally {
			masterPool.returnResource(jedis);
		}

		return (int)size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#putObject(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void putObject(Object key, Object value) {
		Jedis jedis = null;
		long size = 0;
		try {
			jedis = masterPool.getResource();
			if(password != null && !password.equals(""))
				jedis.auth(password);
			jedis.select(DATABASE);
			jedis.set(key.toString().getBytes(), ObjectByteArrayUtils.toBytes(value));

			log.info("set key = " + key);
		} catch (Exception e) {
			System.out.println(StringUtils.printStackTrace(e));
		} finally {
			masterPool.returnResource(jedis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.cache.Cache#removeObject(java.lang.Object)
	 */
	@Override
	public Object removeObject(Object key) {
		Jedis jedis = null;
		Object value = null;
		long size = 0;
		try {
			jedis = masterPool.getResource();
			jedis.auth(password);
			jedis.select(DATABASE);
			value = ObjectByteArrayUtils.toObject(jedis.get(((CacheKey)key).toString().getBytes()));
			jedis.del(ObjectByteArrayUtils.toBytes(key));

			log.info("del key = " + key);
		} catch (Exception e) {
			log.error("can not del object for " + key);
			log.error(StringUtils.printStackTrace(e));
		} finally {
			masterPool.returnResource(jedis);
		}
		
		return value;
	}

}
