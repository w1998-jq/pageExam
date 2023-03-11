package com.wang.MokerTree;

import java.util.LinkedList;

/**
 * 前缀树
 * @ClassName Tries
 * Description
 * @Author jqWang
 * Date 2023/2/24 20:47
 **/
public class Tries {
    private TrieNode root;
    private int count;

    public Tries() {
        root = new TrieNode();
        count = 0;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void insert(int id, String str) {
        int num = 0;
        TrieNode cur = root;

        String[] split = str.split("-");
        for (int i = 0; i < split.length; ++i) {
            String c = split[i];
            if(!cur.children.containsKey(c)){
                TrieNode node = new TrieNode();
                cur.children.put(c,node);
                num ++;
            }
            cur = cur.children.get(c);
        }
        cur.setEnd();
        LinkedList<Integer> ids = cur.ids;
        if(!ids.contains(id)){
            cur.ids.add(id);
        }
        count += num;
    }

    private TrieNode searchPrefix(String word) {
        TrieNode cur = root;
        String[] split = word.split("-");
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                return null;
            }
        }
        return cur;
    }

    // 字符串的查找
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    //前缀的查找
    public boolean startsWith(String prefix) {
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }


    public static void main(String[] args) {
        String[] a = {"yue", "wu", "sheng", "wz", "yu"};
        String subPath = "123-345-556";
        MerkelTrie merkelTrie = new MerkelTrie();
        merkelTrie.insert(3,subPath);

        System.out.println(merkelTrie.search("123-345-556"));
        System.out.println(merkelTrie.startsWith("123-345-556"));
    }
}
