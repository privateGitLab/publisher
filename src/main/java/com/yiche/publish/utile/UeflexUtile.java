package com.yiche.publish.utile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Document;

import com.yiche.publish.entity.Article;

public class UeflexUtile<T> {

	/**
	 * 将注解的值还原为初始值
	 */
	public String deoxidizeInitialValue()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Class<? extends Object> clazz = Article.class ;

		// 获取类上注解实例
		Annotation annotation = clazz.getAnnotation(Document.class);

		// 获取 annotation 这个代理实例所持有的 InvocationHandler
		InvocationHandler h = Proxy.getInvocationHandler(annotation);

		// 获取 AnnotationInvocationHandler 的 memberValues 字段
		Field hField = h.getClass().getDeclaredField("memberValues");

		// 因为这个字段事 private final 修饰，所以要打开权限
		hField.setAccessible(true);

		// 获取 memberValues
		Map<String, String> memberValues = (Map) hField.get(h);

		// 修改 value 属性值
		String put = memberValues.put("indexName","test_the_text_is_released_index_");
		
		return getObjectAnnotationName(new Article()) ;
	}

	/**
	 * 获取类上的注解的属性值
	 * 
	 * @param t
	 * @return
	 */
	public String getObjectAnnotationName(Article article) {
		if (article == null) {
			throw new MyException("参数不能为NULL，会造成空指针异常");
		}
		Class<? extends Object> clazz = article.getClass();

		Document annotation = clazz.getAnnotation(Document.class);
		String indexName = annotation.indexName();
		return indexName;
	}

	/**
	 * 对类上注解的属性值进行赋值
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void setField(T t, Class<? extends Annotation> annotationClass, String fieldName, String fieldValue)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Class<? extends Object> clazz = t.getClass();

		// 获取类上注解实例
		Annotation annotation = clazz.getAnnotation(annotationClass);

		// 获取 annotation 这个代理实例所持有的 InvocationHandler
		InvocationHandler h = Proxy.getInvocationHandler(annotation);

		// 获取 AnnotationInvocationHandler 的 memberValues 字段
		Field hField = h.getClass().getDeclaredField("memberValues");

		// 因为这个字段事 private final 修饰，所以要打开权限
		hField.setAccessible(true);

		// 获取 memberValues
		Map<String, String> memberValues = (Map) hField.get(h);

		// 修改 value 属性值
		String put = memberValues.put(fieldName, fieldValue);
	}
}
