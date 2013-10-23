package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class DialogBeginWeek extends Dialog
{    
    public DialogBeginWeek(String title, ArrayList<ListAdapterBeginWeek.BeginWeekItem> events, Activity activity)
    {
        super(activity);

        setContentView(R.layout.dialog_begin_week);
        setTitle(title);

        ListView list = (ListView) findViewById(R.id.listViewEvents);
        ListAdapterBeginWeek adapter = new ListAdapterBeginWeek(activity, events);        
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
