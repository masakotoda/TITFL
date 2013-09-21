package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TITFLTownLayout 
{
	private TITFLActivity m_activity;

	public TITFLTownLayout(Activity activity)
	{
		m_activity = (TITFLActivity)activity;
	}
	
	public void initialize()
	{
		Button buttonTT1 = (Button) m_activity.findViewById(R.id.ButtonTonyTest1);
		setButtonActionTT1(buttonTT1);

		Button buttonTT2 = (Button) m_activity.findViewById(R.id.ButtonTonyTest2);
		setButtonActionTT2(buttonTT2);

		Button buttonWP1 = (Button) m_activity.findViewById(R.id.ButtonWillieTest1);
		setButtonActionWP1(buttonWP1);

		Button buttonWP2 = (Button) m_activity.findViewById(R.id.ButtonWillieTest2);
		setButtonActionWP2(buttonWP2);

		Button buttonMT1 = (Button) m_activity.findViewById(R.id.ButtonMasakoTest1);
		setButtonActionMT1(buttonMT1);

		Button buttonMT2 = (Button) m_activity.findViewById(R.id.ButtonMasakoTest2);
		setButtonActionMT2(buttonMT2);

		Button buttonMenu = (Button) m_activity.findViewById(R.id.ButtonMenu);
		setButtonActionMenu(buttonMenu);
	}
	
	private void setButtonActionTT1(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoEtapUtility.showAlert(m_activity, "TT1", "Write your test code!");
			}
		});
	}
	
	private void setButtonActionTT2(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoEtapUtility.showAlert(m_activity, "TT2", "Write your test code!");
			}
		});
	}
	
	private void setButtonActionWP1(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoEtapUtility.showAlert(m_activity, "WP1", "Write your test code!");
			}
		});
	}
	
	private void setButtonActionWP2(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				NoEtapUtility.showAlert(m_activity, "WP2", "Write your test code!");
			}
		});
	}

	private void setButtonActionMT1(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				m_activity.getTown().changeRandomElement();
				m_activity.findViewById(R.id.townView).invalidate();
			}
		});
	}

	private void setButtonActionMT2(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
			}
		});
	}
	
	private void setButtonActionMenu(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				m_activity.openOptionsMenu();
			}
		});
	}	
}
