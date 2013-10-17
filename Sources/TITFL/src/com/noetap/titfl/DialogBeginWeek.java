package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DialogBeginWeek extends Dialog
{    
    public DialogBeginWeek(String title, ArrayList<String> events, Activity activity)
    {
        super(activity);

        setContentView(R.layout.dialog_begin_week);
        setTitle(title);

        ListView list = (ListView) findViewById(R.id.listViewEvents);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_smallfont, events);
        list.setAdapter(adapter);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
    }

    private void setButtonActionOk(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                dismiss();
            }
        });
    }
}
