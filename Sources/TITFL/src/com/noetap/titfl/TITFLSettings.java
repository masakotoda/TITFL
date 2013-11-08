package com.noetap.titfl;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TITFLSettings 
{
    public float m_bgmVolume = 0.5f;
    public boolean m_reverseLayout = true;

    private static String atr_bgmVolume = "settings.bgmVolume";
    private static String atr_reverseLayout = "settings.reverseLayout";

    public boolean save(Activity activity)
    {
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            TITFL.save(atr_bgmVolume, m_bgmVolume, db);
            TITFL.save(atr_reverseLayout, m_reverseLayout ? 1 : 0, db);
        }
        catch (Exception e)
        {
        }
        db.close();
        return true;
    }

    public void load(Activity activity)
    {
        SQLiteOpenHelper helper = new TITFLSQLiteOpenHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            m_bgmVolume = TITFL.loadFloat(atr_bgmVolume, db);
            int reverseLayout = TITFL.loadInteger(atr_reverseLayout, db);
            m_reverseLayout = reverseLayout == 1 ? true : false;
        }
        catch (Exception e)
        {
        }
        db.close();
    }
}
