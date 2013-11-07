package com.noetap.titfl;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TITFLMainMenuLayout implements TITFLLayout {
	
	TITFLActivity m_activity;

	public TITFLMainMenuLayout(TITFLActivity mainActivity){
		m_activity = mainActivity;
	}
	
	@Override
	public void invalidate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() 
	{
        m_activity.playMusic("arabesque.ogg");

	    Button buttonNewGame = (Button) m_activity.findViewById(R.id.buttonNewGame);
		setButtonNewGameListener(buttonNewGame);
		
		Button buttonLoadGame = (Button) m_activity.findViewById(R.id.buttonLoadGame);
		setButtonLoadGameListener(buttonLoadGame);
		
		Button buttonSettings = (Button) m_activity.findViewById(R.id.buttonSettings);
		setButtonSettingsListener(buttonSettings);
		
		Button buttonAbout = (Button) m_activity.findViewById(R.id.buttonAbout);
		setButtonAboutListener(buttonAbout);
	}

    @Override
    public void onBackPressed()
    {
        m_activity.finish();
    }

	public void setButtonNewGameListener(Button button)
	{
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	DialogSelectPlayer selectDialog = new DialogSelectPlayer(m_activity); 
                selectDialog.show();
            }
        });
	}
	
	public void setButtonLoadGameListener(Button button)
	{
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                if (!m_activity.resumeGame())
                	Toast.makeText(m_activity, "No saved game was found", Toast.LENGTH_SHORT).show(); 
            }
        });
	}
	
	public void setButtonSettingsListener(Button button)
	{
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                m_activity.showSettings();
            }
        });
	}

	public void setButtonAboutListener(Button button)
	{
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                m_activity.showAbout();
            }
        });
	}
}
