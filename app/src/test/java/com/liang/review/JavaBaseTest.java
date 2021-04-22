package com.liang.review;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class JavaBaseTest {

    @Test
    public void testHashMap(){
        LinkedHashMap<String, Integer> lmap = new LinkedHashMap<String, Integer>();
        lmap.put("aa", 1);
        lmap.put("bb", 2);
        lmap.put("cc", 3);
        lmap.put("dd", 4);
        lmap.put("ee", 5);
        lmap.put("ff", 6);
        lmap.put("gg", 7);
        lmap.put("hh", 8);
        for(Entry<String, Integer> entry : lmap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
