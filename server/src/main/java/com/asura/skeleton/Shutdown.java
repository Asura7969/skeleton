package com.asura.skeleton;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author asura7969
 * @create 2022-03-03-20:07
 */

// https://www.cnblogs.com/jimmyfan/p/11340899.html
public class Shutdown {
    public static void main(String[] args) {
        String url = null;
        if (args.length > 0) {
            url = args[0];
        } else {
            return;
        }
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
