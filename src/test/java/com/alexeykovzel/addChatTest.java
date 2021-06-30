package com.alexeykovzel;

import com.amazon.rdsdata.client.RdsDataClient;
import com.amazonaws.services.rdsdata.AWSRDSData;
import com.amazonaws.services.rdsdata.AWSRDSDataClient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class addChatTest {
    private static final String RESOURCE_ARN = "arn:aws:rds:us-east-2:574318152798:cluster:polygbot-cluster";
    private static final String SECRET_ARN = "arn:aws:secretsmanager:us-east-2:574318152798:secret:RDSSecret-flIq4l3NCrdW-rEvOVb";
    private static final String DB_NAME = "polygbotDB";

    @Test
    public void addChat() {
        /*AWSRDSData rdsData = AWSRDSDataClient.builder().build();

        RdsDataClient client = RdsDataClient.builder()
                .rdsDataService(rdsData)
                .database(DB_NAME)
                .secretArn(SECRET_ARN)
                .resourceArn(RESOURCE_ARN).build();

        String uuid = UUID.randomUUID().toString();

        client.forSql("INSERT INTO chat(id) VALUES(:id)")
                .withParameter("id", uuid)
                .execute();*/
    }
}
