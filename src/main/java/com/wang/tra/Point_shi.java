package com.wang.tra;

/**
 * @ClassName Point_shi
 * Description
 * @Author jqWang
 * Date 2023/2/12 11:02
 **/
public class Point_shi {
    private int pos;
    private int time; //当前时间戳

    public Point_shi() {
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Point_shi(int pos, int time){
        this.pos = pos;
        this.time = time;
    }



}
