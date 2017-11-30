package com.yiche.publish.before;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class DynamicStateIP {

	public DynamicStateIP() throws IOException{
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("elasticseach.proptrties") ;
		
		Properties properties = new Properties() ;
		properties.load(resourceAsStream);
		
		properties.setProperty("elasticsearch.cluster-nodes6", "172.20.33.146:9300") ;
		properties.setProperty("elasticsearch.cluster-nodes7", "172.20.33.146:9301") ;
		properties.setProperty("elasticsearch.cluster-nodes9", "172.20.33.146:9302") ;
	}
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		URL resource = DynamicStateIP.class.getClassLoader().getResource("elasticseach.proptrties") ;
		URI uri = resource.toURI() ;
		
		File file = new File(uri) ;
		FileInputStream fileInputStream = new FileInputStream(file) ;
		
		byte[] bytes = new byte[10] ;
		int read = fileInputStream.read(bytes) ;
	}
}
