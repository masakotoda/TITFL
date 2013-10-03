package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLJob
{
	private TITFLTownElement m_townelement;    // Parent element

	private String m_id;
	private String m_name;
	private int m_dressCode;
	private int m_wage;            // @ town's economyFactor = 1
	private int m_openingNumber;   // @ town's economyFactor = 1
	private TITFL.CharacterFactor m_requiredCharacter;	
	private TITFL.DisciplineLevel m_requiredEducation;
	private TITFL.DisciplineLevel m_requiredExperience;
			
    private static String asset_xml_name = "default_job.xml";
	private static String tag_root = "TITFL";
    private static String atr_townelement_id = "townelement_id";
	private static String tag_item = "job";
	private static String atr_id = "job_id";
	private static String atr_name = "name";
    private static String atr_dress_code = "dress_code";
    private static String atr_wage = "wage_base";
    private static String atr_opening = "job_opening_base";
    private static String atr_req_intelligent = "required_factor_intelligent";  
    private static String ate_req_hardworking = "required_factor_hardworking";  
    private static String atr_req_goodlooking = "required_factor_goodlooking";  
    private static String atr_req_physical = "required_factor_physical";  
    private static String atr_req_lucky = "required_factor_lucky";  
    private static String atr_req_edu_basic = "reqired_edu_basic";
    private static String atr_req_edu_engineer = "reqired_edu_engineer";
    private static String atr_req_edu_business = "reqired_edu_business";
    private static String atr_req_edu_academic = "reqired_edu_academic";
    private static String atr_req_exp_basic = "reqired_exp_basic";
    private static String atr_req_exp_engineer = "reqired_exp_engineer";
    private static String atr_req_exp_business = "reqired_exp_business";
    private static String atr_req_exp_academic = "reqired_exp_academic";

	public TITFLJob()
	{
	    m_requiredCharacter = new TITFL.CharacterFactor();
	    m_requiredEducation = new TITFL.DisciplineLevel();
	    m_requiredExperience = new TITFL.DisciplineLevel();
	}
	
    public TITFLTownElement townelement()
    {
        return m_townelement;
    }
    
    public String id()
    {
        return m_id;
    }

    public String name()
    {
        return m_name;
    }
    
    public int dressCode()
    {
        return m_dressCode;
    }
    
    public int wage()
    {
        return m_wage;
    }
    
    public int openingNumber()
    {
        return m_openingNumber;
    }
    
    public TITFL.CharacterFactor requiredCharacter()
    {
        return m_requiredCharacter;  
    }
    
    public TITFL.DisciplineLevel requiredEducation()
    {
        return m_requiredEducation;
    }

    public TITFL.DisciplineLevel requiredExperience()
    {
        return m_requiredExperience;
    }

    public static ArrayList<TITFLJob> loadDefaultJobs(AssetManager am, TITFLTown town)
	{
		ArrayList<TITFLJob> ret = new ArrayList<TITFLJob>();		
		try
		{
			String ns = "";
			InputStream in = am.open(asset_xml_name);
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

		    	TITFLJob job = new TITFLJob();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
                    if (attribName.equals(atr_id))
                        job.m_id = attribValue;
		    		else if (attribName.equals(atr_name))
		    		    job.m_name = attribValue;
		    		else if (attribName.equals(atr_townelement_id))
		    		    job.m_townelement = town.findElement(attribValue);
		    		else if (attribName.equals(atr_dress_code))
		    		    job.m_dressCode = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_wage))
                        job.m_wage = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_opening))
                        job.m_openingNumber = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_intelligent))
                        job.m_requiredCharacter.m_intelligent = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(ate_req_hardworking))
                        job.m_requiredCharacter.m_hardWorking = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_goodlooking))
                        job.m_requiredCharacter.m_goodLooking = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_physical))
                        job.m_requiredCharacter.m_physical = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_lucky))
                        job.m_requiredCharacter.m_lucky = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_edu_basic))
                        job.m_requiredEducation.m_basic = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_edu_engineer))
                        job.m_requiredEducation.m_engineering = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_edu_business))
                        job.m_requiredEducation.m_business_finance = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_edu_academic))
                        job.m_requiredEducation.m_academic = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_exp_basic))
                        job.m_requiredExperience.m_basic = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_exp_engineer))
                        job.m_requiredExperience.m_engineering = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_exp_business))
                        job.m_requiredExperience.m_business_finance = Integer.parseInt(attribValue);
		    	    else if (attribName.equals(atr_req_exp_academic))
                        job.m_requiredExperience.m_academic = Integer.parseInt(attribValue);
		    	}
		    	
		    	ret.add(job);
                if (job.m_townelement != null)
                    job.m_townelement.jobs().add(job);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}

		return ret;	
	}
}