package com.noetap.titfl;

import java.io.InputStream;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.graphics.drawable.Drawable;

public class DialogAvatarSelect extends Dialog {

	private TITFLActivity m_activity;
	float lastX;
	
	public DialogAvatarSelect(TITFLActivity parent) {
		super(parent);
		
		setContentView(R.layout.avatar_select);
        setTitle("Select avatar");
        
        m_activity = parent;
        
        //Create ViewFlipper dynamically
        LinearLayout layoutFlipper = (LinearLayout) findViewById(R.id.layoutFlipper);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, m_activity.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, m_activity.getResources().getDisplayMetrics());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, m_activity.getResources().getDisplayMetrics());
        
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        
        for (int i = 0; i < m_activity.mainMenu().getNumOfPlayer(); i++)
        {
	        ImageView image1 = new ImageView(m_activity);
	        image1.setBackgroundResource(R.drawable.black_border);
	        image1.setPadding(padding, padding, padding, padding);
	        image1.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_a_frm01.png", width, height));
	        image1.setLayoutParams(layoutParams);
	        
	        ImageView image2 = new ImageView(m_activity);
	        image2.setBackgroundResource(R.drawable.black_border);
	        image2.setPadding(padding, padding, padding, padding);
	        image2.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_b_frm01.png", width, height));
	        image2.setLayoutParams(layoutParams);
	        
	        ImageView image3 = new ImageView(m_activity);
	        image3.setBackgroundResource(R.drawable.black_border);
	        image3.setPadding(padding, padding, padding, padding);
	        image3.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_c_frm01.png", width, height));
	        image3.setLayoutParams(layoutParams);
	        
	        ImageView image4 = new ImageView(m_activity);
	        image4.setBackgroundResource(R.drawable.black_border);
	        image4.setPadding(padding, padding, padding, padding);
	        image4.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_d_frm01.png", width, height));
	        image4.setLayoutParams(layoutParams);
	        
	        ImageView image5 = new ImageView(m_activity);
	        image5.setBackgroundResource(R.drawable.black_border);
	        image5.setPadding(padding, padding, padding, padding);
	        image5.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_e_frm01.png", width, height));
	        image5.setLayoutParams(layoutParams);
	        
	        ViewFlipper flipper = new ViewFlipper(m_activity);	        
	        flipper.setLayoutParams(layoutParams);
	        flipper.addView(image1);
	        flipper.addView(image2);
	        flipper.addView(image3);
	        flipper.addView(image4);
	        flipper.addView(image5);
	        setTouchListener(flipper);
	        
	        layoutFlipper.addView(flipper);
        }
        
        /*int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, m_activity.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, m_activity.getResources().getDisplayMetrics());
        ImageView image1 = (ImageView)findViewById(R.id.imageView1);
        image1.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_a_frm01.png", width, height));
        
        ImageView image2 = (ImageView)findViewById(R.id.imageView2);
        image2.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_b_frm01.png", width, height));
        
        ImageView image3 = (ImageView)findViewById(R.id.imageView3);
        image3.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, "avatar_casual/avatar_c_frm01.png", width, height));
        
        
        ViewFlipper flipper1 = (ViewFlipper)findViewById(R.id.flipper1);
        setTouchListener(flipper1);*/
        
	}
	
	private void setTouchListener(ViewFlipper flipper)
	{
		flipper.setOnTouchListener(new OnTouchListener(){
        	@Override
            public boolean onTouch(View v, MotionEvent event) {
        		//perform the action
        		ViewFlipper flipper = (ViewFlipper)v;
        		switch (event.getAction())
        		{
	        		case MotionEvent.ACTION_DOWN:
	        		{
	        			lastX = event.getX();
	        			
	                    break;
	                }
	        		case MotionEvent.ACTION_UP: 
                    {
                        float currentX = event.getX();
                        
                        // if left to right swipe on screen
                        if (lastX < currentX) 
                        {
                             // If no more View/Child to flip
                            if (flipper.getDisplayedChild() == 0)
                                break;
                            
                            // set the required Animation type to ViewFlipper
                            // The Next screen will come in form Left and current Screen will go OUT from Right 
                            //flipper1.setInAnimation(this, R.anim.in_from_left);
                            //.setOutAnimation(this, R.anim.out_to_right);
                            // Show the next Screen
                            flipper.showNext();
                        }
                        
                        // if right to left swipe on screen
                        if (lastX > currentX)
                        {
                            if (flipper.getDisplayedChild() == 1)
                                break;
                            // set the required Animation type to ViewFlipper
                            // The Next screen will come in form Right and current Screen will go OUT from Left 
                            //flipper1.setInAnimation(this, R.anim.in_from_right);
                            //flipper1.setOutAnimation(this, R.anim.out_to_left);
                            // Show The Previous Screen
                            flipper.showPrevious();
                        }
                        break;
                    }
        		}
                return true;
            }
        });
	}

}
