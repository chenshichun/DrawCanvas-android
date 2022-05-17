package com.csc.drawcanvas;

/**
 * @author chenshichun
 * 创建日期：2022/4/29
 * 描述：
 */
public class LineBean {
    float x;
    float y;
    float x1;
    float y1;

    @Override
    public String toString() {
        return "LineBean{" +
                "x=" + x +
                ", y=" + y +
                ", x1=" + x1 +
                ", y1=" + y1 +
                '}';
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }
}
