package com.adinnet.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by LS on 2017/5/18 0018.
 */

public class SuperItemView extends View {
    private final float mLeftTextPaddingLeft;
    private final float mRightTextPaddingRight;
    private float mLeftTextSize;
    private float mRightTextSize;
    private int mLeftTextColor;
    private int mRightTextColor;
    private String mLeftText;
    private String mRightText;
    private Drawable mArrow;
    private Drawable mRightDrawable;
    private Drawable mLeftDrawable;
    private Paint mPaint;

    private float mBottomLinePaddingLeft;
    private float mBottomLinePaddingRight;
    private float mTopLinePaddingLeft;
    private float mTopLinePaddingRight;

    private float mRightTextPaddingTop;

    private float mLineHeight = 1f;
    private int mLineColor;

    private boolean showTopLine;
    private boolean showBottomLine;
    private boolean hasNew;
    private int mPointColor;

    public SuperItemView(Context context) {
        this(context,null);
    }

    public SuperItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperItemView);

        float left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics());
        float right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mLeftTextSize = a.getDimension(R.styleable.SuperItemView_text_left_size, left);
        mRightTextSize = a.getDimension(R.styleable.SuperItemView_text_right_size, right);

        mLeftText = a.getString(R.styleable.SuperItemView_text_left);
        mRightText = a.getString(R.styleable.SuperItemView_text_right);

        mLeftTextColor = a.getColor(R.styleable.SuperItemView_text_left_color, 0xFF222222);
        mRightTextColor = a.getColor(R.styleable.SuperItemView_text_right_color, 0xFFA4A4A4);
        mLineColor = a.getColor(R.styleable.SuperItemView_line_color, 0xFFE1E1E1);
        mPointColor = a.getColor(R.styleable.SuperItemView_point_color, 0xFFFF6E2A);

        showTopLine = a.getBoolean(R.styleable.SuperItemView_show_top_line, false);
        showBottomLine = a.getBoolean(R.styleable.SuperItemView_show_bottom_line, true);
        hasNew = a.getBoolean(R.styleable.SuperItemView_show_has_new, false);

        mBottomLinePaddingLeft = a.getDimension(R.styleable.SuperItemView_bottom_line_paddingLeft, 0);
        mBottomLinePaddingRight = a.getDimension(R.styleable.SuperItemView_bottom_line_paddingRight, 0);
        mTopLinePaddingLeft = a.getDimension(R.styleable.SuperItemView_top_line_paddingLeft, 0);
        mTopLinePaddingRight = a.getDimension(R.styleable.SuperItemView_top_line_paddingRight, 0);

        mLeftTextPaddingLeft = a.getDimension(R.styleable.SuperItemView_left_text_paddingLeft, 0);

        mRightTextPaddingTop = a.getDimension(R.styleable.SuperItemView_right_text_paddingTop, 0);
        mRightTextPaddingRight = a.getDimension(R.styleable.SuperItemView_right_text_paddingRight, 0);

        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SuperItemView_icon_left) {
                mLeftDrawable = a.getDrawable(attr);
            } else if (attr == R.styleable.SuperItemView_arrow) {
                mArrow = a.getDrawable(attr);
            } else if (attr == R.styleable.SuperItemView_right_drawable) {
                mRightDrawable = a.getDrawable(attr);
            }
        }

