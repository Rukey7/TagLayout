package com.dl7.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.dl7.taglayout.utils.TagWordFactory;
import com.dl7.taglayout.utils.ToastUtils;

public class TagChoiceActivity extends AppCompatActivity implements TagView.OnTagClickListener, TagView.OnTagLongClickListener {

    private TagLayout mTagLayout1;
    private TagView mTagDel;
    private TagView mTagAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);
        initView();
    }

    private void initView() {
        mTagLayout1 = (TagLayout) findViewById(R.id.tag_layout_1);
        mTagDel = (TagView) findViewById(R.id.tag_del);
        mTagAdd = (TagView) findViewById(R.id.tag_add);
        mTagLayout1.setTags(TagWordFactory.TAG_WORD);
        mTagLayout1.setTagClickListener(this);
        mTagLayout1.setTagLongClickListener(this);

        mTagAdd.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(String text, @TagView.TagMode int tagMode) {
                String word = TagWordFactory.provideTagWord();
                mTagLayout1.addTag(word);
            }
        });
        mTagDel.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(String text, @TagView.TagMode int tagMode) {
                mTagLayout1.deleteTag(0);
            }
        });
    }

    @Override
    public void onTagClick(String text, @TagView.TagMode int tagMode) {
        ToastUtils.showToast(text);
    }

    @Override
    public void onTagLongClick(String text, @TagView.TagMode int tagMode) {
        ToastUtils.showToast("长按:" + text);
    }
}
