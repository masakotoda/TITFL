package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TITFLTownElementHomeLayout implements TITFLLayout
{
    private TITFLActivity m_activity;
    private TITFLPlayerView m_playerView;
    private TITFLTownElement m_element;

    public TITFLTownElementHomeLayout(Activity activity, TITFLTownElement element)
    {
        m_activity = (TITFLActivity)activity;
        m_element = element;
    }
    
    public TITFLActivity activity()
    {
        return m_activity;
    }

    public TITFLPlayerView playerView()
    {
        return m_playerView;
    }

    public TITFLTownElement element()
    {
        return m_element;
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
        
        ImageView avatar = (ImageView) m_activity.findViewById(R.id.imageViewAvatar);
        int w = m_element.visitor().getAvatarWidth(m_activity);
        int h = m_element.visitor().getAvatarHeight(m_activity);
        avatar.setImageDrawable(NoEtapUtility.createDrawableFromAsset(m_activity, m_element.visitor().outfitImage(0), w, h));

        Button buttonClose = (Button) m_activity.findViewById(R.id.buttonClose);
        setButtonActionClose(buttonClose);

        Button buttonRelax = (Button) m_activity.findViewById(R.id.buttonRelax);
        setButtonActionRelax(buttonRelax);

        if (m_element.visitor().home() != m_element)
            buttonRelax.setVisibility(View.GONE);
     }
    
    private void setButtonActionClose(Button clicked)
    {
        clicked.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                m_activity.closeTownElement(m_element.visitor());
            }
        });
    }
    
    protected void setButtonActionRelax(Button clicked)
    {
        clicked.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                m_element.visitor().relax();
                m_playerView.invalidate();
            }
        });
    }
}
