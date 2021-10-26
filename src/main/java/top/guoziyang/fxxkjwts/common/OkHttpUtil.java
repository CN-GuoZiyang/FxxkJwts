package top.guoziyang.fxxkjwts.common;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
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

}
