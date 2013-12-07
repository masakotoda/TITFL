package com.noetap.titfl;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class TITFLBelongingEvent 
{
    private TITFLGoodsEvent m_eventRef;
    private TITFLBelonging m_parent;
    private int m_lastEventOccurred;    // Week# last time this event occurred.
    
    private static String tag_item = "belonging_event";
    private static String atr_event_id = "event_id";
    private static String atr_last_event = "last_event_occurred";

    private TITFLBelongingEvent(TITFLBelonging parent)
    {
        m_parent = parent;
    }
    
    public TITFLBelongingEvent(TITFLGoodsEvent eventRef, TITFLBelonging parent)
    {
        m_eventRef = eventRef;
        m_parent = parent;
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

    public static TITFLBelongingEvent deserialize(XmlPullParser parser, TITFLBelonging belonging)
    {
        TITFLBelongingEvent ret = new TITFLBelongingEvent(belonging);
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
                     ret.m_eventRef = findEventRef(attribValue, belonging.goodsRef());
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
    
    public boolean isDue(int week)
    {
        if (m_eventRef == null)
            return false;
        
        int price = eventRef().price();
        if (price < 0)
        {
            // Need to pay financial charge
            int balance = m_parent.loanAmount() - m_parent.completedPayment();
            if (balance == 0)
            {
                return false;
            }
        }
        
        if (m_lastEventOccurred + m_eventRef.cycle() <= week)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean process(TITFLPlayer owner, ListAdapterBeginWeek.BeginWeekItem event)
    {
        if (m_eventRef == null)
        {
            event.m_description = "unexpected error";
            return false;
        }

        
        //String message = event.m_description;
        //message += ": ";
        String message = "";
        message += eventRef().description();

        int week = owner.currentLocation().town().currentWeek();

        int price = eventRef().price();
        if (price < 0)
        {
            // Pay financial charge
            int balance = m_parent.loanAmount() - m_parent.completedPayment();
            price = Math.max(1, (int)(0.005 * balance));
        }
        else if (!eventRef().isFixedPrice())
        {
            price *= owner.currentLocation().town().economyFactor();
        }
        
        // Oil change can be rejected (player will lose the car though)
        // Rent cannot be rejected (the balance may become minus.)
        if (owner.cash() < price && eventRef().canReject())
        {
            message += " - FAIL!";
            event.m_description = message;
            return false;
        }
        
        event.m_description = message;
        event.setPrice(price);
        event.setHour(eventRef().timeToPay());
        
        owner.pay(price);
        owner.addHours(eventRef().timeToPay());
        m_lastEventOccurred = week;
        return true;
    }
}
