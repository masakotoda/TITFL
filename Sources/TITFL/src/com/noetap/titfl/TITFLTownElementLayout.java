package com.noetap.titfl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TITFLTownElementLayout implements TITFLLayout
{
    protected TITFLActivity m_activity;
    protected TITFLPlayerView m_playerView;
    protected TITFLTownElement m_element;
    protected TextView m_greetingText;

    public TITFLTownElementLayout(Activity activity, TITFLTownElement element)
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

    public TextView greetingText()
    {
        return m_greetingText;
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

        m_greetingText = (TextView) m_activity.findViewById(R.id.textViewGreeting);
        m_greetingText.setText(m_element.greeting());
        
        Button buttonClose = (Button) m_activity.findViewById(R.id.buttonClose);
        setButtonActionClose(buttonClose);

        Button buttonWork = (Button) m_activity.findViewById(R.id.buttonWork);
        setButtonActionWork(buttonWork);
        
        Button buttonRelax = (Button) m_activity.findViewById(R.id.buttonRelax);
        setButtonActionRelax(buttonRelax);

        TextView name = (TextView) m_activity.findViewById(R.id.textViewName);
        name.setText(m_element.name());
                
        ListView list = (ListView) m_activity.findViewById(R.id.listViewGoods);
        setListAction(list);
    }
    
    protected void setButtonActionClose(Button clicked)
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

    protected void setButtonActionWork(Button clicked)
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

    protected void setListAction(ListView list)
    {
        final TITFLTownElementLayout layout = this;
        ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_element.merchandise());
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLGoods goods = m_element.merchandise().get(position);
                DialogPurchaseGoods dialog = new DialogPurchaseGoods(goods, layout);
                dialog.show();
            }
        });
    }
}
