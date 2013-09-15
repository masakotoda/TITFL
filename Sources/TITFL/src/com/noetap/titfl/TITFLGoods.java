package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLGoods
{
	private ArrayList<TITFLGoodsEvent> m_events;
	private TITFLTownElement m_townelement;
	
	private String m_id;
	private String m_townelement_id;
	private String m_name;
	private int m_price;
	private int m_speed;

	private static String tag_root = "TITFL";
	private static String tag_item = "goods";
	private static String atr_name = "name";
	private static String atr_id = "goods_id";
	private static String atr_townelement_id = "townelement_id";
	private static String atr_price = "price";
	private static String atr_speed = "speed";

	
    public static ArrayList<TITFLGoods> loadDefaultGoods(AssetManager am)
	{
		ArrayList<TITFLGoods> ret = new ArrayList<TITFLGoods>();
		try
		{
			String ns = "";
			InputStream in = am.open("default_goodsevent.xml");
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

		    	TITFLGoods element = new TITFLGoods();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_id))
		    			element.m_id = attribValue;
		    		else if (attribName.equals(atr_name))
		    			element.m_name = attribValue;
		    		else if (attribName.equals(atr_townelement_id))
		    			element.m_townelement_id = attribValue;
		    		else if (attribName.equals(atr_price))
		    			element.m_price = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_speed))
		    			element.m_speed = Integer.parseInt(attribValue);
		    	}
		    	
		    	ret.add(element);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}
		
		ArrayList<TITFLGoodsEvent> events = TITFLGoodsEvent.loadDefaultGoodsEvent(am);
		assignEvents(events, ret);

		return ret;
	}

	private static void assignEvents(ArrayList<TITFLGoodsEvent> events, ArrayList<TITFLGoods> goods)
	{
		for (int i = 0; i < goods.size(); i++)
		{
			TITFLGoods g = goods.get(i);
			g.m_events = new ArrayList<TITFLGoodsEvent>();
			for (int j = 0; j < events.size(); j++)
			{
				TITFLGoodsEvent event = (TITFLGoodsEvent)events.get(j);
				if (event.goods_id().equals(g.id()))
				{
					event.setGoods(g);
					g.m_events.add(event);
				}				
			}
		}
	}

	String townelement_id()
	{
		if (m_townelement_id == null)
			m_townelement_id = "";
		
		return m_townelement_id;
	}
	
	void setTownElement(TITFLTownElement element)
	{
		m_townelement = element;
	}
	
	String id()
	{
		return m_id;
	}
}
