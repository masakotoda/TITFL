package com.noetap.titfl;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class DialogApplyJob extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLTownElement m_element;
    
    public DialogApplyJob(TITFLTownElement element, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_element = element;

        setContentView(R.layout.apply_job);
        setTitle("Select your job");

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);
        
        ListView list = (ListView) findViewById(R.id.listViewJobs);
        setListAction(list);
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
    
    private void setListAction(ListView list)
    {
        final Activity activity = m_element.town().activity();
        ArrayAdapter<TITFLJob> adapter = new ArrayAdapter<TITFLJob>(activity, android.R.layout.simple_list_item_1, m_element.jobs());
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                TITFLJob job = m_element.jobs().get(position);
                m_element.visitor().applyJob(job);
                
                if (job == m_element.visitor().job())
                    NoEtapUtility.showAlert(activity, "Congrats", "You got a job!");
                else
                    NoEtapUtility.showAlert(activity, "Sorry", "You didn't get a job.");
                    
                m_parent.playerView().invalidate();
                dismiss();                
            }
        });
    }
}
