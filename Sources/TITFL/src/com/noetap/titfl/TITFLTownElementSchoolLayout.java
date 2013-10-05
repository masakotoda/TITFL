package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TITFLTownElementSchoolLayout extends TITFLTownElementLayout
{
    private ArrayList<TITFLGoods> m_allCourses;
    private ArrayList<TITFLBelonging> m_myCourses;
    
    public TITFLTownElementSchoolLayout(Activity activity, TITFLTownElement element) 
    {
        super(activity, element);

        m_allCourses = new ArrayList<TITFLGoods>();
    }
    
    @Override
    public void invalidate() 
    {
        super.invalidate();
    }

    @Override
    public void initialize()
    {
        super.initialize();
                
        ListView mylist = (ListView) m_activity.findViewById(R.id.listViewMyCourses);
        setMyCourseAction(mylist);

        ListView list = (ListView) m_activity.findViewById(R.id.listViewAllCourses);
        setAllCourseAction(list);
    }    
    
    private static boolean findGoods(TITFLGoods g, ArrayList<TITFLBelonging> belongings)
    {
        for (TITFLBelonging b : belongings)
        {
            if (b.goodsRef() == g)
                return true;
        }
        return false;
    }

    private void updateAllCourses()
    {
        m_allCourses.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (!findGoods(g, m_myCourses))
            {
                m_allCourses.add(g);
            }
        }
    }

    private void setAllCourseAction(ListView list)
    {
        updateAllCourses();
        
        final TITFLTownElementSchoolLayout layout = this;
        ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_allCourses);
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLGoods goods = m_allCourses.get(position);
                DialogEnrollCourse dialog = new DialogEnrollCourse(goods, layout);
                dialog.show();
            }
        });
    }

    private void setMyCourseAction(final ListView list)
    {
        m_myCourses = m_element.visitor().getDegrees();
        
        ArrayAdapter<TITFLBelonging> adapter = new ArrayAdapter<TITFLBelonging>(m_activity, android.R.layout.simple_list_item_1, m_myCourses);
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                TITFLBelonging degree = m_myCourses.get(position);
                m_element.visitor().study(degree);
                setMyCourseAction(list);
                m_playerView.invalidate();
            }
        });
    }
    
    public void updateMyCourses()    
    {
        ListView mylist = (ListView) m_activity.findViewById(R.id.listViewMyCourses);
        setMyCourseAction(mylist);
                
        ListView list = (ListView) m_activity.findViewById(R.id.listViewAllCourses);
        setAllCourseAction(list);
    }
}
