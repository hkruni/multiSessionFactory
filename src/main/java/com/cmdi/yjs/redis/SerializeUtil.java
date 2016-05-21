package com.cmdi.yjs.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {  
    public static byte[] serialize(Object object) {  
            ObjectOutputStream oos = null;  
            ByteArrayOutputStream baos = null;  
            try {  
                    // 序列化  
                    baos = new ByteArrayOutputStream();  
                    oos = new ObjectOutputStream(baos);  
                    oos.writeObject(object);  
                    byte[] bytes = baos.toByteArray();  
                    return bytes;  
            } catch (Exception e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }  

    public static Object unserialize(byte[] bytes) {  
            if(bytes == null)return null;  
            ByteArrayInputStream bais = null;  
            try {  
                    // 反序列化  
                    bais = new ByteArrayInputStream(bytes);  
                    ObjectInputStream ois = new ObjectInputStream(bais);  
                    return ois.readObject();  
            } catch (Exception e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }
    
    public static void main(String []args){
    	String key = "1880842226:1000290837:com.cmdi.yjs.dao.UserDAO.getUserByName:0:2147483647:select * from user u where u.username=?:hukai";
    	System.out.println(key);
    	byte []value =  serialize(key);
    	System.out.println(value);
    	
    	byte []value1 = serialize(key);
    	System.out.println(value1);
    	
    	System.out.println(unserialize(value).toString());
    	System.out.println(unserialize(value1).toString());
    }
}
