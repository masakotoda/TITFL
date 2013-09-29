package com.noetap.titfl;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class TITFLBelongingEvent 
{
    private TITFLGoodsEvent m_eventRef;
    private int m_lastEventOccurred;    // Week# last time this event occurred.
    
    private static String tag_item = "belonging_event";
    private static String atr_event_id = "event_id";
    private static String atr_last_event = "last_event_occurred";

    private TITFLBelongingEvent()
    {        
    }
    
    public TITFLBelongingEvent(TITFLGoodsEvent eventRef)
    {
        m_eventRef = eventRef;
    }
    
    public TITFLGoodsEvent eventRef()
    {
        return m_eventRef;
    }
    
    public int lastEventOccurred()
    {
        return m_lastEventOccurred;
    }
    
    public boolean serialize(XmlSerializer serializer)
    {
        try 
        {
            String ns = "";
            serializer.startTag(ns, tag_item);
            serializer.attribute(ns, atr_event_id, m_eventRef.id());
            serializer.attribute(ns, atr_last_event, Integer.toString(m_lastEventOccurred));
            serializer.endTag(ns, tag_item);
        }
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
        return true;
    }    

    public static TITFLBelongingEvent deserialize(XmlPullParser parser, TITFLGoods goods)
    {
        TITFLBelongingEvent ret = new TITFLBelongingEvent();
        try
        {
               parser.next(); //<belonging_event>
            if (parser.getEventType() != XmlPullParser.START_TAG) 
                  throw new Exception();                
            String name = parser.getName();
            if (!name.equals(tag_item))
                throw new Exception();

            for (int i = 0; i < parser.getAttributeCount(); i++)
            {
                String attribName = parser.getAttributeName(i);
                String attribValue = parser.getAttributeValue(i);
                if (attribName.equals(atr_event_id))
                     ret.m_eventRef = findEventRef(attribValue, goods);
                else if (attribName.equals(atr_last_event))
                    ret.m_lastEventOccurred = Integer.parseInt(attribValue);
            }
                
            parser.next(); //</belonging_event>
        }
        catch (Exception e)
        {
            ret = null;
        }
        return ret;
    }

    private static TITFLGoodsEvent findEventRef(String eventId, TITFLGoods goods)
    {
        for (TITFLGoodsEvent event : goods.events())
        {
            if (event.id().equals(eventId))
            {
                return event;
            }
        }
        return null;
    }
}
