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
    private TITFLMainMenu m_mainMenu;

    public TITFL(Activity activity, TITFLMainMenu mainMenu)
    {
        m_activity = activity;
        m_mainMenu = mainMenu;
    }
    
    public boolean dirty()
    {
        return m_dirty;
    }
    
    public void setDirty()
    {
        m_dirty = true;
    }
    
    public TITFLTown getTown()
    {
        return m_town;
    }
    
    public void run()
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
        //m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(0))); // <- This should be done in the steps above
        //m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(1))); // <- This should be done in the steps above
        //m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(2))); // <- This should be done in the steps above
        //m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(3))); // <- This should be done in the steps above
        //m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(4))); // <- This should be done in the steps above
        //NoEtapUtility.showAlert(m_activity, "TODO", "Initiate game");
        for (int i = 0; i < m_mainMenu.getNumOfPlayer(); i++)
        {
        	m_players.add(TITFLPlayer.createPlayer(m_defaultPlayers.get(i)));
        }
        
        m_town = new TITFLTown(m_activity, this, m_townmaps.get(mapType()));
        initiatePlayers();
        setNextPlayer(null);
    }
    
    public boolean resume()
    {
        boolean ret = false;
        
        m_defaultPlayers = TITFLPlayer.loadDefaultPlayers(m_activity.getAssets());
        m_randomEvents = TITFLRandomEvent.loadRandomEvents(m_activity.getAssets());
        m_townmaps = TITFLTownMap.loadTownMaps(m_activity.getAssets());
        m_players = new ArrayList<TITFLPlayer>();        
        
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
            if (player == null)
                return false;
            m_players.add(player);
        }

        if (m_players == null)
            return false;
        
        if (!m_town.load(key + ".town", db))
            return false;
        
        TITFLPlayer player = m_town.activePlayer();
        if (player != null)
        {
            TITFLTownElement element = player.currentLocation();
            if (element != null)
                element.setVisitor(player);
        }
        
        return true;
    }

    private void initiatePlayers()
    {
        TITFLGoods bicycle = m_town.getDefaultTransportation();
        TITFLGoods outfit = m_town.getDefaultOutfit();
        TITFLGoods apartment = m_town.getDefaultHome();
        TITFLGoods food = m_town.getDefaultFood();
        for (TITFLPlayer player : m_players)
        {
            player.buy(bicycle, 1, 0);
            player.buy(outfit, 1, 0);
            player.buy(apartment, 1, 0);
            player.buy(food, 1, 0);
            player.satisfaction().m_health = 100;
        }
    }
    
    public void setNextPlayer(TITFLPlayer oldPlayer)    
    {
        TITFLPlayer newPlayer;
        if (oldPlayer == null)
        {
            newPlayer = m_players.get(0);
        }
        else
        {
            Iterator<TITFLPlayer> it = m_players.iterator();
            while (it.hasNext())
            {
                if (it.next() == oldPlayer)
                    break;
            }
            if (it.hasNext())
            {
                newPlayer = it.next();
            }
            else
            {
                m_town.addCurrentWeek();
                newPlayer = m_players.get(0);
            }
        }

        m_town.setActivePlayer(newPlayer);
        newPlayer.beginWeek(getRandomEvent());
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

    public ArrayList<TITFLPlayer> players()
    {
        return m_players;
    }
    
    public ArrayList<TITFLPlayer> defaultPlayers()
    {
    	return m_defaultPlayers;
    }
    
    private TITFLRandomEvent getRandomEvent()
    {
        Random random = new Random();
        int index = (int)(random.nextFloat() * m_randomEvents.size());
        if (index == m_randomEvents.size())
            index--;
        return (TITFLRandomEvent) (m_randomEvents.get(index));
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
        
        int sum()
        {
            int sum = 1 + m_intelligent + m_hardWorking + m_goodLooking + m_physical + m_lucky;
            return sum;
        }
        
        float intelligent()
        {
            return 1 + (float)m_intelligent / (float)sum();
        }
        
        float goodlooking()
        {
            return 1 + (float)m_goodLooking / (float)sum();
        }
        
        float hardworking()
        {
            return 1 + (float)m_hardWorking / (float)sum();
        }

        float physical()
        {
            return 1 + (float)m_physical / (float)sum();
        }

        float lucky()
        {
            return 1 + (float)m_lucky / (float)sum();
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

        void add(DisciplineLevel addition, float factor)
        {
            m_basic += (addition.m_basic * factor);
            m_engineering += (addition.m_engineering * factor);
            m_business_finance += (addition.m_business_finance * factor);
            m_academic += (addition.m_academic * factor);
        }

        int m_basic; // GED
        int m_engineering;
        int m_business_finance;
        int m_academic;
    };

    // satisfaction used by TITFLPlayer
    public static class Satisfaction
    {
        Satisfaction()
        {            
        }
        
        Satisfaction(Satisfaction other)
        {
            m_health = other.m_health;
            m_wealth = other.m_wealth;
            m_education = other.m_education;
            m_career = other.m_career;
            m_life = other.m_life;
        }

        int m_health;
        int m_wealth;
        int m_education;
        int m_career;
        int m_life;
    }
}
