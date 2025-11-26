package com.gft.tag_ms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "https://tag-ms.onrender.com",
                        description = "Render Production"
                )
        }
)
@SpringBootApplication
public class TagMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TagMsApplication.class, args);
	}

}
