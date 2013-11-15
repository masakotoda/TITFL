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
        BeginWeekItem(Bitmap image, String description, int price, int hour, int health)
        {
            m_image = image;
            if (description != null)
            {
                m_description = description;
                setPrice(price);
                setHour(hour);
                setHealth(health);
            }
            else
            {
                m_price = "price";
                m_hour = "hour";
                m_health = "health";
            }
        }
        
        public void setPrice(int price)
        {
            if (price == 0)
                m_price = "-";
            else if (price > 0)
                m_price = "-$" + Integer.toString(price);
            else if (price < 0)
                m_price = "+$" + Integer.toString(Math.abs(price));        
        }
        
        public void setHour(int hour)
        {
            if (hour > 0)
                m_hour = "-" + Integer.toString(hour) + " hrs";
            else
                m_hour = "-";
        }
        
        public void setHealth(int health)
        {
            if (health == 0)
                m_health = "-";
            else if (health > 0)
                m_health = "+" + Integer.toString(health) + " pts";
            else
                m_health = Integer.toString(health) + " pts";
        }

        Bitmap m_image;
        String m_description;
        String m_price;
        String m_hour;
        String m_health;
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
            gridView = inflater.inflate(R.layout.adapter_begin_week, null);
        }
        else 
        {
            gridView = convertView;
        }

        BeginWeekItem item = m_items.get(position);
            
        TextView description = (TextView) gridView.findViewById(R.id.description);
        description.setText(item.m_description);

        TextView price = (TextView) gridView.findViewById(R.id.price);
        price.setText(item.m_price);

        TextView hour = (TextView) gridView.findViewById(R.id.hour);
        hour.setText(item.m_hour);

        TextView health = (TextView) gridView.findViewById(R.id.health);
        health.setText(item.m_health);

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
