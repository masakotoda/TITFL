package com.noetap.titfl;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TITFLActivity extends Activity 
{
	TITFL m_game;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_titfl);
		
		m_game = new TITFL(this);
		m_game.run();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.titfl, menu);
		return true;
	}
}
