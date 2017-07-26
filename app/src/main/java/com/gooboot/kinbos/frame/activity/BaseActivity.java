package com.gooboot.kinbos.frame.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by gooboot on 2017/7/24.
 */

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initVariables();
        this.initViews(savedInstanceState);
        this.loadData();
    }

    /**
     * 初始化Activity所需变量，如Intent携带的数据信息等
     */
    protected abstract void initVariables();

    /**
     * 加载layout布局文件，初始化UI控件变量
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 请求网络数据
     */
    protected abstract void loadData();
}
