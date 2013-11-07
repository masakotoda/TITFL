package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TITFLTownView  extends View 
{
    private Rect m_rect;
    private TITFLActivity m_activity;
    
    public TITFLTownView(Context context, AttributeSet attrs)
    {
        super(context, attrs); // Do not call super(context). Always pass attrs. Otherwise findViewById will NOT work!
        setFocusable(true);
    }
    
    public static int width(TITFLActivity activity)
    {
        int h = NoEtapUtility.getScreenHeight(activity);
        int playerInfoW = TITFLPlayerView.getWidth(activity);
        return (h - playerInfoW);
    }

    public void initialize()
    {
        m_activity = (TITFLActivity)getContext();
        m_rect = new Rect(0, 0, width(m_activity), NoEtapUtility.getScreenWidth(m_activity));

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = m_rect.width();
        params.height = m_rect.height();
        this.setLayoutParams(params);
    }    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {    
        final TITFLActivity activity = (TITFLActivity)getContext();
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                activity.getTown().handleActionDown(event);
                activity.invalidate();
                break;
            default:
                break;
        }
        
        return true;
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);

        if (m_activity == null)
            return;
        
        try
        {
            Paint paint = new Paint();
            paint.setARGB(255, 224, 224, 224);
            canvas.drawRect(m_rect, paint);        
    
            // Draw town    
            if (m_activity.getTown() != null)
                m_activity.getTown().draw(canvas, paint);        
        }
        catch (Exception e)
        {            
        }
    }
}
