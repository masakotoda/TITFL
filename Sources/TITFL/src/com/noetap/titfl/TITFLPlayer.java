package com.noetap.titfl;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private final float m_maxHour = 24;
    
    private String m_name;
    private String m_alias;

    private TITFL.CharacterFactor m_character;
    private TITFL.DisciplineLevel m_education;
    private TITFL.DisciplineLevel m_experience;
    private TITFL.Satisfaction  m_satisfaction;
    private int m_happiness;
    
    private int m_cash;
    private int m_saving;
    private int m_general_loan;
    private int m_student_loan;
    private int m_mortgage;
    private int m_goldUnit;
    private int m_bondUnit;
    private int m_stockUnit;
    private ArrayList<TITFLBelonging> m_belongings;
        
    private TITFLJob m_job;
    private int m_lastWorkedWeek;
    private TITFLGoods m_transportation;    
    private TITFLTownElement m_currentLocation;
    private TITFLTownElement m_home;
    
    private int m_counter = -1;
    private float m_speedFactor = 0.5f; // 1 is default. 0.5 is x2 faster. 2 is x2 slower.
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
    private static String atr_avatar_frm_01 = "avatar_frm_01";
    private static String atr_avatar_frm_02 = "avatar_frm_02";
    private static String atr_avatar_frm_03 = "avatar_frm_03";
    private static String atr_avatar_frm_04 = "avatar_frm_04";
    private static String atr_theme_color_r = "theme_color_r";
    private static String atr_theme_color_g = "theme_color_g";
    private static String atr_theme_color_b = "theme_color_b";
    private static String atr_current_location = "current_location";
    private static String atr_counter = "counter";
    private static String atr_speed_factor = "speed_factor";
    private static String atr_hour = "hour";
    
    public TITFLPlayer()
    {
        m_belongings = new ArrayList<TITFLBelonging>();
        m_character = new TITFL.CharacterFactor();
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
    
    public int happiness()
    {
        return m_happiness;    
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
        return m_general_loan;
    }

    public int studentLoan()
    {
        return m_student_loan;
    }
    
    public int mortgage()
    {
        return m_mortgage;
    }
    
    public int goldUnit()
    {
        return m_goldUnit;
    }
    
    public int bondUnit()
    {
        return m_bondUnit;
    }
    
    public int stockUnit()
    {
        return m_stockUnit;
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
    
    public TITFLTownElement currentLocation()
    {
        return m_currentLocation;
    }
    
    public TITFLTownElement home()
    {
        return m_home;   
    }
    
    public int counter()
    {
        return m_counter;
    }
    
    public float speedFactor()
    {
        return m_speedFactor;
    }
    
    public float hour()
    {
        return m_hour;
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

                player = deserializeProperties(parser, null);
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
            
            serializer.attribute(ns, atr_name, m_name);
            serializer.attribute(ns, atr_alias, m_alias);
            serializer.attribute(ns, atr_factor_intelligent, Integer.toString(m_character.m_intelligent));
            serializer.attribute(ns, atr_factor_hardworking, Integer.toString(m_character.m_hardWorking));
            serializer.attribute(ns, atr_factor_goodlooking, Integer.toString(m_character.m_goodLooking));
            serializer.attribute(ns, atr_factor_physical, Integer.toString(m_character.m_physical));
            serializer.attribute(ns, atr_factor_lucky, Integer.toString(m_character.m_lucky));
            serializer.attribute(ns, atr_current_location, currentLocationId);
            serializer.attribute(ns, atr_speed_factor, Float.toString(m_speedFactor));
            serializer.attribute(ns, atr_counter, Integer.toString(m_counter));
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
        new TITFLPlayer();
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
            else if (attribName.equals(atr_counter))
                player.m_counter = Integer.parseInt(attribValue);
            else if (attribName.equals(atr_speed_factor))
                player.m_speedFactor = Float.parseFloat(attribValue);
            else if (attribName.equals(atr_hour))
                player.m_hour = Float.parseFloat(attribValue);
            else if (attribName.equals(atr_current_location))
            {
                if (town != null)
                {
                    player.m_currentLocation = town.findElement(attribValue);
                }
            }
        }
        
        player.m_themeColor = Color.rgb(r, g, b);
        
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
    
    public void draw(Canvas canvas, Paint paint)
    {
        if (m_currentLocation == null)
            return;

        // Change theme color depending on the character?
        paint.setColor(m_themeColor);

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        canvas.drawRect(0, 0, w, h, paint);

        float factor = NoEtapUtility.getFactor(m_currentLocation.town().activity());
        float textSize = 48 * factor;

        paint.setARGB(255, 0, 0, 0);
        paint.setTextSize(textSize);

        float top = 0;
        float left = textSize;
        
        top += textSize;
        canvas.drawText(Float.toString(m_hour) + " " + this.m_alias, left, top, paint);
        top += textSize;
        
        left += w / 2;
        top += textSize;
        textSize = 32 * factor;
        paint.setTextSize(textSize);

        top += textSize;
        canvas.drawText("Wealth: " + Float.toString(getWealthLevel()), left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Education: " + Float.toString(getEducationLevel()), left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Carrier: " + Float.toString(getCarrierLevel()), left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Life: " + Float.toString(getLifeLevel()), left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Health: " + Float.toString(getHealthLevel()), left, top, paint);
        top += textSize;

        top += textSize;
        canvas.drawText("Happiness: " + Float.toString(getHappinessLevel()), left, top, paint);
        top += textSize;
        
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
    
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void notifyActive(Activity activity)
    {
        ImageView avatarImg = (ImageView) activity.findViewById(R.id.imageView2);
        //avatarImg.setBackgroundResource(R.drawable.frame_anim_empty);
        //AnimationDrawable avatarWalk = (AnimationDrawable)avatarImg.getBackground();
        AnimationDrawable avatarWalk = new AnimationDrawable(); 
        avatarWalk.setOneShot(false);

        int sdkVer = android.os.Build.VERSION.SDK_INT;
        if (sdkVer < 16)
            avatarImg.setBackgroundDrawable(avatarWalk);
        else
            avatarImg.setBackground(avatarWalk);

        if (avatarWalk.getNumberOfFrames() == 0)
        {
            float factor = NoEtapUtility.getFactor(activity);
            int w = (int)(360 * factor);
            int h = (int)(700 * factor);
            Drawable d1 = NoEtapUtility.createDrawableFromAsset(activity, m_avatar_frm_01, w, h);
            Drawable d2 = NoEtapUtility.createDrawableFromAsset(activity, m_avatar_frm_02, w, h);
            Drawable d3 = NoEtapUtility.createDrawableFromAsset(activity, m_avatar_frm_03, w, h);
            Drawable d4 = NoEtapUtility.createDrawableFromAsset(activity, m_avatar_frm_04, w, h);
            avatarWalk.addFrame(d1, 200);
            avatarWalk.addFrame(d2, 200);
            avatarWalk.addFrame(d3, 200);
            avatarWalk.addFrame(d4, 200);
            avatarImg.setImageBitmap(null);
        }
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
            m_hour = 0;
            m_currentLocation.setVisitor(null);
            m_currentLocation.town().setNextPlayer();
            NoEtapUtility.showAlert(activity, "Info", "User switch.");
            return false;
        }
        
        m_hour += hour;

        setLocation(destination);
        
        final ArrayList<TITFLTownMapNode> route = route1;

        final ImageView avatarImg = (ImageView) activity.findViewById(R.id.imageView2);
        notifyActive(activity);
        final AnimationDrawable avatarWalk = (AnimationDrawable) avatarImg.getBackground();

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
                    m_counter = -1;
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
                    ImageView marbleImg = (ImageView) activity.findViewById(R.id.imageView1);

                    TITFLTownMapNode current = route.get(m_counter);
                    TITFLTownMapNode nextStop = (route.size() > m_counter + 1) ? route.get(m_counter + 1) : route.get(m_counter);

                    Animation anim = AnimationUtils.loadAnimation(activity, getAnim(current, nextStop));
                    anim.setAnimationListener(this);

                    int slot = route.get(m_counter).index();
                    Rect rect = destination.town().nodeToPosition(slot);
                    marbleImg.layout(rect.left, rect.top, rect.left + rect.width(), rect.top + rect.height());

                    anim.scaleCurrentDuration(m_speedFactor);
                    marbleImg.startAnimation(anim);
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
                    ImageView marbleImg = (ImageView) activity.findViewById(R.id.imageView1);
                    int slot = route.get(0).index();
                    Rect rect = destination.town().nodeToPosition(slot);
                    marbleImg.layout(rect.left, rect.top, rect.left + rect.width(), rect.top + rect.height());
                }
            }
        });
        
        avatarWalk.start();

        m_counter = -1;
        ImageView marbleImg = (ImageView) activity.findViewById(R.id.imageView1);
        marbleImg.startAnimation(anim);
        
        return true;
    }
    
    public void beginWeek()
    {        
        //TODO
    }
    
    public void work()
    {
        //TODO
        m_hour++;
    }

    public void buy(TITFLGoods goods, int acquiredWeek)
    {
        TITFLBelonging belonging = new TITFLBelonging(goods, acquiredWeek);
        m_cash -= goods.price();
        m_belongings.add(belonging);
    }

    public void sell(TITFLGoods goods)
    {
        //TODO
    }

    public void relax()
    {
        //TODO
    }

    public void study()
    {
        //TODO
    }

    public void exercise()
    {
        //TODO
    }

    public void socialize()
    {
        //TODO
    }

    public void applyJob(TITFLJob job)
    {
        //TODO
    }

    public void withdraw(int amount)
    {
        //TODO
    }

    public void deposit(int amount)
    {
        //TODO
    }

    public void loan(int loanType, int amount)
    {
        //TODO
    }

    public void setHome(TITFLTownElement home)
    {
        //TODO
    }

    public boolean isMoving()
    {
        if (m_counter < 0)
            return false;
        else
            return true;            
    }
    
    private float getWealthLevel()
    {
        return 0;
    }

    private float getEducationLevel()
    {
        return 0;
    }

    private float getCarrierLevel()
    {
        return 0;
    }

    private float getLifeLevel()
    {
        return 0;
    }

    private float getHealthLevel()
    {
        return 0;
    }

    private float getHappinessLevel()
    {
        return 0;
    }

    public boolean hasRefrigerator()
    {
        return false;
    }
    
    public boolean hasFreezer()
    {
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
}
