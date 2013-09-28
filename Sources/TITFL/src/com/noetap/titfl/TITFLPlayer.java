package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
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
import android.view.View;
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
	
	private int m_cash;
	private int m_saving;
	private int m_general_loan;
	private int m_student_loan;
	private int m_mortgage;
	private int m_goldUnit;
	private int m_bondUnit;
	private int m_stockUnit;
	private ArrayList<TITFLGoods> m_belongings;
		
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
	
	private static String tag_root = "TITFL";
	private static String tag_item = "player";
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
	
	public static ArrayList<TITFLPlayer> loadDefaultPlayers(AssetManager am)
	{
		ArrayList<TITFLPlayer> ret = new ArrayList<TITFLPlayer>();		
		try
		{
			String ns = "";
			InputStream in = am.open("default_player.xml");
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

		    	TITFLPlayer element = new TITFLPlayer();
		    	int r = 0, g = 0, b = 0;
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_name))
		    			element.m_name = attribValue;
		    		else if (attribName.equals(atr_alias))
		    			element.m_alias = attribValue;
		    		else if (attribName.equals(atr_factor_intelligent))
		    			element.m_character.m_intelligent = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_hardworking))
		    			element.m_character.m_hardWorking = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_goodlooking))
		    			element.m_character.m_goodLooking = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_physical))
		    			element.m_character.m_physical = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_lucky))
		    			element.m_character.m_lucky = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_avatar_frm_01))
		    			element.m_avatar_frm_01 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_02))
		    			element.m_avatar_frm_02 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_03))
		    			element.m_avatar_frm_03 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_04))
		    			element.m_avatar_frm_04 = attribValue;
		    		else if (attribName.equals(atr_theme_color_r))
		    			r = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_theme_color_g))
		    			g = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_theme_color_b))
		    			b = Integer.parseInt(attribValue);
		    	}
		    	
		    	element.m_themeColor = Color.rgb(r, g, b);
		    	ret.add(element);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}

		return ret;		
	}
	
	// Default constructor
	public TITFLPlayer()
	{
		m_belongings = new ArrayList<TITFLGoods>();
		m_character = new TITFL.CharacterFactor();
	}
	
	// Copy constructor
	public TITFLPlayer(TITFLPlayer other)
	{
		m_name = other.m_name;
		m_alias = other.m_alias;
		m_character = new TITFL.CharacterFactor(other.m_character);
		m_currentLocation = other.m_currentLocation;
		m_counter = other.m_counter;
		m_speedFactor = other.m_speedFactor;
		m_hour = other.m_hour;
		m_avatar_frm_01 = other.m_avatar_frm_01;
		m_avatar_frm_02 = other.m_avatar_frm_02;
		m_avatar_frm_03 = other.m_avatar_frm_03;
		m_avatar_frm_04 = other.m_avatar_frm_04;
		m_themeColor = other.m_themeColor;

		m_belongings = new ArrayList<TITFLGoods>();
		Iterator<TITFLGoods> it = other.m_belongings.iterator();
		while (it.hasNext())
		{
			m_belongings.add(it.next());
		}
	}
	
	public void setLocation(TITFLTownElement destination)
	{
		if (m_currentLocation != null)
			m_currentLocation.setVisitor(null);
		m_currentLocation = destination;
		destination.setVisitor(this);
	}
	
	public TITFLTownElement getLocation()
	{
		return m_currentLocation;
	}
	
	public float hour()
	{
		return m_hour;
	}
	
	public String name()
	{
		return m_name;
	}
	
	public boolean save(String key, SQLiteDatabase db)
	{
		String currentLocationId = "";
		if (m_currentLocation != null)
			currentLocationId = m_currentLocation.id();
		
		TITFL.save(key + ".name", m_name, db);
		TITFL.save(key + ".alias", m_alias, db);
		TITFL.save(key + ".factor_intelligent", m_character.m_intelligent, db);
		TITFL.save(key + ".factor_hardworking", m_character.m_hardWorking, db);
		TITFL.save(key + ".factor_goodlooking", m_character.m_goodLooking, db);
		TITFL.save(key + ".factor_physical", m_character.m_physical, db);
		TITFL.save(key + ".factor_lucky", m_character.m_lucky, db);
		TITFL.save(key + ".currentLocation", currentLocationId, db);
		TITFL.save(key + ".counter", m_counter, db);
		TITFL.save(key + ".speedFactor", m_speedFactor, db);
		TITFL.save(key + ".hour", m_hour, db);
		TITFL.save(key + ".avatar_frm_01", m_avatar_frm_01, db);
		TITFL.save(key + ".avatar_frm_02", m_avatar_frm_02, db);
		TITFL.save(key + ".avatar_frm_03", m_avatar_frm_03, db);
		TITFL.save(key + ".avatar_frm_04", m_avatar_frm_04, db);
		
		//TODO save m_belongings;
		return true;
	}
	
	public static TITFLPlayer loadPlayer(String key, SQLiteDatabase db, TITFLTown town)	
	{
		TITFLPlayer player = new TITFLPlayer();

		player.m_name = TITFL.loadString(key + ".name", db);
		player.m_alias = TITFL.loadString(key + ".alias", db);
		player.m_character.m_intelligent = TITFL.loadInteger(key + ".factor_intelligent", db);
		player.m_character.m_hardWorking = TITFL.loadInteger(key + ".factor_hardworking", db);
		player.m_character.m_goodLooking = TITFL.loadInteger(key + ".factor_goodlooking", db);
		player.m_character.m_physical = TITFL.loadInteger(key + ".factor_physical", db);
		player.m_character.m_lucky = TITFL.loadInteger(key + ".factor_lucky", db);
		String currentLocationId = TITFL.loadString(key + ".currentLocation", db);
		player.m_currentLocation = town.findElement(currentLocationId);
		player.m_counter = TITFL.loadInteger(key + ".counter", db);
		player.m_speedFactor = TITFL.loadFloat(key + ".speedFactor", db);
		player.m_hour = TITFL.loadFloat(key + ".hour", db);
		player.m_avatar_frm_01 = TITFL.loadString(key + ".avatar_frm_01", db);
		player.m_avatar_frm_02 = TITFL.loadString(key + ".avatar_frm_02", db);
		player.m_avatar_frm_03 = TITFL.loadString(key + ".avatar_frm_03", db);
		player.m_avatar_frm_04 = TITFL.loadString(key + ".avatar_frm_04", db);

		player.m_belongings = new ArrayList<TITFLGoods>();
		//TODO load m_belongings
		return player;
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
		ArrayList<TITFLTownMapNode> route2 = finder.findRoute(m_currentLocation.getNode(), destination.getNode());
		ArrayList<TITFLTownMapNode> route1 = finder.findRoute(destination.getNode(), m_currentLocation.getNode());
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
	}

	void buy(TITFLGoods goods)
	{
		//TODO
	}

	void sell(TITFLGoods goods)
	{
		//TODO
	}


	void relax()
	{
		//TODO
	}

	void study()
	{
		//TODO
	}

	void exercise()
	{
		//TODO
	}

	void socialize()
	{
		//TODO
	}

	void applyJob(TITFLJob job)
	{
		//TODO
	}

	void withdraw(int amount)
	{
		//TODO
	}

	void deposit(int amount)
	{
		//TODO
	}

	void loan(int loanType, int amount)
	{
		//TODO
	}

	void SetHome(TITFLTownElement home)
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
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	private float getEducationLevel()
	{
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	private float getCarrierLevel()
	{
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	private float getLifeLevel()
	{
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	private float getHealthLevel()
	{
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	private float getHappinessLevel()
	{
		//TODO - calc wealth level based on what the player owns.
		return 0;
	}

	boolean hasRefrigerator()
	{
		return false;
	}
	
	boolean hasFreezer()
	{
		return false;
	}
	
	boolean hasTatoo()
	{
		return false;
	}

	boolean hasSpouse()
	{
		return false;
	}
}
