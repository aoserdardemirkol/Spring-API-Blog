package com.folksdev.blog;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(
			@Value("FolksDev Blog API") String title,
			@Value("1.0") String version,
			@Value("FolksDev SpringBoot Bootcamp süresince geliştirilen blog projesi") String description
	) {
		return new OpenAPI()
				.info(new Info()
						.title(title)
						.version(version)
						.description(description)
						.contact(new Contact()
								.name("Ali Osman Serdar Demirkol")
								.url("https://github.com/aoserdardemirkol/Spring-API-Blog")
								.email("aoserdardemirkol@gmail.com"))
						.license(new License().name("Blog API Licence").url("https://github.com/aoserdardemirkol/Spring-API-Blog/blob/master/LICENCE")));	}
}
