package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.Xml;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class TITFLPlayer 
{
	private String m_name;
	private String m_alias;
	private int m_factor_intelligent;
	private int m_factor_hardworking;
	private int m_factor_goodlooking;
	private int m_factor_physical;
	private int m_factor_lucky;
	private TITFLTownElement m_currentLocation;
	private int m_counter = 0;
	
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
	}
	
	private void setLocation(TITFLTownElement destination)
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
			
	public void goTo(Activity activity, TITFLTownElement destination, ArrayList<TITFLTownMapNode> route1)
	{
		if (destination == null)
			return;

		if (route1.size() == 0)
			return;
		
		m_counter = 0;
		
		final ArrayList<TITFLTownMapNode> route = route1;
		
		setLocation(destination);
		TITFLTownView view = (TITFLTownView) activity.findViewById(R.id.townView);
		if (view != null)
			view.invalidate();
		
		ImageView marbleImg = (ImageView) activity.findViewById(R.id.imageView1);
		Animation anim = AnimationUtils.loadAnimation(activity, R.anim.anim_test);

		final ImageView playerImg = (ImageView) activity.findViewById(R.id.imageView2);
		playerImg.setBackgroundResource(R.drawable.frame_anim_test);
		final AnimationDrawable playerWalk = (AnimationDrawable) playerImg.getBackground(); 

		final TITFLTownElement fDest = destination;
		final Activity fActivity = activity;
		anim.setAnimationListener(new AnimationListener() 
		{
			@Override
		    public void onAnimationEnd(Animation arg0) 
			{
				ImageView marbleImg = (ImageView) fActivity.findViewById(R.id.imageView1);

				m_counter++;
				if (m_counter == route.size())
				{
					playerWalk.stop();					
					NoEtapUtility.showAlert(fActivity, fDest.name(), fDest.id());
				}
				else
				{
					Animation anim = AnimationUtils.loadAnimation(fActivity, R.anim.anim_test);
					anim.setAnimationListener(this);
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
				//Rect rect = TITFLTownView.getPlayerRect();
				//playerImg.layout(rect.left, rect.top, rect.right, rect.bottom);

				ImageView marbleImg = (ImageView) fActivity.findViewById(R.id.imageView1);
				int slot = route.get(m_counter).m_index;
				Rect rect = fDest.town().nodeToPosition(slot);
				marbleImg.layout(rect.left, rect.top, rect.left + rect.width() / 2, rect.top + rect.height() / 2);
			}
		});
		
		playerWalk.start();
		marbleImg.startAnimation(anim);
	}
}
