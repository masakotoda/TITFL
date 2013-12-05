package com.noetap.titfl;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Xml;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class TITFLPlayer 
{
    private ImageView m_avatarImg;
    private ImageView m_marbleImg;

    private final float m_maxHour = 40;
    private final int m_incre = 10;
    
    private String m_name;
    private String m_alias;

    private TITFL.CharacterFactor m_character;
    private TITFL.DisciplineLevel m_education;
    private TITFL.DisciplineLevel m_experience;
    private TITFL.Satisfaction  m_satisfaction;
    //private int m_happiness;
    
    private int m_cash;
    private int m_saving;
    private ArrayList<TITFLBelonging> m_belongings;
        
    private TITFLJob m_job;
    private int m_lastWorkedWeek;
    private TITFLGoods m_transportation;    
    private TITFLGoods m_outfit;
    private TITFLTownElement m_currentLocation;
    private TITFLTownElement m_home;
    
    private int m_counter = -2;
    private float m_speedFactor = 2.0f; // 1 is default. 0.5 is x2 faster. 2 is x2 slower.
    private float m_hour = 0;
    private String m_avatar_frm_01;
    private String m_avatar_frm_02;
    private String m_avatar_frm_03;
    private String m_avatar_frm_04;
    private int m_themeColor;
    
    private static String asset_xml_name = "default_player.xml";
    private static String tag_root = "TITFL";
    private static String tag_item = "player";
    private static String tag_belongings = "belongings";
    private static String atr_count = "count";
    private static String atr_name = "name";
    private static String atr_alias = "alias";
    private static String atr_factor_intelligent = "factor_intelligent";
    private static String atr_factor_hardworking = "factor_hardworking";
    private static String atr_factor_goodlooking = "factor_goodlooking";
    private static String atr_factor_physical = "factor_physical";
    private static String atr_factor_lucky = "factor_lucky";
    private static String atr_edu_basic = "reqired_edu_basic";
    private static String atr_edu_engineer = "edu_engineer";
    private static String atr_edu_business = "edu_business";
    private static String atr_edu_academic = "edu_academic";
    private static String atr_exp_basic = "exp_basic";
    private static String atr_exp_engineer = "exp_engineer";
    private static String atr_exp_business = "exp_business";
    private static String atr_exp_academic = "exp_academic";
    private static String atr_satisfaction_health = "satisfaction_health";
    private static String atr_satisfaction_wealth = "satisfaction_wealth";
    private static String atr_satisfaction_education = "satisfaction_education";
    private static String atr_satisfaction_career = "satisfaction_career";
    private static String atr_satisfaction_life = "satisfaction_life";
    private static String atr_happiness = "happiness";
    private static String atr_cash = "cash";
    private static String atr_saving = "saving";
    private static String atr_avatar_frm_01 = "avatar_frm_01";
    private static String atr_avatar_frm_02 = "avatar_frm_02";
    private static String atr_avatar_frm_03 = "avatar_frm_03";
    private static String atr_avatar_frm_04 = "avatar_frm_04";
    private static String atr_theme_color_r = "theme_color_r";
    private static String atr_theme_color_g = "theme_color_g";
    private static String atr_theme_color_b = "theme_color_b";
    private static String atr_current_location = "current_location";
    //private static String atr_counter = "counter";
    private static String atr_speed_factor = "speed_factor";
    private static String atr_hour = "hour";
    private static String atr_job = "job";
    private static String atr_last_worked_week = "last_worked_week";
    private static String atr_transportation = "transportation";
    private static String atr_outfit = "outfit";
    private static String atr_home = "home";
    
    public TITFLPlayer()
    {
        m_belongings = new ArrayList<TITFLBelonging>();
        m_character = new TITFL.CharacterFactor();
        m_education = new TITFL.DisciplineLevel();
        m_experience = new TITFL.DisciplineLevel();
        m_satisfaction = new TITFL.Satisfaction();
    }
    
    public static TITFLPlayer createPlayer(TITFLPlayer defaultPlayer)
    {
        TITFLPlayer ret = new TITFLPlayer();
        ret.m_name = defaultPlayer.m_name;
        ret.m_alias = defaultPlayer.m_alias;
        ret.m_character = new TITFL.CharacterFactor(defaultPlayer.m_character);
        ret.m_cash = defaultPlayer.m_cash;
        ret.m_avatar_frm_01 = defaultPlayer.m_avatar_frm_01;
        ret.m_avatar_frm_02 = defaultPlayer.m_avatar_frm_02;
        ret.m_avatar_frm_03 = defaultPlayer.m_avatar_frm_03;
        ret.m_avatar_frm_04 = defaultPlayer.m_avatar_frm_04;
        ret.m_themeColor = defaultPlayer.m_themeColor;
        return ret;
    }
        
    public String name()
    {
        return m_name;
    }
    
    public String alias()
    {
        return m_alias;
    }
    
    public TITFL.CharacterFactor character()
    {
        return m_character;
    }
    
    public TITFL.DisciplineLevel education()
    {
        return m_education;
    }
    
    public TITFL.DisciplineLevel experience()
    {
        return m_experience;
    }
    
    public TITFL.Satisfaction satisfaction()
    {
        return m_satisfaction;
    }
    
    public void addHappiness(int happiness)
    {
        m_satisfaction.m_happiness += happiness;
    }
    
    public int cash()
    {
        return m_cash;
    }
    
    public int saving()
    {
        return m_saving;
    }

    public int generalLoan()
    {
        int amount = 0;
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isGeneralLoan())
                amount += g.loanAmount();
        }
        return amount;
    }

    public int studentLoan()
    {
        int amount = 0;
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isStudentLoan())
                amount += g.loanAmount();
        }
        return amount;
    }
    
    public int mortgage()
    {
        int amount = 0;
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isMortgage())
                amount += g.loanAmount();
        }
        return amount;
    }

    public ArrayList<TITFLBelonging> belongings()
    {
        return m_belongings;        
    }
    
    public TITFLJob job()
    {
        return m_job;
    }
    
    public int lastWorkedWeek()
    {
        return m_lastWorkedWeek;
    }
    
    public TITFLGoods transportation()
    {
        return m_transportation;    
    }

    public TITFLGoods outfit()
    {
        return m_outfit;
    }

    public TITFLTownElement currentLocation()
    {
        return m_currentLocation;
    }
    
    public TITFLTownElement home()
    {
        return m_home;   
    }
    
    //public int counter()
    //{
    //    return m_counter;
    //}
    
    public float speedFactor()
    {
        return m_speedFactor;
    }
    
    public float hour()
    {
        return m_hour;
    }
    
    public int hourByAngle()
    {
        int ret = (int)((360 * hour()) / m_maxHour);
        return ret;
    }

    public boolean isWeekOver()    
    {
        if (m_hour >= m_maxHour)
            return true;
        else
            return false;
    }
    
    public void closeWeek()
    {
        m_hour = 0;
        m_currentLocation.setVisitor(null);
    }
    
    public void setLocation(TITFLTownElement destination)
    {
        if (m_currentLocation != null)
            m_currentLocation.setVisitor(null);
        m_currentLocation = destination;
        destination.setVisitor(this);
    }
        
    public static ArrayList<TITFLPlayer> loadDefaultPlayers(AssetManager am)
    {
        ArrayList<TITFLPlayer> ret = new ArrayList<TITFLPlayer>();        
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

                TITFLPlayer element = deserializeProperties(parser, null);
                ret.add(element);
                parser.next();
            }
        }
        catch (Exception e)
        {
        }

        return ret;        
    }
       
    public boolean save(String key, SQLiteDatabase db)
    {
        TITFL.save(key + ".properties", serializeProperties(), db);
        TITFL.save(key + ".belongings", serializeBelongings(), db);        
        return true;
    }
    
    public static TITFLPlayer loadPlayer(String key, SQLiteDatabase db, TITFLTown town)    
    {
        final String properties = ".properties";
        final String belongings = ".belongings";

        TITFLPlayer player = null;
        {
            String xml = TITFL.loadString(key + properties, db);
            try
            {
                String ns = "";
                StringReader in = new StringReader(xml);
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in);
                parser.nextTag();                 
                parser.require(XmlPullParser.START_TAG, ns, tag_root);
                
                parser.next(); // <player>
                if (parser.getEventType() != XmlPullParser.START_TAG) 
                    return null;                    
                String name = parser.getName();
                if (!name.equals(tag_item))
                    return null;                    

                player = deserializeProperties(parser, town);
                parser.next(); // </player>
            }
            catch (Exception e)
            {
            }
        }
        
        if (player != null)
        {
            String xml = TITFL.loadString(key + belongings, db);
            player.m_belongings = deserializeBelongings(xml, town);
        }
        return player;
    }
    
    private String serializeProperties()
    {
        String ret = "";
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try 
        {
            String ns = "";
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag(ns, tag_root);
            serializer.startTag(ns, tag_item);
            
            String currentLocationId = "";
            if (m_currentLocation != null)
                currentLocationId = m_currentLocation.id();
            
            String jobId = "";
            if (m_job != null)
                jobId = m_job.id();

            String transportationId = "";
            if (m_transportation != null)
                transportationId = m_transportation.id();
            
            String outfitId = "";
            if (m_outfit != null)
                outfitId = m_outfit.id();

            String homeId = "";
            if (m_home != null)
                homeId = m_home.id();

            serializer.attribute(ns, atr_name, m_name);
            serializer.attribute(ns, atr_alias, m_alias);
            serializer.attribute(ns, atr_factor_intelligent, Integer.toString(m_character.m_intelligent));
            serializer.attribute(ns, atr_factor_hardworking, Integer.toString(m_character.m_hardWorking));
            serializer.attribute(ns, atr_factor_goodlooking, Integer.toString(m_character.m_goodLooking));
            serializer.attribute(ns, atr_factor_physical, Integer.toString(m_character.m_physical));
            serializer.attribute(ns, atr_factor_lucky, Integer.toString(m_character.m_lucky));
            serializer.attribute(ns, atr_edu_basic, Integer.toString(m_education.m_basic));
            serializer.attribute(ns, atr_edu_engineer, Integer.toString(m_education.m_engineering));
            serializer.attribute(ns, atr_edu_business, Integer.toString(m_education.m_business_finance));
            serializer.attribute(ns, atr_edu_academic, Integer.toString(m_education.m_academic));
            serializer.attribute(ns, atr_exp_basic, Integer.toString(m_experience.m_basic));
            serializer.attribute(ns, atr_exp_engineer, Integer.toString(m_experience.m_engineering));
            serializer.attribute(ns, atr_exp_business, Integer.toString(m_experience.m_business_finance));
            serializer.attribute(ns, atr_exp_academic, Integer.toString(m_experience.m_academic));
            serializer.attribute(ns, atr_satisfaction_health, Integer.toString(m_satisfaction.m_health));
            serializer.attribute(ns, atr_satisfaction_wealth, Integer.toString(m_satisfaction.m_wealth));
            serializer.attribute(ns, atr_satisfaction_education, Integer.toString(m_satisfaction.m_education));
            serializer.attribute(ns, atr_satisfaction_career, Integer.toString(m_satisfaction.m_career));
            serializer.attribute(ns, atr_satisfaction_life, Integer.toString(m_satisfaction.m_life));
            serializer.attribute(ns, atr_happiness, Integer.toString(m_satisfaction.m_happiness));
            serializer.attribute(ns, atr_cash, Integer.toString(m_cash));
            serializer.attribute(ns, atr_saving, Integer.toString(m_saving));
            serializer.attribute(ns, atr_current_location, currentLocationId);
            serializer.attribute(ns, atr_job, jobId);
            serializer.attribute(ns, atr_last_worked_week, Integer.toString(m_lastWorkedWeek));
            serializer.attribute(ns, atr_transportation, transportationId);
            serializer.attribute(ns, atr_outfit, outfitId);
            serializer.attribute(ns, atr_home, homeId);
            serializer.attribute(ns, atr_speed_factor, Float.toString(m_speedFactor));
            //serializer.attribute(ns, atr_counter, Integer.toString(m_counter));
            serializer.attribute(ns, atr_hour, Float.toString(m_hour));
            serializer.attribute(ns, atr_avatar_frm_01, m_avatar_frm_01);
            serializer.attribute(ns, atr_avatar_frm_02, m_avatar_frm_02);
            serializer.attribute(ns, atr_avatar_frm_03, m_avatar_frm_03);
            serializer.attribute(ns, atr_avatar_frm_04, m_avatar_frm_04);
            serializer.attribute(ns, atr_theme_color_r, Integer.toString(Color.red(m_themeColor)));
            serializer.attribute(ns, atr_theme_color_g, Integer.toString(Color.green(m_themeColor)));
            serializer.attribute(ns, atr_theme_color_b, Integer.toString(Color.blue(m_themeColor)));

            serializer.endTag(ns, tag_item);
            serializer.endTag(ns, tag_root);
            serializer.endDocument();
            ret = writer.toString();
        }
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }        
        return ret;
    }
    
    private String serializeBelongings()
    {
        String ret = "";
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try 
        {
            String ns = "";
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag(ns, tag_root);
            serializer.startTag(ns, tag_belongings);
            serializer.attribute(ns, atr_count, Integer.toString(m_belongings.size()));
            for (TITFLBelonging belonging : m_belongings)
                belonging.serialize(serializer);
            serializer.endTag(ns, tag_belongings);
            serializer.endTag(ns, tag_root);
            serializer.endDocument();
            ret = writer.toString();
        }
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }        
        return ret;
    }
    
    private static TITFLPlayer deserializeProperties(XmlPullParser parser, TITFLTown town)
    {
        TITFLPlayer player = new TITFLPlayer();
        int r = 0, g = 0, b = 0;
        for (int i = 0; i < parser.getAttributeCount(); i++)
        {
            String attribName = parser.getAttributeName(i);
            String attribValue = parser.getAttributeValue(i);
            if (attribName.equals(atr_name))
                player.m_name = attribValue;
            else if (attribName.equals(atr_alias))
                player.m_alias = attribValue;
            else if (attribName.equals(atr_factor_intelligent))
                player.m_character.m_intelligent = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_factor_hardworking))
                player.m_character.m_hardWorking = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_factor_goodlooking))
                player.m_character.m_goodLooking = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_factor_physical))
                player.m_character.m_physical = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_factor_lucky))
                player.m_character.m_lucky = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_edu_basic))
                player.m_education.m_basic = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_edu_engineer))
                player.m_education.m_engineering = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_edu_business))
                player.m_education.m_business_finance = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_edu_academic))
                player.m_education.m_academic = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_exp_basic))
                player.m_experience.m_basic = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_exp_engineer))
                player.m_experience.m_engineering = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_exp_business))
                player.m_experience.m_business_finance = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_exp_academic))
                player.m_experience.m_academic = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_satisfaction_health))
                player.m_satisfaction.m_health = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_satisfaction_wealth))
                player.m_satisfaction.m_wealth = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_satisfaction_education))
                player.m_satisfaction.m_education = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_satisfaction_career))
                player.m_satisfaction.m_career = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_satisfaction_life))
                player.m_satisfaction.m_life = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_happiness))
                player.m_satisfaction.m_happiness = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_cash))
                player.m_cash = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_saving))
                player.m_saving = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_avatar_frm_01))
                player.m_avatar_frm_01 = attribValue;
            else if (attribName.equals(atr_avatar_frm_02))
                player.m_avatar_frm_02 = attribValue;
            else if (attribName.equals(atr_avatar_frm_03))
                player.m_avatar_frm_03 = attribValue;
            else if (attribName.equals(atr_avatar_frm_04))
                player.m_avatar_frm_04 = attribValue;
            else if (attribName.equals(atr_theme_color_r))
                r = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_theme_color_g))
                g = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_theme_color_b))
                b = Integer.parseInt(attribValue);
            //else if (attribName.equals(atr_counter))
            //    player.m_counter = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_speed_factor))
                player.m_speedFactor = Float.parseFloat(attribValue);
            else if (attribName.equals(atr_hour))
                player.m_hour = Float.parseFloat(attribValue);
            else if (attribName.equals(atr_last_worked_week))
                player.m_lastWorkedWeek = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_current_location))
            {
                if (town != null)
                {
                    player.m_currentLocation = town.findElement(attribValue);
                }
            }
            else if (attribName.equals(atr_job))
            {
                if (town != null)
                {
                    player.m_job = town.findJob(attribValue);
                }
            }
            else if (attribName.equals(atr_transportation))
            {
                if (town != null)
                {
                    player.m_transportation = town.findGoods(attribValue);
                }
            }
            else if (attribName.equals(atr_outfit))
            {
                if (town != null)
                {
                    player.m_outfit = town.findGoods(attribValue);
                }
            }
            else if (attribName.equals(atr_home))
            {
                if (town != null)
                {
                    player.m_home = town.findElement(attribValue);
                }
            }
        }
        
        player.m_themeColor = Color.rgb(r, g, b);

        if (town != null && player.m_home == null) // Just in case
        {
            player.m_home = town.findApartment();
        }

        return player;
    }
    
    private static ArrayList<TITFLBelonging> deserializeBelongings(String xml, TITFLTown town)
    {
        ArrayList<TITFLBelonging> ret = new ArrayList<TITFLBelonging>();
        try
        {
            String ns = "";
            StringReader in = new StringReader(xml);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in);
            parser.nextTag();
             
            parser.require(XmlPullParser.START_TAG, ns, tag_root);
            parser.next(); // <belongings>
            if (parser.getEventType() != XmlPullParser.START_TAG) 
                return null;
                
            String name = parser.getName();
            if (!name.equals(tag_belongings))
                return null;

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
                TITFLBelonging belonging = TITFLBelonging.deserialize(parser, town);
                if (belonging != null)
                    ret.add(belonging);
            }
        }
        catch (Exception e)
        {
        }
        
        return ret;
    }
    
    private int getAnim(TITFLTownMapNode current, TITFLTownMapNode next)
    {
        if (next.x() < current.x())
            return R.anim.anim_marble_left;
        else if (next.x() > current.x())
            return R.anim.anim_marble_right;
        else if (next.y() < current.y())
            return R.anim.anim_marble_up;
        else if (next.y() > current.y())
            return R.anim.anim_marble_down;
        else
            return R.anim.anim_marble_stay;
    }

    public void setImageViews(Activity activity, ImageView avatarImg, ImageView marbleImg)
    {
        m_avatarImg = avatarImg;
        m_marbleImg = marbleImg;
        updateAvatar(activity);
    }

    public void setActive(Activity activity)
    {
        TITFLTownElement location = currentLocation();
        if (location == null)
        {
            location = m_home;
            setLocation(m_home);
        }
        goTo(activity, location, false);
    }

    public String outfitImage(int frameNum)
    {
        String outfit = "";
        if (m_outfit == null)
        {
            outfit = "avatar_naked/";
        }
        else if (m_outfit.isCasualOutfit())
        {
            outfit = "avatar_casual/";
        }
        else if (m_outfit.isBusinessOutfit())
        {
            outfit = "avatar_business/";
        }
        else if (m_outfit.isDressOutfit())
        {
            outfit = "avatar_dress/";
        }

        switch (frameNum)
        {
        case 0:
            return outfit + m_avatar_frm_01;
        case 1:
            return outfit + m_avatar_frm_02;
        case 2:
            return outfit + m_avatar_frm_03;
        case 3:
            return outfit + m_avatar_frm_04;
        default:
            return outfit + m_avatar_frm_01;
        }
    }

    public int getAvatarWidth(Activity activity)
    {
        float factor = NoEtapUtility.getFactor(activity);
        int w = (int)(340 * factor);
        return w;
    }

    public int getAvatarHeight(Activity activity)
    {
        float factor = NoEtapUtility.getFactor(activity);
        int h = (int)(700 * factor);
        return h;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void updateAvatar(Activity activity)
    {
        //avatarImg.setBackgroundResource(R.drawable.frame_anim_empty);
        //AnimationDrawable avatarWalk = (AnimationDrawable)avatarImg.getBackground();
        AnimationDrawable avatarWalk = new AnimationDrawable(); 
        avatarWalk.setOneShot(false);

        int sdkVer = android.os.Build.VERSION.SDK_INT;
        if (sdkVer < 16)
            m_avatarImg.setBackgroundDrawable(avatarWalk);
        else
            m_avatarImg.setBackground(avatarWalk);

        if (avatarWalk.getNumberOfFrames() == 0)
        {
            int w = getAvatarWidth(activity);
            int h = getAvatarHeight(activity);
            Drawable d1 = NoEtapUtility.createDrawableFromAsset(activity, outfitImage(0), w, h);
            Drawable d2 = NoEtapUtility.createDrawableFromAsset(activity, outfitImage(1), w, h);
            Drawable d3 = NoEtapUtility.createDrawableFromAsset(activity, outfitImage(2), w, h);
            Drawable d4 = NoEtapUtility.createDrawableFromAsset(activity, outfitImage(3), w, h);
            avatarWalk.addFrame(d1, 200);
            avatarWalk.addFrame(d2, 200);
            avatarWalk.addFrame(d3, 200);
            avatarWalk.addFrame(d4, 200);
            m_avatarImg.setImageBitmap(null);
        }
    }

    private int avatarOffset(Activity activity)
    {
        boolean reverse = ((TITFLActivity) activity).settings().m_reverseLayout;
        if (reverse)
        {
            return TITFLPlayerView.getWidth(activity);
        }
        return 0;
    }

    public boolean goTo(final Activity activity, final TITFLTownElement destination, final boolean openDestination)
    {
        if (destination == null)
            return true;

        TITFLTownMapRouteFinder finder = new TITFLTownMapRouteFinder(m_currentLocation.town().townMap().nodes());
        @SuppressWarnings("unused")
        ArrayList<TITFLTownMapNode> route2 = finder.findRoute(m_currentLocation.node(), destination.node());
        ArrayList<TITFLTownMapNode> route1 = finder.findRoute(destination.node(), m_currentLocation.node());
        // route1 and route2 may be different. We can pick the shorter route if we want.

        if (route1.size() == 0)
            return true;
        
        float hour = (route1.size() - 1) * m_speedFactor;
        
        if (m_hour + hour > m_maxHour)
        {
            closeWeek();
            ((TITFLActivity) activity).setNextPlayer(this);
            return false;
        }
        
        m_hour += hour;

        setLocation(destination);

        if (m_avatarImg == null || m_marbleImg == null)
            return true;
        

        final ArrayList<TITFLTownMapNode> route = route1;

        updateAvatar(activity);
        final AnimationDrawable avatarWalk = (AnimationDrawable) m_avatarImg.getBackground();

        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.anim_marble_stay);
        anim.scaleCurrentDuration(m_speedFactor);
        anim.setAnimationListener(new AnimationListener() 
        {
            @Override
            public void onAnimationEnd(Animation arg0) 
            {
                m_counter++;
                
                if (m_counter == route.size())
                {                    
                    avatarWalk.stop();
                    m_counter = -2;
                    if (openDestination)
                    {
                        int delay = 10; // One of Masako's slow device doesn't work without delay.
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                destination.open();
                            }
                        }, delay);                        
                    }                        
                }
                else
                {
                    TITFLTownMapNode current = route.get(m_counter);
                    TITFLTownMapNode nextStop = (route.size() > m_counter + 1) ? route.get(m_counter + 1) : route.get(m_counter);

                    Animation anim = AnimationUtils.loadAnimation(activity, getAnim(current, nextStop));
                    anim.setAnimationListener(this);

                    int slot = route.get(m_counter).index();
                    Rect rect = destination.town().nodeToPosition(slot);
                    m_marbleImg.layout(rect.left + avatarOffset(activity), rect.top, rect.left + rect.width() + avatarOffset(activity), rect.top + rect.height());

                    anim.scaleCurrentDuration(m_speedFactor);
                    m_marbleImg.startAnimation(anim);
                }
            }
        
            @Override
            public void onAnimationRepeat(Animation arg0) 
            {
            }
            
            @Override
            public void onAnimationStart(Animation arg0) 
            {
                //ImageView avatarImg = (ImageView) activity.findViewById(R.id.imageView2);
                //Rect rectAvatar = m_currentLocation.town().avatarRect();
                //avatarImg.layout(rectAvatar.left, rectAvatar.top, rectAvatar.right, rectAvatar.bottom);
                if (m_counter == -1)
                {
                    int slot = route.get(0).index();
                    Rect rect = destination.town().nodeToPosition(slot);
                    m_marbleImg.layout(rect.left + avatarOffset(activity), rect.top, rect.left + rect.width() + avatarOffset(activity), rect.top + rect.height());
                }
            }
        });
        
        if (!openDestination)
            return true;

        avatarWalk.start();

        m_counter = -1;
        if (transportation() != null)
            m_marbleImg.setImageBitmap(NoEtapUtility.getBitmap(activity, TITFLActivity.pathGoods + transportation().id() + ".png"));
        m_marbleImg.startAnimation(anim);
        
        return true;
    }

    public void beginWeek(TITFLRandomEvent randomEvent)
    {
        Activity activity = currentLocation().town().activity();
        ArrayList<ListAdapterBeginWeek.BeginWeekItem> events = new ArrayList<ListAdapterBeginWeek.BeginWeekItem>();

        // This is header
        Bitmap bmBlank = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bg_white);
        events.add(new ListAdapterBeginWeek.BeginWeekItem(bmBlank, null, 0, 0, 0));
        
        // Process lottery
        processLottery(events);
        
        // Process foods
        processFood(events);
        
        // Process soon - expiration
        processSoonExpire(events);

        // Process other expiration
        ArrayList<TITFLBelonging> lost1 = processExpiration(events);
        
        // Process belongings
        ArrayList<TITFLBelonging> lost2 = processBelongingEvents(events);

        // Random event
        ArrayList<TITFLBelonging> lost3 = processRandomEvent(randomEvent, events);

        // Process lost belongings
        lost1.addAll(lost2);
        lost1.addAll(lost3);
        processLost(lost1);
                
        // Set bitmap if no bitmap is set
        Bitmap bmCheck = BitmapFactory.decodeResource(activity.getResources(), R.drawable.event_check);
        for (ListAdapterBeginWeek.BeginWeekItem x : events)
        {
            if (null == x.m_image)
                x.m_image = bmCheck;
        }
        
        // Display all events
        int week = currentLocation().town().currentWeek();
        String title = alias() + " Week " + Integer.toString(week);
        DialogBeginWeek dialog = new DialogBeginWeek(title, events, m_currentLocation.town().activity());
        dialog.show();
    }
    
    public void buyFree(TITFLGoods goods, int unit, int acquiredWeek)
    {
        TITFLBelonging belonging = new TITFLBelonging(goods, unit, acquiredWeek);
        m_belongings.add(belonging);
    }
    
    public void buy(TITFLGoods goods, int unit, int acquiredWeek)
    {
        if (!goods.isEligible(this))
            return;

        boolean success = false;
        if (m_cash >= goods.getPrice() * unit)
        {
            success = true;
            m_cash -= (goods.getPrice() * unit);
        }
        else if (goods.isHouse())
        {
            success = updateMortgageBalance(goods.getPrice());
        }
        else if (goods.isDegree())
        {
            success = updateStudentLoanBalance(goods.getPrice());
        }
        else
        {
            success = updateGeneralLoanBalance(goods.getPrice());
        }
        
        if (success)
        {
            TITFLBelonging belonging = new TITFLBelonging(goods, unit, acquiredWeek);
            m_belongings.add(belonging);
            addBenefit(goods, unit);
            setTransportation(goods);
            setHome(goods);
            setOutfit(goods);
        }
        else
        {
            if (m_currentLocation != null)
            {
                NoEtapUtility.showAlert(m_currentLocation.town().activity(), "Sorry", "You cannot afford.");
            }
        }
    }

    private void addBenefit(TITFLGoods goods, int unit)
    {
        float happiness = 0;
        int health = goods.affectOnHealth() * m_incre;
        happiness += goods.affectOnHappiness().goodlooking() * this.character().goodlooking();
        happiness += goods.affectOnHappiness().hardworking() * this.character().hardworking();
        happiness += goods.affectOnHappiness().intelligent() * this.character().intelligent();
        happiness += goods.affectOnHappiness().lucky() * this.character().lucky();
        happiness += goods.affectOnHappiness().physical() * this.character().physical();

        m_satisfaction.m_happiness += happiness;
        m_satisfaction.m_health += health;
    }
    
    public void pay(int amount)
    {
        m_cash -= amount;
    }

    public void sell(TITFLBelonging belonging, int unit)
    {
        for (TITFLBelonging b : m_belongings)            
        {
            if (b == belonging)
            {
                int remainingUnit = b.unit() - unit;
                if (remainingUnit <= 0)
                    m_belongings.remove(b);
                else
                    b.setUnit(remainingUnit);
                m_cash += (b.goodsRef().getPrice() * unit);
                return;
            }
        }
    }
    
    public void makePayment(TITFLBelonging belonging, int payment)
    {
        int balance = belonging.loanAmount() - belonging.completedPayment();
        if (payment > balance)
            payment = balance;
        if (payment > cash())
            payment = Math.max(0, cash());
        
        if (payment == 0)
        {
            if (balance > 0)
                NoEtapUtility.showAlert(currentLocation().town().activity(), "Sorry", "You have no money to make payment.");
        }
        else
        {
            belonging.addPayment(payment);
            pay(payment);
            balance = belonging.loanAmount() - belonging.completedPayment();
        }
        
        if (balance == 0)
        {
            m_belongings.remove(belonging);
            NoEtapUtility.showAlert(currentLocation().town().activity(), "Congrats", "You paid it off!");
        }
    }
    
    public int beg()
    {
        float added = addHour();
        if (added == 0)
        {
            return 0;
        }
        if (m_cash >= 100)
        {
            return 0;
        }

        Random random = new Random();
        int amount = (int)(random.nextFloat() * 100);
        m_cash += amount;
        m_satisfaction.m_life = 0;
        return amount;
    }

    private void showNoTimeMessage(String action)
    {
        NoEtapUtility.showAlert(m_currentLocation.town().activity(), "Sorry", "No enough time to " + action + ". Close and try next week again.");
    }

    private int outfitCode()
    {
        if (m_outfit == null)
            return 0;
        else if (m_outfit.isCasualOutfit())
            return 1;
        else if (m_outfit.isBusinessOutfit())
            return 2;
        else if (m_outfit.isDressOutfit())
            return 3;
        else
            return 0;
    }

    public void work()
    {
        if (m_job == null)
        {
            return;
        }

        if (m_lastWorkedWeek + 2 < m_currentLocation.town().currentWeek())
        {
            NoEtapUtility.showAlert(m_currentLocation.town().activity(), "You are fired", "I'm sorry, but you are fired since you didn't come to work regulary.");
            m_job = null;
            m_satisfaction.m_career -= 10; // It hurts your career.
            return;
        }
        
        if (outfitCode() < m_job.dressCode())
        {
            NoEtapUtility.showAlert(m_currentLocation.town().activity(), "Info", "You are not properly dressed. Buy new cloth or go home and change your cloth.");
            return;
        }

        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Work");
            return;
        }

        m_lastWorkedWeek = m_currentLocation.town().currentWeek();
        int wage = (int)(added * m_job.getWage());
        int care = (int)(added * m_incre);
        int happ = m_character.isHardworking() ? 1 : 0;
        float educFactor = added * 1;
        float expeFactor = added * 0.01f;

        m_cash += wage;

        m_satisfaction.m_career += care;
        m_satisfaction.m_happiness += happ;
        m_experience.add(m_job.requiredEducation(), educFactor);
        m_experience.add(m_job.requiredExperience(), expeFactor);
        m_experience.m_basic++;
    }
    
    public boolean isEmployer(TITFLTownElement element)
    {
        if (m_job == null)
            return false;
        return (m_job.townelement() == element);
    }

    public void relax()
    {
        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Relax");
            return;
        }

        int life = (int)(added * m_incre);
        int happ = m_character.isLucky() ? 1 : 0;

        m_satisfaction.m_life += life;
        m_satisfaction.m_happiness += happ;
    }

    public void study(TITFLBelonging degree)
    {
        if (degree.completedCredit() >= degree.goodsRef().classCredit())
        {
            NoEtapUtility.showAlert(m_currentLocation.town().activity(), "Info", "You already acquired the selected degree.");
            return;
        }
        
        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Study");
            return;
        }
        
        int educ = (int)(added * m_incre);
        int happ = m_character.isIntelligent() ? 1 : 0;
        int cred = (int)(added * 1);

        if (cred < 1)
        {
            ; // Not enough to earn credit
        }
        else
        {
            if (degree.goodsRef().isDegreeBasic())
                m_education.m_basic++;
            else if (degree.goodsRef().isDegreeEngineering())
                m_education.m_engineering++;
            else if (degree.goodsRef().isDegreeBusiness())
                m_education.m_business_finance++;
            else if (degree.goodsRef().isDegreeAcademic())
                m_education.m_academic++;

            degree.addCredit(1);
        }

        m_satisfaction.m_education += educ;
        m_satisfaction.m_happiness += happ;
    }

    public void exercise()
    {
        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Exercise");
            return;
        }

        int heal = (int)(added * m_incre);
        int happ = m_character.isPhysical() ? 1 : 0;

        m_satisfaction.m_health += heal;
        m_satisfaction.m_happiness += happ;
    }

    public void socialize()
    {
        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Socialize");
            return;
        }

        int life = (int)(added * m_incre);
        int happ = m_character.isGoodlooking() ? 1 : 0;

        m_satisfaction.m_life +=life;
        m_satisfaction.m_happiness += happ;
    }

    public void applyJob(TITFLJob job)
    {
        float added = addHour();
        if (added == 0)
        {
            showNoTimeMessage("Interview");
            return;
        }

        if (added < 1)
        {
            NoEtapUtility.showAlert(m_currentLocation.town().activity(), "Sorry", "Interview didn't go well!");
            return;
        }

        if (job.accept(this))
        {
            m_job = job;
            m_lastWorkedWeek = m_currentLocation.town().currentWeek();
        }
    }

    public void withdraw(int amount)
    {
        m_saving -= amount;
        m_cash += amount;
    }

    public void deposit(int amount)
    {
        m_saving += amount;
        m_cash -= amount;
    }

    public boolean isMoving()
    {
        if (m_counter <= -2)
            return false;
        else
            return true;            
    }

    public boolean isWinner(TITFL.Satisfaction goal)
    {
        if (goal.m_career > m_satisfaction.m_career)
            return false;
        if (goal.m_education > m_satisfaction.m_education)
            return false;
        if (goal.m_happiness > m_satisfaction.m_happiness)
            return false;
        if (goal.m_health > m_satisfaction.m_health)
            return false;
        if (goal.m_life > m_satisfaction.m_life)
            return false;
        if (goal.m_wealth > getWealthLevel())
            return false;
        return true;
    }
    
    public float getWealthLevel()
    {
        int wealth = 0;
        wealth += m_cash;
        wealth += m_saving;
        for (TITFLBelonging x : m_belongings)
        {
            TITFLTownElement townelement = x.goodsRef().townelement();
            if (x.goodsRef().isLoan())
                wealth -= x.loanAmount();
            else if (townelement != null && townelement.isBank())
                wealth+= x.goodsRef().getPrice();
            else if (x.goodsRef().isHouse())
                wealth += x.goodsRef().getPrice();
            else if (x.goodsRef().isTransportation())
                wealth += (x.goodsRef().getPrice() / 2); // Car's cash value is only 50%.
        }
        return wealth;
    }

    public float getEducationLevel()
    {
        return m_satisfaction.m_education;
    }

    public float getCareerLevel()
    {
        return m_satisfaction.m_career;
    }

    public float getLifeLevel()
    {
        return m_satisfaction.m_life;
    }

    public float getHealthLevel()
    {
        return m_satisfaction.m_health;
    }

    public float getHappinessLevel()
    {
        return m_satisfaction.m_happiness;
    }

    public int getWealthPercent(int goal)
    {
        if (goal == 0 || goal < getWealthLevel())
            return 100;
        else
            return (int)(100 * getWealthLevel()) / goal;
    }

    public int getEducationPercent(int goal)
    {
        if (goal == 0 || goal < getEducationLevel())
            return 100;
        else
            return (int)(100 * getEducationLevel()) / goal;
    }

    public int getCareerPercent(int goal)
    {
        if (goal == 0 || goal < getCareerLevel())
            return 100;
        else
            return (int)(100 * getCareerLevel()) / goal;
    }

    public int getLifePercent(int goal)
    {
        if (goal == 0 || goal < getLifeLevel())
            return 100;
        else
            return (int)(100 * getLifeLevel()) / goal;
    }

    public int getHealthPercent(int goal)
    {
        if (goal == 0 || goal < getHealthLevel())
            return 100;
        else
            return (int)(100 * getHealthLevel()) / goal;
    }

    public int getHappinessPercent(int goal)
    {
        if (goal == 0 || goal < getHappinessLevel())
            return 100;
        else
            return (int)(100 * getHappinessLevel()) / goal;
    }

    public int themeColor()
    {
        return m_themeColor;
    }

    public int themeColorLight()
    {
        int red = Math.min(255, (int)(Color.red(m_themeColor) * 1.1));
        int green = Math.min(255, (int)(Color.green(m_themeColor) * 1.1));
        int blue = Math.min(255, (int)(Color.blue(m_themeColor) * 1.1));        
        return Color.argb(255, red, green, blue);
    }

    public boolean hasRefrigerator()
    {
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isRefrigerator())
                return true;
        }
        return false;
    }
    
    public boolean hasFreezer()
    {
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isFreezer())
                return true;
        }
        return false;
    }
    
    public boolean hasTatoo()
    {
        return false;
    }

    public boolean hasSpouse()
    {
        return false;
    }
    
    public boolean hasHealthInsurance()
    {
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isHealthInsurance())
                return true;
        }
        return false;
    }

    public boolean hasCarInsurance()
    {
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().isCarInsurance())
                return true;
        }
        return false;
    }

    public TITFLBelonging findBelonging(String goods)
    {
        if (goods == null)
            return null;
        
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().id().equals(goods))
                return g;
        }
        return null;
    }
    
    public int countBelongings(String goods)
    {
        int count = 0;
        for (TITFLBelonging g : m_belongings)
        {
            if (g.goodsRef().id().equals(goods))
                count++;
        }
        return count;
    }

    private float addHour()
    {
        if (m_hour >= m_maxHour)
            return 0;

        float add = 1;
        if (m_hour + 1 >= m_maxHour)
            add = m_maxHour - m_hour;

        m_hour += add;
        return add;
    }
    
    public float addHours(float hours)
    {
        if (m_hour + hours > m_maxHour)
            return 0;

        m_hour += hours;
        return hours;
    }

    private boolean updateMortgageBalance(int price)
    {
        for (TITFLBelonging b : m_belongings)
        {
            if (b.goodsRef().isMortgage() && b.loanAmount() == 0)
            {
                b.setLoanAmount(price);
                return true;
            }
        }
        return false;
    }
 
    private boolean updateStudentLoanBalance(int price)
    {
        for (TITFLBelonging b : m_belongings)
        {
            if (b.goodsRef().isStudentLoan() && b.loanAmount() == 0)
            {
                b.setLoanAmount(price);
                return true;
            }
        }
        return false;
    }

    private boolean updateGeneralLoanBalance(int price)
    {
        for (TITFLBelonging b : m_belongings)
        {
            if (b.goodsRef().isGeneralLoan() && b.loanAmount() == 0)
            {
                b.setLoanAmount(price);
                return true;
            }
        }
        return false;
    }

    public ArrayList<TITFLItem> getDegrees()
    {
        ArrayList<TITFLItem> degrees = new ArrayList<TITFLItem>();
        for (TITFLBelonging degree : m_belongings)            
        {
            if (degree.goodsRef().isDegree())
                degrees.add(0, degree);
        }
        return degrees;
    }

    public void setTransportation(TITFLGoods goods)
    {
        if (goods.isTransportation())
        {
            m_speedFactor = (float)(2.0 - 0.25 * goods.speed() / 10);
            m_transportation = goods;
        }
    }
    
    private void setHome(TITFLGoods goods)
    {
        TITFLTownElement element = goods.convertToTownElement();
        if (element != null)
        {
            // Remove old apartment
            if (m_home != null && m_home.isApartment())
            {
                for (TITFLBelonging b : m_belongings)
                {
                    if (b.goodsRef().isApartment() && goods != b.goodsRef())
                    {
                        m_belongings.remove(b);
                        break;
                    }
                }
            }

            m_home = element;
        }
    }
    
    public void setOutfit(TITFLGoods goods)
    {
        if (goods.isOutfit())
        {
            m_outfit = goods;
        }
    }

    private boolean hasFood()
    {
        for (TITFLBelonging x : m_belongings)
        {
            if (x.goodsRef().foodValue() > 0)
                return true;
        }
        return false;
    }

    private ArrayList<TITFLBelonging> getSoonExpire()
    {
        ArrayList<TITFLBelonging> soonExpire = new ArrayList<TITFLBelonging>();
        for (TITFLBelonging x : m_belongings)
        {
            int weeks = m_currentLocation.town().currentWeek() - x.acquiredWeek();
            if (weeks + 1 == x.goodsRef().expire())
            {
                soonExpire.add(x);
            }
        }
        return soonExpire;
    }
    
    private ArrayList<TITFLBelonging> processExpiration(ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        Activity activity = m_currentLocation.town().activity();
        ArrayList<TITFLBelonging> expired = new ArrayList<TITFLBelonging>();
        for (TITFLBelonging x : m_belongings)
        {
            if (x.goodsRef().expire() > 0)
            {
                int weeks = m_currentLocation.town().currentWeek() - x.acquiredWeek();
                if (weeks >= x.goodsRef().expire())
                {
                    expired.add(x);
                    Bitmap bm = NoEtapUtility.getBitmap(activity, TITFLActivity.pathGoods + x.goodsRef().id() + ".png");
                    String message = "";
                    if (x.goodsRef().losing() != null)
                    {
                        message = x.goodsRef().losing();
                    }
                    if (message.length() == 0)
                    {            
                        message = "Expired: " + x.goodsRef().name();
                    }
                    events.add(new ListAdapterBeginWeek.BeginWeekItem(bm, message, 0, 0, 0));
                }
            }
        }        
        m_belongings.removeAll(expired);
        return expired;
    }
    
    private ArrayList<TITFLBelonging> processRandomEvent(TITFLRandomEvent randomEvent, ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        int week = currentLocation().town().currentWeek();

        ArrayList<TITFLBelonging> lost = new ArrayList<TITFLBelonging>();
        if (week > 1)
        {
            TITFLBelonging losing = findBelonging(randomEvent.losing_goods());
            ListAdapterBeginWeek.BeginWeekItem event = new ListAdapterBeginWeek.BeginWeekItem(null, "", 0, 0, 0);
            if (randomEvent.process(this, event))
            {
                events.add(event);
                if (losing != null)
                    lost.add(losing);                
            }
        }
        
        return lost;
    }
    
    private void processLost(ArrayList<TITFLBelonging> lost)
    {
        boolean lostTransportation = false;
        boolean lostOutfit = false;
        for (TITFLBelonging x : lost)
        {
            if (x.goodsRef() == m_transportation)
            {
                lostTransportation = true;
            }
            else if (x.goodsRef() == m_outfit)
            {
                lostOutfit = true;
            }
        }

        // Reset transportation
        if (lostTransportation)
        {
            for (TITFLBelonging x : m_belongings)
            {
                if (x.goodsRef().isTransportation())
                {
                    setTransportation(x.goodsRef());
                    break;
                }
            }
        }
    
        // Reset outfit
        if (lostOutfit)
        {
            m_outfit = null;
            for (TITFLBelonging x : m_belongings)
            {
                if (x.goodsRef().isOutfit())
                {
                    setOutfit(x.goodsRef());
                    break;
                }
            }
        }
    }
    
    private ArrayList<TITFLBelonging> processBelongingEvents(ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        Activity activity = m_currentLocation.town().activity();
        int week = currentLocation().town().currentWeek();

        ArrayList<TITFLBelonging> lost = new ArrayList<TITFLBelonging>();
        for (TITFLBelonging x : m_belongings)
        {
            Bitmap bm = NoEtapUtility.getBitmap(activity, TITFLActivity.pathGoods + x.goodsRef().id() + ".png");
            if (bm == null)
                bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.goods_sample);
            boolean eventFail = false;
            for (TITFLBelongingEvent e : x.events())
            {
                if (e.isDue(week))
                {
                    ListAdapterBeginWeek.BeginWeekItem event = new ListAdapterBeginWeek.BeginWeekItem(bm, x.goodsRef().name(), 0, 0, 0);
                    if (e.process(this, event))
                    {
                        events.add(event);
                    }
                    else
                    {
                        //events.add(event); // We probably don't have to explain the reason...
                        eventFail = true;
                    }
                }
            }
            if (eventFail)
            {
                lost.add(x);
                String message = "";
                if (x.goodsRef().losing() != null)
                {
                    message += x.goodsRef().losing();
                }
                if (message.length() == 0)
                {
                    message = "You've just lost ";
                    message += x.goodsRef().name();
                }
                events.add(new ListAdapterBeginWeek.BeginWeekItem(bm, message, 0, 0, 0));
            }
        }
        m_belongings.removeAll(lost);
        return lost;
    }
    
    private ArrayList<TITFLBelonging> consumeFood()
    {
        ArrayList<TITFLBelonging> consumed = new ArrayList<TITFLBelonging>();
        for (TITFLBelonging x : m_belongings)
        {
            if (x.goodsRef().foodValue() > 0)
            {
                int weeks = m_currentLocation.town().currentWeek() - x.acquiredWeek();
                if (weeks >= x.goodsRef().foodValue())
                {
                    consumed.add(x);
                }
                else
                {
                    // Remaining food may go bad if player doesn't have freezer/refrigerator
                    if (x.goodsRef().foodValue() > 2)
                    {
                        if (hasFreezer())
                            ;
                        else
                            consumed.add(x);
                    }
                    else if (x.goodsRef().foodValue() > 1)
                    {
                        if (hasRefrigerator() || hasFreezer())
                            ;
                        else
                            consumed.add(x);
                    }
                }
            }
        }
        m_belongings.removeAll(consumed);
        return consumed;
    }
    
    private void processFood(ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        Activity activity = currentLocation().town().activity();
        if (!hasFood())
        {
            m_hour += 4;
            m_satisfaction.m_health -= 4;
            events.add(new ListAdapterBeginWeek.BeginWeekItem(null, "No Food, less hour, less health :-(", 0, 4, -4));
        }
        else
        {
            ArrayList<TITFLBelonging> consumed = consumeFood();
            for (TITFLBelonging x : consumed)
            {
                Bitmap bm = NoEtapUtility.getBitmap(activity, TITFLActivity.pathGoods + x.goodsRef().id() + ".png");
                String message = "You've just finished: " + x.goodsRef().name();
                events.add(new ListAdapterBeginWeek.BeginWeekItem(bm, message, 0, 0, 0));
            }
        }
    }
    
    private void processSoonExpire(ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        Activity activity = currentLocation().town().activity();
        ArrayList<TITFLBelonging> soonExpire = getSoonExpire();
        for (TITFLBelonging x : soonExpire)
        {
            Bitmap bm = NoEtapUtility.getBitmap(activity, TITFLActivity.pathGoods + x.goodsRef().id() + ".png");
            String message = "Get new: " + x.goodsRef().name();
            events.add(new ListAdapterBeginWeek.BeginWeekItem(bm, message, 0, 0, 0));
        }
    }

    private void processLottery(ArrayList<ListAdapterBeginWeek.BeginWeekItem> events)
    {
        for (TITFLBelonging x : belongings())
        {
            if (x.goodsRef().isLottery())
            {
                Random random = new Random();
                int winningNumber = (int)(random.nextFloat() * 1000);
                boolean won = (winningNumber < x.unit());
                if (won)
                {
                    String message = "You won the lottery!";
                    events.add(new ListAdapterBeginWeek.BeginWeekItem(null, message, -5000, 0, 0));
                    m_cash += 5000;
                }
                else
                {
                    String message = "You didnt win the lottery...";
                    events.add(new ListAdapterBeginWeek.BeginWeekItem(null, message, 0, 0, 0));
                }
                belongings().remove(x);
            }
        }
    }
}
