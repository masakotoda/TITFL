package com.noetap.titfl;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TITFLActivity extends Activity 
{
    private TITFL m_game;
    private TITFLLayout m_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        //Set orientation (we support only landscape for now).
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Remove title bar (if we hide it, we won't have default action bar. do we want hide it??)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_titfl);
        
        if (!resumeGame())
            runGame();
    }

    @Override
    protected void onDestroy()
    {
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
        saveGame();
  
        super.onDestroy();        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.titfl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
          case R.id.action_run:
              runGame();
            break;
          case R.id.action_about:
              showAbout();
            break;
          case R.id.action_settings:
              showSettings();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
            
    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        //This is just in case we support landscape layout.
        super.onConfigurationChanged(newConfig);
    }
    
    public TITFLTown getTown()
    {
        return m_game.getTown();
    }
    
    private void runGame()
    {
        m_game = new TITFL(this);
        
        m_game.run();

        m_layout = new TITFLTownLayout(this);
        m_layout.initialize();        
    }
    
    private boolean resumeGame()
    {
        m_game = new TITFL(this);
        
        if (!m_game.resume())
            return false;

        m_layout = new TITFLTownLayout(this);
        m_layout.initialize();
        return true;
    }

    private void saveGame()
    {        
        if (m_game.dirty())
            m_game.save();
    }
    
    public void openTownElement(TITFLTownElement element)
    {
        m_layout = TITFLTownElementFactory.createLayout(this, element);
        m_layout.initialize();        
    }
    
    public void closeTownElement()
    {
        setContentView(R.layout.activity_titfl);
        m_layout = new TITFLTownLayout(this);
        m_layout.initialize();        
    }
    
    public void invalidate()
    {
        m_layout.invalidate();
    }
    
    private void showAbout()
    {
        NoEtapUtility.showAlert(this, "About", "TITFL - Tony In The Fast Lane\n (c) 2013 Team noEtap");
    }
    
    private void showSettings()
    {
        NoEtapUtility.showAlert(this, "Settings", "Under construction...");
    }    
}
