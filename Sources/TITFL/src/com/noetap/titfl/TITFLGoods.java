package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Xml;

public class TITFLGoods implements TITFLItem
{
    private TITFLTownElement m_townelement;      // Parent element
    
    private ArrayList<TITFLGoodsEvent> m_events; // Array of possible TownGoodsEvent (repair, divorce, accident, insurance premium payment, etc.)

    private String m_id;
    private String m_name;
    private String m_group;            // General merchandise, Wearable merchandise, Food, Insurance, Degree, Transportation, House, Home Goods, Spouse
    private int m_price;               // Monetary value
    private int m_foodValue;           // Value of food (How many weeks). Applicable if it's food group.
    private int m_classCredit;         // How many credits? Applicable if it's degree group.
    private int m_speed;               // Speed factor. Applicable if it's transportation group.
    private int m_expire;              // Expire period
    private Point m_positionToDisplay; // It’s mainly for home goods & wearable merchandise
    private TITFL.CharacterFactor m_affectOnHappiness; // Affect on happy level for each characteristic
    private int m_affectOnHealth;      // Affect on health level
    private String m_greeting;         // Greeting upon purchase
    private String m_losing;           // Message when losing/expiring
    private String m_requiredGoods;    // Required goods to buy this goods
    private int m_maxUnits = 1;        // Max units that player can buy at once
    private Bitmap m_bitmap = null;
    static private Bitmap m_defaultBitmap = null;

    private static String asset_xml_name = "default_goods.xml";
    private static String tag_root = "TITFL";
    private static String tag_item = "goods";
    private static String atr_name = "name";
    private static String atr_id = "goods_id";
    private static String atr_townelement_id = "townelement_id";
    private static String atr_price = "price";
    private static String atr_group = "group";    
    private static String atr_foodValue = "food_value";
    private static String atr_classCredit = "class_credit";
    private static String atr_speed = "speed";
    private static String atr_expire = "expire";
    private static String atr_displayX = "x_to_display";
    private static String atr_displayY = "y_to_display";
    private static String atr_happiness_1 = "happiness_per_intelligent_factor";
    private static String atr_happiness_2 = "happiness_per_hardworking_factor";
    private static String atr_happiness_3 = "happiness_per_goodlooking_factor";
    private static String atr_happiness_4 = "happiness_per_physical_factor";
    private static String atr_happiness_5 = "happiness_per_lucky_factor";    
    private static String atr_health = "affect_on_health";
    private static String atr_greeting = "greeting";
    private static String atr_losing = "losing";
    private static String atr_required_goods = "required_goods";
    private static String atr_max_units = "max_units";

    public TITFLGoods()
    {
        m_events = new ArrayList<TITFLGoodsEvent>();
        m_positionToDisplay = new Point();
        m_affectOnHappiness = new TITFL.CharacterFactor();
    }
    
    @Override
    public String toString() 
    {
        int price = getPrice();
        if (isLoan())
            return m_name;
        else
            return m_name + " - $" + Integer.toString(price);
    }

    public TITFLTownElement townelement()
    {
        return m_townelement;
    }
    
    public ArrayList<TITFLGoodsEvent> events()
    {
        return m_events;
    }
    
    public String id()
    {
        return m_id;
    }

    public String getImageName()
    {
        return TITFLActivity.pathGoods + m_id + ".png";
    }

    public Bitmap getBitmap()
    {
        if (m_bitmap != null)
            return m_bitmap;

        Activity activity = m_townelement.town().activity();
        m_bitmap = NoEtapUtility.getBitmap(activity, getImageName());
        if (m_bitmap == null)
        {
            if (m_defaultBitmap == null)
                m_defaultBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.goods_sample);
            m_bitmap = m_defaultBitmap;
        }

