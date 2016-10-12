package cxy.com.waterviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/*****************************************
 * 水波纹效果
 *
 * @author cxy
 *         created at  2016/10/11 10:39
 ****************************************/
public class WaterView extends View {

    private final boolean isShowFrame;
    private int mWidth;
    private int mHeight;
    private float centerX;
    private float centerY;

    Path canvasPath = new Path();
    private Path firstPath = new Path();
    private Paint firstPaint;
    private Path secondPath = new Path();
    private Paint secondPaint;
    private Paint framePaint;

    private float sin_cycle = 0.01f;//周期 ， 0.01f左右
    float sin_offset = 0.0f;//初项，偏移量
    float h = 0f;

    private int secondPaintColor = Color.parseColor("#9292DA");
    private int firstPaintColor = Color.parseColor("#5353C7");
    private int frameColor = Color.parseColor("#FF0000");
    private float frameWidth;
    private int sin_amplitude = 20;//振幅 ，10到100之间
    private float sin_offset_increment_value = 0.4f;//初项递增值，表示波浪的快慢
    private int sin_up_velocity = 5;//上升速度，参考值3
    private int sleep_time = 100; //休眠时间，参考值100
    private boolean isStart = false;
    private boolean isRun = false;
    private boolean isStop = false;
    private int progress;//当前进度

    public WaterView(Context context) {
        this(context, null);
    }

    public WaterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        初始化值
        secondPaintColor = Color.parseColor("#9292DA");
        firstPaintColor = Color.parseColor("#5353C7");
        sin_amplitude = 20;//振幅 ，10到100之间
        sin_offset_increment_value = 0.4f;//初项递增值，表示波浪的快慢
        sin_up_velocity = 5;//上升速度，参考值3
        sleep_time = 100; //休眠时间，参考值100
        frameColor = Color.parseColor("#FF0000");
        frameWidth = dip2px(context, 2);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterView);
        firstPaintColor = typedArray.getColor(R.styleable.WaterView_waterview_paint_color_first, firstPaintColor);
        secondPaintColor = typedArray.getColor(R.styleable.WaterView_waterview_paint_color_second, secondPaintColor);
        sin_amplitude = typedArray.getInt(R.styleable.WaterView_waterview_amplitude, sin_amplitude);
        sin_offset_increment_value = typedArray.getFloat(R.styleable.WaterView_waterview_offset_increment_value, sin_offset_increment_value);
        sin_up_velocity = typedArray.getInt(R.styleable.WaterView_waterview_up_velocity, sin_up_velocity);
        sleep_time = typedArray.getInt(R.styleable.WaterView_waterview_sleep_time, sleep_time);
        sleep_time = typedArray.getInt(R.styleable.WaterView_waterview_sleep_time, sleep_time);
        frameWidth = typedArray.getDimension(R.styleable.WaterView_waterview_frame_width, frameWidth);
        frameColor = typedArray.getColor(R.styleable.WaterView_waterview_frame_color, frameColor);
        typedArray.recycle();
        if (frameWidth == 0) {
            isShowFrame = false;
        } else {
            isShowFrame = true;
        }

        firstPaint = new Paint();
        firstPaint.setColor(firstPaintColor);
        firstPaint.setAntiAlias(true);
        secondPaint = new Paint();
        secondPaint.setColor(secondPaintColor);
        secondPaint.setAntiAlias(true);
        framePaint = new Paint();
        framePaint.setStrokeWidth(frameWidth);
        framePaint.setAntiAlias(true);
        framePaint.setColor(frameColor);
        framePaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = mHeight;

        canvasPath.addCircle(centerX, centerY, mHeight / 2, Path.Direction.CCW);
        reset();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(canvasPath, Region.Op.INTERSECT);
        if (isShowFrame)
            canvas.drawCircle(centerX, centerY, mHeight / 2, framePaint);
        if (isStart) {
            canvas.drawPath(secondPath(), secondPaint);
            canvas.drawPath(firstPath(), firstPaint);
        }
    }

    //y = Asin(wx+b)+h ，这个公式里：w影响周期，A影响振幅，h影响y位置，b为初相；
    private Path firstPath() {
        firstPath.reset();
        firstPath.moveTo(0, mHeight);// 移动到左下角的点

        for (float x = 0; x <= mWidth; x++) {
            float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset + 40)) + h;
            firstPath.lineTo(x, y);
        }
        firstPath.lineTo(mWidth, mHeight);
        firstPath.lineTo(0, mHeight);
        firstPath.close();
        return firstPath;
    }

    private Path secondPath() {
        secondPath.reset();
        secondPath.moveTo(0, mHeight);// 移动到左下角的点

        for (float x = 0; x <= mWidth; x++) {
            float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset)) + h;
            secondPath.lineTo(x, y);
        }
        secondPath.lineTo(mWidth, mHeight);
        secondPath.lineTo(0, mHeight);
        secondPath.close();
        return secondPath;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 设置宽度
         * 单位 px
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        } else {
            mWidth = dip2px(getContext(), 150);
        }

        /***
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else {
            mHeight = dip2px(getContext(), 150);
        }
        centerX = mWidth / 2;
        centerY = mHeight / 2;

        setMeasuredDimension(mWidth, mHeight);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void reset() {
        if (runThread != null) {
            runThread = null;
        }
        isRun = false;
        isStart = false;
        isStop=false;
        h = mHeight;
        sin_offset = 0;
        invalidate();
    }

    RunThread runThread;

    public void start() {
        isRun = true;
        isStop=false;
        if (!isStart) {
            isStart = true;
            runThread = new RunThread();
            runThread.start();
        }
    }

    public void stop() {
        isStop=true;
    }
    public void recover() {
        isStop=false;
    }

    class RunThread extends Thread {

        @Override
        public void run() {
            while (isStart) {
                if (!isRun) {
                    return;
                }
                if (isStop) {
                    continue;
                }
                try {
                    Thread.sleep(sleep_time);
                    h -= sin_up_velocity;
                    sin_offset += sin_offset_increment_value;
                    postInvalidate();
                    if (h + sin_amplitude < 0) {
                        if (listener != null) {
                            WaterView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.finish();
                                }
                            });
                        }
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Listener listener;

    public interface Listener {
        void finish();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}

