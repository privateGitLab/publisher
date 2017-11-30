package com.yiche.publish.utile;

import com.google.gson.Gson;

public class JsonUtile {

	public static String objectToJson(Object args){
		
		if (args != null) {
			Gson gson = new Gson() ;
			return gson.toJson(args) ;
		}
		return null ;
	}
}
