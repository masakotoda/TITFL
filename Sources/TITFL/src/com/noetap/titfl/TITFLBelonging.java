package com.noetap.titfl;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.graphics.Bitmap;

public class TITFLBelonging implements TITFLItem
{
    private ArrayList<TITFLBelongingEvent> m_events;
    private TITFLGoods m_goodsRef;
    private int m_acquiredWeek;
    private int m_completedCredit;
    private int m_loanAmount;
    private int m_completedPayment;
    private int m_unit;
    
    private static String tag_item = "belonging";
    private static String tag_events = "belonging_events";
    private static String atr_goods_id = "goods_id";
    private static String atr_acquired_week = "acquired_week";
    private static String atr_completed_credit = "completed_credit";
    private static String atr_loan_amount = "loan_amount";
    private static String atr_completed_payment = "completed_payment";
    private static String atr_unit = "unit";
    private static String atr_event_count = "event_count";

    private TITFLBelonging()
    {        
        m_events = new ArrayList<TITFLBelongingEvent>();
    }
    
    public TITFLBelonging(TITFLGoods goodsRef, int unit, int acquiredWeek)
    {
        m_events = new ArrayList<TITFLBelongingEvent>();
        m_goodsRef = goodsRef;
        m_acquiredWeek = acquiredWeek;
        m_unit = unit;
        
        for (TITFLGoodsEvent event : m_goodsRef.events())
            m_events.add(new TITFLBelongingEvent(event));
    }
    
    @Override
    public String toString()
    {
        String ret = goodsRef().name();
        if (goodsRef().isDegree())
            ret = ret + " - " + Integer.toString(m_completedCredit) + "/" + Integer.toString(goodsRef().classCredit());
        else if (goodsRef().isLoan())
            ret = ret + " - $" + Integer.toString(m_loanAmount);
        else
            ret = ret + " - " + Integer.toString(m_unit);
        return ret;
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
    
    public int loanAmount()
    {
        return m_loanAmount;
    }
    
    public void setLoanAmount(int loanAmount)
    {
        m_loanAmount = loanAmount;
    }

    public int completedPayment()
    {
        return m_completedPayment;
    }
    
    public int unit()
    {
        return m_unit;
    }

    public void setUnit(int unit)
    {
        m_unit = unit;
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
            serializer.attribute(ns, atr_loan_amount, Integer.toString(m_loanAmount));
            serializer.attribute(ns, atr_completed_payment, Integer.toString(m_completedPayment));
            serializer.attribute(ns, atr_unit, Integer.toString(m_unit));

            serializer.startTag(ns, tag_events);
            serializer.attribute(ns, atr_event_count, Integer.toString(m_events.size()));
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
                else if (attribName.equals(atr_loan_amount))
                    ret.m_loanAmount = Integer.parseInt(attribValue);                
                else if (attribName.equals(atr_completed_payment))
                    ret.m_completedPayment = Integer.parseInt(attribValue);                
                else if (attribName.equals(atr_unit))
                    ret.m_unit = Integer.parseInt(attribValue);                
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
                if (attribName.equals(atr_event_count))
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
        for (TITFLGoods goods : town.goods())
        {
            if (goods.id().equals(goodsId))
            {
                return goods;
            }
        }
        return null;
    }
    
    public void addCredit(int credit)
    {
        m_completedCredit += credit;
        if (m_completedCredit > goodsRef().classCredit())
            m_completedCredit = goodsRef().classCredit();
    }

    public void addPayment(int payment)
    {
        m_completedPayment += payment;
        if (m_completedPayment > m_loanAmount)
            m_completedPayment = m_loanAmount;
    }

    @Override
    public Bitmap getBitmap() 
    {
        if (m_goodsRef == null)
            return null;
        return m_goodsRef.getBitmap();
    }

    @Override
    public String name() 
    {
        if (m_goodsRef == null)
            return null;
        return m_goodsRef.name();
    }

    @Override
    public int getPrice() 
    {
        if (m_goodsRef == null)
            return 0;
        return m_goodsRef.getPrice();
    }
}
