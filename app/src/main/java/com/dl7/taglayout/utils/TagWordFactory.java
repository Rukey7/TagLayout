package com.dl7.taglayout.utils;

import java.util.Random;

/**
 * Created by long on 2016/12/9.
 */

public final class TagWordFactory {

    public static final String[] TAG_WORD = new String[] {
            "Hello", "Android", "Java", "我是TagView", "Hello World",
            "This is a long string, This is a long string, This is a long string",
            "这是长字符串，这是长字符串，这是长字符串，这是长字符串", "它在我的机器上可以很好运行T_T",
            "Learn not and know not.", "Life is but a span.", "Never say die.", "知识改变命运，英语成就未来"
    };

    public static final String[] TAG_WORD_2 = new String[] {
            "Success", "Failure", "美女", "影视", "豆瓣Top250",
            "Have you given any thought to your future?",
            "我猜着了开头，但我猜不中这结局"
    };

    private static Random sRandom = new Random();

    private TagWordFactory() {
        throw new AssertionError();
    }


    public static String provideTagWord() {
        return TAG_WORD[sRandom.nextInt(TAG_WORD.length)];
    }
}
