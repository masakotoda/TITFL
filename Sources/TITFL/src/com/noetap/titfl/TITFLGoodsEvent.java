package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLGoodsEvent implements TITFLEvent
{
    private TITFLGoods m_goods;      // Parent goods
    
    private String m_id;
    private String m_description;
    private int m_cycle;             // How often? Every 2 weeks? Every 4 weeks?
    private int m_price;             // How much do you need to pay?
    private int m_timeToPay;         // For example, to keep spouse, he/she needs to pay "time".
    private boolean m_canReject;     // Player has choice to accept or reject (unlike original jones). If he/she rejects, he/she lose the goods.
    private boolean m_canSkip;       // Does the Event happen by chance? (If so and lucky, last event occured will be updated without actual event.)
    private boolean m_isFixedPrice;  // Is Fixed Event Price? (If not, the actual maintenance fee will be determined with economy factor.)

    private static String asset_xml_name = "default_goodsevent.xml";
    private static String tag_root = "TITFL";
    private static String tag_item = "goodsevent";
    private static String atr_id = "goodsevent_id";
    private static String atr_description = "description";
    private static String atr_goods_id = "goods_id";
    private static String atr_cycle = "cycle";
    private static String atr_price = "price";
    private static String atr_time = "time_to_pay";
    private static String atr_can_reject = "can_reject";
    private static String att_can_skip = "can_skipped";
    private static String atr_is_fixed_price = "is_fixed_price";
    
    public TITFLGoodsEvent()
    {        
    }
    
    public TITFLGoods goods()
    {
        return m_goods;
    }
    
    public String id()
    {
        return m_id;
    }

    public String description()
    {
        return m_description;
    }
    
    public int cycle()
    {
        return m_cycle;
    }

    public int price()
    {
        return m_price;
    }
    
    public int timeToPay()
    {
        return m_timeToPay;
    }
    
    public boolean canReject()
    {
        return m_canReject;
    }
    
    public boolean canSkip()
    {
        return m_canSkip;
    }

    public boolean isFixedPrice()
    {
        return m_isFixedPrice;
    }

    public static ArrayList<TITFLGoodsEvent> loadDefaultGoodsEvent(AssetManager am, TITFLTown town)
    {
        ArrayList<TITFLGoodsEvent> ret = new ArrayList<TITFLGoodsEvent>();
        
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

                TITFLGoodsEvent element = new TITFLGoodsEvent();
                for (int i = 0; i < parser.getAttributeCount(); i++)
                {
                    String attribName = parser.getAttributeName(i);
                    String attribValue = parser.getAttributeValue(i);
                    if (attribName.equals(atr_id))
                        element.m_id = attribValue;
                    else if (attribName.equals(atr_description))
                        element.m_description = attribValue;
                    else if (attribName.equals(atr_goods_id))
                        element.m_goods = town.findGoods(attribValue);
                    else if (attribName.equals(atr_cycle))
                        element.m_cycle = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_price))
                        element.m_price = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_time))
                        element.m_timeToPay = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_can_reject))
                        element.m_canReject = Boolean.parseBoolean(attribValue);
                    else if (attribName.equals(att_can_skip))
                        element.m_canSkip = Boolean.parseBoolean(attribValue);
                    else if (attribName.equals(atr_is_fixed_price))
                        element.m_isFixedPrice = Boolean.parseBoolean(attribValue);
                }
                
                ret.add(element);
                if (element.m_goods != null)
                    element.m_goods.events().add(element);
                parser.next();
            }
        }
        catch (Exception e)
        {
        }

        return ret;    
    }
}
