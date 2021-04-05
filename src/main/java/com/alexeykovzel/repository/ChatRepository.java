package com.alexeykovzel.repository;

import com.amazonaws.services.rdsdata.AWSRDSDataClient;
import com.amazonaws.services.rdsdata.model.ExecuteStatementRequest;
import com.amazonaws.services.rdsdata.model.ExecuteStatementResult;
import com.amazonaws.services.rdsdata.model.Field;

import java.util.List;
import java.util.UUID;

public class ChatRepository extends Repository {

    public static void addChat(String firstName, String lastName, String username, Double memoryStability) {
        rdsData = AWSRDSDataClient.builder().build();
        String uuid = UUID.randomUUID().toString();
        String sql = String.format("insert into chat(id, first_name, last_name, username, memory_stability) values ('%s', '%s', '%s', '%s', %s)",
                uuid, firstName, lastName, username, memoryStability);

        String transactionId = beginTransaction();
        executeStatement(transactionId, sql);
        commitTransaction(transactionId);
    }

    public static void getAllChats() {
        rdsData = AWSRDSDataClient.builder().build();
        String sql = "select * from chat";
        ExecuteStatementResult result = executeStatement(sql);

        for (List<Field> fields : result.getRecords()) {
            String stringValue = fields.get(0).getStringValue();
            String firstName = fields.get(1).getStringValue();
            String lastName = fields.get(2).getStringValue();
            String username = fields.get(3).getStringValue();
            Double memoryStability = fields.get(4).getDoubleValue();

            System.out.println("String value: " + stringValue + "\n"
                    + "First name: " + firstName + "\n"
                    + "Last name: " + lastName + "\n"
                    + "Username: " + username + "\n"
                    + "Memory Stability: " + memoryStability + "\n");
        }
    }
}
