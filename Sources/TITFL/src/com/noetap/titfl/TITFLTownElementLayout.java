package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TITFLTownElementLayout implements TITFLLayout
{
	private TITFLActivity m_activity;
	private TITFLPlayerView m_playerView;
	private TITFLTownElement m_element;

	public TITFLTownElementLayout(Activity activity, TITFLTownElement element)
	{
		m_activity = (TITFLActivity)activity;
		m_element = element;
	}
	
	@Override
	public void invalidate() 
	{
		m_playerView.invalidate();
	}

	@Override
	public void initialize()
	{
		m_playerView = (TITFLPlayerView) m_activity.findViewById(R.id.playerView);
		m_playerView.setPlayer(m_element.visitor());
		m_playerView.initialize();
		m_playerView.invalidate();

		Button buttonClose = (Button) m_activity.findViewById(R.id.buttonClose);
		setButtonActionClose(buttonClose);

		Button buttonWork = (Button) m_activity.findViewById(R.id.buttonWork);
		setButtonActionWork(buttonWork);
		
		TextView name = (TextView) m_activity.findViewById(R.id.textViewName);
		name.setText(m_element.name());
	}
	
	private void setButtonActionClose(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				m_activity.closeTownElement();
			}
		});
	}

	private void setButtonActionWork(Button clicked)
	{
		clicked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				m_element.visitor().work();
				m_playerView.invalidate();
			}
		});
	}
}
