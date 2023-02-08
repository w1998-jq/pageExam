package com.wang.dataSetInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jqwang
 * 统计轨迹数据集信息
 * @version 1.0
 * @description: TODO
 * @date 2022/5/21 15:37
 */
public class GetDateSetInfo {

    /**
     * @description:  获取Proto数据集平均长度
     * @param:
     * @return: void
     * @author jqwang
     * @date: 2022/5/21 15:37
     */
    @Test
    public void test() throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(new File("D:\\TraDataSet\\波兰\\data"));
        int count = 0;
        int traCount = 0;
        while(lineIterator.hasNext()){
            traCount ++;
            String line = lineIterator.next();
            String[] split = line.split("\t");
            count += split.length;
        }
        System.out.println(count / traCount);
    }

    @Test
    public void test1() throws IOException {
        File file = new File("D:\\TraDataSet\\T-drive Taxi Trajectories\\release\\taxi_log_2008_by_id");
        File[] files = file.listFiles();
        System.out.println(files.length);
        int count = 0;
        for(File f : files){
            LineIterator lineIterator = FileUtils.lineIterator(f);

            while(lineIterator.hasNext()){
                String lint = lineIterator.next();
                count ++;
            }
        }
        System.out.println(count / files.length);
    }

    @Test
    public void test04() throws IOException {
        File file = new File("D:\\TraDataSet\\滴滴\\gps");
        //LineIterator lineIterator = FileUtils.lineIterator(new File("D:\\TraDataSet\\滴滴\\gps\\gps_20161101"));
        File[] files = file.listFiles();
        Map<String,Integer> map = new HashMap<>();

        for(File file1: files){
            LineIterator lineIterator = FileUtils.lineIterator(file1);
            while(lineIterator.hasNext()){
                String next = lineIterator.next();
                String[] split = next.split(",");
                map.put(split[0],map.getOrDefault(split[0],0) + 1);
            }
        }
        int max = -1,min = 10000000;

        for(Map.Entry<String,Integer> entry : map.entrySet()){
            max = Math.max(max,entry.getValue());
            min = Math.min(min,entry.getValue());
        }
        System.out.println(map.size() + "   " + max + "   " + min);
    }
}