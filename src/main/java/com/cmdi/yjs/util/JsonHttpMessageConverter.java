package com.cmdi.yjs.util;



import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cmdi.yjs.jsonp.JSONPObject;

public class JsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    	if (obj instanceof JSONPObject) {
    		JSONPObject jsonp = (JSONPObject) obj;
    		OutputStream out = outputMessage.getBody();
    		String text = jsonp.getFunction() + "(" + JSON.toJSONString(jsonp.getJson(), getFeatures()) + ")";
    		byte[] bytes = text.getBytes(getCharset());
    		out.write(bytes);
	} else {
			super.writeInternal(obj, outputMessage);
	}
    } 
}