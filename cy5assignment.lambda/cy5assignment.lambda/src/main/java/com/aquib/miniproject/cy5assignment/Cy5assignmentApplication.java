package com.aquib.miniproject.cy5assignment;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.*;
import com.amazonaws.services.identitymanagement.model.Tag;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootApplication
public class Cy5assignmentApplication {


    public static void main(String[] args) throws IOException {
        SpringApplication.run(Cy5assignmentApplication.class, args);
    }




    @Bean
    public RestHighLevelClient getRestHighLevelClient() throws IOException {
        //Establish credentials to use basic authentication.
        //Only for demo purposes. Don't specify your credentials in code.
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Map<String,String> secrets = getSecret();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(secrets.get("opensearch.userName"),secrets.get("opensearch.password")));

        //Create a client.
        RestClientBuilder builder = RestClient.builder(new HttpHost("search-cy5assignment-t44piolxkou65mx3h5lxy3iiq4.ap-south-1.es.amazonaws.com", 443, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        return new RestHighLevelClient(builder);
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
    @Bean
    public Function<String, String> retrieveUsers(@Autowired RestHighLevelClient restHighLevelClient) {
        return new Function<String, String>() {
            @Override
            public String apply(String input) {

                final AmazonIdentityManagement iam =
                        AmazonIdentityManagementClientBuilder.defaultClient();

                boolean done = false;
                ListUsersRequest request = new ListUsersRequest();
                while (!done) {
                    ListUsersResult response = iam.listUsers(request);

                    for (User user : response.getUsers()) {
                        UserData userData = new UserData();
                        ListUserTagsRequest listUserTagsRequest = new ListUserTagsRequest();
                        listUserTagsRequest.setUserName(user.getUserName());
                        ListUserTagsResult result = iam.listUserTags(listUserTagsRequest);
                        Optional<Tag> purposeTag=result.getTags().stream().filter(tag->tag.getKey().equals("Purpose")).findFirst();

                        if(purposeTag.isPresent() && !purposeTag.get().getValue().equals("Assignment")){
                            continue;
                        }

                        ListGroupsForUserRequest listGroupsForUserRequest = new ListGroupsForUserRequest();
                        listGroupsForUserRequest.setUserName(user.getUserName());
                        ListGroupsForUserResult groupsForUserResult = iam.listGroupsForUser(listGroupsForUserRequest);

                        IndexRequest indexRequest = new IndexRequest("data-index"); //Add a document to the custom-index we created.
                        indexRequest.id(user.getUserId()); //Assign an ID to the document.


                        userData.setUserName(user.getUserName());
                        userData.setUserArn(user.getArn());
                        userData.setGroups(groupsForUserResult.getGroups()
                                .stream()
                                .map(group -> new UserGroupData(group.getGroupName(), group.getArn()))
                                .collect(Collectors.toList()));

                        Map<String, Object> map = new ObjectMapper().convertValue(userData, Map.class);

                        indexRequest.source(map); //Place your content into the index's source.
                        try {
                            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    request.setMarker(response.getMarker());

                    if (!response.getIsTruncated()) {
                        done = true;
                    }
                }
                return "success";
            }
        };
    }
}
