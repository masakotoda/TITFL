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
        int playerInfoW = (int)(0.542 * w); // Nexus 4 has exceptional screen ratio (1184x 768 -> 1.542. The physical screen size is 1280. But on-screen button takes 96.)
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
                drawPlayer(canvas, paint);
            else
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        }
        catch (Exception e)
        {            
        }
    }
    
    private void drawPlayer(Canvas canvas, Paint paint)
    {
        if (m_player.currentLocation() == null)
            return;

        // Change theme color depending on the character?
        paint.setColor(m_player.themeColor());

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        canvas.drawRect(0, 0, w, h, paint);

        float factor = NoEtapUtility.getFactor(m_player.currentLocation().town().activity());
        float textSize = 48 * factor;

        paint.setARGB(255, 0, 0, 0);
        paint.setTextSize(textSize);

        float top = 0;
        float left = textSize;
        
        top += textSize;
        canvas.drawText(Float.toString(m_player.hour()) + " " + m_player.alias(), left, top, paint);
        top += textSize;
        
        left += w / 2;
        top += textSize;
        textSize = 32 * factor;
        paint.setTextSize(textSize);

        final TITFL.Satisfaction goal = m_activity.game().goal();

        top += textSize;
        canvas.drawText("Wealth: " + Integer.toString(m_player.getWealthPercent(goal.m_wealth)) + "%", left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Education: " + Integer.toString(m_player.getEducationPercent(goal.m_education)) + "%", left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Career: " + Integer.toString(m_player.getCareerPercent(goal.m_career)) + "%", left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Life: " + Integer.toString(m_player.getLifePercent(goal.m_life)) + "%", left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Health: " + Integer.toString(m_player.getHealthPercent(goal.m_health)) + "%", left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Happiness: " + Integer.toString(m_player.getHappinessPercent(goal.m_happiness)) + "%", left, top, paint);
        top += textSize;
        
        top += textSize;
        canvas.drawText("Cash: $" + Integer.toString(m_player.cash()), left, top, paint);
        top += textSize;

        top += textSize;
        int count = 0;
        for (int i = m_player.belongings().size() - 1; i >= 0 && count < 20; i--)
        {
            canvas.drawText(m_player.belongings().get(i).toString(), left, top, paint);
            top += textSize;
            count++;
        }

        top = h;
        left = textSize;

        top -= textSize;
        canvas.drawText("Home: " + m_player.home().name(), left, top, paint);

        top -= textSize;
        canvas.drawText("Transp: " + m_player.transportation().name(), left, top, paint);

        top -= textSize;
        if (m_player.job() == null)
            canvas.drawText("Work as: Jobless", left, top, paint);
        else
            canvas.drawText("Work as: " + m_player.job().name(), left, top, paint);

        top -= textSize;
        if (m_player.job() == null)
            canvas.drawText("Work at: N/A", left, top, paint);
        else
            canvas.drawText("Work at: " + m_player.job().townelement().name(), left, top, paint);

        /*
        top -= textSize;

        top -= textSize;
        canvas.drawText("Edu.Academic: " + Integer.toString(m_education.m_academic), left, top, paint);
        top -= textSize;
        canvas.drawText("Edu.Engineer: " + Integer.toString(m_education.m_engineering), left, top, paint);
        top -= textSize;
        canvas.drawText("Edu.Business: " + Integer.toString(m_education.m_business_finance), left, top, paint);
        top -= textSize;
        canvas.drawText("Edu.Basic: " + Integer.toString(m_education.m_basic), left, top, paint);

        top -= textSize;

        top -= textSize;
        canvas.drawText("Exp.Academic: " + Integer.toString(m_experience.m_academic), left, top, paint);
        top -= textSize;
        canvas.drawText("Exp.Engineer: " + Integer.toString(m_experience.m_engineering), left, top, paint);
        top -= textSize;
        canvas.drawText("Exp.Business: " + Integer.toString(m_experience.m_business_finance), left, top, paint);
        top -= textSize;
        canvas.drawText("Exp.Basic: " + Integer.toString(m_experience.m_basic), left, top, paint);
        */        
    }
}
