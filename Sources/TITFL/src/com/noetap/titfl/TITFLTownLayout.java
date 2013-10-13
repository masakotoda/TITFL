package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TITFLTownLayout implements TITFLLayout
{
    private TITFLActivity m_activity;
    private TITFLTownView m_townView;
    private TITFLPlayerView m_playerView;
    private TITFLPlayer m_player;

    public TITFLTownLayout(Activity activity)
    {
        m_activity = (TITFLActivity)activity;

        if (m_activity.getTown() != null)
        {
            m_player = m_activity.getTown().activePlayer();
        }
    }
    
    @Override
    public void invalidate()
    {
        m_townView.invalidate();
        m_playerView.invalidate();
    }

    @Override
    public void initialize()
    {        
        m_townView = (TITFLTownView) m_activity.findViewById(R.id.townView);
        m_townView.initialize();
        m_townView.invalidate();

        m_playerView = (TITFLPlayerView) m_activity.findViewById(R.id.playerView);
        m_playerView.setPlayer(m_player);
        m_playerView.initialize();
        m_playerView.invalidate();

        if (m_player != null)
            m_player.updateAvatar(m_activity);
        
        ImageView avatarImg = (ImageView) m_activity.findViewById(R.id.imageView2);
        avatarImg.setImageBitmap(null);
        //avatarImg.setBackgroundResource(R.drawable.frame_anim_test);

        ImageView marbleImg = (ImageView) m_activity.findViewById(R.id.imageView1);
        marbleImg.setVisibility(View.INVISIBLE);

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

    public void changePlayer(TITFLPlayer player)
    {
        m_player = player;
        m_playerView.setPlayer(m_player);
        m_playerView.invalidate();
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
                TITFLPlayer player = m_activity.getTown().activePlayer();
                if (player != null)
                {
                    String belongings = "";
                    for (TITFLBelonging b : player.belongings())
                    {
                        if (b.goodsRef() != null)
                        {
                            belongings += b.goodsRef().name();
                            belongings += ", ";
                        }
                    }
                    NoEtapUtility.showAlert(m_activity, "belongings", belongings);
                }
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
