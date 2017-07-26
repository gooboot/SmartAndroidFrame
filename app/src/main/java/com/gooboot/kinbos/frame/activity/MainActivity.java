package com.gooboot.kinbos.frame.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gooboot.kinbos.frame.R;
import com.gooboot.kinbos.frame.net.http.okhttp.BaseCallback;
import com.gooboot.kinbos.frame.net.http.okhttp.OkHttpHelper;
import com.gooboot.kinbos.frame.test.bean.Weather;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private OkHttpHelper helper;
    private TextView tv;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        this.tv = (TextView) this.findViewById(R.id.tv);
    }

    @Override
    protected void loadData() {
        String url = "http://www.weather.com.cn/data/sk/101110101.html";
        this.helper = OkHttpHelper.getInstance();
        this.helper.get(url,new BaseCallback<Weather>(){

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailrue(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, Weather weather) {
                tv.setText(weather.getWeatherinfo().getCity());
            }

            @Override
            public void onError(Response response, int httpCode, Exception e) {

            }
        });
    }

}
