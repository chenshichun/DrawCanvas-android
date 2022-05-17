package com.csc.drawcanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshichun
 * 创建日期：2022/4/25
 * 描述：
 */
public class DrawView extends View implements View.OnClickListener {

    private final Canvas canvas;//声明画笔
    private final Paint paint;//声明画布
    private final Paint surfacePaint;//声明画布
    private final Bitmap bitmap;//声明位图
    private final Paint textPaint;//声明画布
    private final Context context;
    private final List<PointBean> pointBeanList = new ArrayList();// 点集合
    private final List<PointBean> pointBeanListCopy = new ArrayList();// 点集合备份数据，用于前进
    private final List<LineBean> lineBeansList = new ArrayList();// 线集合

    public DrawView(Context context) {
        super(context);
        this.context = context;
        // TODO 自动生成的构造函数存根
        // bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test).copy(Bitmap.Config.ARGB_8888, true);//设置位图的宽高
        bitmap = Bitmap.createBitmap(1280, 1800, Bitmap.Config.ALPHA_8);//设置位图的宽高
        canvas = new Canvas(bitmap);

        // 点和线paint
        paint = new Paint();//创建一个画笔
        paint.setStyle(Paint.Style.STROKE);//设置非填充
        paint.setStrokeWidth(10);//笔宽5像素
        paint.setColor(Color.parseColor("#197AFF"));
        paint.setAntiAlias(true);//锯齿不显示
        paint.setDither(true);//设置图像抖动处理
        paint.setStrokeJoin(Paint.Join.ROUND);//设置图像的结合方式
        paint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为圆形样式

        // 面paint
        surfacePaint = new Paint(Paint.DITHER_FLAG);//创建一个画笔
        surfacePaint.setStyle(Paint.Style.FILL);//设置填充
        surfacePaint.setStrokeWidth(5);//笔宽5像素
        surfacePaint.setColor(Color.parseColor("#54197AFF"));
        surfacePaint.setAntiAlias(true);//锯齿不显示
        surfacePaint.setDither(true);//设置图像抖动处理
        surfacePaint.setStrokeJoin(Paint.Join.ROUND);//设置图像的结合方式
        surfacePaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为圆形样式

        // 文字paint
        textPaint = new Paint(Paint.DITHER_FLAG);//创建一个画笔
        textPaint.setStyle(Paint.Style.FILL);//设置填充
        textPaint.setStrokeWidth(2);//笔宽5像素
        textPaint.setColor(Color.parseColor("#ffffffff"));
        textPaint.setTextSize(56f);
        textPaint.setAntiAlias(true);//锯齿不显示
        textPaint.setDither(true);//设置图像抖动处理
        textPaint.setStrokeJoin(Paint.Join.ROUND);//设置图像的结合方式
        textPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为圆形样式
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private boolean isDraw = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                float currentX = event.getX();
                float currentY = event.getY();
                if (isRestart) {
                    PointBean pointBean = new PointBean();
                    pointBean.setX(currentX);
                    pointBean.setY(currentY);
                    pointBeanList.add(pointBean);
                    pointBeanListCopy.add(pointBean);

                    if (pointBeanListCopy.size() < pointBeanList.size()) {// 说明是后退以后再点击
                        pointBeanList.clear();
                        pointBeanList.addAll(pointBeanListCopy);
                    }

                    drawPointLines(pointBeanList);
                } else {// 点击设置长度
                    // 创建线集合数据
                    lineBeansList.clear();
                    for (int i = 0; i < pointBeanList.size(); i++) {
                        LineBean lineBean = new LineBean();
                        if (i + 1 < pointBeanList.size()) {
                            lineBean.x = pointBeanList.get(i).getX();
                            lineBean.y = pointBeanList.get(i).getY();
                            lineBean.x1 = pointBeanList.get(i + 1).getX();
                            lineBean.y1 = pointBeanList.get(i + 1).getY();
                            lineBeansList.add(lineBean);
                        }
                    }

                    isDraw = false;
                    for (LineBean lineBean : lineBeansList) {
                        if (((currentX < lineBean.x && currentX > lineBean.x1)
                                || (currentX > lineBean.x && currentX < lineBean.x1)) &&
                                ((currentY < lineBean.y && currentY > lineBean.y1)
                                        || (currentY > lineBean.y && currentY < lineBean.y1))) {// 点击的点所在的线
                            Toast.makeText(context, "ss", Toast.LENGTH_SHORT).show();
                            textPaint.setColor(getResources().getColor(R.color.white));
                            canvas.drawText("100", (lineBean.x + lineBean.x1) / 2, (lineBean.y + lineBean.y1) / 2, textPaint);
                            invalidate();
                            isDraw = true;
                        }
                    }

                    if (!isDraw) {
                        Toast.makeText(context, "请点击对应线", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /*
     * 画点线面
     * */
    private boolean isRestart = true;
    Path path1;
    Path path2;

    private void drawPointLines(List<PointBean> beanList) {
        // 每次清除画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if (beanList.size() > 1) {
            path1 = new Path();// 线的颜色蓝色
            path2 = new Path();// 面的颜色淡蓝色

            //  判断最后一个点和第一个点重合
            if (Math.abs(beanList.get(0).getX() - beanList.get(beanList.size() - 1).getX()) < 30
                    && Math.abs(beanList.get(0).getY() - beanList.get(beanList.size() - 1).getY()) < 30) {
                beanList.get(beanList.size() - 1).setX(beanList.get(0).getX());
                beanList.get(beanList.size() - 1).setY(beanList.get(0).getY());

                for (int i = 0; i < beanList.size(); i++) {
                    if (i == 0) {
                        path1.moveTo(beanList.get(i).getX(), beanList.get(i).getY());
                        path2.moveTo(beanList.get(i).getX(), beanList.get(i).getY());
                    } else {
                        path1.lineTo(beanList.get(i).getX(), beanList.get(i).getY());
                        path2.lineTo(beanList.get(i).getX(), beanList.get(i).getY());
                    }
                }
                path2.close();
                isRestart = false;
                surfacePaint.setStyle(Paint.Style.FILL);
            } else {
                for (int i = 0; i < beanList.size(); i++) {
                    if (i == 0) {
                        path1.moveTo(beanList.get(i).getX(), beanList.get(i).getY());
                    } else {
                        path1.lineTo(beanList.get(i).getX(), beanList.get(i).getY());
                    }
                }
            }
            canvas.drawPath(path1, paint);
            canvas.drawPath(path2, surfacePaint);
        } else {
            canvas.drawPoint(beanList.get(0).getX(), beanList.get(0).getY(), paint);
        }
        invalidate();//使绘画动作生效
    }

    @Override

    public void onClick(View v) {


    }

    /*
     * 前进
     * */
    public void forward() {
        if (pointBeanListCopy.size() < pointBeanList.size()) {
            pointBeanListCopy.add(pointBeanList.get(pointBeanListCopy.size()));
            drawPointLines(pointBeanListCopy);
            invalidate();//使绘画动作生效
        }
    }

    /*
     * 后退
     * */
    public void backOff() {
        isRestart = true;
        pointBeanListCopy.remove(pointBeanListCopy.size() - 1);
        drawPointLines(pointBeanListCopy);
        invalidate();//使绘画动作生效
    }

    public void clear() {
        isRestart = true;
        pointBeanList.clear();
        pointBeanListCopy.clear();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();//使绘画动作生效
    }

}

