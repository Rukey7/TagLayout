package com.dl7.taglayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;

public class MainActivity extends AppCompatActivity {

    private final String[] mTagWords = new String[]{
            "测试不同边框形状", "Android", "我是TagView",
    };
    private TagLayout mTagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mTagLayout = (TagLayout) findViewById(R.id.tag_layout);
        mTagLayout.setTags(mTagWords);
        mTagLayout.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(String text, @TagView.TagMode int tagMode) {
                if (mTagWords[0].equals(text)) {
                    startActivity(new Intent(MainActivity.this, TagShapeActivity.class));
                } else if (mTagWords[1].equals(text)) {
                    startActivity(new Intent(MainActivity.this, TagChoiceActivity.class));
                }
            }
        });
    }
}
