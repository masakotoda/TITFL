package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TITFLTownLayout 
{
	private Activity m_activity;

	public TITFLTownLayout(Activity activity)
	{
		m_activity = activity;
	}
	
	public void initialize()
	{
		Button button1 = (Button) m_activity.findViewById(R.id.button1);
		setButtonAction(button1);
	}
	
	private void setButtonAction(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoEtapUtility.showAlert(m_activity, "Note", "Write your test code!");
			}
		});
	}
}
