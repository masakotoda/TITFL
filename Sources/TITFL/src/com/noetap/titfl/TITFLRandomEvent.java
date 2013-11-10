package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

public class TITFLRandomEvent implements TITFLEvent
{
    private String m_id;
    private String m_description;
    //private TITFL.Satisfaction m_trigger;
    //private TITFL.Satisfaction m_affect;
    private TITFL.CharacterFactor m_affectOnHappiness;
    private float m_health_insurance_coverage;
    private float m_car_insurance_coverage;
    private int m_price;
    private int m_timeToPay;
    private int m_health;
    private String m_required_goods;
    private String m_losing_goods;
    private String m_acquiring_goods;            

    private static String asset_xml_name = "default_randomevent.xml";
    private static String tag_root = "TITFL";
    private static String tag_item = "randomevent";
    private static String atr_id = "randomevent_id";
    private static String atr_description = "description";
    //private static String atr_trigger_1 = "trigger_health";
    //private static String atr_trigger_2 = "trigger_wealth";
    //private static String atr_trigger_3 = "trigger_education";
    //private static String atr_trigger_4 = "trigger_career";
    //private static String atr_trigger_5 = "trigger_life";
    //private static String atr_affect_1 = "affect_health";
    //private static String atr_affect_2 = "affect_wealth";
    //private static String atr_affect_3 = "affect_education";
    //private static String atr_affect_4 = "affect_career";
    //private static String atr_affect_5 = "affect_life";
    private static String atr_price = "price";
    private static String atr_time_to_pay = "time_to_pay";
    private static String atr_health = "health";
    private static String atr_required_goods = "required_goods";
    private static String atr_losing_goods = "losing_goods";
    private static String atr_acquiring_goods = "acquiring_goods";            
    private static String atr_happiness_1 = "happiness_per_intelligent_factor";
    private static String atr_happiness_2 = "happiness_per_hardworking_factor";
    private static String atr_happiness_3 = "happiness_per_goodlooking_factor";
    private static String atr_happiness_4 = "happiness_per_physical_factor";
    private static String atr_happiness_5 = "happiness_per_lucky_factor";
    private static String atr_health_insurance = "health_insurance_coverage";
    private static String atr_car_insurance = "car_insurance_coverage";

    public TITFLRandomEvent()
    {
        //m_trigger = new TITFL.Satisfaction();
        //m_affect = new TITFL.Satisfaction();
        m_affectOnHappiness = new TITFL.CharacterFactor();        
    }
    
    public String id()
    {
        return m_id;
    }
    
    public String description()
    {
        return m_description;
    }

    //public TITFL.Satisfaction trigger()
    //{
    //    return m_trigger;
    //}

    //public TITFL.Satisfaction affect()
    //{
    //    return m_affect;
    //}
    
    public int price()
    {
        return m_price;
    }
    
    public int timeToPay()
    {
        return m_timeToPay;
    }
    
    public int health()
    {
        return m_health;
    }
    
    public String required_goods()
    {
        return m_required_goods;
    }
    
    public String losing_goods()
    {
        return m_losing_goods;
    }
    
    public String acquiring_goods()
    {
        return m_acquiring_goods;            
    }

    public TITFL.CharacterFactor affectOnHappiness()
    {
        return m_affectOnHappiness;
    }
    
    public float healthInsuranceCoverage()
    {
        return m_health_insurance_coverage;
    }

    public float carInsuranceCoverage()
    {
        return m_car_insurance_coverage;
    }
    
