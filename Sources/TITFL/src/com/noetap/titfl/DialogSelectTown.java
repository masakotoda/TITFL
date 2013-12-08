package com.noetap.titfl;

import android.app.Dialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.graphics.Bitmap;

public class DialogSelectTown extends Dialog {

	private TITFLActivity m_activity;
	
	public DialogSelectTown(TITFLActivity parent)
	{
		super(parent);
		setContentView(R.layout.dialog_town_select);
        setTitle("Select town");
        
        m_activity = parent;
        
        ImageButton townOne = (ImageButton) findViewById(R.id.buttonTownOne);
        Bitmap townOneBg = NoEtapUtility.getBitmap(m_activity, "townmap/1.png");
        townOne.setImageBitmap(townOneBg);
        setButtonListener(townOne, 1);
        
        ImageButton townTwo = (ImageButton) findViewById(R.id.buttonTownTwo);
        Bitmap townTwoBg = NoEtapUtility.getBitmap(m_activity, "townmap/2.png");
        townTwo.setImageBitmap(townTwoBg);
        setButtonListener(townTwo, 2);
        
        ImageButton townThree = (ImageButton) findViewById(R.id.buttonTownThree);
        Bitmap townThreeBg = NoEtapUtility.getBitmap(m_activity, "townmap/3.png");
        townThree.setImageBitmap(townThreeBg);
        setButtonListener(townThree, 3);
        
        ImageButton townFour = (ImageButton) findViewById(R.id.buttonTownFour);
        Bitmap townFourBg = NoEtapUtility.getBitmap(m_activity, "townmap/4.png");
        townFour.setImageBitmap(townFourBg);
        setButtonListener(townFour, 4);
        
        ImageButton townFive = (ImageButton) findViewById(R.id.buttonTownFive);
        Bitmap townFiveBg = NoEtapUtility.getBitmap(m_activity, "townmap/5.png");
        townFive.setImageBitmap(townFiveBg);
        setButtonListener(townFive, 5);
	}
	
	public void setButtonListener(ImageButton button, int townID)
	{
		button.setTag(townID);
		
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	m_activity.mainMenu().setTownID((Integer)v.getTag());            	
                //m_activity.runGame();
            	
            	DialogAvatarSelect avatarSelect = new DialogAvatarSelect(m_activity);
            	avatarSelect.show();
            	int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, m_activity.getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, m_activity.getResources().getDisplayMetrics());
                avatarSelect.getWindow().setLayout(width, height);
                
                dismiss();
            }
        });
	}
}
