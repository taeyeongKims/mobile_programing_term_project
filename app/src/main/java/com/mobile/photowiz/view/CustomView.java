package com.mobile.photowiz.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private Bitmap image;
    private OnBackgroundClickListener backgroundClickListener;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        int imageResId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "imageSrc", 0);
        if(imageResId != 0){
            image = BitmapFactory.decodeResource(getResources(),imageResId);
        }
    }
    public interface OnBackgroundClickListener{
        void onBackgroundClick();
    }
    @Override
    protected  void onDraw(android.graphics.Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(image,null, new android.graphics.RectF(0,0,getWidth(),getHeight()),null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(backgroundClickListener != null){
                backgroundClickListener.onBackgroundClick();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setOnBackgroundClickListener(OnBackgroundClickListener listener){
        this.backgroundClickListener = listener;
    }


}
