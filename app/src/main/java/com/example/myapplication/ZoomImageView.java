package com.example.myapplication;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class ZoomImageView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener{

    /** 记录是拖拉照片模式还是放大缩小照片模式 */
    private int mode = 0;// 初始状态
    /** 拖拉照片模式 */
    private static final int MODE_DRAG = 1;
    /** 放大缩小照片模式 */
    private static final int MODE_ZOOM = 2;

    public static final float MAXSCALE = 2f;

    public static final float MINSCALE = 0.5f;

    /** 用于记录开始时候的坐标位置 */
    private PointF startPoint = new PointF();
    /** 用于记录拖拉图片移动的坐标位置 */
    private Matrix matrix = new Matrix();
    /** 用于记录图片要进行拖拉时候的坐标位置 */
    private Matrix initMatrix = new Matrix();
    private float finalScale = 1f;
    /** 两个手指的开始距离 */
    private float startDis;

    private float finalX = 0;
    private float finalY = 0;
    /** 两个手指的中间点 */
    private PointF midPoint;
    Transform transform;

    public ZoomImageView(final Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        initMatrix.set(getImageMatrix());
        transform = new Transform();
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Point p = getScreen(context);
                transform.screenWidth = p.x;
                transform.screenHeight = p.y;
                getImage();
                Log.d("Sam","ScreenWidth: "+ p.x + " height:" + p.y);
            }
        });
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setOnTouchListener(this);
    }

    public ZoomImageView(Context context) {
        this(context, null);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                // 记录ImageView当前的移动位置
                startPoint.set(event.getX(), event.getY());
                return true;
            //手指在屏幕上移动，该事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if(mode == MODE_DRAG){
                    float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                    float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    matrix.set(initMatrix);
                    matrix.postTranslate(transform.xMove,transform.yMove);
                    matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
                    matrix.postTranslate(dx, dy);
                    finalX = dx;
                    finalY = dy;
                }
                // 放大缩小图片
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        float scale = endDis/startDis;// 得到缩放倍数
                        finalScale = scale;
                        matrix.set(initMatrix);
                        matrix.postTranslate(transform.xMove,transform.yMove);
                        matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
                        matrix.postScale(scale,scale,midPoint.x,midPoint.y);
                    }
                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                if(mode == MODE_ZOOM){
                    transform.scale(midPoint.x, midPoint.y, finalScale);
                    matrix.set(initMatrix);
                    matrix.postTranslate(transform.xMove,transform.yMove);
                    matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
                }else if(mode == MODE_DRAG){
                    transform.move(finalX,finalY);
                    matrix.set(initMatrix);
                    matrix.postTranslate(transform.xMove,transform.yMove);
                    matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
                }
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(event);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event);
                    //记录当前ImageView的缩放倍数
                }
                return true;
        }
        setImageMatrix(matrix);
        return true;
    }

    /** 计算两个手指间的距离 */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /** 计算两个手指间的中间点 */
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    public void setInitPosition(int x, int y){
        transform.move(x,y);
        matrix.set(initMatrix);
        matrix.postTranslate(transform.xMove,transform.yMove);
        matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
        setImageMatrix(matrix);
    }

    public void setInitScale(float pointX, float pointY, float scale){
        transform.scale(pointX, pointY, scale);
        matrix.set(initMatrix);
        matrix.postTranslate(transform.xMove,transform.yMove);
        matrix.postScale(transform.scale, transform.scale, transform.xMove,transform.yMove);
        setImageMatrix(matrix);
    }

    public void getImage(){
        transform.imageWidth = this.getDrawable().getBounds().width();
        transform.imageHeight = this.getDrawable().getBounds().height();
        Log.d("Sam","imageWidth: "+ transform.imageWidth + " height:" + transform.imageHeight);
    }

    public static Point getScreen(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size;

    }
}

//该类用来管理位移信息
class Transform{
    public float xMove = 0;
    public float yMove = 0;
    public float scale = 1f;
    public int imageWidth = 0;
    public int imageHeight = 0;
    public int screenWidth = 0;
    public int screenHeight = 0;
    //输出的时候始终是先在1f的情况下平移，再以0,0为中心点缩放
    public void move(float moveX, float moveY){
        if(xMove + moveX > 0){
            moveX = -xMove;
        }else if(xMove + moveX < screenWidth - imageWidth*scale){
            moveX = screenWidth - imageWidth*scale - xMove;
        }
        if(yMove + moveY > 0){
            moveY = -yMove;
        }else if(yMove + moveY < screenHeight - imageHeight*scale){
            moveY = screenHeight - imageHeight*scale - yMove;
        }
        xMove += moveX;
        yMove += moveY;
    }

    public void scale(float pointX, float pointY, float finalScale){
        finalScale = Math.max(finalScale,ZoomImageView.MINSCALE / this.scale);
        finalScale = Math.min(finalScale,ZoomImageView.MAXSCALE / this.scale);
        this.scale *= finalScale;
        //xMove -= (finalScale - 1.0)*(pointX-xMove);
        //yMove -= (finalScale - 1.0)*(pointY-yMove);
        move(-(finalScale - 1.0f)*(pointX-xMove),-(finalScale - 1.0f)*(pointY-yMove));

    }
}

