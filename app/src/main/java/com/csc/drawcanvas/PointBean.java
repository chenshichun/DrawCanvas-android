package com.csc.drawcanvas;

/**
 * @author chenshichun
 * 创建日期：2022/4/25
 * 描述：
 */
public class PointBean {
    float x;
    float y;

    @Override
    public String toString() {
        return "PointBean{" +
                "x=" + x +
                ", y=" + y +
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
}
