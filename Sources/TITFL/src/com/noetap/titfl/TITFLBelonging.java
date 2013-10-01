package com.noetap.titfl;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class TITFLBelonging 
{
    private ArrayList<TITFLBelongingEvent> m_events;
    private TITFLGoods m_goodsRef;
    private int m_acquiredWeek;    
    private int m_completedCredit;
    
    private static String tag_item = "belonging";
    private static String tag_events = "belonging_events";
    private static String atr_goods_id = "goods_id";
    private static String atr_acquired_week = "acquired_week";
    private static String atr_completed_credit = "completed_credit";
    private static String atr_count = "count";

    private TITFLBelonging()
    {        
        m_events = new ArrayList<TITFLBelongingEvent>();
    }
    
    public TITFLBelonging(TITFLGoods goodsRef, int acquiredWeek)
    {
        m_events = new ArrayList<TITFLBelongingEvent>();
        m_goodsRef = goodsRef;
        m_acquiredWeek = acquiredWeek;
        
        for (TITFLGoodsEvent event : m_goodsRef.events())
            m_events.add(new TITFLBelongingEvent(event));
    }
               
    public ArrayList<TITFLBelongingEvent> events()
    {
        return m_events;
    }
    
    public TITFLGoods goodsRef()
    {
        return m_goodsRef;
    }
    
    public int acquiredWeek()
    {
        return m_acquiredWeek;
    }

    public int completedCredit()
    {
        return m_completedCredit;
    }
    
    public boolean serialize(XmlSerializer serializer)
    {
        try
        {
            String ns = "";
            serializer.startTag(ns, tag_item);
            serializer.attribute(ns, atr_goods_id, m_goodsRef.id());
            serializer.attribute(ns, atr_acquired_week, Integer.toString(m_acquiredWeek));
            serializer.attribute(ns, atr_completed_credit, Integer.toString(m_completedCredit));

            serializer.startTag(ns, tag_events);
            serializer.attribute(ns, atr_count, Integer.toString(m_events.size()));
            for (TITFLBelongingEvent event : m_events)
            {
                event.serialize(serializer);
            }
            serializer.endTag(ns, tag_events);
            
            serializer.endTag(ns, tag_item);
        }
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static TITFLBelonging deserialize(XmlPullParser parser, TITFLTown town)
    {
        TITFLBelonging ret = new TITFLBelonging();
        try
        {
            parser.next(); // <belonging>
            if (parser.getEventType() != XmlPullParser.START_TAG) 
                throw new Exception();
            String name = parser.getName();
            if (!name.equals(tag_item))
                throw new Exception();

            for (int i = 0; i < parser.getAttributeCount(); i++)
            {
                String attribName = parser.getAttributeName(i);
                String attribValue = parser.getAttributeValue(i);
                if (attribName.equals(atr_goods_id))
                    ret.m_goodsRef = findGoodsRef(attribValue, town);
                else if (attribName.equals(atr_acquired_week))
                    ret.m_acquiredWeek = Integer.parseInt(attribValue);
                else if (attribName.equals(atr_completed_credit))
                    ret.m_completedCredit = Integer.parseInt(attribValue);                
            }                
                
            parser.next(); // <belonging_events>
            if (parser.getEventType() != XmlPullParser.START_TAG) 
                throw new Exception();
            name = parser.getName();
            if (!name.equals(tag_events))
                throw new Exception();
                
            int count = 0;
            for (int i = 0; i < parser.getAttributeCount(); i++)
            {
                String attribName = parser.getAttributeName(i);
                String attribValue = parser.getAttributeValue(i);
                if (attribName.equals(atr_count))
                     count = Integer.parseInt(attribValue);
            }
                
            for (int i = 0; i < count; i++)
            {
                TITFLBelongingEvent event = TITFLBelongingEvent.deserialize(parser, ret.m_goodsRef);
                 if (event != null)
                       ret.m_events.add(event);
            }

            parser.next(); // </belonging_events>
            parser.next(); // </belonging>
        }
        catch (Exception e)
        {
            ret = null;
        }
        return ret;
    }
    
    static TITFLGoods findGoodsRef(String goodsId, TITFLTown town)
    {
        for (TITFLTownElement element : town.elements())
        {
            for (TITFLGoods goods : element.merchandise())
            {
                if (goods.id().equals(goodsId))
                {
                    return goods;
                }
            }
        }
        return null;
    }
}
