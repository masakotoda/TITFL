package com.noetap.titfl;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class DialogSettings extends Dialog
{    
    private TITFLActivity m_activity;
    private RadioGroup m_radioLayout;
    
    public DialogSettings(TITFLActivity activity)
    {
        super(activity);
        m_activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);

        setRadioButtonLayout();

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarVolume);
        setSeekBarAction(seekBar);
        
        Button closeButton = (Button) findViewById(R.id.buttonClose);
        setButtonActionClose(closeButton);

        Button exitButton = (Button) findViewById(R.id.buttonExit);
        setButtonActionExit(exitButton);

        Button mainMenuButton = (Button) findViewById(R.id.buttonMainMenu);
        setButtonActionMainMenu(mainMenuButton);
    }

    private void setRadioButtonLayout()
    {
        m_radioLayout = (RadioGroup) findViewById(R.id.radioLayout);
        RadioButton radioAvatarLeft = (RadioButton) findViewById(R.id.radioButtonAvatarLeft);
        RadioButton radioAvatarRight = (RadioButton) findViewById(R.id.radioButtonAvatarRight);
        
        if (m_activity.settings().m_reverseLayout)
        {
            radioAvatarLeft.setChecked(true);
        }
        else
        {
            radioAvatarRight.setChecked(true);
        }
    }
    
    private void setSeekBarAction(SeekBar seekBar)
    {
        seekBar.setMax(100);
        seekBar.setProgress((int)(m_activity.settings().m_bgmVolume * 100));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() 
        {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) 
            {
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) 
            {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
            {
                m_activity.settings().m_bgmVolume = (float)progress / 100.f;
                m_activity.updateVolume();
            }
        });       
    }

    private boolean updateSettings()
    {
        boolean reloadLayout = false;
        int selected = m_radioLayout.getCheckedRadioButtonId();
        if (R.id.radioButtonAvatarLeft == selected)
        {
            if (m_activity.settings().m_reverseLayout != true)
            {
                m_activity.settings().m_reverseLayout = true;
                reloadLayout = true;
            }
        }
        else
        {
            if (m_activity.settings().m_reverseLayout != false)
            {
                m_activity.settings().m_reverseLayout = false;
                reloadLayout = true;
            }
        }
        return reloadLayout;
    }
    
    private void setButtonActionClose(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                boolean reloadLayout = updateSettings();
                if (reloadLayout)
                {
                    m_activity.reloadTownView();
                }
                dismiss();
            }
        });
    }
    
    private void setButtonActionExit(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                updateSettings();
                m_activity.finish();
            }
        });
    }
    
    private void setButtonActionMainMenu(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                updateSettings();
                m_activity.createMainScreen();
                dismiss();
            }
        });
    }
}
