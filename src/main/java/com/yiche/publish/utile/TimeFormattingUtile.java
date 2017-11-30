package com.yiche.publish.utile;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeFormattingUtile {

	static Logger logger = LoggerFactory.getLogger(TimeFormattingUtile.class) ;
	
	public static long getFormatStr(String dateFormat){
		SimpleDateFormat formatter = null ;
		Date format = null ;
		long time = 0 ;
		try{
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format = formatter.parse(dateFormat) ;
			time = format.getTime() ;
		}catch(Exception e){
			logger.error("格式化时间出现错误，ERROR：",e);
		}
		return time ;
	}
	
	public static String getTimeFormat(Date date) {
		SimpleDateFormat formatter = null ;
		String format = null ;
		try{
			formatter = new SimpleDateFormat("yyyy.MM.dd");
			format = formatter.format(date) ;
		}catch(Exception e){
			logger.error("格式化时间出现错误，ERROR：",e);
		}
		return format ;
	}
	
	public static String getLongToTimeFormat(Long timeLong){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss") ;
		String format = null ;
		try {
			format = dateFormat.format(new Date(timeLong));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("格式化时间出现错误，ERROR：",e);
		}
		
		return format ;
	}
}
