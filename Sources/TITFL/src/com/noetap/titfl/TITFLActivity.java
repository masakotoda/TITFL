package com.noetap.titfl;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TITFLActivity extends Activity 
{
    public static final String pathGoods = "goods/";
    public static final String pathGreeter = "greeter/";
    public static final String pathElement = "townelement/";
    public static final String pathMap = "townmap/";    
    
    private TITFL m_game;
    private TITFLLayout m_layout;
    private MediaPlayer m_mediaPlayer;
    private TITFLSettings m_settings = new TITFLSettings();
    private TITFLMainMenu m_mainMenu;

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

        /*setContentView(R.layout.activity_titfl);
        
        if (!resumeGame())
            runGame();*/
        
        m_mainMenu = new TITFLMainMenu();
        createMainScreen();              

        startMusic();
    }

    @Override
    protected void onDestroy()
    {
        destroyMusic();

        if (m_game != null)
        {
	        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
	        saveGame();
        }
  
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
    
    public void runGame()
    {
        m_game = new TITFL(this, m_mainMenu);
        
        m_game.run();

        setContentView(R.layout.activity_titfl);	//init townview layout first
        m_layout = new TITFLTownLayout(this);
        m_layout.initialize();        
    }
    
    public boolean resumeGame()
    {
        m_game = new TITFL(this, m_mainMenu);	//Resume game will have numOfPlayer = -1
        
        if (!m_game.resume())
            return false;

        setContentView(R.layout.activity_titfl);	//init townview layout first
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
    
    public void closeTownElement(TITFLPlayer player)
    {
        setContentView(R.layout.activity_titfl);
        m_layout = new TITFLTownLayout(this);
        m_layout.initialize();        

        if (player.isWeekOver())
        {
            player.closeWeek();
            setNextPlayer(player);
        }
    }
    
    public void setNextPlayer(TITFLPlayer oldPlayer)
    {
        m_game.setNextPlayer(oldPlayer);
        ((TITFLTownLayout) m_layout).changePlayer(m_game.getTown().activePlayer());
    }

    public void invalidate()
    {
        m_layout.invalidate();
    }
    
    public void showAbout()
    {
        NoEtapUtility.showAlert(this, "About", "TITFL - Tony In The Fast Lane\n (c) 2013 Team noEtap");
    }
    
    public void showSettings()
    {
        NoEtapUtility.showAlert(this, "Settings", "Under construction...");
    }    

    private void startMusic()
    {
        m_mediaPlayer = new MediaPlayer();
        try
        {
            AssetFileDescriptor descriptor = this.getAssets().openFd("audio/spirit_of_chivalry.ogg");
            m_mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            m_mediaPlayer.prepare();
            m_mediaPlayer.setLooping(true);
            m_mediaPlayer.setVolume(1, 1);
            int delay = 500; // Music starts with little delay.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    m_mediaPlayer.start();
                }
            }, delay);
        }
        catch (Exception e)
        {
        }
    }

    private void destroyMusic()
    {
        m_mediaPlayer.stop();
        m_mediaPlayer.release();
    }

    public final TITFLSettings settings()
    {
        return m_settings;
    }
    
    public final TITFLMainMenu mainMenu()
    {
    	return m_mainMenu;
    }
    
    private void createMainScreen()
    {
    	setContentView(R.layout.main_menu);
    	m_layout = new TITFLMainMenuLayout(this);
    	m_layout.initialize();
    }
}
