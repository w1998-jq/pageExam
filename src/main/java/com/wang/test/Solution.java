package com.wang.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/26 19:43
 */
class Solution {
    Map<Integer, Integer> ori = new HashMap<>();
    Map<Integer, Integer> cnt = new HashMap<>();

    public int minWindow(int[] s, int[] t) {
        int tLen = t.length;
        for (int i = 0; i < tLen; i++) {
            int c = t[i];
            ori.put(c, ori.getOrDefault(c, 0) + 1);
        }
        int l = 0, r = -1;
        int len = Integer.MAX_VALUE, ansL = -1, ansR = -1;
        int sLen = s.length;
        while (r < sLen) {
            ++r;
            if (r < sLen && ori.containsKey(s[r])) {
                cnt.put(s[r], cnt.getOrDefault(s[r], 0) + 1);
            }
            while (check() && l <= r) {
                if (r - l + 1 < len) {
                    len = r - l + 1;
                    ansL = l;
                    ansR = l + len;
                }
                if (ori.containsKey(s[l])) {
                    cnt.put(s[l], cnt.getOrDefault(s[l], 0) - 1);
                }
                ++l;
            }
        }
        return ansL == -1 ? 0 : ansR - ansL;
    }

    public boolean check() {
        Iterator iter = ori.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Character key = (Character) entry.getKey();
            Integer val = (Integer) entry.getValue();
            if (cnt.getOrDefault(key, 0) < val) {
                return false;
            }
        }
        return true;
    }
}
