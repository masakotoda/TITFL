package com.noetap.titfl;

import android.app.Dialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogSelectPlayer extends Dialog {
	
	private TITFLActivity m_activity;
	
	public DialogSelectPlayer(TITFLActivity parent)
	{
		super(parent);
		setContentView(R.layout.dialog_player_select);
        setTitle("Select number of players");
        
        m_activity = parent;
        
        Button onePlayer = (Button) findViewById(R.id.buttonOne);
        setButtonListener(onePlayer, 1);
        Button twoPlayer = (Button) findViewById(R.id.buttonTwo);
        setButtonListener(twoPlayer, 2);
        Button threePlayer = (Button) findViewById(R.id.buttonThree);
        setButtonListener(threePlayer, 3);
	}

	public void setButtonListener(Button button, int numOfPlayer)
	{
		button.setTag(numOfPlayer);
		button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	m_activity.mainMenu().setNumOfPlayer((Integer)v.getTag());            	
                m_activity.runGame();
                
            	/*DialogSelectTown townSelect = new DialogSelectTown(m_activity);
            	townSelect.show();
            	int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, m_activity.getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, m_activity.getResources().getDisplayMetrics());
                townSelect.getWindow().setLayout(width, height);*/
            	
                dismiss();
            }
        });
	}
}
