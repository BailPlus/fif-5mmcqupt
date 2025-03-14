package com.cqupt5mm;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetQuestionids {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    public List<questions> getQuestionids(List<String> LevelsIds, String token) {
        List<questions> questions = new ArrayList<>();
        for(String LevelId: LevelsIds) {
            HttpPost httppost = new HttpPost("https://moral.fifedu.com/kyxl-app/column/getLevelInfo");
            httppost.setHeader("Authorization", token);
            httppost.setHeader("source", "10003");
            List<NameValuePair> paramPairs = new ArrayList<>();
            paramPairs.add(new BasicNameValuePair("levelId", LevelId));
            try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            httppost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            String te = EntityUtils.toString(response.getEntity());
                JSONObject json = JSONUtil.parseObj(te);
                JSONArray moshi = json.getJSONObject("data").getJSONObject("content").getJSONArray("moshi");
                JSONObject quejson = moshi.getJSONObject(1).getJSONObject("question");
                JSONArray queNum = quejson.getJSONObject("qcontent").getJSONArray("item").getJSONObject(0).getJSONArray("questions");
                String queid = quejson.get("questionid").toString();
                questions question = new questions();
                question.questionId = queid;
                question.number = queNum.size();
                questions.add(question);
                System.out.println("挑战ID: questionId: " + queid + " 任务数：" + queNum.size());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return questions;
    }
}
