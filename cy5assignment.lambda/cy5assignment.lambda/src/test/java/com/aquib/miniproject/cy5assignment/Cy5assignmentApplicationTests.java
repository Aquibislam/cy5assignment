package com.aquib.miniproject.cy5assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Cy5assignmentApplicationTests {

	@Autowired
	private Cy5assignmentApplication cy5assignmentApplication;

	@Test
	void contextLoads() {
		System.out.println(cy5assignmentApplication.retrieveUsers(null).apply(null));
	}

}
