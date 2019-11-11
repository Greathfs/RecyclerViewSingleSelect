package com.hfs.recyclerviewsingleselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.rv_test);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter adapter = new ItemAdapter(initData(), this, rv);
        rv.setAdapter(adapter);
    }

    public List<ItemBean> initData() {
        List<ItemBean> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            ItemBean itemBean = new ItemBean("这个是测试数据....", i == 5);
            list.add(itemBean);
        }
        return list;
    }
}
