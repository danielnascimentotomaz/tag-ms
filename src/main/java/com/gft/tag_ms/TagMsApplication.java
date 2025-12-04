package com.gft.tag_ms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
public class TagMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TagMsApplication.class, args);
	}

}
