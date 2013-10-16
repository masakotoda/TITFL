package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TITFLPlayerView  extends View 
{
    private TITFLActivity m_activity;
    private TITFLPlayer m_player;
    
    public TITFLPlayerView(Context context, AttributeSet attrs)
    {
        super(context, attrs); // Do not call super(context). Always pass attrs. Otherwise findViewById will NOT work!
        setFocusable(true);
    }
    
    static int getWidth(Activity activity)
    {
        int w = NoEtapUtility.getScreenWidth(activity);
        int playerInfoW = (int)(0.6 * w);
        return playerInfoW;
    }

    public void initialize()
    {
        m_activity = (TITFLActivity)getContext();
        int w = NoEtapUtility.getScreenWidth(m_activity);
        int playerInfoW = getWidth(m_activity);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = playerInfoW;
        params.height = w;
        this.setLayoutParams(params);
    }    
    
    public void setPlayer(TITFLPlayer player)
    {
        m_player = player;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {    
        @SuppressWarnings("unused")
        final TITFLActivity activity = (TITFLActivity)getContext();
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                DialogPlayerStatus dialog = new DialogPlayerStatus(m_player, m_activity);
                dialog.show();
                invalidate();
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
            paint.setColor(Color.LTGRAY);

            if (m_player != null)
                m_player.draw(canvas, paint);
            else
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        }
        catch (Exception e)
        {            
        }
    }
}
