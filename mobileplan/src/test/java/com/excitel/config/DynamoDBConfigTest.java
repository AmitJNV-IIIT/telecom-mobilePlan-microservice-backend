package com.excitel.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DynamoDBConfig.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class DynamoDBConfigTest {
    @MockBean
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBConfig dynamoDBConfig;

    /**
     * Method under test: {@link DynamoDBConfig#amazonDynamoDB()}
     */
    @Test
    void testAmazonDynamoDB() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange and Act
        dynamoDBConfig.amazonDynamoDB();
    }
}
