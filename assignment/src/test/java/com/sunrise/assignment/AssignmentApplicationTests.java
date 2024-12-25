package com.sunrise.assignment;

import com.sunrise.assignment.service.ProductService;
import com.sunrise.assignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AssignmentApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
	}

}
