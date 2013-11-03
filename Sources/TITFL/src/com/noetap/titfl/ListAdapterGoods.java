package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterGoods  extends BaseAdapter
{
    private Activity m_activity;
    private ArrayList<TITFLGoods> m_items;
         
    public ListAdapterGoods(
        Activity activity, 
        ArrayList<TITFLGoods> items) 
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
            gridView = inflater.inflate(R.layout.goods_adapter, null);
        }
        else 
        {
            gridView = convertView;
        }

        TITFLGoods item = m_items.get(position);
            
        String iconPath = TITFLActivity.pathGoods + item.id() + ".png";
        Bitmap bm = NoEtapUtility.getBitmap(m_activity, iconPath);
        if (bm == null)
            bm = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.goods_sample);

        TextView description = (TextView) gridView.findViewById(R.id.description);
        description.setText(item.toString());

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
