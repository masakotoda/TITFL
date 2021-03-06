package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Xml;

public class TITFLTownElement
{
    private TITFLTown m_town;
    private ArrayList<TITFLGoods> m_merchandise;
    private ArrayList<TITFLJob> m_jobs;
    private String m_name;
    private String m_id;
    private int m_slot;    
    private String m_greeting;
    private float m_speechRate;
    private float m_speechPitch;
    private boolean m_canBeg;
    private boolean m_canRelax;
    private boolean m_canExercise;
    private boolean m_canSocialize;
    private Bitmap m_bitmap;
    private TITFLPlayer m_visitor;
    private TITFLTownMapNode m_node;

    private static String asset_xml_name = "default_townelement.xml";
    private static String tag_root = "TITFL";
    private static String tag_item = "townelement";
    private static String atr_id = "townelement_id";
    private static String atr_name = "name";
    private static String atr_slot = "slot";
    private static String atr_greeting = "greeting";
    private static String atr_speechPitch = "speech_pitch";
    private static String atr_speechRate = "speech_rate";
    private static String atr_can_beg = "can_beg";
    private static String atr_can_relax = "can_relax";
    private static String atr_can_exercise = "can_exercise";
    private static String atr_can_socialize = "can_socialize";
    
    public TITFLTownElement()
    {
        m_merchandise = new ArrayList<TITFLGoods>();
        m_jobs = new ArrayList<TITFLJob>();
    }
    
    public TITFLTown town()
    {
        return m_town;
    }

    public ArrayList<TITFLGoods> merchandise()
    {
        return m_merchandise;
    }
    
    public ArrayList<TITFLJob> jobs()
    {
        return m_jobs;
    }
    
    public String name()
    {
        return m_name;
    }
    
    public String id()
    {
        return m_id;
    }
    
    public int slot()
    {
        return m_slot;
    }
    
    public boolean canBeg()
    {
        return m_canBeg;
    }
    
    public boolean canExercise()
    {
        return m_canExercise;
    }
    
    public boolean canRelax()
    {
        return m_canRelax;
    }
    
    public boolean canSocialize()
    {
        return m_canSocialize;
    }
    
    public void setSlot(int slot)
    {
        m_slot = slot;
    }
    
    public String greeting()
    {
        return m_greeting;
    }
    
    public float speechRate()
    {
        return m_speechRate;
    }

    public float speechPitch()
    {
        return m_speechPitch;
    }
   
    public TITFLPlayer visitor()   
    {
        return m_visitor;
    }
    
    public void setVisitor(TITFLPlayer player)
    {
        m_visitor = player;
    }
   
    public TITFLTownMapNode node()
    {
        return m_node;
    }

    public void setNode(TITFLTownMapNode node)
    {
        m_node = node;
    }
    
    public boolean isHouse()
    {
        if (m_id.equals("townelement_house"))
            return true;
        else
            return false;
    }

    public boolean isApartment()
    {
        if (m_id.equals("townelement_apartment"))
            return true;
        else
            return false;
    }

    public boolean isHome()
    {
        if (isApartment() || isHouse())
            return true;
        else
            return false;
    }

    public boolean isSchool()
    {
        if (m_id.equals("townelement_school"))
            return true;
        else
            return false;
    }

    public boolean isBank()
    {
        if (m_id.equals("townelement_bank"))
            return true;
        else
            return false;
    }

    public static ArrayList<TITFLTownElement> loadTownElements(AssetManager am, TITFLTown town)
    {
        ArrayList<TITFLTownElement> ret = new ArrayList<TITFLTownElement>();        
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

                TITFLTownElement element = new TITFLTownElement();
                for (int i = 0; i < parser.getAttributeCount(); i++)
                {
                    String attribName = parser.getAttributeName(i);
                    String attribValue = parser.getAttributeValue(i);
                    if (attribName.equals(atr_id))
                        element.m_id = attribValue;
                    else if (attribName.equals(atr_name))
                        element.m_name = attribValue;
                    else if (attribName.equals(atr_slot))
                        element.m_slot = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_greeting))
                        element.m_greeting = attribValue;
                    else if (attribName.equals(atr_speechRate))
                        element.m_speechRate = Float.parseFloat(attribValue);
                    else if (attribName.equals(atr_speechPitch))
                        element.m_speechPitch = Float.parseFloat(attribValue);
                    else if (attribName.equals(atr_can_beg))
                        element.m_canBeg = Boolean.parseBoolean(attribValue);
                    else if (attribName.equals(atr_can_relax))
                        element.m_canRelax = Boolean.parseBoolean(attribValue);
                    else if (attribName.equals(atr_can_exercise))
                        element.m_canExercise = Boolean.parseBoolean(attribValue);
                    else if (attribName.equals(atr_can_socialize))
                        element.m_canSocialize = Boolean.parseBoolean(attribValue);
                }
                
                element.m_town = town;
                ret.add(element);
                parser.next();
            }
        }
        catch (Exception e)
        {
        }        
        return ret;
    }
    
    public String getInsideImageName()
    {
        return TITFLActivity.pathElementInside + m_id + ".png";
    }

    public String getImageName()
    {
        return TITFLActivity.pathElement + m_id + ".png";
    }
    
    public Bitmap getBitmap()
    {
        if (m_bitmap != null)
            return m_bitmap;
        
        m_bitmap = NoEtapUtility.getBitmap(m_town.activity(), getImageName());
        return m_bitmap;
    }
    
    public String getGreeterFrame(int frame)
    {
        String path = TITFLActivity.pathGreeter + id() + "_01.png";
        Bitmap bm = NoEtapUtility.getBitmap(m_town.activity(), path);
        if (bm == null)
        {
            path = TITFLActivity.pathGreeter + "greeter_a_frm0" + Integer.toString(frame) + ".png";
        }
        else
        {
            path = TITFLActivity.pathGreeter + id() + "_0" + Integer.toString(frame) + ".png";
        }
        return path;
    }

    public void draw(Canvas canvas, Paint paint)
    {
        //TODO
        if (m_slot < 0)
            return;

        Rect rect = m_town.slotToPosition(m_slot);

        if (m_visitor != null)
        {
            paint.setARGB(100, 50, 255, 255);        
            canvas.drawRect(rect, paint);
        }

        Bitmap bitmap = getBitmap();
        if (bitmap != null)
        {
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, null);
        }
            
        //paint.setColor(Color.BLACK);
        //paint.setTextSize(32 * NoEtapUtility.getFactor(m_town.activity()));
        //canvas.drawText(m_name, rect.left, (rect.bottom), paint);            
    }
    
    public void open()
    {
        TITFLActivity activity = (TITFLActivity) m_town.activity();
        activity.openTownElement(this);
    }
    
    public boolean canWork()
    {
        return (m_jobs.size() > 0);
    }
}
