package com.rainbow.blue.espresso;

import android.app.Application;
import android.graphics.PointF;
import android.test.ApplicationTestCase;

import com.rainbow.blue.espresso.chart.Series;

import junit.framework.Assert;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final int NUM = 200;
    private Series mSeriesBuffer;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSeriesBuffer = new Series();
        for (int i = 0; i < NUM; i++) {
            mSeriesBuffer.addPoint(i * 0.1f, i);
        }
    }

    public void testSeries() {
        Assert.assertEquals(mSeriesBuffer.size(), NUM);
        List<PointF> allData = mSeriesBuffer.fetchBuffer();
        Assert.assertEquals(allData.size(), NUM);
        List<PointF> tmp = mSeriesBuffer.fetchBuffer(0.4f, 0.6f);
        mSeriesBuffer.remove(0.1f);
        mSeriesBuffer.remove(0.6f);
        mSeriesBuffer.remove(new PointF(1.5f, 15));
    }

}