package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
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
	private int m_factor_intelligent;
	private int m_factor_hardworking;
	private int m_factor_goodlooking;
	private int m_factor_physical;
	private int m_factor_lucky;
	private TITFLTownElement m_currentLocation;
	private int m_counter = -1;
	private float m_speedFactor = 0.5f; // 1 is default. 0.5 is x2 faster. 2 is x2 slower.
	private float m_hour = 0;
	private ArrayList<TITFLGoods> m_belongings;
	private String m_avatar_frm_01;
	private String m_avatar_frm_02;
	private String m_avatar_frm_03;
	private String m_avatar_frm_04;
	
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
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_name))
		    			element.m_name = attribValue;
		    		else if (attribName.equals(atr_alias))
		    			element.m_alias = attribValue;
		    		else if (attribName.equals(atr_factor_intelligent))
		    			element.m_factor_intelligent = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_hardworking))
		    			element.m_factor_hardworking = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_goodlooking))
		    			element.m_factor_goodlooking = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_physical))
		    			element.m_factor_physical = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_factor_lucky))
		    			element.m_factor_lucky = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_avatar_frm_01))
		    			element.m_avatar_frm_01 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_02))
		    			element.m_avatar_frm_02 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_03))
		    			element.m_avatar_frm_03 = attribValue;
		    		else if (attribName.equals(atr_avatar_frm_04))
		    			element.m_avatar_frm_04 = attribValue;
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
	
	// Default constructor
	public TITFLPlayer()
	{
		m_belongings = new ArrayList<TITFLGoods>();
	}
	
	// Copy constructor
	public TITFLPlayer(TITFLPlayer other)
	{
		m_name = other.m_name;
		m_alias = other.m_alias;
		m_factor_intelligent = other.m_factor_intelligent;
		m_factor_hardworking= other.m_factor_hardworking;
		m_factor_goodlooking = other.m_factor_goodlooking;
		m_factor_physical = other.m_factor_physical;
		m_factor_lucky = other.m_factor_lucky;
		m_currentLocation = other.m_currentLocation;
		m_counter = other.m_counter;
		m_speedFactor = other.m_speedFactor;
		m_hour = other.m_hour;
		m_avatar_frm_01 = other.m_avatar_frm_01;
		m_avatar_frm_02 = other.m_avatar_frm_02;
		m_avatar_frm_03 = other.m_avatar_frm_03;
		m_avatar_frm_04 = other.m_avatar_frm_04;

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
	
	public void draw(Canvas canvas, Paint paint)
	{
		if (m_currentLocation == null)
			return;

		// Change theme color depending on the character?
		if (m_factor_hardworking > m_factor_intelligent)
			paint.setARGB(255, 255, 192, 192);
		else
			paint.setARGB(255, 192, 255, 192);

		int w = canvas.getWidth();
		int h = canvas.getHeight();
		canvas.drawRect(0, 0, w, h, paint);

		float factor = NoEtapUtility.getFactor(m_currentLocation.town().getActivity());
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

		m_counter = 0;
		TITFLTownMapNode current = route.get(m_counter);
		TITFLTownMapNode nextStop = (route.size() > m_counter + 1) ? route.get(m_counter + 1) : route.get(m_counter);
		
		ImageView marbleImg = (ImageView) activity.findViewById(R.id.imageView1);
		Animation anim = AnimationUtils.loadAnimation(activity, getAnim(current, nextStop));
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
			}
		});
		
		avatarWalk.start();

		int slot = route.get(m_counter).index();
		Rect rect = destination.town().nodeToPosition(slot);
		marbleImg.layout(rect.left, rect.top, rect.left + rect.width(), rect.top + rect.height());
		marbleImg.setVisibility(View.VISIBLE);

		anim.scaleCurrentDuration(m_speedFactor);
		marbleImg.startAnimation(anim);
		
		return true;
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
	
	public void work()
	{
		//TODO
	}
}
