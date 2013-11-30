package com.noetap.titfl;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TITFLPlayerView  extends RelativeLayout
{
    private TITFLActivity m_activity;
    private TITFLPlayer m_player;

    private String m_nameHome;
    private String m_item1;
    private String m_item2;
    private String m_item3;
    private String m_item4;
    private String m_item5;
    private ImageView m_image1;
    private ImageView m_image2;
    private ImageView m_image3;
    private ImageView m_image4;
    private ImageView m_image5;

    private ProgressBar m_clock;
    private ImageView m_imageHome;
    private ProgressBar m_barWealth;
    private ProgressBar m_barCareer;
    private ProgressBar m_barEducation;
    private ProgressBar m_barLeisure;
    private ProgressBar m_barHealth;
    private ProgressBar m_barHappiness;
    private TextView m_textJob;
    private TextView m_textAlias;
    private TextView m_textWeek;
    private TextView m_textCash;
    
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

        inflate(m_activity, R.layout.viewgroup_player, this);

        m_clock = (ProgressBar) findViewById(R.id.progressBarClock);
        m_image1 = (ImageView) findViewById(R.id.imageView1);
        m_image2 = (ImageView) findViewById(R.id.imageView2);
        m_image3 = (ImageView) findViewById(R.id.imageView3);
        m_image4 = (ImageView) findViewById(R.id.imageView4);
        m_image5 = (ImageView) findViewById(R.id.imageView5);
        m_imageHome = (ImageView) findViewById(R.id.imageViewHome);
        m_barWealth = (ProgressBar) findViewById(R.id.progressBar1);
        m_barCareer = (ProgressBar) findViewById(R.id.progressBar2);
        m_barEducation = (ProgressBar) findViewById(R.id.progressBar3);
        m_barLeisure = (ProgressBar) findViewById(R.id.progressBar4);
        m_barHealth = (ProgressBar) findViewById(R.id.progressBar5);
        m_barHappiness = (ProgressBar) findViewById(R.id.progressBar6);
        m_textJob = (TextView) findViewById(R.id.textViewJob);
        m_textAlias = (TextView) findViewById(R.id.textViewAlias);
        m_textWeek = (TextView) findViewById(R.id.textViewWeek);
        m_textCash = (TextView) findViewById(R.id.textViewCash);
    }    
    
    public void setPlayer(TITFLPlayer player)
    {
        m_player = player;
    }
    
    private void updateSatisfaction()
    {
        TITFL.Satisfaction goal = m_activity.game().goal();
        m_barWealth.setMax(100);
        m_barCareer.setMax(100);
        m_barEducation.setMax(100);
        m_barLeisure.setMax(100);
        m_barHealth.setMax(100);
        m_barHappiness.setMax(100);
        m_barWealth.setProgress(m_player.getWealthPercent(goal.m_wealth));
        m_barCareer.setProgress(m_player.getCareerPercent(goal.m_career));
        m_barEducation.setProgress(m_player.getEducationPercent(goal.m_education));
        m_barLeisure.setProgress(m_player.getLifePercent(goal.m_life));
        m_barHealth.setProgress(m_player.getHealthPercent(goal.m_health));
        m_barHappiness.setProgress(m_player.getHappinessPercent(goal.m_happiness));
    }
    
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void updateClock()
    {
        int color = Color.rgb(255, 201, 14);
        int angle = m_player.hourByAngle();

        ShapeDrawable d0 = new ShapeDrawable(new ArcShape(0, 360));
        d0.getPaint().setColor(Color.WHITE);

        Drawable d1 = m_activity.getResources().getDrawable(R.drawable.progressbar_clock);
        
        ShapeDrawable d2 = new ShapeDrawable(new ArcShape(270, angle));
        d2.getPaint().setColor(color);
        d2.getPaint().setAlpha(192);
        d2.getPaint().setStrokeWidth(10);
       
        Drawable[] ds = new Drawable[3];
        ds[0] = d0;
        ds[1] = d1;
        ds[2] = d2;
        LayerDrawable l = new LayerDrawable(ds);
        
        int sdkVer = android.os.Build.VERSION.SDK_INT;
        if (sdkVer < 16)
            m_clock.setBackgroundDrawable(l);
        else
            m_clock.setBackground(l);
    }
    
    private void updateHome()
    {
        if (!m_player.home().id().equals(m_nameHome))
        {
            m_nameHome = m_player.home().id();
            m_imageHome.setImageBitmap(m_player.home().getBitmap());
        }
    }
    
    private void updateBelongings()
    {
        Bitmap[] bitmaps = new Bitmap[5];
        String[] strings = new String[5];
        for (int i = 0; i < 5; i++)
            strings[i] = "";
        
        ArrayList<TITFLBelonging> b = m_player.belongings();
        int count = 0;
        for (int i = b.size() - 1; i >= 0 && count < 5; i--)
        {
            if (b.get(i).goodsRef().getBitmap(m_activity) != null)
            {
                bitmaps[count] = b.get(i).getBitmap();
                strings[count] = b.get(i).goodsRef().id();
                count++;
            }
        }
        if (!strings[0].equals(m_item1))
        {
            m_item1 = strings[0];
            m_image1.setImageBitmap(bitmaps[0]);
        }
        if (!strings[1].equals(m_item2))
        {
            m_item2 = strings[1];
            m_image2.setImageBitmap(bitmaps[1]);
        }
        if (!strings[2].equals(m_item3))
        {
            m_item3 = strings[2];
            m_image3.setImageBitmap(bitmaps[2]);
        }
        if (!strings[3].equals(m_item4))
        {
            m_item4 = strings[3];
            m_image4.setImageBitmap(bitmaps[3]);
        }
        if (!strings[4].equals(m_item5))
        {
            m_item5 = strings[4];
            m_image5.setImageBitmap(bitmaps[4]);
        }
    }

    private void updateTexts()
    {
        String job;
        if (m_player.job() == null)
        {
            job = "Jobless";
        }
        else
        {
            job = "Work @ " + m_player.job().townelement().name();
        }
        m_textJob.setText(job);
        
        m_textAlias.setText(m_player.alias());
        
        String week = "Week #" + Integer.toString(m_player.currentLocation().town().currentWeek());
        m_textWeek.setText(week);

        String cash = "$" + Integer.toString(m_player.cash());
        m_textCash.setText(cash);
    }
    
    private void updateBackground()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            ViewGroup c = (ViewGroup)getChildAt(i);
            c.setBackgroundColor(m_player.themeColor());
        }
    }
    
    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) 
    {
        if (isInEditMode())
            return;

        if (arg0 == true)
        {
            int w = arg3 - arg1;
            int h = arg4 - arg2;
            for (int i = 0; i < getChildCount(); i++)
            {
                ViewGroup c = (ViewGroup)getChildAt(i);
                c.layout(0, 0, w, h);
                c.setBackgroundColor(m_player.themeColor());
            }
            m_clock.setIndeterminateDrawable(getBackground());
            m_clock.setIndeterminate(false);
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (isInEditMode())
            return;

        updateBackground();
        updateClock();
        updateSatisfaction();
        updateTexts();
        updateHome();
        updateBelongings();
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
