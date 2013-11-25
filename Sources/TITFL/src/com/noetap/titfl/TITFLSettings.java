package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;

public class TITFLSettings 
{
    public float m_bgmVolume = 0.5f;
    public boolean m_reverseLayout = true;
    private Activity m_activity;

    private static String atr_bgmVolume = "settings.bgmVolume";
    private static String atr_reverseLayout = "settings.reverseLayout";

    public TITFLSettings(Activity activity)
    {
        m_activity = activity;
    }

    public boolean save()
    {
        refreshSetting();

        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFL.save(atr_bgmVolume, m_bgmVolume, db);
            TITFL.save(atr_reverseLayout, m_reverseLayout ? 1 : 0, db);

            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
            item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeAppEnd;
            TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
        return true;
    }

    public void load()
    {
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            m_bgmVolume = TITFL.loadFloat(atr_bgmVolume, db);
            int reverseLayout = TITFL.loadInteger(atr_reverseLayout, db);
            m_reverseLayout = reverseLayout == 1 ? true : false;

            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
            item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeAppStart;
            TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
    }

    public void refreshSetting()
    {
        AudioManager am = (AudioManager) m_activity.getSystemService(Context.AUDIO_SERVICE);
        int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        m_bgmVolume = (float)volume / (float)maxVol;
    }

    public ArrayList<TITFLSQLiteOpenHelper.HistoryItem> loadHistory()
    {
        ArrayList<TITFLSQLiteOpenHelper.HistoryItem> items = null;
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            items = TITFLSQLiteOpenHelper.loadAllHistory(db);
        }
        catch (Exception e)
        {
        }
        db.close();
        return items;
    }

    public void clearHisotry()
	{
	    SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    try
	    {
	        TITFLSQLiteOpenHelper.clearHistory(db);
	    }
	    catch (Exception e)
	    {
	    }
	    db.close();
	}

	public void notifyNewGame()
	{
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
	        item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeNewGame;
	        TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
	}

	public void notifyResumeGame(TITFL game)
	{
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
            item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeResumeGame;
            item.m_week = game.getTown().currentWeek();
            TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
	}

	public void notifySaveGame(TITFL game)
	{
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
            item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeSuspendGame;
            item.m_week = game.getTown().currentWeek();
            TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
	}

	public void notifyFinishGame(TITFL game, TITFLPlayer winner)
	{
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(m_activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFLSQLiteOpenHelper.HistoryItem item = new TITFLSQLiteOpenHelper.HistoryItem();
            item.m_type = TITFLSQLiteOpenHelper.HistoryItem.typeFinishGame;
            item.m_week = game.getTown().currentWeek();
            item.m_description = winner.alias() + " won!";
            TITFLSQLiteOpenHelper.saveItem(item, db);
        }
        catch (Exception e)
        {
        }
        db.close();
	}
}
