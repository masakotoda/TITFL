package com.noetap.titfl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class TITFLTown 
{
	final int m_roadWidth = 60;
	
	private ArrayList<Rect> m_nodeRect;
	private Activity m_activity;
	private float m_economyFactor;
	private int m_currentWeek;
	private ArrayList<TITFLTownElement> m_elements;
	private ArrayList<TITFLTownElement> m_fixedElements;
	private ArrayList<TITFLTownElement> m_randomElements;
	private int m_maxSlot;
	private TITFLPlayer m_activePlayer;
	private TITFLTownMap m_townMap;
	private TITFL m_game;
	
	public TITFLTown(Activity activity, TITFL game, TITFLTownMap townMap)
	{		
		m_activity = activity;
		m_game = game;
		m_townMap = townMap;
		m_elements = TITFLTownElement.loadTownElements(m_activity.getAssets(), this);

		m_fixedElements = new ArrayList<TITFLTownElement>();
		m_randomElements = new ArrayList<TITFLTownElement>();
		for (int i = 0; i < m_elements.size(); i++)
		{
			TITFLTownElement element = m_elements.get(i);
			if (element.slot() < 0)
				m_randomElements.add(element);
			else
				m_fixedElements.add(element);
		}
		m_maxSlot = m_fixedElements.size() + 1;

        @SuppressWarnings("unused")
        ArrayList<TITFLJob> jobs = TITFLJob.loadDefaultJobs(m_activity.getAssets(), this);

        @SuppressWarnings("unused")
        ArrayList<TITFLGoods> goods = TITFLGoods.loadDefaultGoods(m_activity.getAssets(), this);
        
		initialize();
	}
	
	public boolean save(String key, SQLiteDatabase db)
	{
		TITFL.save(key + ".economyFactor", m_economyFactor, db);
		TITFL.save(key + ".currentWeek", m_currentWeek, db);
		TITFL.save(key + ".activePlayer", m_activePlayer.name(), db);
		TITFL.save(key + ".townMap", m_townMap.name(), db);

		for (int i = 0; i < m_elements.size(); i++)
		{
			TITFL.save(key + ".element." + Integer.toString(i), m_elements.get(i).id(), db);
		}
		
		return true;
	}
	
	public boolean load(String key, SQLiteDatabase db)
	{
		m_economyFactor = TITFL.loadFloat(key + ".economyFactor", db);
		m_currentWeek = TITFL.loadInteger(key + ".currentWeek", db);
		String activePlayersName = TITFL.loadString(key + ".activePlayer", db);
		String townMapName = TITFL.loadString(key + ".townMap", db);
		m_game.setActivePlayer(activePlayersName);
		m_game.setTownMap(townMapName);

		ArrayList<TITFLTownElement> newElements = new ArrayList<TITFLTownElement>();
		int count = m_elements.size();
		for (int i = 0; i < count; i++)
		{
			String elementId = TITFL.loadString(key + ".element." + Integer.toString(i), db);
			TITFLTownElement element = findElement(elementId);
			newElements.add(element);
		}

		m_elements.clear();
		for (int i = 0; i < m_townMap.nodes().size(); i++)
		{
			TITFLTownMapNode node = m_townMap.nodes().get(i);
			if (node.occupied())
			{
				int next = m_elements.size();
				TITFLTownElement element = newElements.get(next);
				element.setSlot(next);
				element.setNode(node);
				m_elements.add(element);
			}
		}

		return true;
	}
	
	public TITFLTownElement findElement(String elementId)
	{
		for (int i = 0; i < m_elements.size(); i++)
		{
			if (m_elements.get(i).id().compareTo(elementId) == 0)
				return m_elements.get(i);
		}
		
		for (int i = 0; i < m_randomElements.size(); i++)
		{
			if (m_randomElements.get(i).id().compareTo(elementId) == 0)
				return m_randomElements.get(i);
		}
		
		return null; // really??
	}
	
    public TITFLGoods findGoods(String goodsId)
    {
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLGoods g : e.merchandise())
            {
                if (g.id().equals(goodsId))
                    return g;
            }
        }
        
        for (TITFLTownElement e : m_randomElements)
        {
            for (TITFLGoods g : e.merchandise())
            {
                if (g.id().equals(goodsId))
                    return g;
            }
        }

        return null; // really??
    }

    public TITFLPlayer activePlayer()
	{
		return m_activePlayer;
	}
	
	public void setActivePlayer(TITFLPlayer player)
	{
		m_activePlayer = player;
		
		if (m_activePlayer != null)
		{
			m_activePlayer.notifyActive(m_activity);
		}
	}
	
	public void setNextPlayer()
	{
		m_game.setNextPlayer(m_activePlayer);
		
		TITFLTownElement location = m_activePlayer.getLocation();
		if (location == null)
		{
			location = m_elements.get(0);
			m_activePlayer.setLocation(location);
		}		
		m_activePlayer.goTo(m_activity, location, false);
	}
	
	public AssetManager getAssets()
	{
		return m_activity.getAssets();
	}
	
	public Activity activity()
	{
		return m_activity;
	}
	
	public TITFLTownMap townMap()
	{
		return m_townMap;
	}

	public void setTownMap(TITFLTownMap townMap)
	{
		m_townMap = townMap;
	}

	public void addCurrentWeek()
	{
		m_currentWeek++;

		Random random = new Random();
		m_economyFactor = random.nextFloat() + 1;
	}
	
	public int currentWeek()
	{
		return m_currentWeek;
	}
	
	public float economyFactor()
	{
		return m_economyFactor;
	}	
	
	public void draw(Canvas canvas, Paint paint)
	{
		paint.setARGB(255, 128, 128, 128);

		int w = m_nodeRect.get(0).width();
		int h = m_nodeRect.get(0).height();
		
		// Draw roads
		for (int i = 0; i < TITFLTownMap.num_nodes; i++)
		{
			TITFLTownMapNode slot = m_townMap.nodes().get(i);
			for (int j = 0; j < slot.link().size(); j++)
			{
				int startX = slot.x() * w + w / 2;
				int stopX = slot.link().get(j).x() * w + w / 2;
				int startY = slot.y() * h + h / 2;
				int stopY = slot.link().get(j).y() * h + h / 2;
				
				paint.setStrokeWidth(m_roadWidth * NoEtapUtility.getFactor(activity()));
				canvas.drawLine(startX, startY, stopX, stopY, paint);

				int halfRoad = (m_roadWidth / 2) - 1;
				paint.setStrokeWidth(1);				
				canvas.drawCircle(startX, startY, halfRoad * NoEtapUtility.getFactor(activity()), paint);
				canvas.drawCircle(stopX, stopY, halfRoad * NoEtapUtility.getFactor(activity()), paint);
			}
		}
		
		// Draw elements
		for (int i = 0; i < m_elements.size(); i++)
		{
			m_elements.get(i).draw(canvas, paint);
		}		
	}
	
	public void handleActionDown(MotionEvent event)
	{
		if (m_activePlayer == null)
			return;
		
		if (m_activePlayer.isMoving())
			return;
		
		int slot = positionToSlot((int)event.getX(), (int)event.getY());
		if (slot < 0)
			return;

		m_game.setDirty();

		TITFLTownElement destination = getTownElement(slot);
			
		TITFLTownElement location = m_activePlayer.getLocation();
		if (location == null)
		{
			location = m_elements.get(0);
			m_activePlayer.setLocation(location);
		}
		m_activePlayer.goTo(m_activity, destination, true);
	}
	
	public int positionToSlot(int x, int y)
	{
		for (int i = 0; i < m_nodeRect.size(); i++)
		{
			if (m_nodeRect.get(i).contains(x, y))
			{
				for (int j = 0; j < m_elements.size(); j++)
				{
					int index = m_elements.get(j).node().index();
					if (i == index)
					{
						return j;
					}
				}
			}
		}
		return -1;
	}
	
	public ArrayList<TITFLTownElement> elements()
	{
		return m_elements;
	}
	
	public TITFLGoods getDefaultTransportation()
	{
		for (TITFLTownElement e : m_elements)
		{
			for (TITFLGoods g : e.merchandise())
				if (g.id().equals("goods_bicycle"))
					return g;			
		}
		return null;
	}
	
	public TITFLGoods getDefaultOutfit()
	{
		for (TITFLTownElement e : m_elements)
		{
			for (TITFLGoods g : e.merchandise())
				if (g.id().equals("goods_casual_outfit"))
					return g;			
		}
		return null;
	}
	
	public TITFLTownElement getTownElement(int slot)
	{
		if (slot < m_elements.size())
			return m_elements.get(slot);
		return null;
	}
	
	public Rect slotToPosition(int slot)
	{
		int index = m_elements.get(slot).node().index();
		return m_nodeRect.get(index);
	}
	
	public Rect nodeToPosition(int slot)
	{
		return m_nodeRect.get(slot);
	}

	private TITFLTownElement getRandomElement()
	{
		Random random = new Random();		
		int remaining = m_randomElements.size();
		int next = (int)(remaining * random.nextFloat());
		if (next == remaining)
			next--;
		return m_randomElements.get(next);
	}
	
	public void changeRandomElement()
	{
		TITFLTownElement randomElement = getRandomElement();
		int maxIndex = m_maxSlot - 1;
		TITFLTownElement outOfBusiness = m_elements.get(maxIndex);
		randomElement.setSlot(outOfBusiness.slot());
		randomElement.setNode(outOfBusiness.node());
		m_elements.set(maxIndex, randomElement);
	}
	
	private void initialize()
	{		
		m_nodeRect = new ArrayList<Rect>();
		
		int w = NoEtapUtility.getScreenWidth(m_activity);		
		int eachW = w / 4;
		int eachH = (eachW * 2) / 3;
		
		for (int i = 0; i < TITFLTownMap.num_rows; i++)
		{
			for (int j = 0; j < TITFLTownMap.num_columns; j++)
			{
				m_nodeRect.add(new Rect(eachW * j, eachH * i, eachW * (1 + j), eachH * (1 + i)));
			}
		}

		m_elements.clear();

		int maxIndex = m_maxSlot - 1;
		Random random = new Random();		
		TITFLTownElement randomElement = getRandomElement();
		
		int remaining = m_fixedElements.size();
		for (int i = 0; i < m_townMap.nodes().size(); i++)
		{
			TITFLTownMapNode node = m_townMap.nodes().get(i);
			if (node.occupied())
			{
				if (m_elements.size() == maxIndex)
				{
					randomElement.setSlot(maxIndex);
					randomElement.setNode(node);
					m_elements.add(randomElement);
				}
				else
				{
					int next = (int)(remaining * random.nextFloat());
					if (next == remaining)
						next--;
	
					TITFLTownElement element = m_fixedElements.get(next);
					element.setSlot(m_elements.size());
					element.setNode(node);
					m_elements.add(element);
					m_fixedElements.remove(element);
					remaining--;
				}
			}
		}
	}
}
