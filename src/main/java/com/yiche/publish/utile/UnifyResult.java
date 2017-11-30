package com.yiche.publish.utile;

//将接口返回数据封装到类中
public class UnifyResult {

	// 状态码
	private int code;

	// 服务器像浏览器返回数据
	private String body;
	
	public UnifyResult(int code,String body) {
		this.code = code ;
		this.body = body ;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "UnifyResult [code=" + code + ", body=" + body + "]";
	}

}
