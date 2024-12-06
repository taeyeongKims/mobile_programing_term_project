package com.mobile.photowiz.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
        if (image != null) {
            // Calculate scale factors to maintain the aspect ratio
            float imageAspectRatio = (float) image.getWidth() / image.getHeight();
            float viewAspectRatio = (float) getWidth() / getHeight();

            float left, top, right, bottom;

            if (imageAspectRatio > viewAspectRatio) {
                // Image is wider than the view
                float scaledHeight = getWidth() / imageAspectRatio;
                left = 0;
                top = (getHeight() - scaledHeight) / 2;
                right = getWidth();
                bottom = top + scaledHeight;
            } else {
                // Image is taller than the view
                float scaledWidth = getHeight() * imageAspectRatio;
                left = (getWidth() - scaledWidth) / 2;
                top = 0;
                right = left + scaledWidth;
                bottom = getHeight();
            }

            // Draw the bitmap with adjusted bounds
            canvas.drawBitmap(image, null, new android.graphics.RectF(left, top, right, bottom), null);
        }
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
