package com.jeepc.service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TestBeanManager.ResultListener<TestBean>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insert(View view) {
        if(TestBeanManager.saveAll(getList())){
            Toast.makeText(this,"个数"+TestBeanManager.getCount(),Toast.LENGTH_LONG).show();
        }

    }

    private int index = 0;

    private List<TestBean> getList() {
        List<TestBean> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            TestBean bean = new TestBean();
            bean.setName("name"+index);
            list.add(bean);
            index++;
        }
        return list;
    }
    private List<TestBean> getList10000() {
        List<TestBean> list = new ArrayList<>();
        for (int i = 0;i<10000;i++){
            TestBean bean = new TestBean();
            bean.setName("name"+index);
            list.add(bean);
            index++;
        }
        return list;
    }

    public void search(View view) {
        Toast.makeText(this,"个数"+TestBeanManager.getCount(),Toast.LENGTH_LONG).show();
    }

    public void insertRx(View view) {
        TestBeanManager.saveAllRx(getList10000(), this);
    }

    @Override
    public void onResult(boolean success) {
        if(success){
            Toast.makeText(this,"个数"+TestBeanManager.getCount(),Toast.LENGTH_LONG).show();
            TextView result = findViewById(R.id.tv_result);
            result.setText("success");
        }
    }

    @Override
    public void onGetAllByPage(List<TestBean> list) {
        if(list!=null&&list.size()>0){
            for (TestBean testBean : list) {
                Log.e("jeepc1", "name: " + testBean.getName());
            }
        }
    }


    @Override
    public void onGetAllByPageFail() {

    }

    public void searchRx(View view) {
        TestBeanManager.getAllByPage(0,100,this);
    }
}
