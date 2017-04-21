package com.dl7.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dl7.tag.TagView;

public class TestActivity extends AppCompatActivity {

    private TagView mTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mTagView = (TagView) findViewById(R.id.tag_view);
        mTagView.setTagMode(TagView.MODE_CHANGE);
    }
}
