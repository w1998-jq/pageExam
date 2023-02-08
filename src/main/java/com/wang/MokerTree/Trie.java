package com.wang.MokerTree;

import java.util.Map;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/12/10 12:26
 */
public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String str) {
        TrieNode cur = root;
        String[] split = str.split("-");
        for (int i = 0; i < split.length; ++i) {
            String c = split[i];
            if(cur.children.containsKey(c)){
                cur = cur.children.get(c);
            }else{
                boolean isOpt = false;
                for(Map.Entry<String,TrieNode> entry : cur.children.entrySet()){
                    if(entry.getKey().split("-")[0].equals(c)){
                        String preKey = entry.getKey();
                        cur.children.put(c,new TrieNode());
                        TrieNode child = cur.children.get(preKey);
                        String[] path = preKey.split("-");
                        StringBuffer sb = new StringBuffer();
                        for(int j = 1;j < path.length - 1;j ++){
                            sb.append(path[j]).append("-");
                        }
                        sb.append(path[path.length - 1]);
                        child.children.put(sb.toString(),cur.children.get(preKey));
                        cur.children.remove(preKey);
                        isOpt = true;
                        break;
                    }
                }
                if(!isOpt){
                    /*StringBuffer sb = new StringBuffer();
                    sb.append(split[i + 1]);
                    for(int j = i + 1;j < split.length;j ++){
                        sb.append("-").append(split[i]);
                    }
                    cur.children.put(sb.toString(),new TrieNode());*/
                }
                break;
            }
            if (!cur.children.containsKey(c)) {
                StringBuffer sb = new StringBuffer();
                sb.append(i);
                for(int j = i + 1;j < split.length;j ++){
                    sb.append('-').append(split[i]);
                }
                cur.children.put(c, new TrieNode());
            }
            cur = cur.children.get(c);
        }
        cur.setEnd();
        cur.ids.add(3);
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
        Trie trie = new Trie();
        trie.insert(subPath);

        System.out.println(trie.search("123-345-556"));
        System.out.println(trie.startsWith("123-345-556"));
    }


}