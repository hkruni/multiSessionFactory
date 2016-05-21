package com.cmdi.yjs.redis;

import java.util.concurrent.locks.ReadWriteLock;  
import java.util.concurrent.locks.ReentrantReadWriteLock;  
  
import org.apache.ibatis.cache.Cache;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
  
import redis.clients.jedis.Jedis;  
import redis.clients.jedis.JedisPool;  
import redis.clients.jedis.JedisPoolConfig;  
  
  
public class MybatisRedisCache implements Cache {  
      
    private static Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);  
    private JedisPool jedisPool=createReids();  
     /** The ReadWriteLock. */    
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();   
      
    private String id;  
      
    public MybatisRedisCache(final String id) {    
        if (id == null) {  
            throw new IllegalArgumentException("Cache instances require an ID");  
        }  
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>MybatisRedisCache:id="+id);  
        this.id = id;  
    }    
    @Override  
    public String getId() {  
        return this.id;  
    }  
  
    @Override  
    public int getSize() {  
     
    	Jedis jedis = jedisPool.getResource();
        return Integer.valueOf(jedis.dbSize().toString());  
    }  
  
    @Override  
    public void putObject(Object key, Object value) { 
    	
    	Jedis jedis = jedisPool.getResource();
    	
    	System.out.println("put redisClient :" + jedis);
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>putObject:"+"person:100".getBytes()+"="+value);  
    	jedis.set("person:100".getBytes(), SerializeUtil.serialize(value));  
    	jedisPool.returnResource(jedis);
    }  
  
    @Override  
    public Object getObject(Object key) {  
    	Jedis jedis = jedisPool.getResource();
    	System.out.println("get redisClient :" + jedis);
        Object value = SerializeUtil.unserialize(jedis.get("person:100".getBytes()));  
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>getObject:"+"person:100".getBytes()+"="+value); 
        jedisPool.returnResource(jedis);
        return value;  
    }  
  
    @Override  
    public Object removeObject(Object key) {  
    	Jedis jedis = jedisPool.getResource();
        return jedis.expire(SerializeUtil.serialize(key.toString()),0);  
    }  
  
    @Override  
    public void clear() {  
    	Jedis jedis = jedisPool.getResource();
    	jedis.flushDB(); 
    	 jedisPool.returnResource(jedis);
    	
    }  
    @Override  
    public ReadWriteLock getReadWriteLock() {  
        return readWriteLock;  
    }  
    protected  JedisPool  createReids(){ 
    	System.out.println("createReids");
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1",6379); 
        return pool;
    }  
}