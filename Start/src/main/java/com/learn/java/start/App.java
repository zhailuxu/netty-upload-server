package com.learn.java.start;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan(basePackages = { "com.learn.java.start" })
public class App {


	@RequestMapping("/")
	String home() {
		return "Hello File Uploder Server";
	}

	/**
	 * 多个文件中间用;分隔
	 * @param paths
	 * @return
	 */
	@RequestMapping("/uploadFile")
	String uploadFile(@RequestParam("filePath") String paths) {


		return  paths;
	}


	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public NettyFileUploadServer fileUploadServer() throws Exception {
		NettyFileUploadServer fileUploadServer = new NettyFileUploadServer();
		fileUploadServer.init();
		
		return fileUploadServer;
	}
}
