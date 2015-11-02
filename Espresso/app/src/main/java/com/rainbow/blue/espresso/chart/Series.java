package com.rainbow.blue.espresso.chart;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by blue on 2015/10/20.
 */
public class Series {
    public static final int INVALID = -1;
    public static final int DEV_POINTS = 12 * 60 * 30;
    private static final float DECIMAL = 1;
    private List<PointF> content = new ArrayList<>();
    private String title;

    public Series(String title) {
        this.title = title;
    }


    public Series() {
        this.title = "default";
    }

    public String getTitle() {
        return this.title;
    }

    public int size() {
        return content.size();
    }

    /**
     * @param point
     */
    public void addPoint(PointF point) {
        if (point == null)
            throw new NullPointerException("the point can not be empty");
        content.add(point);
    }

    public void addPoint(float x, float y) {
        PointF point = new PointF(x, y);
        content.add(point);
    }

    public void remove(float x) {
        Iterator<PointF> iterator = content.iterator();
        while (iterator.hasNext()) {
            PointF p = iterator.next();
            if (p.x == x)
                iterator.remove();
        }
    }

    public void remove(PointF point) {
        Iterator<PointF> iterator = content.iterator();
        while (iterator.hasNext()) {
            PointF p = iterator.next();
            if (p.x == point.x && point.y == point.y)
                iterator.remove();
        }
    }

    /**
     * fetch more data that less than the start and large than the end
     *
     * @param start
     * @param end
     * @param decimal be sure that large than 0
     * @return
     */
    public List<PointF> fetchBuffer(float start, float end, float decimal) {
        if (start > end) {
            Log.e("series", String.format("the start:%s can not be large than end: %s", start, end));
            float tmp = start;
            start = end;
            end = tmp;
        }
        List<PointF> list = new ArrayList<>();
        for (PointF point : content) {
            if (point.x >= (start - decimal) && point.x <= end + decimal) {
                list.add(point);
            }
        }
        return list;
    }

    /**
     * @param start
     * @param end
     * @return
     */
    public List<PointF> fetchBuffer(float start, float end) {

        return this.fetchBuffer(start, end, DECIMAL);
    }

    public List<PointF> fetchBuffer(double start, double end) {

        return this.fetchBuffer((float) start, (float) end, DECIMAL);
    }

    public List<PointF> fetchBuffer() {
        return this.content;
    }

    /**
     * add data for developer
     */
    public void devInitData(int start) {
        for (int i = 0; i < DEV_POINTS; i++) {
            addPoint(i * 2, (float) (start + Math.random() * 4));
        }
    }

    public enum Mode {
        Sec, Min;
    }


}