package com.noetap.titfl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;  

public class TITFLSQLiteOpenHelper extends SQLiteOpenHelper
{
	// Database Version 
	private static final int DATABASE_VERSION = 1; 
		  
	// Database Name 
	private static String DATABASE_NAME = "TITFL";
		  
	// Table name 
	private static final String TABLE_SETTINGS = "Settings"; 
		  
	// Table Columns names 
	private static final String KEY_SETTINGS_ID = "id"; 
	private static final String KEY_SETTINGS_KEY = "key"; 
	private static final String KEY_SETTINGS_VALUE = "value"; 

		
	public TITFLSQLiteOpenHelper(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	}
		
	// Creating Tables 
	@Override
	public void onCreate(SQLiteDatabase db) 
	{ 
		String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
	                + KEY_SETTINGS_ID + " INTEGER PRIMARY KEY," 
	        		+ KEY_SETTINGS_KEY + " TEXT,"
	        		+ KEY_SETTINGS_VALUE + " TEXT)"; 
	    db.execSQL(CREATE_SETTINGS_TABLE); 
	} 
		  
	// Upgrading database 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
	    // Drop older table if existed 
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS); 
		  
	    // Create tables again 
	    onCreate(db); 
	} 
		  
	// Save item
	public static boolean saveItem(String key, String value, SQLiteDatabase db) 
	{ 
		boolean success = false;

		if (db == null)
		   	return success;

		try
	    {
		    Cursor cursor = db.query(TABLE_SETTINGS, new String[] { 
		        		KEY_SETTINGS_ID, 
		                KEY_SETTINGS_KEY,
		                KEY_SETTINGS_VALUE }, KEY_SETTINGS_KEY + "=?", 
		                new String[] { key }, null, null, null, null); 
		        
		    ContentValues values = new ContentValues(); 
		    values.put(KEY_SETTINGS_KEY, key);
		    values.put(KEY_SETTINGS_VALUE, value); 	  
		    if (cursor != null && cursor.getCount() > 0)
		    {
		        db.update(TABLE_SETTINGS, values, KEY_SETTINGS_KEY + "= ?", new String[] { key }); 
		    }
		    else
		    {
			    db.insert(TABLE_SETTINGS, null, values);
		    }
		    cursor.close();
		    success = true;
	    }
	    catch (Exception ex)
	    {
	    }
	        
	    return success;
	} 
	  
	// Save item
	public static boolean saveItem(String key, int value, SQLiteDatabase db) 
    { 
    	return saveItem(key, Integer.toString(value), db);
    } 

	// Save item
	public static boolean saveItem(String key, float value, SQLiteDatabase db) 
    { 
    	return saveItem(key, Float.toString(value), db);
    } 

	// Load item
    public static String loadString(String key, String defaultVal, SQLiteDatabase db)
    {
    	String ret = defaultVal;	    	
        if (db == null)
        	return ret;
	  	  
        Cursor cursor = db.query(TABLE_SETTINGS, new String[] { 
        		KEY_SETTINGS_ID, 
                KEY_SETTINGS_KEY,
                KEY_SETTINGS_VALUE }, KEY_SETTINGS_KEY + "=?", 
                new String[] { key }, null, null, null, null); 

        if (cursor != null) 
        {
            boolean moveFirst = cursor.moveToFirst();
            if (moveFirst)
            	ret = cursor.getString(2);
        }

        return ret; 
    }
	    
    public static int loadInteger(String key, int defaultVal, SQLiteDatabase db)
    {
    	int ret;
    	try
    	{
    		ret = Integer.parseInt(loadString(key, Integer.toString(defaultVal), db));
    	}
    	catch (Exception ex)
    	{
    		ret = defaultVal;
    	}
    	return ret;
    }

    public static float loadFloat(String key, float defaultVal, SQLiteDatabase db)
    {
    	float ret;
    	try
    	{
    		ret = Float.parseFloat(loadString(key, Float.toString(defaultVal), db));
    	}
    	catch (Exception ex)
    	{
    		ret = defaultVal;
    	}
    	return ret;
    }

    // Get all settings 
    public Map<String, String> loadAllSettings(SQLiteDatabase db) 
    { 
        HashMap<String, String> settings = new HashMap<String, String>(); 
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS; 
	  
        Cursor cursor = db.rawQuery(selectQuery, null); 
	  
        // looping through all rows and adding to list 
        if (cursor.moveToFirst()) 
        { 
            do 
            { 
            	String key = cursor.getString(1);
            	String val = cursor.getString(2);
            	settings.put(key, val);
            }
            while (cursor.moveToNext()); 
        } 
  
        return settings; 
    } 
	  
    public boolean deleteSetting(String key, SQLiteDatabase db)
    { 
        if (db == null)
        	return false;
	        
        db.delete(TABLE_SETTINGS, KEY_SETTINGS_KEY + " = ?", new String[] { key }); 
        return true;
    } 
	  
    public boolean deleteAllSettings(SQLiteDatabase db)
    {
        if (db == null)
        	return false;

        String selectQuery = "SELECT " + KEY_SETTINGS_ID + " FROM " + TABLE_SETTINGS + " ORDER BY " + KEY_SETTINGS_ID; 
	  
        Cursor cursor = db.rawQuery(selectQuery, null); 
	  
        List<Integer> ids = new ArrayList<Integer>(); 
        if (cursor.moveToFirst()) 
        { 
            do 
            { 
            	ids.add(Integer.parseInt(cursor.getString(0)));
            } 
            while (cursor.moveToNext()); 
        } 

        for (int i = 0; i < ids.size(); i++)
        {
        	db.delete(TABLE_SETTINGS, KEY_SETTINGS_ID + " = ?", 
                new String[] { String.valueOf(ids.get(i)) }); 
        }

        return true;
    }	    
}
