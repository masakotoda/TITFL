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
    private int m_cycle;            // How often? Every 2 weeks? Every 4 weeks?
    private int m_price;            // How much do you need to pay?
    private int m_time;             // For example, to keep spouse, he/she needs to pay "time".
    private boolean m_canReject;     // Player has choice to accept or reject (unlike original jones). If he/she rejects, he/she lose the goods.
    private boolean m_canBeSkipped;    // Does the Event happen by chance? (If so and lucky, last event occured will be updated without actual event.)
    private boolean m_isFixedPrice;    // Is Fixed Event Price? (If not, the actual maintenance fee will be determined with economy factor.)

    private static String tag_root = "TITFL";
    private static String tag_item = "goodsevent";
    private static String atr_id = "goodsevent_id";
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
                    if (attribName.equals(atr_id))
                        element.m_id = attribValue;
                    else if (attribName.equals(atr_description))
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

    public String id()
    {
        return m_id;
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
