package com.noetap.titfl;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class DialogWinner extends Dialog
{    
    private TITFLPlayer m_winner;
    private TITFLActivity m_activity;
    
    public DialogWinner(TITFLPlayer winner, Activity activity)
    {
        super(activity);
        m_activity = (TITFLActivity) activity;
        m_winner = winner;
        setContentView(R.layout.dialog_winner);
        setTitle("Winner - " + m_winner.alias());
        setCancelable(false);

        ImageView avatar = (ImageView) findViewById(R.id.imageViewAvatar);
        int w = winner.getAvatarWidth(m_activity);
        int h = winner.getAvatarHeight(m_activity);
        avatar.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, m_winner.outfitImage(0), w, h));

        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.anim_marble_stay);
        avatar.startAnimation(anim);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
    }

    private void setButtonActionOk(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                m_activity.endGame(m_winner);
                m_activity.createMainScreen();
                dismiss();
            }
        });
    }
}
