package com.cqupt5mm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class GetUnitInfo {
    public HashMap<String,String> getUnitInfo(String url, String token) throws URISyntaxException {

        HashMap<String,String> map = new HashMap<>();
        for(int i = 0;i <url.length();i++) {
            if(url.charAt(i) == '#') {
                String begin = url.substring(0, i-1);
                String end = url.substring(i+1, url.length());
                url = begin + end;
                break;
            }
        }
        // 解析UR
        URI uri = new URI(url);

        // 获取查询参数部分
        String query = uri.getQuery();

        if (query != null && !query.isEmpty()) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                map.put(key, value);
            }
        }

        System.out.println(map);
        return map;

    }

}
