package com.noetap.titfl;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class TITFLTownLayout 
{
	private Activity m_activity;
	private Point m_marble = new Point();
	private int m_count;

	public TITFLTownLayout(Activity activity)
	{
		m_activity = activity;

		Button button1 = (Button) m_activity.findViewById(R.id.button1);
		setAction(button1);
	}
	
	private void setAction(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				ImageView marbleImg = (ImageView) m_activity.findViewById(R.id.imageView1);
				Animation anim = AnimationUtils.loadAnimation(m_activity, R.anim.anim_test);

				anim.setAnimationListener(new AnimationListener() 
				{
		            @Override
		            public void onAnimationEnd(Animation arg0) {
		            	
						m_count++;

						int deltaX = 0, deltaY = 0;
						int modula = m_count % 30;
						if (modula < 10)
						{
							deltaX = 0;
							deltaY = 100;
						}
						else if (modula < 15)
						{
							deltaX = 100;
							deltaY = 0;
						}
						else if (modula < 25)
						{
							deltaX = 0;
							deltaY = -100;
						}
						else if (modula < 30)
						{
							deltaX = -100;
							deltaY = 0;
						}
						
						if (modula == 0 || modula == 10 || modula == 15 || modula == 25)
							return;
						
		        		ImageView marbleImg = (ImageView) m_activity.findViewById(R.id.imageView1);
		                Animation anim = AnimationUtils.loadAnimation(m_activity, R.anim.anim_test);
		                anim.setAnimationListener(this);
		                m_marble.x += deltaX;
		                m_marble.y += deltaY;
		                marbleImg.layout(m_marble.x, m_marble.y, m_marble.x + 100, m_marble.y + 100);
		                marbleImg.startAnimation(anim);
		            }

		            @Override
		            public void onAnimationRepeat(Animation arg0) 
		            {
		            }

		            @Override
		            public void onAnimationStart(Animation arg0) 
		            {
		            }
		        });
				
				marbleImg.startAnimation(anim);
			}
		});
	}
}
