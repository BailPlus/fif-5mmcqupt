package com.cqupt5mm;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLevels {
    CloseableHttpClient httpclient = HttpClients.createDefault();

    public List<String> getLevel(String unitId, String token) {

        HttpGet httpget = new HttpGet("https://moral.fifedu.com/kyxl-app/stu/column/stuUnitInfo?unitId=" + unitId);
        httpget.setHeader("source", "10003");
        httpget.setHeader("Authorization", token);
        try {
            CloseableHttpResponse response = httpclient.execute(httpget);
            JSONObject json = new JSONObject();
            HttpEntity entity = response.getEntity();
            String te = EntityUtils.toString(entity);
            json = JSONUtil.parseObj(te);
            JSONArray levelList = json.getJSONObject("data").getJSONArray("levelList");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < levelList.size(); i++) {
                JSONObject level = levelList.getJSONObject(i);
                String levelId = level.getStr("levelId");
                list.add(levelId);
                System.out.println("关卡ID: LevelId: " + levelId);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
