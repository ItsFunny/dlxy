package com.dlxy.common.utils;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joker.library.json.GsonInterfaceAdapter;

/**
 * 
 * @author joker
 * @date 创建时间：2018年5月14日 上午10:54:40
 */
public class JsonUtil
{
	private static Gson gson = null;
	static
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Serializable.class, new GsonInterfaceAdapter());
		gson = gsonBuilder.create();
	}

	public static <K,V> Map<K,V> json2Map(String json,Type type)
	{
		return gson.fromJson(json, type);
	}
	public static void main(String[] args)
	{
		String[] arr = new String[]
		{ "1", "2" };
		System.out.println(arr);
	}

	public static String obj2Json(Object object)
	{
		String json = gson.toJson(object);

		return json;
	}

	public static <T> List<T> json2List(String json, Type type)
	{
		return gson.fromJson(json, type);
	}

	public static <T> T json2Object(String json, Class<T> c)
	{
		T t = gson.fromJson(json, c);
		return t;
	}
}
