package com.dl7.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;

public class TagChoiceActivity extends AppCompatActivity implements TagView.OnTagClickListener, TagView.OnTagLongClickListener {

    private TagLayout mTagLayout1;
//    private TagView mTagDel;
//    private TagView mTagAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);
        initView();
    }

    private void initView() {
        mTagLayout1 = (TagLayout) findViewById(R.id.tag_layout_1);
//        mTagDel = (TagView) findViewById(R.id.tag_del);
//        mTagAdd = (TagView) findViewById(R.id.tag_add);
        mTagLayout1.setTags("This is a long string, This is a long string, This is a long string");
        mTagLayout1.setTagClickListener(this);
        mTagLayout1.setTagLongClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagLayout1.addTag("This is a long string, This is a long string, This is a long string");
            }
        });

//        mTagAdd.setTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(String text, @TagView.TagMode int tagMode) {
//                String word = TagWordFactory.provideTagWord();
//                mTagLayout1.addTag(word);
//            }
//        });
//        mTagDel.setTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(String text, @TagView.TagMode int tagMode) {
////                mTagLayout1.deleteTag(0);
//            }
//        });
    }

    @Override
    public void onTagClick(String text, @TagView.TagMode int tagMode) {

    }

    @Override
    public void onTagLongClick(String text, @TagView.TagMode int tagMode) {

    }
}
