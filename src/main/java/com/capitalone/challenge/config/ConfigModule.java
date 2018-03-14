package com.capitalone.challenge.config;

import com.capitalone.challenge.IServiceClient;
import com.capitalone.challenge.ServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static com.capitalone.challenge.Constants.*;

public class ConfigModule  extends AbstractModule {
    @Override
    protected void configure() {
        bind(IServiceClient.class).toInstance(new ServiceClient());
        bind(SimpleDateFormat.class).toInstance(new SimpleDateFormat(DATE_FORMAT));
    }

    @Provides
    @Singleton
    public Client httpClient() {
        Client client = ClientBuilder.newClient();
        client.target(API_BASE_URL);
        return client;
    }

    @Provides
    @Singleton
    public ObjectMapper getMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public Properties getProperties() throws Exception {
        InputStream input = ConfigModule.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if(input==null){
            throw new Exception("Sorry, unable to find " + CONFIG_FILE_NAME);
        }

        //load a properties file from class path, inside static method
        Properties properties = new Properties();
        properties.load(input);
        return properties;
    }
}
