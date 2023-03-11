package com.wang.test;

import com.wang.MokerTree.Tries;
import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Trajectory;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/13 15:50
 */
public class One {

    @Test
    public void getShiTest(){
        try {
            File file = new File("D:\\桌面\\毕业论文\\性能测试执行记录.xls");
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int[] rows = {5,7,11,12,13};
            Map<String,int[]> map = new HashMap<>();
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    System.out.print(i + ":" + sheet.getColumns());
                    // sheet.getColumns()返回该页的总列数
                    String path = null;
                    String time;
                    time = sheet.getCell(5, i).getContents();
                    path = sheet.getCell(7,i).getContents();
                    int[] temp = {Integer.parseInt(sheet.getCell(11,i).getContents())
                                 ,Integer.parseInt(sheet.getCell(12,i).getContents())
                                ,Integer.parseInt(sheet.getCell(13,i).getContents())};
                    System.out.println();
                }
                break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TrajectoryDataset\\data");
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TrajectoryDataset\\NewYork\\len80");
        //轨迹ID ：轨迹
        Map<String,Trajectory> map = new HashMap<>();
        for(int i = 0;i < trajectoriesSet.size();i++){
            map.put(trajectoriesSet.get(i).getName(),trajectoriesSet.get(i));
        }
        String size = RamUsageEstimator.humanSizeOf(map);
        System.out.println("count = "+ size + " 时内存消耗大小为："+ size);
        int[] counts = {3};
        Map<Integer, Tries> bucket = new HashMap<>();
        for(int count : counts){
            CreateBucket createBucket = new CreateBucket(count,trajectoriesSet,
                    trajectoriesSet.size(),60,50,"trie",bucket);

            String size1 = RamUsageEstimator.humanSizeOf(createBucket);
            System.out.println("count = "+ count + " 时内存消耗大小为："+ size1);
            bucket.clear();
        }
    }


}