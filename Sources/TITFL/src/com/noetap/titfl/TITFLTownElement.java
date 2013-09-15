package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Xml;

public class TITFLTownElement
{
	private TITFLTown m_town;
	private ArrayList<TITFLGoods> m_marchandise;
	private ArrayList<TITFLJob> m_jobs;
	private String m_name;
	private String m_id;
	private int m_slot;

	private static String tag_root = "TITFL";
	private static String tag_item = "townelement";
	private static String atr_id = "townelement_id";
	private static String atr_name = "name";
	private static String atr_slot = "slot";
	
	public int slot()
	{
		return m_slot;
	}
	
	public String name()
	{
		return m_name;
	}
	
	public String id()
	{
		return m_id;
	}
	
	TITFLTown town()
	{
		return m_town;
	}

	public static ArrayList<TITFLTownElement> loadTownElements(AssetManager am, TITFLTown town)
	{
		ArrayList<TITFLTownElement> ret = new ArrayList<TITFLTownElement>();		
		try
		{
			String ns = "";
			InputStream in = am.open("default_townelement.xml");
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
 			
			parser.require(XmlPullParser.START_TAG, ns, tag_root);
			while (parser.next() != XmlPullParser.END_TAG) 
			{
				if (parser.getEventType() != XmlPullParser.START_TAG) 
			    	continue;
			    
				String name = parser.getName();
			    if (!name.equals(tag_item))
			    	continue;

		    	TITFLTownElement element = new TITFLTownElement();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_id))
		    			element.m_id = attribValue;
		    		else if (attribName.equals(atr_name))
		    			element.m_name = attribValue;
		    		else if (attribName.equals(atr_slot))
		    			element.m_slot = Integer.parseInt(attribValue);
		    	}
		    	
		    	element.m_town = town;
		    	ret.add(element);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}
		
		ArrayList<TITFLJob> jobs = TITFLJob.loadDefaultJobs(am);
		assignJobs(jobs, ret);

		ArrayList<TITFLGoods> goods = TITFLGoods.loadDefaultGoods(am);
		assignGoods(goods, ret);
		
		return ret;
	}
	
	private static void assignJobs(ArrayList<TITFLJob> jobs, ArrayList<TITFLTownElement> elements)
	{
		for (int i = 0; i < elements.size(); i++)
		{
			TITFLTownElement element = elements.get(i);
			element.m_jobs = new ArrayList<TITFLJob>();
			for (int j = 0; j < jobs.size(); j++)
			{
				TITFLJob job = jobs.get(j);
				if (job.townelement_id().equals(element.id()))
				{
					job.setTownElement(element);
					element.m_jobs.add(job);
				}				
			}
		}
	}

	private static void assignGoods(ArrayList<TITFLGoods> goods, ArrayList<TITFLTownElement> elements)
	{		
		for (int i = 0; i < elements.size(); i++)
		{
			TITFLTownElement element = elements.get(i);
			element.m_marchandise = new ArrayList<TITFLGoods>();
			for (int j = 0; j < goods.size(); j++)
			{
				TITFLGoods g = goods.get(j);
				if (g.townelement_id().equals(element.id()))
				{
					g.setTownElement(element);
					element.m_marchandise.add(g);
				}				
			}
		}
	}

	public void draw(Canvas canvas)
	{
		//TODO
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(48);

		if (m_slot >= 0)
		{
			Rect rect = m_town.slotToPosition(m_slot);
			canvas.drawText(m_name, rect.left, (rect.top + rect.bottom) / 2, paint);
			paint.setARGB(50, 50, 50, 255);
			canvas.drawRect(rect, paint);
		}
	}
}
