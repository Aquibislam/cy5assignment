package com.aquib.cy5assignment.web.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class OpenSearchConfig {

    @Bean
    public OpenSearchClient getOpenSearchClient() throws JsonProcessingException {
        RestClient restClient = null;
        Map<String,String> secrets = getSecret();
        //Only for demo purposes. Don't specify your credentials in code.
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(secrets.get("opensearch.userName"),secrets.get("opensearch.password")));

        //Initialize the client with SSL and TLS enabled
        restClient = RestClient.builder(new HttpHost("search-cy5assignment-t44piolxkou65mx3h5lxy3iiq4.ap-south-1.es.amazonaws.com", 443, "https")).
                setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                }).build();
        OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        OpenSearchClient client = new OpenSearchClient(transport);
        return client;
    }

    public static Map<String,String> getSecret() throws JsonProcessingException {

        String secretName = "assignment/cy5/secrets";
        String region = "ap-south-1";

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
        // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
        // We rethrow the exception by default.

        String secret=null, decodedBinarySecret=null;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        }

        return new ObjectMapper().readValue(getSecretValueResult.getSecretString(),Map.class);
    }
}
