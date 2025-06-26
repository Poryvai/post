package com.poryvai.post;

import com.poryvai.post.util.BasePgSqlIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test class for the Spring Boot application.
 * This test verifies that the application context loads successfully, ensuring
 * that all beans are correctly configured and wired.
 * It extends {@link BasePgSqlIT} to provide a running PostgreSQL database
 * for the test environment.
 */
@SpringBootTest
class DemoApplicationTests implements BasePgSqlIT {

	/**
	 * A simple test method that asserts the successful loading of the Spring application context.
	 * If the context fails to load, this test will fail, indicating a configuration issue.
	 */
	@Test
	void contextLoads() {
	}

}
