package com.noetap.titfl;

import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class TITFLTown 
{
    final int m_roadWidth = 60;
    private int m_offset; // Width of extra area - it's different depending on the screen ratio.
    private Bitmap m_backgroundTile = null;
    private Bitmap m_backgroundMap = null;
    
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
    
    public Activity activity()
    {
        return m_activity;
    }
    
    public float economyFactor()
    {
        return m_economyFactor;
    }    
    
    public int currentWeek()
    {
        return m_currentWeek;
    }

    public ArrayList<TITFLTownElement> elements()
    {
        return m_elements;
    }
    
    public TITFLPlayer activePlayer()
    {
        return m_activePlayer;
    }
    
    public TITFLTownMap townMap()
    {
        return m_townMap;
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

        m_game.setActivePlayer(activePlayersName); // It should be called last.
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
    
    public TITFLJob findJob(String jobId)
    {
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLJob j : e.jobs())
            {
                if (j.id().equals(jobId))
                    return j;
            }
        }
        return null; // really??
    }

    public TITFLTownElement findHouse()
    {
        for (TITFLTownElement e : m_elements)
        {
            if (e.isHouse())
                return e;
        }
        return null;
    }

    public TITFLTownElement findApartment()
    {
        for (TITFLTownElement e : m_elements)
        {
            if (e.isApartment())
                return e;
        }
        return null;
    }

    public void setTownMap(TITFLTownMap townMap)
    {
        m_townMap = townMap;
    }

    public void setActivePlayer(TITFLPlayer player)
    {
        m_activePlayer = player;
        
        if (m_activePlayer != null)
        {
            m_activePlayer.setActive(m_activity);
        }
    }
    
    public AssetManager getAssets()
    {
        return m_activity.getAssets();
    }
        
    public void addCurrentWeek()
    {
        m_currentWeek++;

        Random random = new Random();
        m_economyFactor = random.nextFloat() + 1;
    }
    
    private Bitmap getBackgroundTile()
    {
        if (m_backgroundTile != null)
            return m_backgroundTile;
        m_backgroundTile = NoEtapUtility.getBitmap(activity(), "townelement_bg001.png");
        return m_backgroundTile;
    }

    private Bitmap getBackgroundMap()
    {
        if (m_backgroundMap != null)
            return m_backgroundMap;

        if (m_townMap != null)
            m_backgroundMap = NoEtapUtility.getBitmap(activity(), "townmap_" + m_townMap.id() + ".png");
        return m_backgroundMap;
    }

    public void draw(Canvas canvas, Paint paint)
    {
        paint.setARGB(255, 144, 80, 80);

        int w = m_nodeRect.get(0).width();
        int h = m_nodeRect.get(0).height();
        
        // Draw background
        Bitmap bitmap = getBackgroundTile();
        if (bitmap != null)
        {
            Rect bitmapRect = null;

            bitmapRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // Draw tiles in the extra area (the size of extra area is different depending on the screen ratio.)
            int right = (int)(bitmap.getWidth() * m_offset / (float)w);
            int left = bitmap.getWidth() - right;
            if (right > 0 && left > 0)
            {
                Rect src1 = new Rect(left, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect src2 = new Rect(0, 0, right, bitmap.getHeight());

                for (int i = 0; i < TITFLTownMap.num_rows; i++)
                {
                    int destTop = i * h;
                    
                    Rect dst1 = new Rect(0, destTop, m_offset, destTop + h);
                    canvas.drawBitmap(bitmap, src1, dst1, null);

                    int destLeft = w * TITFLTownMap.num_columns + m_offset;
                    Rect dst2 = new Rect(destLeft, destTop, destLeft + m_offset, destTop + h);
                    canvas.drawBitmap(bitmap, src2, dst2, null);
                }
            }

            // Draw tiles in the rest of the area
            Rect unionRect = new Rect();
            for (int i = 0; i < m_nodeRect.size(); i++)
            {
                if (bitmap != null)
                {
                    canvas.drawBitmap(bitmap, bitmapRect, m_nodeRect.get(i), null);
                }

                unionRect.union(m_nodeRect.get(i));
            }

            // Draw map on top of tiles
            Bitmap backgroundMap = getBackgroundMap();
            if (backgroundMap != null)
            {
                bitmapRect = new Rect(0, 0, backgroundMap.getWidth(), backgroundMap.getHeight());
                canvas.drawBitmap(backgroundMap, bitmapRect, unionRect, null);
            }
        }


        // Draw roads
        for (int i = 0; i < TITFLTownMap.num_nodes; i++)
        {            
            TITFLTownMapNode slot = m_townMap.nodes().get(i);
            
            for (int j = 0; j < slot.link().size(); j++)
            {
                int startX = m_offset + slot.x() * w + w / 2;
                int stopX = m_offset + slot.link().get(j).x() * w + w / 2;
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
            
        TITFLTownElement location = m_activePlayer.currentLocation();
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
    
    public TITFLGoods getDefaultTransportation()
    {
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLGoods g : e.merchandise())
                if (g.isBicycle())
                    return g;            
        }
        return null;
    }
    
    public TITFLGoods getDefaultOutfit()
    {
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLGoods g : e.merchandise())
                if (g.isCasualOutfit())
                    return g;            
        }
        return null;
    }
    
    public TITFLGoods getDefaultHome()
    {
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLGoods g : e.merchandise())
                if (g.isApartment())
                    return g;
        }
        return null;
    }

    public TITFLGoods getDefaultFood()
    {
        TITFLGoods ret = null;
        int price = 1000;
        for (TITFLTownElement e : m_elements)
        {
            for (TITFLGoods g : e.merchandise())
            {
                if (g.foodValue() >= 1 && g.price() < price)
                {
                    ret = g;
                    price = g.price();
                }
            }
        }
        return ret;
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
        addCurrentWeek();

        m_nodeRect = new ArrayList<Rect>();
        
        int w = NoEtapUtility.getScreenWidth(m_activity);        
        int eachW = w / 4;
        int eachH = (eachW * 2) / 3;
        
        int h = NoEtapUtility.getScreenHeight(m_activity);
        m_offset = (h - TITFLPlayerView.getWidth(m_activity) - w) / 2;
        
        for (int i = 0; i < TITFLTownMap.num_rows; i++)
        {
            for (int j = 0; j < TITFLTownMap.num_columns; j++)
            {
                m_nodeRect.add(new Rect(m_offset + eachW * j, eachH * i, m_offset + eachW * (1 + j), eachH * (1 + i)));
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
