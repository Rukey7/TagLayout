package com.dl7.taglayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] mTagWords = new String[] {
            "Hello", "Android", "我是TagView",
            "This is a long string, This is a long string, This is a long string",
            "这是长字符串，这是长字符串，这是长字符串，这是长字符串"
    };
    private String[] mTagWords2 = new String[] {
            "Dota", "LOL", "Somebody",
            "Noting",
            "蓝色的小鸟",
    };
    private TagLayout mTagGroup;
    private Button mBtnAdd;
    private Button mBtnClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagGroup = (TagLayout) findViewById(R.id.tag_group);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mBtnClean = (Button) findViewById(R.id.btn_clean);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            Random random = new Random();

            @Override
            public void onClick(View v) {
                mTagGroup.addTag(mTagWords[random.nextInt(mTagWords.length)]);
            }
        });
        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagGroup.cleanTags();
            }
        });
        mTagGroup.setPressFeedback(true);
        mTagGroup.setEnableRandomColor(true);
        mTagGroup.setFitTagNum(3);
        mTagGroup.setTagBgColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        mTagGroup.setTagBorderColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        mTagGroup.setBgColor(ContextCompat.getColor(this, android.R.color.white));
        mTagGroup.setBorderColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        mTagGroup.setBorderWidth(1);
        mTagGroup.setTags(mTagWords);
        mTagGroup.setTagShape(TagView.SHAPE_ARC);
        mTagGroup.setTagTextColor(Color.RED);
        mTagGroup.addTagWithIcon("更多", R.mipmap.ic_home_more_press);
        mTagGroup.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(String text, @TagView.TagMode int tagMode) {
                Log.w("MainActivity", text);
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                if (tagMode == TagView.MODE_CHANGE) {
                    Log.d("MainActivity", "");
                    mTagGroup.updateTags(mTagWords2);
                }
            }
        });
        mTagGroup.setTagCheckListener(new TagView.OnTagCheckListener() {
            @Override
            public void onTagCheck(String text, boolean isChecked) {
                Log.e("MainActivity", text + " - " + isChecked);
            }
        });


        final TagView tagView = (TagView) findViewById(R.id.tag_view);
        tagView.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(String text, @TagView.TagMode int tagMode) {
                tagView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tagView.isChecked()) {
                            tagView.setTagText("标签关注");
                            tagView.setChecked(false);
                        } else {
                            tagView.setTagText("取消关注");
                            tagView.setChecked(true);
                        }
                    }
                }, 2000);
            }
        });
    }
}
