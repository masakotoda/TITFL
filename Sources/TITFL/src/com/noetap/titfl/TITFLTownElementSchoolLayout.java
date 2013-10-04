package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TITFLTownElementSchoolLayout extends TITFLTownElementLayout
{
    public TITFLTownElementSchoolLayout(Activity activity, TITFLTownElement element) 
    {
        super(activity, element);

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
        
        m_greetingText = (TextView) m_activity.findViewById(R.id.textViewGreeting);
        m_greetingText.setText(m_element.greeting());

        Button buttonClose = (Button) m_activity.findViewById(R.id.buttonClose);
        setButtonActionClose(buttonClose);
                
        ListView list = (ListView) m_activity.findViewById(R.id.listViewAllCourses);
        setListAction(list);

        ListView mylist = (ListView) m_activity.findViewById(R.id.listViewMyCourses);
        setMyListAction(mylist);
     }    
    
    protected void setListAction(ListView list)
    {
        final TITFLTownElementSchoolLayout layout = this;
        ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_element.merchandise());
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLGoods goods = m_element.merchandise().get(position);
                DialogEnrollCourse dialog = new DialogEnrollCourse(goods, layout);
                dialog.show();
            }
        });
    }

    private void setMyListAction(final ListView list)
    {
        final ArrayList<TITFLBelonging> degrees = m_element.visitor().getDegrees();
        
        ArrayAdapter<TITFLBelonging> adapter = new ArrayAdapter<TITFLBelonging>(m_activity, android.R.layout.simple_list_item_1, degrees);
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                TITFLBelonging degree = degrees.get(position);
                m_element.visitor().study(degree);
                setMyListAction(list);
                m_playerView.invalidate();
            }
        });
    }
    
    public void updateMyCourses()    
    {
        ListView mylist = (ListView) m_activity.findViewById(R.id.listViewMyCourses);
        setMyListAction(mylist);
    }
}
