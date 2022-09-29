package com.example.graphql.webmvc;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
class RandomPortTests {

	private static final String DOCUMENT = """
			{
			  project(slug:"spring-boot") {
				name
			  }
			}
			""";

	@Test
	void getProjectUsingHttp(@Autowired HttpGraphQlTester graphQlTester) {
		graphQlTester.document(DOCUMENT).execute().path("project.name").entity(String.class).isEqualTo("Spring Boot");
	}

}