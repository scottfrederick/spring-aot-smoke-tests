package com.example.graphql.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

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
	@LocalServerPort
	private int localPort;

	@Test
	void getProjectUsingHttp(@Autowired HttpGraphQlTester graphQlTester) {
		graphQlTester.document(DOCUMENT).execute().path("project.name").entity(String.class).isEqualTo("Spring Boot");
	}

	@Test
	void getProjectUsingWebSocket() {
		WebSocketClient webClient = new ReactorNettyWebSocketClient();
		WebSocketGraphQlClient graphQlClient = WebSocketGraphQlClient
				.builder("http://localhost:" + localPort + "/graphql", webClient).build();
		Mono<String> projectName = graphQlClient.document(DOCUMENT).retrieve("project.name").toEntity(String.class);
		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
	}

//	@Test
//	void getProjectUsingRSocket() {
//		URI uri = URI.create("http://localhost:" + localPort +  "/graphql");
//		WebsocketClientTransport transport = WebsocketClientTransport.create(uri);
//		RSocketGraphQlClient graphQlClient = RSocketGraphQlClient.builder().clientTransport(transport).build();
//		Mono<String> projectName = graphQlClient.document(DOCUMENT).retrieve("project.name").toEntity(String.class);
//		StepVerifier.create(projectName).expectNext("Spring Boot").expectComplete().verify();
//	}

}