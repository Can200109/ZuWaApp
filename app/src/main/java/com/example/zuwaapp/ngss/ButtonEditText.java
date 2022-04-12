package com.example.zuwaapp.ngss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zuwaapp.R;

public class ButtonEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Button mButton;
    private int mButtonHeightPadding = 10;
    private int mButtonWeightPadding = 10;

    public ButtonEditText(Context context) {
        super(context);
        init();
    }

    public ButtonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        mButton = new Button(getContext());
        mButton.setText("搜索");
      //  mButton.setBackgroundColor(R.drawable.button);
        mButton.scrollBy(0,9);
        mButton.setBackgroundResource(R.drawable.button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked me!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getY() > mButtonHeightPadding && event.getY() < getHeight() - mButtonHeightPadding &&
                event.getX() > getWidth() - mButtonWeightPadding - mButton.getMeasuredWidth() &&
                event.getX() < getWidth() - mButtonWeightPadding) {
            return mButton.dispatchTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mButton.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - mButtonHeightPadding * 2, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

       // mButton.layout(0, -100, mButton.getMeasuredWidth(), mButton.getMeasuredHeight());
        mButton.layout(-3, -100, mButton.getMeasuredWidth(), mButton.getMeasuredHeight()-90);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.save();
        canvas.translate(getMeasuredWidth() - (mButton.getMeasuredWidth() + mButtonWeightPadding), mButtonHeightPadding);
        mButton.draw(canvas);
        canvas.restore();
    }

}
