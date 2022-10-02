package com.aquib.cy5assignment.web.dao;


import com.aquib.cy5assignment.web.entities.UserData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDataDao {

    @Autowired
    private OpenSearchClient openSearchClient;

    public List<UserData> getUserData() throws IOException {
        SearchResponse<UserData> searchResponse = openSearchClient.search(s -> s.index("data-index"), UserData.class);
        return searchResponse.hits().hits().stream().map(hit->hit.source()).collect(Collectors.toList());
    }
}