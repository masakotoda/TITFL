package com.noetap.titfl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;

public class TITFL 
{
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
	
	void resume()
	{
		// TODO
		// 1. Load saved data to m_town & players
		// 2. Resume game...
	}
	
	TITFLTown getTown()
	{
		return m_town;
	}
	
	void setNextPlayer(TITFLPlayer oldPlayer)	
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
}
