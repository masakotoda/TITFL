package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLGoodsEvent implements TITFLEvent
{
	private TITFLGoods m_goods;
	
	private String m_id;
	private String m_goods_id;
	private String m_description;
	private int m_cycle;
	private int m_price;

	private static String tag_root = "TITFL";
	private static String tag_item = "goodsevent";
	private static String atr_description = "description";
	private static String atr_goods_id = "goods_id";
	private static String atr_cycle = "cycle";
	private static String atr_price = "price";

	public static ArrayList<TITFLGoodsEvent> loadDefaultGoodsEvent(AssetManager am)
	{
		ArrayList<TITFLGoodsEvent> ret = new ArrayList<TITFLGoodsEvent>();
		
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

		    	TITFLGoodsEvent element = new TITFLGoodsEvent();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_description))
		    			element.m_description = attribValue;
		    		else if (attribName.equals(atr_goods_id))
		    			element.m_goods_id = attribValue;
		    		else if (attribName.equals(atr_cycle))
		    			element.m_cycle = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_price))
		    			element.m_price = Integer.parseInt(attribValue);
		    	}
		    	
		    	ret.add(element);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}

		return ret;	
	}

	public String goods_id()	
	{
		return m_goods_id;
	}
	
	public void setGoods(TITFLGoods goods)
	{
		m_goods = goods;
	}
}
