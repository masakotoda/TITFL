package com.noetap.titfl;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListAdapterBeginWeek extends BaseAdapter
{
    public static class BeginWeekItem
    {
        BeginWeekItem(Bitmap image, String description, int price, int hour)
        {
            m_image = image;
            m_description = description;
            m_price = price;
            m_hour = hour;            
        }
        Bitmap m_image;
        String m_description;
        int m_price;
        int m_hour;
    }
    
    private Activity m_activity;
    private ArrayList<BeginWeekItem> m_items;
         
    public ListAdapterBeginWeek(
        Activity activity, 
        ArrayList<BeginWeekItem> items) 
    {
        m_activity = activity;
        m_items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) 
    {    
        View gridView;
        if (convertView == null) 
        {    
            LayoutInflater inflater = (LayoutInflater) m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = new View(m_activity);    
            gridView = inflater.inflate(R.layout.begin_week_adapter, null);
        }
        else 
        {
            gridView = convertView;
        }

        BeginWeekItem item = m_items.get(position);
            
        TextView description = (TextView) gridView.findViewById(R.id.description);
        description.setText(item.m_description);

        TextView price = (TextView) gridView.findViewById(R.id.price);
        if (item.m_price == 0)
            price.setText("-");
        else
            price.setText("$" + Integer.toString(item.m_price));

        TextView hour = (TextView) gridView.findViewById(R.id.hour);
        if (item.m_hour == 0)
            hour.setText("-");
        else
            hour.setText(Integer.toString(item.m_hour) + " hrs");

        ImageView imageView = (ImageView) gridView.findViewById(R.id.image);
        if (item.m_image != null)
            imageView.setImageBitmap(item.m_image);

        return gridView;
    }

    @Override
    public int getCount() 
    {
        return m_items.size();
    }

    @Override
    public Object getItem(int position) 
    {
        return null;
    }

    @Override
    public long getItemId(int position) 
    {
        return position;
    }
}
