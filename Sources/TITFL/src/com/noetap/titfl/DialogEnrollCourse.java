package com.noetap.titfl;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogEnrollCourse 
{    
    private TITFLTownElementSchoolLayout m_parent;
    private TITFLGoods m_goods;
    
    public DialogEnrollCourse(TITFLGoods goods, TITFLTownElementSchoolLayout parent)
    {
        m_parent = parent;
        m_goods = goods;
    }

    public void show()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_parent.activity());

        String message = m_goods.name() + " $" + m_goods.getPrice();
        
        // Set title
        alertDialogBuilder.setTitle("Would you like to enroll?");
     
        // Set dialog message
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNegativeButton("Change My Mind",
                new DialogInterface.OnClickListener() 
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setPositiveButton("Enroll", 
            new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    m_parent.activity().speakOut("To earn the credits, tap the course you wish to do so.", m_parent.element().speechPitch(), m_parent.element().speechRate());
                    m_parent.element().visitor().buy(m_goods, 1, m_parent.element().town().currentWeek());
                    m_parent.greetingText().setText(m_goods.greeting());
                    m_parent.playerView().invalidate();
                    m_parent.updateMyCourses();
                    dialog.dismiss();
                }
            });

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();       
        alertDialog.show();    
    }
}