        return m_bitmap;
    }

    public String name()
    {
        return m_name;
    }

    public String group()
    {
        return m_group;
    }
    
    public int price()
    {
        return m_price;
    }

    public int foodValue()
    {
        return m_foodValue;
    }
    
    public int classCredit()
    {
        return m_classCredit;
    }
    
    public int speed()
    {
        return m_speed;
    }
    
    public int expire()
    {
        return m_expire;
    }

    public Point positionToDisplay()
    {
        return m_positionToDisplay;
    }
    
    public TITFL.CharacterFactor affectOnHappiness()
    {
        return m_affectOnHappiness;
    }
    
    public int affectOnHealth()
    {
        return m_affectOnHealth;
    }

    public String greeting()
    {
        return m_greeting;
    }
    
    public String losing()
    {
        return m_losing;
    }
    
    public int maxUnits()
    {
        return m_maxUnits;
    }

    public static ArrayList<TITFLGoods> loadDefaultGoods(AssetManager am, TITFLTown town)
    {
        ArrayList<TITFLGoods> ret = new ArrayList<TITFLGoods>();
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

                TITFLGoods goods = new TITFLGoods();
                for (int i = 0; i < parser.getAttributeCount(); i++)
                {
                    String attribName = parser.getAttributeName(i);
                    String attribValue = parser.getAttributeValue(i);
                    if (attribName.equals(atr_id))
                        goods.m_id = attribValue;
                    else if (attribName.equals(atr_name))
                        goods.m_name = attribValue;
                    else if (attribName.equals(atr_townelement_id))
                        goods.m_townelement = town.findElement(attribValue);
                    else if (attribName.equals(atr_price))
                        goods.m_price = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_speed))
                        goods.m_speed = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_expire))
                        goods.m_expire = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_group))   
                        goods.m_group = attribValue;
                    else if (attribName.equals(atr_foodValue))
                        goods.m_foodValue = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_classCredit))
                        goods.m_classCredit = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_displayX))
                        goods.m_positionToDisplay.x = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_displayY))
                        goods.m_positionToDisplay.y = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_1))
                        goods.m_affectOnHappiness.m_intelligent = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_2))
                        goods.m_affectOnHappiness.m_hardWorking = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_3))
                        goods.m_affectOnHappiness.m_goodLooking = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_4))
                        goods.m_affectOnHappiness.m_physical = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_happiness_5))   
                        goods.m_affectOnHappiness.m_lucky = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_health))
                        goods.m_affectOnHealth = Integer.parseInt(attribValue);
                    else if (attribName.equals(atr_greeting))
                        goods.m_greeting = attribValue;
                    else if (attribName.equals(atr_losing))
                        goods.m_losing = attribValue;
                    else if (attribName.equals(atr_required_goods))
                        goods.m_requiredGoods = attribValue;
                    else if (attribName.equals(atr_max_units))
                        goods.m_maxUnits = Integer.parseInt(attribValue);
                }
                
                ret.add(goods);
                if (goods.m_townelement != null)
                    goods.m_townelement.merchandise().add(goods);
                else
                    town.goods().add(goods);
                parser.next();
            }
        }
        catch (Exception e)
        {
        }
        
        @SuppressWarnings("unused")
        ArrayList<TITFLGoodsEvent> events = TITFLGoodsEvent.loadDefaultGoodsEvent(am, town);

        return ret;
    }
    
    public boolean isRefrigerator()
    {
        if (m_id.equals("goods_refrigerator"))
            return true;
        else
            return false;
    }
    
    public boolean isFreezer()
    {
        if (m_id.equals("goods_freezer"))
            return true;
        else
            return false;
    }
    
    public boolean isHouse()
    {
        if (m_id.equals("goods_house"))
            return true;
        else
            return false;
    }

    public boolean isApartment()
    {
        if (m_id.equals("goods_cheap_apartment"))
            return true;
        else
            return false;
    }

    public boolean isCasualOutfit()
    {
        if (m_id.equals("goods_casual_outfit"))
            return true;
        else
            return false;
    }

    public boolean isBusinessOutfit()
    {
        if (m_id.equals("goods_business_outfit"))
            return true;
        else
            return false;
    }

    public boolean isDressOutfit()
    {
        if (m_id.equals("goods_dress_outfit"))
            return true;
        else
            return false;
    }

    public boolean isHealthInsurance()
    {
        if (m_id.equals("goods_health_insurance"))
            return true;
        else
            return false;
    }
    
    public boolean isCarInsurance()
    {
        if (m_id.equals("goods_car_insurance"))
            return true;
        else
            return false;
    }
    
    public boolean isLottery()
    {
        if (m_id.equals("goods_lottery_ticket"))
            return true;
        else
            return false;
    }
    
    public boolean isOutfit()
    {
        if (isCasualOutfit() ||
            isBusinessOutfit() ||
            isDressOutfit())
            return true;
        else
            return false;
    }
    public boolean isBicycle()
    {
        if (m_id.equals("goods_bicycle"))
            return true;
        else
            return false;
    }

    public boolean isDegree()
    {
        if (isDegreeBasic())
            return true;
        if (isDegreeEngineering())
            return true;
        if (isDegreeBusiness())
            return true;
        if (isDegreeAcademic())
            return true;
        
        return false;
    }

    public boolean isDegreeBasic()
    {
        if (m_group != null && m_group.equals("degree_basic"))
            return true;
        else
            return false;        
    }

    public boolean isDegreeEngineering()
    {
        if (m_group != null && m_group.equals("degree_engineering"))
            return true;
        else
            return false;        
    }
    
    public boolean isDegreeBusiness()
    {
        if (m_group != null && m_group.equals("degree_business"))
            return true;
        else
            return false;        
    }
    
    public boolean isDegreeAcademic()
    {
        if (m_group != null && m_group.equals("degree_academic"))
            return true;
        else
            return false;        
    }
    
    public boolean isLoan()
    {
        if (isGeneralLoan())
            return true;
        if (isStudentLoan())
            return true;
        if (isMortgage())
            return true;
        
        return false;
    }

    public boolean isGeneralLoan()
    {
        if (m_id.equals("goods_general_loan"))
            return true;
        else
            return false;
    }

    public boolean isStudentLoan()
    {
        if (m_id.equals("goods_student_loan"))
            return true;
        else
            return false;
    }

    public boolean isMortgage()
    {
        if (m_id.equals("goods_mortgage"))
            return true;
        else
            return false;
    }
    
    public boolean isTransportation()
    {
        if (m_speed == 0)
            return false;
        else
            return true;
    }

    public int getPrice()
    {
        return (int)(m_price * m_townelement.town().economyFactor());        
    }
    
    public boolean isEligible(TITFLPlayer player)
    {
        if (m_requiredGoods == null)
            return true;
        
        for (TITFLBelonging b : player.belongings())            
        {
            if (id().equals(b.goodsRef().id()))
            {
                if (maxUnits() > 0 && maxUnits() >= b.unit())
                {
                    NoEtapUtility.showAlert(m_townelement.town().activity(), "Information", "You can't have so many " + m_requiredGoods + ".");
                    return false;
                }
            }
        }
        
        for (TITFLBelonging b : player.belongings())
        {
            if (m_requiredGoods.equals(b.goodsRef().id()))
            {
                if (b.completedCredit() == b.goodsRef().classCredit())
                {
                    return true;
                }
                else
                {
                    NoEtapUtility.showAlert(m_townelement.town().activity(), "Information", "Please finish " + m_requiredGoods + " first.");
                    return false;
                }
            }
        }

        NoEtapUtility.showAlert(m_townelement.town().activity(), "Information", "Please get " + m_requiredGoods + " first.");
        return false;
    }

    public TITFLTownElement convertToTownElement()
    {
        TITFLTownElement element = null;

        if (isApartment())
            element = m_townelement.town().findApartment();
        else if (isHouse())
            element = m_townelement.town().findHouse();

        return element;
    }
}
