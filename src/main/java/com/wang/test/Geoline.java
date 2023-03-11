package com.wang.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  读取Geoline轨迹数据集文件
 *  @ClassName Geoline
 * Description
 * @Author jqWang
 * Date 2023/2/12 14:03
 **/
public class Geoline {
    public static void main(String[] args) throws IOException {
        String path = "F:\\BaiduNetdiskDownload\\Geolife Trajectories 1.3\\Geolife Trajectories 1.3\\Data";
        File file = new File(path);
        File[] files = file.listFiles();//用户层
        int userCount = 0;
        int posCount = 0;
        for(int i = 0;i < files.length;i ++){
            //遍历每一个用户
            File[] curFiles = files[i].listFiles();//获取每个用户下的用户轨迹
            for(int j = 0;j < curFiles.length;j ++){
                if(!curFiles[j].getName().equals("Trajectory")){
                    continue;
                }else {
                    File[] trajectories = curFiles[j].listFiles();
                    for(File trajectory : trajectories){
                        LineIterator it = FileUtils.lineIterator(trajectory);
                        for(int k = 0;k < 6;k ++){
                            it.next();
                        }
                        List<String> traInfo = new ArrayList<>();
                        while(it.hasNext()){
                            posCount ++;
                            String line = it.next();
                            String[] split = line.split(",");
                            String[] time = split[6].split(":");
                            int seconds =   Integer.parseInt(time[0]) * 3600 +
                                            Integer.parseInt(time[1]) * 60 +
                                            Integer.parseInt(time[2]);
                            traInfo.add(split[0] + "\t" + split[1] + "\t" + seconds);
                        }
                        /*BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\TrajectoryDataset\\geoline",true));
                        for (String tra : traInfo) {
                            bw.write(userCount + "\t" + tra);
                            bw.write("\n");
                        }
                        bw.flush();
                        bw.close();*/
                        userCount ++;
                    }
                }
            }
        }
        System.out.println("平均长度为：" + posCount/userCount);
    }
}
