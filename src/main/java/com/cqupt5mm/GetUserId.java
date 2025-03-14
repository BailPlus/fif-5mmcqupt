package com.cqupt5mm;

import cn.hutool.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GetUserId {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    public String gerUserId(String token) {
        HttpPost httppost = new HttpPost("https://moral.fifedu.com/kyxl-app/account/getUserInfo");
        httppost.addHeader("Authorization", token);
        httppost.addHeader("source", "10003");
        try {
            CloseableHttpResponse response = httpclient.execute(httppost);
            String te = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(te);
            String userId = json.getJSONObject("data").get("userId").toString();
            String realName = json.getJSONObject("data").get("realName").toString();
            System.out.println("当前用户id: " + userId + " 姓名: " + realName);
            return userId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
