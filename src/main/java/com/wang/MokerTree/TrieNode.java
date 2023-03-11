package com.wang.MokerTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/12/10 12:25
 */
public class TrieNode {

    //子节点
    HashMap<String,TrieNode> children = new HashMap<>();
    LinkedList<Integer> ids = new LinkedList<>();
    private boolean isEnd;

    public HashMap<String, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(HashMap<String, TrieNode> children) {
        this.children = children;
    }

    public LinkedList<Integer> getIds() {
        return ids;
    }

    public void setIds(LinkedList<Integer> ids) {
        this.ids = ids;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    //设置成有效字符串
    public void setEnd() {
        isEnd = true;
    }

    //判断从根节点到这个节点路径表示的字符串是否有效
    public boolean isEnd() {
        return isEnd;
    }


}