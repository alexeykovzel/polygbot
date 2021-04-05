package com.alexeykovzel.repository;

import com.alexeykovzel.BotConfig;
import com.amazonaws.services.rdsdata.AWSRDSData;
import com.amazonaws.services.rdsdata.model.*;

abstract class Repository {
    static AWSRDSData rdsData;
    private static final String RESOURCE_ARN = BotConfig.getResourceArn();
    private static final String SECRET_ARN = BotConfig.getSecretArn();
    private static final String DB_NAME = BotConfig.getDbName();

    public static String beginTransaction() {
        BeginTransactionRequest request = new BeginTransactionRequest()
                .withDatabase(DB_NAME)
                .withResourceArn(RESOURCE_ARN)
                .withSecretArn(SECRET_ARN);
        BeginTransactionResult response = rdsData.beginTransaction(request);
        return response.getTransactionId();
    }

    public static void commitTransaction(String transactionId) {
        CommitTransactionRequest request = new CommitTransactionRequest()
                .withTransactionId(transactionId)
                .withResourceArn(RESOURCE_ARN)
                .withSecretArn(SECRET_ARN);
        rdsData.commitTransaction(request);
    }

    public static void executeStatement(String transactionId, String sql) {
        ExecuteStatementRequest executeStatementRequest = new ExecuteStatementRequest()
                .withTransactionId(transactionId)
                .withResourceArn(RESOURCE_ARN)
                .withSecretArn(SECRET_ARN)
                .withSql(sql);
        rdsData.executeStatement(executeStatementRequest);
    }

    public static ExecuteStatementResult executeStatement(String sql) {
        ExecuteStatementRequest executeStatementRequest = new ExecuteStatementRequest()
                .withDatabase(DB_NAME)
                .withResourceArn(RESOURCE_ARN)
                .withSecretArn(SECRET_ARN)
                .withSql(sql);
        return rdsData.executeStatement(executeStatementRequest);
    }
}
