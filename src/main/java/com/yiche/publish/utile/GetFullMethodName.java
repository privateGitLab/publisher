package com.yiche.publish.utile;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFullMethodName {

	static Logger logger = LoggerFactory.getLogger(GetFullMethodName.class) ;
	
	/**
	 * 通过反射获取方法的全名
	 * @param objectClazz
	 * @param methodName
	 * @param 
	 * @return
	 */
	public static String getFullMethodName(Class<?> objectClazz,String methodName,Class<?> ... parameterTypes){
		
		Method method = null ;
		String name = null ;
		String className = null ;
		String classAndMethodName = null ;
		try {
			method = objectClazz.getDeclaredMethod(methodName, parameterTypes) ;
			methodName = method.getName() ;
			className = objectClazz.getName() ;
			classAndMethodName = className + "." + methodName ;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			logger.error("通过反射获取方法的名出现错误ERROR：");
		} catch (SecurityException e) {
			e.printStackTrace();
			logger.error("通过反射获取方法的名出现错误ERROR：");
		}
		return classAndMethodName ;
	} 
	
}
