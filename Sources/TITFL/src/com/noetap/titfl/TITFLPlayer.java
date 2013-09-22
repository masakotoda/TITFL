package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
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
	private int m_factor_intelligent;
	private int m_factor_hardworking;
	private int m_factor_goodlooking;
	private int m_factor_physical;
	private int m_factor_lucky;
	private TITFLTownElement m_currentLocation;
	private int m_counter = -1;
	private float m_speedFactor = 0.5f; // 1 is default. 0.5 is x2 faster. 2 is x2 slower.
	private float m_hour = 0;
	
	private static String tag_root = "TITFL";
	private static String tag_item = "player";
	private static String atr_name = "name";
	private static String atr_alias = "alias";
	private static String atr_factor_intelligent = "factor_intelligent";
	private static String atr_factor_hardworking = "factor_hardworking";
	private static String atr_factor_goodlooking = "factor_goodlooking";
	private static String atr_factor_physical = "factor_physical";
	private static String atr_factor_lucky = "factor_lucky";

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
		
		TITFLTown town = m_currentLocation.town();
		Rect r = town.playerInfoRect();

		paint.setARGB(255, 128, 192, 128);
		canvas.drawRect(r, paint);

		paint.setARGB(255, 0, 0, 0);
		paint.setTextSize(48 * NoEtapUtility.getFactor(town.getActivity()));
		canvas.drawText(Float.toString(m_hour) + " " + this.m_alias, r.left, r.bottom - 100, paint);
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
			return false;
		}
		
		m_hour += hour;

		setLocation(destination);
		
		final ArrayList<TITFLTownMapNode> route = route1;

		final ImageView avatarImg = (ImageView) activity.findViewById(R.id.imageView2);
		avatarImg.setImageBitmap(null);
		avatarImg.setBackgroundResource(R.drawable.frame_anim_test);
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
					if (openDestination)
						NoEtapUtility.showAlert(activity, destination.name(), destination.id());
					m_counter = -1;
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
				ImageView avatarImg = (ImageView) activity.findViewById(R.id.imageView2);
				Rect rectAvatar = m_currentLocation.town().avatarRect();
				avatarImg.layout(rectAvatar.left, rectAvatar.top, rectAvatar.right, rectAvatar.bottom);
			}
		});
		
		avatarWalk.start();

		int slot = route.get(m_counter).index();
		Rect rect = destination.town().nodeToPosition(slot);
		marbleImg.layout(rect.left, rect.top, rect.left + rect.width(), rect.top + rect.height());

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
}
