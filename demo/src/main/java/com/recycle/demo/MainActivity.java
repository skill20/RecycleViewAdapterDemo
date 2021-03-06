package com.recycle.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
    }

    private void findViewByIds() {
        findViewById(R.id.default_text).setOnClickListener(this);
        findViewById(R.id.multi_text).setOnClickListener(this);
        findViewById(R.id.stick_text).setOnClickListener(this);
        findViewById(R.id.load_text).setOnClickListener(this);
        findViewById(R.id.recycle).setOnClickListener(this);
        findViewById(R.id.xrecycle).setOnClickListener(this);
        findViewById(R.id.state).setOnClickListener(this);
        findViewById(R.id.view_pager).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_text:
                startActivity(new Intent(this, DefRecycleViewActivity.class));
                break;
            case R.id.multi_text:
                startActivity(new Intent(this, MulRecycleViewActivity.class));
                break;
            case R.id.stick_text:
                startActivity(new Intent(this, StickRecycleViewActivity.class));
                break;
            case R.id.load_text:
                startActivity(new Intent(this, LoadMoreRecycleViewActivity.class));
                break;
            case R.id.recycle:
                startActivity(new Intent(this,RecycleActivity.class));
                break;
            case R.id.xrecycle:
                startActivity(new Intent(this,XRecycleActivity.class));
                break;
            case R.id.state:
                startActivity(new Intent(this,StateActivity.class));
                break;
            case R.id.view_pager:
                startActivity(new Intent(this,ViewPagerActivity.class));
                break;
        }
    }
}
