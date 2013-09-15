package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLRandomEvent implements TITFLEvent
{
	private String m_id;
    private String m_description;
    private int m_trigger_health;
    private int m_trigger_wealth;
    private int m_trigger_education;
    private int m_trigger_carrier;
    private int m_trigger_life;
    private int m_affect_health;
    private int m_affect_wealth;
    private int m_affect_education;
    private int m_affect_carrier;
    private int m_happiness_per_intelligent_factor;
    private int m_happiness_per_hardworking_factor;
    private int m_happiness_per_goodlooking_factor;
    private int m_happiness_per_physical_factor;
    private int m_happiness_per_lucky_factor;
    private float m_health_insurance_coverage;
    private float m_car_insurance_coverage;

    private static String tag_root = "TITFL";
	private static String tag_item = "randomevent";
	private static String atr_id = "randomevent_id";
	private static String atr_description = "description";
	private static String atr_trigger_1 = "trigger_health";
	private static String atr_trigger_2 = "trigger_wealth";
	private static String atr_trigger_3 = "trigger_education";
	private static String atr_trigger_4 = "trigger_carrier";
	private static String atr_trigger_5 = "trigger_life";
	private static String atr_affect_1 = "affect_health";
	private static String atr_affect_2 = "affect_wealth";
	private static String atr_affect_3 = "affect_education";
	private static String atr_affect_4 = "affect_carrier";
	private static String atr_affect_5 = "affect_life";
	private static String atr_happiness_1 = "happiness_per_intelligent_factor";
	private static String atr_happiness_2 = "happiness_per_hardworking_factor";
	private static String atr_happiness_3 = "happiness_per_goodlooking_factor";
	private static String atr_happiness_4 = "happiness_per_physical_factor";
	private static String atr_happiness_5 = "happiness_per_lucky_factor";
	private static String atr_health_insurance = "health_insurance_coverage";
	private static String atr_car_insurance = "car_insurance_coverage";

    public static ArrayList<TITFLEvent> loadRandomEvents(AssetManager am)
	{
		ArrayList<TITFLEvent> ret = new ArrayList<TITFLEvent>();		

		try
		{
			String ns = "";
			InputStream in = am.open("default_randomevent.xml");
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

		    	TITFLRandomEvent element = new TITFLRandomEvent();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_id))
		    			element.m_id = attribValue;
		    		else if (attribName.equals(atr_description))
		    			element.m_description = attribValue;
		    		// TODO
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
}
