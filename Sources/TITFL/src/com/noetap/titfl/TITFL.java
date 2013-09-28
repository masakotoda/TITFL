package com.noetap.titfl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TITFL 
{
	private boolean m_dirty = false;
	private Activity m_activity;
	private TITFLTown m_town;
	private ArrayList<TITFLPlayer> m_players;
	private ArrayList<TITFLPlayer> m_defaultPlayers;
	private ArrayList<TITFLEvent> m_randomEvents;
	private ArrayList<TITFLTownMap> m_townmaps;
	

	TITFL(Activity activity)
	{
		m_activity = activity;
	}
	
	boolean dirty()
	{
		return m_dirty;
	}
	
	void setDirty()
	{
		m_dirty = true;
	}
	
	void run()
	{
		m_defaultPlayers = TITFLPlayer.loadDefaultPlayers(m_activity.getAssets());
		m_randomEvents = TITFLRandomEvent.loadRandomEvents(m_activity.getAssets());
		m_townmaps = TITFLTownMap.loadTownMaps(m_activity.getAssets());
		m_players = new ArrayList<TITFLPlayer>();		
		
		// TODO
		// 1. Select Level.
		// 2. Select # of players.
		// 3. Select characters.
		// 4. Select who you are.
		// 5. Enter your name.
		// 6. Repeat 4 & 5
		// 7. Initialize TITFLTown
		m_players.add(new TITFLPlayer(m_defaultPlayers.get(0))); // <- This should be done in the steps above
		m_players.add(new TITFLPlayer(m_defaultPlayers.get(1))); // <- This should be done in the steps above
		NoEtapUtility.showAlert(m_activity, "TODO", "Initiate game");
		
		shuffleRandomEvent();
		
		m_town = new TITFLTown(m_activity, this, m_townmaps.get(mapType()));
		m_town.setActivePlayer(m_players.get(0));
	}
	
	boolean resume()
	{
		boolean ret = false;
		
		m_defaultPlayers = TITFLPlayer.loadDefaultPlayers(m_activity.getAssets());
		m_randomEvents = TITFLRandomEvent.loadRandomEvents(m_activity.getAssets());
		m_townmaps = TITFLTownMap.loadTownMaps(m_activity.getAssets());
		m_players = new ArrayList<TITFLPlayer>();		
		
		shuffleRandomEvent();
		
		m_town = new TITFLTown(m_activity, this, m_townmaps.get(mapType()));
		
		SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
		SQLiteDatabase db = helper.getWritableDatabase(); 
		try
		{
			ret = load("TITFL", db);
		}
		catch (Exception e)
		{			
		}
		
        db.close();
		
		return ret;
	}
	
	public boolean save()
	{
		SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
		SQLiteDatabase db = helper.getWritableDatabase(); 
		try
		{
			save("TITFL", db);
		}
		catch (Exception e)
		{			
		}
		
        db.close();
		return true;
	}
	
	private boolean save(String key, SQLiteDatabase db)
	{		
		m_town.save(key + ".town", db);

		TITFL.save(key + ".num_players", m_players.size(), db);
		for (int i = 0; i < m_players.size(); i++)
		{
			m_players.get(i).save(key + ".player." + Integer.toString(i), db);
		}

		return true;
	}
	
	private boolean load(String key, SQLiteDatabase db)
	{		
		int numPlayers = TITFL.loadInteger(key + ".num_players", db);
		m_players = new ArrayList<TITFLPlayer>();
		for (int i = 0; i < numPlayers; i++)
		{
			TITFLPlayer player = TITFLPlayer.loadPlayer(key + ".player." + Integer.toString(i), db, m_town);
			m_players.add(player);
		}

		if (m_players == null)
			return false;
		
		if (!m_town.load(key + ".town", db))
			return false;
		
		TITFLPlayer player = m_town.activePlayer();
		if (player != null)
		{
			TITFLTownElement element = player.getLocation();
			if (element != null)
				element.setVisitor(player);
		}
		
		return true;
	}

	public TITFLTown getTown()
	{
		return m_town;
	}
	
	public void setNextPlayer(TITFLPlayer oldPlayer)	
	{		
		Iterator<TITFLPlayer> it = m_players.iterator();
		while (it.hasNext())
		{
			if (it.next() == oldPlayer)
				break;
		}
		if (it.hasNext())
			m_town.setActivePlayer(it.next());
		else
			m_town.setActivePlayer(m_players.get(0));
	}
	
	// used when resuming game
	public void setActivePlayer(String activePlayersName)
	{
		for (int i = 0; i < m_players.size(); i++)
		{
			TITFLPlayer player = m_players.get(i);
			if (player.name().compareTo(activePlayersName) == 0)
			{
				m_town.setActivePlayer(player);
				return;
			}
		}
	}
	
	// used when resuming game
	public void setTownMap(String townMapName)
	{
		for (int i = 0; i < m_townmaps.size(); i++)
		{
			TITFLTownMap townmap = m_townmaps.get(i);
			if (townmap.name().compareTo(townMapName) == 0)
			{
				m_town.setTownMap(townmap);
				return;
			}
		}
	}

	private void shuffleRandomEvent()
	{
		// TODO: Shuffle m_randomEvents
		for (int i = 0; i < m_randomEvents.size(); i++)
		{
			;
		}
	}
	
	private int mapType()
	{
		Random random = new Random();
		int type = (int)(random.nextFloat() * m_townmaps.size());
		if (type == m_townmaps.size())
			type--;
		return type;
	}
	
	static public boolean save(String key, float value, SQLiteDatabase db)
	{
		TITFLSQLiteOpenHelper.saveItem(key, value, db);
		return true;
	}

	static public boolean save(String key, String value, SQLiteDatabase db)
	{
		TITFLSQLiteOpenHelper.saveItem(key, value, db);
		return true;
	}

	static public boolean save(String key, int value, SQLiteDatabase db)
	{
		TITFLSQLiteOpenHelper.saveItem(key, value, db);
		return true;
	}

	static public float loadFloat(String key, SQLiteDatabase db)
	{
		return TITFLSQLiteOpenHelper.loadFloat(key, 0.f, db);
	}

	static public int loadInteger(String key, SQLiteDatabase db)
	{
		return TITFLSQLiteOpenHelper.loadInteger(key, 0, db);
	}

	static public String loadString(String key, SQLiteDatabase db)
	{
		return TITFLSQLiteOpenHelper.loadString(key, "", db);
	}
	
	// character factor used by TITFLPlayer, TITFLJob, TITFLRandomEvent
	public static class CharacterFactor
	{
		CharacterFactor()
		{			
		}

		CharacterFactor(CharacterFactor other)
		{
			m_intelligent = other.m_intelligent;
			m_hardWorking = other.m_hardWorking;
			m_goodLooking = other.m_goodLooking;
			m_physical = other.m_physical;
			m_lucky = other.m_lucky;
		}
				
		int m_intelligent;
		int m_hardWorking;
		int m_goodLooking;
		int m_physical;
		int m_lucky;
	};

	// character factor used by TITFLPlayer, TITFLJob
	public static class DisciplineLevel
	{
		DisciplineLevel()		
		{			
		}
		
		DisciplineLevel(DisciplineLevel other)
		{
			m_basic = other.m_basic;
			m_engineering = other.m_engineering;
			m_business_finance = other.m_business_finance;
			m_academic = other.m_academic;
		}
		
		int m_basic; // GED
		int m_engineering;
		int m_business_finance;
		int m_academic;
	};
}