//        if(mArrow == null) mArrow = context.getResources().getDrawable(R.mipmap.icon_farward);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if(getPaddingLeft() > 0) canvas.translate(getPaddingLeft(), 0);
        if(mLeftDrawable != null)drawLeftIcon(canvas);

        if(!TextUtils.isEmpty(mLeftText))drawLeftText(canvas);

        canvas.restore();

        canvas.save();
        if(mArrow != null)drawArrow(canvas);
        else {
            canvas.translate(-getPaddingRight(), getPaddingTop());
        }

        if(!TextUtils.isEmpty(mRightText))drawRightText(canvas);
        else if(mRightDrawable != null) drawRightDrawable(canvas);

        canvas.restore();
        drawLine(canvas);
    }

    private void drawLeftIcon(Canvas canvas) {
//        int left = getPaddingLeft();
        int top = (getHeight() - mLeftDrawable.getIntrinsicHeight()) / 2 + getPaddingTop();
        mLeftDrawable.setBounds(0, top, mLeftDrawable.getIntrinsicWidth(), top+mLeftDrawable.getIntrinsicHeight());
        mLeftDrawable.draw(canvas);

//        canvas.save();
        canvas.translate(mLeftDrawable.getIntrinsicWidth(), getPaddingTop());
    }

    /**画线，底部和顶部，线的padding是单独配置，不受其他padding影响{@link com.adinnet.widget.R.styleable#SuperItemView_top_line_paddingLeft}*/
    private void drawLine(Canvas canvas) {
        mPaint.setStrokeWidth(mLineHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        if(showBottomLine)canvas.drawLine(mBottomLinePaddingLeft, getHeight() - mLineHeight, getWidth() - mBottomLinePaddingRight, getHeight() - mLineHeight, mPaint);

        if(showTopLine)canvas.drawLine(mTopLinePaddingLeft, mLineHeight, getWidth() - mTopLinePaddingRight, mLineHeight, mPaint);
    }

    /** 右侧图标，常见的就是一个箭头*/
    private void drawArrow(Canvas canvas) {
        int left = getWidth() - mArrow.getIntrinsicWidth() - getPaddingRight();
        int top = (getHeight() - mArrow.getIntrinsicHeight()) / 2 + getPaddingTop();
        mArrow.setBounds(left, top, left+mArrow.getIntrinsicWidth(), top+mArrow.getIntrinsicHeight());
        mArrow.draw(canvas);

        canvas.translate(-(mArrow.getIntrinsicWidth() + getPaddingRight()), getPaddingTop());
    }

    private void drawRightDrawable(Canvas canvas) {
        int widthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, Resources.getSystem().getDisplayMetrics());
        int left = getWidth() - widthAndHeight;
        int top = (getHeight() - widthAndHeight) / 2;
        mRightDrawable.setBounds(left, top, left+widthAndHeight, top+widthAndHeight);
        mRightDrawable.draw(canvas);
    }

    private void drawLeftText(Canvas canvas) {

        mPaint.setTextSize(mLeftTextSize);
        mPaint.setColor(mLeftTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        Rect bounds =  new Rect();
        mPaint.getTextBounds(mLeftText, 0, mLeftText.length(), bounds);
//        canvas.drawRect(bounds, mPaint);
        canvas.drawText(mLeftText, mLeftTextPaddingLeft,getHeight()/2+ bounds.height()/4 + getPaddingTop(), mPaint);
    }

    private void drawRightText(Canvas canvas) {
        mPaint.setTextSize(mRightTextSize);
        mPaint.setColor(mRightTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect();
        mPaint.getTextBounds(mRightText, 0, mRightText.length(), bounds);
        // 真机发现文字位置和图标稍微有偏差,增加单独设置文字paddingtop
        int top = (int) (getHeight() / 2 + bounds.height() / 4 + mRightTextPaddingTop);
        int left = (int) (getWidth() - bounds.width() - mRightTextPaddingRight);
        canvas.drawText(mRightText, left, top, mPaint);

//        canvas.translate(-bounds.width(), 0);
        if(hasNew) {
            mPaint.setColor(mPointColor);
//            float pointMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics());
            float pointRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, Resources.getSystem().getDisplayMetrics()) / 2;
            canvas.drawCircle(left + bounds.width(), top+pointRadius*2-bounds.height(), pointRadius, mPaint);
        }
    }

    public void setRightText(String text) {
        this.mRightText = text;

        postInvalidate();
    }

    public void setLeftText(String text) {
        this.mLeftText = text;

        postInvalidate();
    }

    public void setRightDrawable(Drawable drawable) {
        postInvalidate();
    }

    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }

    public String getRightText() {
        return mRightText;
    }
}
