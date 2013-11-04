package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class DialogApplyJob extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLTownElement m_element;
    private Activity m_activity;
    private TITFLJob m_job = null;

    public DialogApplyJob(TITFLTownElement element, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_element = element;
        m_activity = m_element.town().activity();

        setContentView(R.layout.dialog_apply_job);
        setTitle("Select a job to apply");

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);
        
        Button applyButton = (Button) findViewById(R.id.buttonApply);
        setButtonActionApply(applyButton);

        ListView list = (ListView) findViewById(R.id.listViewJobs);
        setListAction(list);

        selectJob(-1);
    }
    
    private void setButtonActionCancel(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View arg0) 
            {
                dismiss();
            }
        });
    }
    
    private void setButtonActionApply(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (m_job == null)
                {
                    NoEtapUtility.showAlert(m_activity, "Info", "Please select a job from the job list first.");
                    return;
                }

                if (m_job == m_element.visitor().job())
                {
                    NoEtapUtility.showAlert(m_activity, "Hey", "That's your current job...");
                    return;
                }

                m_element.visitor().applyJob(m_job);

                if (m_job == m_element.visitor().job())
                    NoEtapUtility.showAlert(m_activity, "Congrats", "You got a job!");

                m_parent.playerView().invalidate();
                dismiss();
            }
        });
    }

    private void setListAction(ListView list)
    {
        ArrayAdapter<TITFLJob> adapter = new ArrayAdapter<TITFLJob>(m_activity, android.R.layout.simple_list_item_1, m_element.jobs());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                selectJob(position);
            }
        });
    }

    private void selectJob(int position)
    {
        TextView textWage = (TextView) findViewById(R.id.textViewWage);
        TextView textDressCode = (TextView) findViewById(R.id.textViewDressCode);
        TextView textQualification = (TextView) findViewById(R.id.textViewQualification);
        ListView list = (ListView) findViewById(R.id.listViewQualification);

        if (position < 0)
        {
            textWage.setText("");
            textDressCode.setText("");
            textQualification.setText("");
            return;
        }

        m_job = m_element.jobs().get(position);

        textQualification.setText("Description:");
        textWage.setText("Wage: $ " + Integer.toString(m_job.wage()));
        textDressCode.setText("Dress Code: " + m_job.dressCode(m_job.dressCode()));

        ArrayList<String> ary = new ArrayList<String>();
        if (m_job.requiredCharacter().m_goodLooking > 0)
            ary.add("Pretty women & handsome are welcomed!");
        if (m_job.requiredCharacter().m_hardWorking > 0)
            ary.add("Hard workers are welcomed!");
        if (m_job.requiredCharacter().m_intelligent > 0)
            ary.add("Smart people are welcomed!");
        if (m_job.requiredCharacter().m_physical > 0)
            ary.add("Strong men & muscle women are welcomed!");
        if (m_job.requiredCharacter().m_lucky > 0)
            ary.add("Lucky people are welcomed!");

        boolean education = false;
        if (m_job.requiredEducation().m_basic > 0)
        {
            education = true;
            ary.add("Basic education.");
        }
        if (m_job.requiredEducation().m_business_finance > 0)
        {
            education = true;
            ary.add("Degree/credits in Business/Finance field.");
        }
        if (m_job.requiredEducation().m_engineering > 0)
        {
            education = true;
            ary.add("Degree/credits in Engineering field.");
        }
        if (m_job.requiredEducation().m_academic > 0)
        {
            education = true;
            ary.add("Degree/credits in Academic field.");
        }
        if (education == false)
        {
            ary.add("No education required.");
        }

        boolean experience = false;
        if (m_job.requiredExperience().m_basic > 0)
        {
            experience = true;
            ary.add("Basic work experience.");
        }
        if (m_job.requiredExperience().m_business_finance > 0)
        {
            experience = true;
            ary.add("Work experience in Business/Finance field.");
        }
        if (m_job.requiredExperience().m_engineering > 0)
        {
            experience = true;
            ary.add("Work experience in Engineering field.");
        }
        if (m_job.requiredExperience().m_academic > 0)
        {
            experience = true;
            ary.add("Work experience Academic field.");
        }
        if (experience == false)
        {
            ary.add("No experience required.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_activity, R.layout.list_smallfont, ary);
        list.setAdapter(adapter);
    }
}
