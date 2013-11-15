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

public class ListAdapterPicture  extends BaseAdapter
{
    public static class PictureItem
    {
        public String m_label;
        public Bitmap m_picture;
    }
    
    private Activity m_activity;
    private ArrayList<PictureItem> m_items;
         
    public ListAdapterPicture(
        Activity activity, 
        ArrayList<PictureItem> items) 
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
            gridView = inflater.inflate(R.layout.adapter_picture, null);
        }
        else 
        {
            gridView = convertView;
        }

        PictureItem item = m_items.get(position);
            
        Bitmap bm = item.m_picture;

        TextView description = (TextView) gridView.findViewById(R.id.description);
        description.setText(item.m_label);

        ImageView imageView = (ImageView) gridView.findViewById(R.id.image);
        if (bm != null)
            imageView.setImageBitmap(bm);

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
