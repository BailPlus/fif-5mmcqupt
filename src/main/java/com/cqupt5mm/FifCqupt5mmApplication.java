package com.cqupt5mm;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FifCqupt5mmApplication {

    public static void main(String[] args) throws URISyntaxException {
        GetUnitInfo getUserInfo = new GetUnitInfo();
        GetLevels getLevels = new GetLevels();
        GetQuestionids getQuestionids = new GetQuestionids();
        SubmitGrade submitGrade = new SubmitGrade();
        GetUserId getUserId = new GetUserId();
        String token; //= "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbl9uYW1lIjoiY3F1cHQyMDI0MjEyMDY4IiwidXNlcl9pZCI6ImY2ZGQxYzFiZjI0YTQ4MWViMjAyYTNjN2ZiNDUwYzNiIiwidXNlcl9rZXkiOiI3MTFjZDc0NGI2NTk0OGUwOTMxZGY3ZWM3ZGU3ZjdiOCIsImV4cCI6MTc0MTkyMzYxMCwiaWF0IjoxNzQxOTE2NDEwfQ.5xS-oTnerFDrpXtraCep6CwedDiD2v0IUleJvbM5fM0";
        String url; //= "https://static.fifedu.com/static/fiforal/kyxl-web-static/student-h5/index.html#/pages/course/studyPage/studyPage?unitid=8d7a0436acc141a48dbcaa3f1bf58816&type=2&taskId=0";
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入Token：");
        token = scanner.nextLine();

        System.out.println("请输入URL：");
        url = scanner.nextLine();

        HashMap<String, String> unitInfo = getUserInfo.getUnitInfo(url, token);
        List<String> LevelsIds = getLevels.getLevel(unitInfo.get("unitid"),token);
        List<questions> questionIds = getQuestionids.getQuestionids(LevelsIds,token);
        String userId = getUserId.gerUserId(token);
        submitGrade.submitGrade(questionIds,LevelsIds,userId,unitInfo.get("taskId"));

        System.out.println("已完成任务！");

    }

}
