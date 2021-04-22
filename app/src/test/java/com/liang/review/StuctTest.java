package com.liang.review;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Author:bernie-mac
 * Data:2021/4/21 17:44
 * Description: com.liang.review
 */
public class StuctTest {

    @Test
    public void testArrayList(){
        ArrayList<String> object =new ArrayList<>(5);
        for (int i = 0; i < 10; i++) {
            object.add("re-"+i);
        }
        System.out.println("添加后的数据"+object.toString());
        object.set(4,"");
    }
}
