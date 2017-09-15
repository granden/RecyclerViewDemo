package com.lipt0n.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    List<String> dataList;
    String str[]={"陈奕迅","周星驰","周迅","肥嘟嘟","高圆圆","高进","权志龙","赵又廷","侧田","周杰伦","张学友","林忆莲","李荣浩","五月天","谢安琪","陶喆"};
    @BindView(R.id.recyclerView)RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dataList=new ArrayList<>();

        for (int i = 0; i < str.length; i++) {
            dataList.add(str[i]);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleItemDecoration(this, new SimpleItemDecoration.ObtainTextCallback() {
            @Override
            public String getText(int position) {
                return dataList.get(position).substring(0,1);
            }
        }));

        QuicklyAdapter quicklyAdapter=new QuicklyAdapter(R.layout.recycler_item,dataList);
        recyclerView.setAdapter(quicklyAdapter);

    }




}
