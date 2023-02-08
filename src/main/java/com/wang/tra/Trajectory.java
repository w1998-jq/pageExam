package com.wang.tra;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/11 14:52
 */
public class Trajectory {
    private String name;
    private Point[] points;

    public Trajectory() {
    }

    public Trajectory(String name, Point[] points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
}