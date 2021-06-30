package com.alexeykovzel.repository;

import com.alexeykovzel.entity.Chat;
import com.amazon.rdsdata.client.RdsDataClient;
import com.amazonaws.services.rdsdata.AWSRDSDataClient;

import java.util.List;
import java.util.UUID;

public class ChatRepository extends Repository {

    public static void addChat(String firstName, String lastName, String username, Double memoryStability) {
        /*client = RdsDataClient.builder()
                .rdsDataService(AWSRDSDataClient.builder().build())
                .database(DB_NAME)
                .secretArn(SECRET_ARN)
                .resourceArn(RESOURCE_ARN).build();*/

        String uuid = UUID.randomUUID().toString();

        client.forSql("INSERT INTO chat(id, first_name, last_name, username, memory_stability) " +
                "VALUES(:id, :firstName, :lastName, :username, :memoryStability)")
                .withParameter("id", uuid)
                .withParameter("firstName", firstName)
                .withParameter("lastName", lastName)
                .withParameter("username", username)
                .withParameter("memoryStability", memoryStability)
                .execute();
    }

    public static List<Chat> getAllChats() {
        client = RdsDataClient.builder()
                .rdsDataService(AWSRDSDataClient.builder().build())
                .database(DB_NAME)
                .secretArn(SECRET_ARN)
                .resourceArn(RESOURCE_ARN).build();

        return client.forSql("SELECT * FROM chat")
                .execute()
                .mapToList(Chat.class);
    }
}
