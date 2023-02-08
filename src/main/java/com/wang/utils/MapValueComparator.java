package com.wang.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/19 14:54
 */
public class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {

        return me2.getValue().compareTo(me1.getValue());
    }
}