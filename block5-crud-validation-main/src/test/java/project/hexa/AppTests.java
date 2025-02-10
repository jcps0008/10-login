package project.hexa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.hexa.components.StringConstants.APP_MESSAGE_CONTEXT;
import static project.hexa.components.StringConstants.APP_MESSAGE_NO_EXCEPTION;

@SpringBootTest
class AppTests {
	@Test
	void contextLoads() {
		assertTrue(true, APP_MESSAGE_CONTEXT);
	}
	@Test
	void mainMethodRunsSuccessfully() {
		assertDoesNotThrow(() -> App.main(new String[] {}),
				APP_MESSAGE_NO_EXCEPTION);
	}


}
