package com.example.appmarket.util;


import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * 
 * json 与java bean 、string之间的转换
 * @author tuomao
 *
 */
public class JsonUtil {
	
	
	/**  
	 * 从一个JSON 对象字符格式中得到一个简单的java对象  
	 * （不包含其他的对象，map等复杂对象）
	 *
	 *  该java对象必须有相应属性的get 和set方法
	 * @param jsonString  
	 * @param beanCalss  
	 * @return  
	 */
	public static <T> T jsonToSimpleBean(String jsonString, Class<T> beanCalss) {
		Gson gson=new GsonBuilder().create(); 
		T bean=gson.fromJson(jsonString, beanCalss);
		return bean;
	}
	
	/**
	 * 
	 *  从一个JSON 对象字符格式中得到一个复杂的java对象  
	 * （list,map,包含其他对象等）
	 *
	 *  该java对象必须有相应属性的get 和set方法
	 * @param jsonString
	 * @param type 要转化的bean的类型  
	 * eg:若要转化成School这样一个bean，则type为new TypeToken<School>(){}.getType()
	 * @return
	 */
	public static <T> T jsonToComplexBean(String jsonString,Type type) {
		Gson gson=new GsonBuilder().create(); 
		T bean=gson.fromJson(jsonString, type);
		return bean;
	}
	/**
	 * 
	 * 将java对象转换成json字符串
	 * @param bean
	 * @param type
	 * @return
	 */
	public static String beanToJson(Object bean,Type type) {
		Gson gson=new GsonBuilder().create();
		return gson.toJson(bean, type); 
	}
	
	/**
	 * 
	 * 将string转化为jsonobject
	 * @param json
	 * @return
	 */
	public static JsonObject stringToJsonObject(String json){
		JsonParser parser=new JsonParser();
		return parser.parse(json).getAsJsonObject();
	}
	
	
	/**
	 * 
	 * 将string转化为jsonoarray
	 * @param json
	 * @return
	 */
	public static JsonArray stringToJsonArray(String json){
		JsonParser parser=new JsonParser();
		return parser.parse(json).getAsJsonArray();
	}

}