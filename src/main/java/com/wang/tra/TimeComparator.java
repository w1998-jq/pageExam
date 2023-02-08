package com.wang.tra;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/2/15 12:42
 */
public class TimeComparator<T> implements Serializable, Comparator<T> {
    /**
     * Compare objects (Points or Trajectories) by time stamp
     * in ascending order.
     */
    public int compare(T obj1, T obj2) {
        if (obj1 instanceof Point) {
            Point p1 = (Point) obj1;
            Point p2 = (Point) obj2;
            return compare(p1, p2);
        }

        return 0;
    }

    /**
     * Compare points by time-stamp in ascending order.
     */
    private int compare(Point p1, Point p2) {
        return p1.getTime() > p2.getTime() ? 1 : (p1.getTime() < p2.getTime() ? -1 : 0);
    }




}