    public boolean process(TITFLPlayer owner, ListAdapterBeginWeek.BeginWeekItem event)
    {
        TITFLTown town = owner.currentLocation().town();
        int week = town.currentWeek();
        Activity activity = town.activity();
        
        // Does player have the required goods?
        if (m_required_goods != null && m_required_goods.length() > 0)
        {
            if (null == owner.findBelonging(m_required_goods))
                return false;

            if (m_losing_goods.length() > 0)
            {
                TITFLBelonging losing_belonging = owner.findBelonging(m_losing_goods);
                if (losing_belonging != null)
                    owner.belongings().remove(losing_belonging);
            }            
        }
        
        if (m_acquiring_goods != null && m_acquiring_goods.length() > 0)
        {
            TITFLGoods goods = town.findGoods(m_acquiring_goods);
            if (goods != null)
            {
                // You can't have two dates/two spouse! No, I say No!
                if (owner.countBelongings(goods.id()) < goods.maxUnits())
                    owner.buyFree(goods, 1, week);
            }
        }

        owner.satisfaction().m_health -= m_health;
        owner.addHours(m_timeToPay);
        
        int price = m_price;
        String note = "";
        if (m_price >= 1000000) // 1 million dollars - stolen/lost wallet
        {
            if (owner.currentLocation() == owner.home())
                return false; // You don't have such accident since you are at home.
            
            price = owner.cash();
            if (price > 0)
                owner.pay(price);
            else
                return false; // You are broke. Nothing to lose!
        }
        else
        {
            if (m_health_insurance_coverage > 0 && owner.hasHealthInsurance())
            {
                note = " (Health Insurance applied)";
                price = (int)(price * (1 - m_health_insurance_coverage));
            }
            if (m_car_insurance_coverage > 0 && owner.hasCarInsurance())
            {
                note = " (Auto Insurance applied)";
                price = (int)(price * (1 - m_car_insurance_coverage));
            }
            owner.pay(price);
        }
        
        int iconId = R.drawable.event_neutral;
        
        float happiness = 0;        
        happiness += (m_affectOnHappiness.m_intelligent * owner.character().intelligent());
        happiness += (m_affectOnHappiness.m_hardWorking * owner.character().hardworking());
        happiness += (m_affectOnHappiness.m_goodLooking * owner.character().goodlooking());
        happiness += (m_affectOnHappiness.m_physical * owner.character().physical());
        happiness += (m_affectOnHappiness.m_lucky * owner.character().lucky());
        owner.addHappiness((int)happiness);
        
        if (happiness < -10)
            iconId = R.drawable.event_unhappy;
        else if (happiness < 10)
            iconId = R.drawable.event_neutral;
        else
            iconId = R.drawable.event_happy;        
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), iconId);
                
        event.m_description = description() + note;
        event.setHealth(m_health);
        event.setHour(m_timeToPay);
        event.setPrice(price);
        event.m_image = bm;
                
        return true;
    }
        
    public static ArrayList<TITFLEvent> loadRandomEvents(AssetManager am)
    {
        ArrayList<TITFLEvent> ret = new ArrayList<TITFLEvent>();        

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

                TITFLRandomEvent element = new TITFLRandomEvent();
                for (int i = 0; i < parser.getAttributeCount(); i++)
                {
                    String attribName = parser.getAttributeName(i);
                    String attribValue = parser.getAttributeValue(i);
                    if (attribName.equals(atr_id))
                        element.m_id = attribValue;
                    else if (attribName.equals(atr_description))
                        element.m_description = attribValue;
                    else if (attribName.equals(atr_price))
                        element.m_price = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_time_to_pay))
                        element.m_timeToPay = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_health))
                        element.m_health = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_required_goods))
                        element.m_required_goods = attribValue;
                    else if (attribName.equals(atr_losing_goods))
                        element.m_losing_goods = attribValue;
                    else if (attribName.equals(atr_acquiring_goods))
                        element.m_acquiring_goods = attribValue;
                    //else if (attribName.equals(atr_trigger_1))
                    //    element.m_trigger.m_health = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_trigger_2))
                    //    element.m_trigger.m_wealth = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_trigger_3))
                    //    element.m_trigger.m_education = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_trigger_4))
                    //    element.m_trigger.m_career = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_trigger_5))
                    //    element.m_trigger.m_life = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_affect_1))
                    //    element.m_affect.m_health = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_affect_2))
                    //    element.m_affect.m_wealth = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_affect_3))
                    //    element.m_affect.m_education= Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_affect_4))
                    //    element.m_affect.m_career = Integer.parseInt(attribValue);
                    //else if (attribName.equals(atr_affect_5))
                    //    element.m_affect.m_life = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_1))
                        element.m_affectOnHappiness.m_intelligent = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_2))
                        element.m_affectOnHappiness.m_hardWorking = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_3))
                        element.m_affectOnHappiness.m_goodLooking = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_4))
                        element.m_affectOnHappiness.m_physical = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_5))
                        element.m_affectOnHappiness.m_lucky = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_health_insurance))
                        element.m_health_insurance_coverage = Float.parseFloat(attribValue);
                    else if (attribName.equals(atr_car_insurance))
                        element.m_car_insurance_coverage = Float.parseFloat(attribValue);
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
