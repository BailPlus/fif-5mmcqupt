package com.cqupt5mm;

import cn.hutool.core.util.RandomUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubmitGrade {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    public void submitGrade(List<questions> questions, List<String> LevelIds, String userId, String taskId, int low, int high, int time) {
        for(int i = 0;i < questions.size();i++) {
            questions question = questions.get(i);
            String questionId = question.questionId;
            Integer num = question.number;
            if(num < 10)
                num = 10;
            for(int j = 0;j < num;j++) {
                JSONArray ResultJson = generateAnswer(questionId, j,low,high,time);
                String resultJson = JSONUtil.toJsonStr(ResultJson);
                HttpPost httppost = new HttpPost("https://moral.fifedu.com/kyxl-app-challenge/evaluation/submitChallengeResults");
                httppost.setHeader("clientType", "6");
                httppost.setHeader("userId", userId);
                List<NameValuePair> paramPairs = new ArrayList<>();
                paramPairs.add(new BasicNameValuePair("levelId", LevelIds.get(i)));
                paramPairs.add(new BasicNameValuePair("studentId", userId));
                paramPairs.add(new BasicNameValuePair("taskId", taskId));
                paramPairs.add(new BasicNameValuePair("resultJson", resultJson));
                System.out.println(paramPairs);
                try {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
                    httppost.setEntity(entity);
                    CloseableHttpResponse response = httpclient.execute(httppost);
                    response.getEntity().getContent().close();//关闭结果集
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
    public JSONArray generateAnswer(String questionId,int num,int low,int high,int time) {
        int semantic = RandomUtil.randomInt(low,high);
        int accuracy = RandomUtil.randomInt(low,high);
        int fluency = RandomUtil.randomInt(low,high);
        float score = (float) (semantic + accuracy + fluency) / 3;
        JSONArray result = new JSONArray();
            JSONObject answer = new JSONObject();
            answer.put("questionId",questionId + "#0#" + num);
            answer.put("semantic", semantic);
            answer.put("accuracy",accuracy );
            answer.put("fluency", fluency);
            answer.put("complete","100");
            answer.put("score", score);
            answer.put("ansDetail","系统出错！请联系管理员！");
            answer.put("recordPath","114514" );
            answer.put("learn_time",time + RandomUtil.randomInt(5,10) );
            result.add(answer);
        return result;
    }
}
