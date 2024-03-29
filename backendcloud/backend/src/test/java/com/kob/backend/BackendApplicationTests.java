package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

	@Test
	void contextLoads() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("pyxc"));
//		System.out.println(passwordEncoder.encode("pcarol"));
//		System.out.println(passwordEncoder.encode("pdave"));
		System.out.println(passwordEncoder.matches("pyxc", "$2a$10$A1KFWEwnKRk1KVxdUaKdF.sWH5Gxwe272JPH7ul5mFLMuXRGS0Yeq"));
	}

}
