package com.noetap.titfl;
import java.util.Locale;

import android.app.Activity;
import android.speech.tts.TextToSpeech;

public class TITFLTextToSpeech implements TextToSpeech.OnInitListener 
{
    private TextToSpeech m_tts;
 
    public TITFLTextToSpeech(Activity activity)
    {
        m_tts = new TextToSpeech(activity, this); 
    }

    public void destroy() 
    {
        if (m_tts != null) 
        {
            m_tts.stop();
            m_tts.shutdown();
        }
    }

    @Override
    public void onInit(int status) 
    {
        if (status == TextToSpeech.SUCCESS) 
        {
            int result = m_tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) 
            {
                ;
            } 
            else
            {
                //speakOut("Speech engine started.");
            }
        } 
        else 
        {
            ;
        }
    }

    public void speakOut(String text, float pitch, float speechRate) 
    {
        m_tts.setPitch(pitch);
        m_tts.setSpeechRate(speechRate);
        m_tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}

