package com.alexeykovzel.repository;

import com.alexeykovzel.BotConfig;
import com.amazon.rdsdata.client.RdsDataClient;

abstract class Repository {
    static RdsDataClient client;
    static final String DB_NAME = BotConfig.getDBName();
    static final String SECRET_ARN = BotConfig.getSecretArn();
    static final String RESOURCE_ARN = BotConfig.getResourceArn();

}
