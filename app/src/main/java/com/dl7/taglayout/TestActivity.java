package com.dl7.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dl7.tag.TagView;
import com.dl7.taglayout.drawable.CircleDrawable;
import com.dl7.taglayout.drawable.MultiCircleDrawable;

public class TestActivity extends AppCompatActivity {

    private TagView mTagView;
    private TagView mTagMultiCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mTagView = (TagView) findViewById(R.id.tag_view);
        mTagView.setDecorateIcon(new CircleDrawable());
        mTagMultiCircle = (TagView) findViewById(R.id.tag_multi_circle);
        mTagMultiCircle.setDecorateIcon(new MultiCircleDrawable());
    }
}
