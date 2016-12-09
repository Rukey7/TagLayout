package com.dl7.taglayout.utils;

import java.util.Random;

/**
 * Created by long on 2016/12/9.
 */

public final class TagWordFactory {

    public static final String[] TAG_WORD = new String[] {
            "Hello", "Android", "Java", "我是TagView", "Hello World",
            "This is a long string, This is a long string, This is a long string",
            "这是长字符串，这是长字符串，这是长字符串，这是长字符串"
    };
    private static Random sRandom = new Random();

    private TagWordFactory() {
        throw new AssertionError();
    }


    public static String provideTagWord() {
        return TAG_WORD[sRandom.nextInt(TAG_WORD.length)];
    }
}
