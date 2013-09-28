package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AssetManager;
import android.util.Xml;
import android.widget.Toast;

public class TITFLJob
{
	private TITFLTownElement m_townelement;
	private String m_townelement_id;
	private String m_name;
	private int m_dressCode;
	private int m_wage; // @ town's economyFactor = 1
	private int m_openingNumber; // @ town's economyFactor = 1
	private TITFL.CharacterFactor m_requiredCharacter;	
	private TITFL.DisciplineLevel m_requiredEducation;
	private TITFL.DisciplineLevel m_requiredExperience;
			
	private static String tag_root = "TITFL";
	private static String tag_item = "job";
	private static String atr_name = "name";
	private static String atr_townelement_id = "townelement_id";

    public static ArrayList<TITFLJob> loadDefaultJobs(AssetManager am)
	{
		ArrayList<TITFLJob> ret = new ArrayList<TITFLJob>();		
		try
		{
			String ns = "";
			InputStream in = am.open("default_job.xml");
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

		    	TITFLJob element = new TITFLJob();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_name))
		    			element.m_name = attribValue;
		    		else if (attribName.equals(atr_townelement_id))
		    			element.m_townelement_id = attribValue;
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

	String townelement_id()
	{
		if (m_townelement_id == null)
		{
			Toast.makeText(null, "TITFJob: townelement_id is null", Toast.LENGTH_SHORT).show();
			m_townelement_id = "";
		}
		return m_townelement_id;
	}
	
	void setTownElement(TITFLTownElement element)
	{
		m_townelement = element;
	}	
}
