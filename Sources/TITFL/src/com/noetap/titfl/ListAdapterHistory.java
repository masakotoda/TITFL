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
 
public class ListAdapterHistory extends BaseAdapter
{
    private Activity m_activity;
    private ArrayList<TITFLSQLiteOpenHelper.HistoryItem> m_items;
    private Bitmap m_happy;
    private Bitmap m_neutral;
    private Bitmap m_unhappy;
         
    public ListAdapterHistory(
        Activity activity, 
        ArrayList<TITFLSQLiteOpenHelper.HistoryItem> items) 
    {
        m_activity = activity;
        m_items = items;
        m_happy = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.event_happy);
        m_neutral = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.event_neutral);
        m_unhappy = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.event_unhappy);
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

        TITFLSQLiteOpenHelper.HistoryItem item = m_items.get(position);
            
        String info = item.m_dateTime + " ";
        Bitmap bitmap = m_neutral;
        switch (item.m_type)        
        {
        case TITFLSQLiteOpenHelper.HistoryItem.typeAppStart:
        	info += "App Start";
        	break;
        case TITFLSQLiteOpenHelper.HistoryItem.typeAppEnd:
        	info += "App End";
        	bitmap = m_unhappy;
        	break;
        case TITFLSQLiteOpenHelper.HistoryItem.typeNewGame:
        	info += "New Game";
        	break;
        case TITFLSQLiteOpenHelper.HistoryItem.typeSuspendGame:
        	info += "Suspend - week ";
        	info += Integer.toString(item.m_week);
        	bitmap = m_unhappy;
        	break;
        case TITFLSQLiteOpenHelper.HistoryItem.typeResumeGame:
        	info += "Resume - week ";
        	info += Integer.toString(item.m_week);
        	break;
        case TITFLSQLiteOpenHelper.HistoryItem.typeFinishGame:
        	info += "Finish - week ";
        	info += Integer.toString(item.m_week);
        	info += " ";
        	info += item.m_description;
        	bitmap = m_happy;
        	break;            
        }
        TextView description = (TextView) gridView.findViewById(R.id.description);
        description.setText(info);

        ImageView imageView = (ImageView) gridView.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);

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
