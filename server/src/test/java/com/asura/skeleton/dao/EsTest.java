package com.asura.skeleton.dao;

import com.asura.common.utils.JsonUtil;
import lombok.Data;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author asura7969
 * @create 2022-04-21-20:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest  extends AbstractJUnit4SpringContextTests {

    // https://wylong.top/Elasticsearch/13-Springboot%E9%9B%86%E6%88%90ES.html#%E6%90%9C%E7%B4%A2%E5%8C%B9%E9%85%8D%E7%9A%84%E3%80%81term%E6%9F%A5%E8%AF%A2%E3%80%81%E5%89%8D%E7%BC%80%E6%9F%A5%E8%AF%A2

    // https://www.jianshu.com/p/ef2ea585ea10
    // 索引
    private static final String NBA_INDEX = "nba";
    // 查询数据起始值
    private static final int START_OFFSET = 0;
    // 查询数量
    private static final int MAX_COUNT = 1000;

    @Resource
    private RestHighLevelClient client;


    private void buildQuery() {
        QueryBuilder query = QueryBuilders.termQuery("", "");
    }

    private List<Item> commonSearch(QueryBuilder query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();
        List<Item> playerList = new LinkedList<>();
        for (SearchHit hit : hits) {
            final Item item = JsonUtil.parseObject(hit.getSourceAsString(), Item.class);
            playerList.add(item);
        }

        return playerList;

    }

    @Data
    private static class Item {
        private int id;
        private String name;
    }
}
