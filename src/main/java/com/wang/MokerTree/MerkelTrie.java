package com.wang.MokerTree;

import org.apache.commons.collections4.Trie;

import java.util.*;

/**
 *
 * Moker 树
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/12/10 12:26
 */
public class MerkelTrie {

    private TrieNode root;
    private int count;

    public MerkelTrie() {
        root = new TrieNode();
        count = 0;
    }
    /**
     *
     * @author jqWang
     * @date 2023/2/15 15:33
     返回树中的所有路径，将路径放在堆中并按照树节点中的数量进行排序
     */
    public void getAllPaths(TrieNode node,Queue<String[]> heap,StringBuilder sb){
        if(node.isEnd()){
            String[] temp = {sb.toString(),node.getIds().size() + ""};
            heap.offer(temp);
            return;
        }
        HashMap<String, TrieNode> children = node.getChildren();
        for(Map.Entry<String,TrieNode> entry : children.entrySet()){
            if(sb.length() == 0){
                sb.append(entry.getKey());
            }else{
                sb.append("-").append(entry.getKey());
            }
            getAllPaths(entry.getValue(),heap,sb);
        }
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
            if(cur.children.containsKey(c)){
                cur = cur.children.get(c);
            }else{
                boolean isOpt = false;
                for(Map.Entry<String,TrieNode> entry : cur.children.entrySet()){
                    //遍历所有叶子节点
                    if(entry.getKey().split("-")[0].equals(c)){
                        //叶子节点与当前节点有前缀
                        String preKey = entry.getKey();
                        //叶子节点分裂
                        TrieNode curSon = new TrieNode();
                        cur.children.put(c,curSon);
                        num ++;
                        //分裂后的节点剩余部分生成新的节点保存
                        String[] path = preKey.split("-");
                        StringBuffer sb = new StringBuffer();
                        for(int j = 1;j < path.length - 1;j ++){
                            sb.append(path[j]).append("-");
                        }
                        sb.append(path[path.length - 1]);
                        curSon.children.put(sb.toString(),new TrieNode());
                        //删除原有节点
                        cur.children.remove(preKey);
                        cur = curSon;
                        isOpt = true;
                        break;
                    }
                }
                if(!isOpt){
                    StringBuffer sb = new StringBuffer();
                    sb.append(split[i]);
                    for(int j = i + 1;j < split.length;j ++){
                        sb.append("-").append(split[j]);
                    }
                    cur.children.put(sb.toString(),new TrieNode());
                    num ++;
                    cur = cur.children.get(sb.toString());
                    break;
                }

            }
        }
        cur.setEnd();
        /*LinkedList<Integer> ids = cur.ids;
        if(!ids.contains(id)){
            cur.ids.add(id);
        }*/
        count += num;
        //System.out.println(str+ ":  :" + count);
    }

    public Set<String> searchCan(String word) {
        TrieNode cur = root;
        String[] split = word.split("-");
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                HashMap<String, TrieNode> children = cur.getChildren();
                for(Map.Entry<String,TrieNode> entry : children.entrySet()){
                    if(entry.getKey().startsWith(c)){
                        int length = entry.getKey().split("-").length;
                        StringBuilder sb = new StringBuilder();
                        sb.append(c);
                        for(int j = i + 1;j < i + length;j++){
                            sb.append("-").append(split[j]);
                        }
                        if(sb.toString().equals(entry.getKey())){
                            cur = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }
        LinkedList<Integer> ids = cur.getIds();
        Set<String> res = new HashSet<>();
        for(int id : ids){
            res.add(id + "");
        }
        return res;
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

    public void delete(String path,Set<Integer> meId){
        TrieNode cur = root;
        String[] split = path.split("-");
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                HashMap<String, TrieNode> children = cur.getChildren();
                for(Map.Entry<String,TrieNode> entry : children.entrySet()){
                    if(entry.getKey().startsWith(c)){
                        int length = entry.getKey().split("-").length;
                        StringBuilder sb = new StringBuilder();
                        sb.append(c);
                        for(int j = i + 1;j < i + length;j++){
                            sb.append("-").append(split[j]);
                        }
                        if(sb.toString().equals(entry.getKey())){
                            cur = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }
        LinkedList<Integer> ids = cur.getIds();
        for(int id : ids){
            if(meId.contains(id)){
                ids.remove(id);
            }
        }
    }

    /**
     * 获取邻居路径
     * @author jqWang
     * @date 2023/3/10 16:27
     * @param path
     * @return Set<String>
     */
    public Set<String> getNeighbor(String path){
        TrieNode cur = root;
        String[] split = path.split("-");
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                HashMap<String, TrieNode> children = cur.getChildren();
                for(Map.Entry<String,TrieNode> entry : children.entrySet()){
                    if(entry.getKey().startsWith(c)){
                        int length = entry.getKey().split("-").length;
                        StringBuilder sb = new StringBuilder();
                        sb.append(c);
                        for(int j = i + 1;j < i + length;j++){
                            sb.append("-").append(split[j]);
                        }
                        if(sb.toString().equals(entry.getKey())){
                            cur = entry.getValue();
                            break;
                        }
                    }
                }
            }

        }
        Set<String> res = new HashSet<>();
        StringBuilder sb = new StringBuilder(path);
        for(Map.Entry<String,TrieNode> entry : cur.getChildren().entrySet()){
            TrieNode temp = entry.getValue();
            while(!temp.isEnd()){
                sb.append("-").append(entry.getKey());
            }
            res.add(sb.toString());
            sb.setLength(0);
        }

        return res;
    }

    // 字符串的查找
    public Set<Integer> search(String word) {
        TrieNode cur = root;
        String[] split = word.split("-");
        for (int i = 0; i < split.length; i++) {
            String c = split[i];
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                HashMap<String, TrieNode> children = cur.getChildren();
                for(Map.Entry<String,TrieNode> entry : children.entrySet()){
                    if(entry.getKey().startsWith(c)){
                        int length = entry.getKey().split("-").length;
                        StringBuilder sb = new StringBuilder();
                        sb.append(c);
                        for(int j = i + 1;j < i + length;j++){
                            sb.append("-").append(split[j]);
                        }
                        if(sb.toString().equals(entry.getKey())){
                            cur = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }
        LinkedList<Integer> ids = cur.getIds();
        Set<Integer> res = new HashSet<>();
        for(int id : ids){
            res.add(id);
        }
        return res;
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