package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DialogPlayerStatus extends Dialog
{    
    public DialogPlayerStatus(TITFLPlayer player, Activity activity)
    {
        super(activity);

        setContentView(R.layout.dialog_player_status);
        setTitle(player.alias() + "'s Status");

        {
            ListView list = (ListView) findViewById(R.id.listViewBelonging);
            ArrayAdapter<TITFLBelonging> adapter = new ArrayAdapter<TITFLBelonging>(activity, R.layout.list_smallfont, player.belongings());
            list.setAdapter(adapter);
        }
        {
            ArrayList<String> ary = new ArrayList<String>();
            ary.add("Basic: " + Integer.toString(player.education().m_basic));
            ary.add("Business: " + Integer.toString(player.education().m_business_finance));
            ary.add("Engineer: " + Integer.toString(player.education().m_engineering));
            ary.add("Academic: " + Integer.toString(player.education().m_academic));
            ListView list = (ListView) findViewById(R.id.listViewEducation);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_smallfont, ary);
            list.setAdapter(adapter);
        }
        {
            ArrayList<String> ary = new ArrayList<String>();
            ary.add("Basic: " + Integer.toString(player.experience().m_basic));
            ary.add("Business: " + Integer.toString(player.experience().m_business_finance));
            ary.add("Engineer: " + Integer.toString(player.experience().m_engineering));
            ary.add("Academic: " + Integer.toString(player.experience().m_academic));
            ListView list = (ListView) findViewById(R.id.listViewExperience);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_smallfont, ary);
            list.setAdapter(adapter);
        }
        {
            ArrayList<String> ary = new ArrayList<String>();
            ary.add("Wealth: " + Integer.toString(player.satisfaction().m_wealth));
            ary.add("Education: " + Integer.toString(player.satisfaction().m_education));
            ary.add("Carrier: " + Integer.toString(player.satisfaction().m_education));
            ary.add("Life: " + Integer.toString(player.satisfaction().m_life));
            ary.add("Health: " + Integer.toString(player.satisfaction().m_health));
            ary.add("Happiness: " + Integer.toString(player.happiness()));
            ListView list = (ListView) findViewById(R.id.ListViewSatisfaction);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_smallfont, ary);
            list.setAdapter(adapter);
        }
        {
            ArrayList<String> ary = new ArrayList<String>();
            ary.add("Cash: $" + Integer.toString(player.cash()));
            ary.add("Saving: $" + Integer.toString(player.saving()));
            ary.add("Home: " + player.home().name());
            if (player.outfit() == null)
            {
                ary.add("Outfit: Naked");
            }
            else
            {
                ary.add("Outfit: " + player.outfit().name());
            }
            ary.add("Transp: " + player.transportation().name());
            if (player.job() == null)
            {
                ary.add("Work as: Jobless");
                ary.add("Work at: N/A");
            }
            else
            {
                ary.add("Work as: " + player.job().name());
                ary.add("Work at: " + player.job().townelement().name());
            }
            ListView list = (ListView) findViewById(R.id.listViewOther);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_smallfont, ary);
            list.setAdapter(adapter);
        }

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
