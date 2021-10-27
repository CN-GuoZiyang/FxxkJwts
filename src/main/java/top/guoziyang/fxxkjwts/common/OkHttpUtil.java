package top.guoziyang.fxxkjwts.common;

import javafx.scene.image.Image;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    public static String COOKIE = "";

    // 单例
    public static OkHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private OkHttpClient singleton;

        Singleton() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(6L, TimeUnit.SECONDS);
            builder.readTimeout(6L, TimeUnit.SECONDS);
            builder.writeTimeout(6L, TimeUnit.SECONDS);
            ConnectionPool connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
            builder.connectionPool(connectionPool);
            singleton = builder.build();
        }

        private OkHttpClient getInstance() {
            return singleton;
        }
    }

    public static void refreshCookie() {
        Request r = new Request.Builder()
                .url("http://jwts.hit.edu.cn/")
                .get()
                .build();
        Call call = getInstance().newCall(r);
        String rawCookies = null;
        try {
            Response response = call.execute();
            Map<String, List<String>> m = response.headers().toMultimap();
            rawCookies = m.get("Set-Cookie").toString();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cookies = rawCookies.substring(1, rawCookies.length()-1).split(";|,");
        List<String> cookiesList = new ArrayList<>();
        for(String cookie : cookies) {
            if(cookie.contains("name") || cookie.contains("JSESSIONID") || cookie.contains("clwz_blc_pst")) {
                cookiesList.add(cookie.trim());
            }
        }
        COOKIE = String.join("; ", cookiesList);
    }

    public static Response getWithCookie(String url) {
        if("".equals(COOKIE)) {
            refreshCookie();
        }
        Request r = new Request.Builder()
                .url(url)
                .header("Cookie", COOKIE)
                .get().build();
        Call call = OkHttpUtil.getInstance().newCall(r);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response postWithCookie(String url, Map<String, String> data) {
        if("".equals(COOKIE)) {
            refreshCookie();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();
        Request r = new Request.Builder()
                .url(url)
                .header("Cookie", COOKIE)
                .post(body).build();
        Call call = OkHttpUtil.getInstance().newCall(r);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
