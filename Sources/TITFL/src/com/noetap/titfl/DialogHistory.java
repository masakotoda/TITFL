package com.noetap.titfl;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class DialogHistory extends DialogSettings
{    
    void setContentView()
    {
    	setContentView(R.layout.dialog_history);
    }

    public DialogHistory(TITFLActivity activity)
    {
        super(activity);

        loadHistory();

        Button buttonClear = (Button) findViewById(R.id.buttonClear);
        setButtonActionClear(buttonClear);
    }

    private void setButtonActionClear(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	m_activity.settings().clearHisotry();
                loadHistory();
            }
        });
    }

    private void loadHistory()
    {
        ArrayList<TITFLSQLiteOpenHelper.HistoryItem> items = m_activity.settings().loadHistory();
        ListView listView = (ListView) findViewById(R.id.listViewHistory);
        ListAdapterHistory adapter = new ListAdapterHistory(m_activity, items);
        listView.setAdapter(adapter);
    }
}
