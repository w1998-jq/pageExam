package com.wang.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取Geoline轨迹数据集文件
 *
 * @ClassName Trucks
 * Description
 * @Author jqWang
 * Date 2023/2/13 13:29
 **/
public class Trucks {
    public static void main(String[] args) throws IOException {
        String path = "D:/TrajectoryDataset/trucks/Trucks.txt";
        File file = new File(path);
        int posCount = 0;

        LineIterator it = FileUtils.lineIterator(file);
        Map<String,List<String>> tras = new HashMap<>();
        while (it.hasNext()) {
            posCount++;
            String line = it.next();
            String[] split = line.split(";");
            String id = split[0];

            String[] time = split[3].split(":");
            int seconds = Integer.parseInt(time[0]) * 3600 +
                    Integer.parseInt(time[1]) * 60 +
                    Integer.parseInt(time[2]);
            if(!tras.containsKey(id)){
                List<String> tra = new ArrayList<>();
                tras.put(id,tra);
            }
            tras.get(id).add(split[4] + "\t" + split[5] + "\t" + time);
        }
        /*BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\TrajectoryDataset\\trucks\\trucks", true));
        for (Map.Entry<String,List<String>> entry : tras.entrySet()){
            List<String> value = entry.getValue();
            for(String tra : value){
                bw.write(entry.getKey() + "\t" + tra + "\n");
            }
        }
        bw.flush();
        bw.close();*/
        System.out.println("轨迹数量：" + tras.size());
        System.out.println("平均长度为：" + posCount / tras.size());
    }
}
