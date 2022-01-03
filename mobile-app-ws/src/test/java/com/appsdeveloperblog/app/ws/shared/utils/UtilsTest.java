package com.appsdeveloperblog.app.ws.shared.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// @ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testGeneratePublicId() {
	int strLength = 30;

	String result = utils.generatePublicId(strLength);

	assertTrue(result.length() == strLength);
    }

}
