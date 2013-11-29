package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        // This townView is just to make reverse layout easier.
        View townView = m_activity.findViewById(R.id.townView);
        NoEtapUtility.setWidth(townView, TITFLTownView.width(m_activity));
        if (m_activity.settings().m_reverseLayout)
        {
            NoEtapUtility.alignParentRight(townView);
        }

        m_playerView = (TITFLPlayerView) m_activity.findViewById(R.id.playerView);
        m_playerView.setPlayer(m_element.visitor());
        if (m_activity.settings().m_reverseLayout)
        {
            NoEtapUtility.alignParentLeft(m_playerView);
        }
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

        Button buttonGarage = (Button) m_activity.findViewById(R.id.buttonChangeTransportation);
        
        Button buttonCloset = (Button) m_activity.findViewById(R.id.buttonChangeOutfit);
        
        Gallery gallery = (Gallery) m_activity.findViewById(R.id.galleryGoods);

        if (m_element.visitor().home() != m_element)
        {
            buttonRelax.setVisibility(View.GONE);
            buttonGarage.setVisibility(View.GONE);
            buttonCloset.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
        }
        else
        {
            ArrayList<ListAdapterPicture.PictureItem> actions = new ArrayList<ListAdapterPicture.PictureItem>();
            for (TITFLBelonging b : m_element.visitor().belongings())
            {
                if (null != b.goodsRef())
                {
                    ListAdapterPicture.PictureItem item;
                    item = new ListAdapterPicture.PictureItem();
                    item.m_label = "";
                    item.m_picture = b.getBitmap();
                    actions.add(item);
                }
            }
            ListAdapterPicture adapter = new ListAdapterPicture(m_activity, actions);
            gallery.setAdapter(adapter);
            gallery.setSelection(actions.size() / 2);
            gallery.setBackgroundColor(m_element.visitor().themeColorLight());
        }
        
        setElementInsideImage();
    }
    
    @Override
    public void onBackPressed()
    {
        m_activity.closeTownElement(m_element.visitor());
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

    private void setElementInsideImage()
    {
        LinearLayout bb1 = (LinearLayout) m_activity.findViewById(R.id.buttonBar);
        {
            if (bb1 != null)
                bb1.setBackgroundColor(m_element.visitor().themeColorLight());            
        }

        int bgH = NoEtapUtility.getScreenWidth(m_activity) - bb1.getHeight();
        int bgW = NoEtapUtility.getScreenHeight(m_activity) - m_playerView.getWidth();
        Drawable backGround = NoEtapUtility.createDrawableFromAsset(m_activity, m_element.getInsideImageName(), bgW, bgH);
        if (backGround != null)
        {
            ImageView noGoods = (ImageView) m_activity.findViewById(R.id.imageViewInside);
            noGoods.setImageDrawable(backGround);
        }
    }
}
