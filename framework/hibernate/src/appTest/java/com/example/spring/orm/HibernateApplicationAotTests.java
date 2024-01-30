package com.example.spring.orm;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import org.springframework.aot.smoketest.support.assertj.AssertableOutput;
import org.springframework.aot.smoketest.support.junit.ApplicationTest;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
class HibernateApplicationAotTests {

	@Test
	void entityGraph(AssertableOutput output) {
		Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
			assertThat(output)
				.hasLineMatching(".*left join \\(?book_authors a1_0 .*")
				.hasSingleLineContaining(
						"namedEntityGraph: Book{title='Spring in Action', authors=[Author{name='Craig Walls'}]}");
		});
	}

}
