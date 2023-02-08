package com.wang.MokerTree;

import java.util.List;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/12/10 12:21
 */
public class Node {
    public String val;
    public List<Node> children;

    public Node() {
    }

    public Node(String val) {
        val = val;
    }

    public Node(String val, List<Node> _children) {
        val = val;
        children = _children;
    }


}