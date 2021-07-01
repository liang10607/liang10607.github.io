package com.liang.review.beans;

import java.util.HashMap;

import androidx.annotation.NonNull;

/**
 * Author:bernie-mac
 * Data:2021/5/6 11:23
 * Description: com.liang.review.beans
 */
public class CloneTest implements Cloneable{
    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        HashMap map = new HashMap(3);
        return super.clone();
    }
}
