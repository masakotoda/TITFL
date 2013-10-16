package com.noetap.titfl;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogDepositWithdraw extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLPlayer m_player;
    private EditText m_quantity;
    private boolean m_deposit;
    
    public DialogDepositWithdraw(TITFLPlayer player, boolean deposit, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_player = player;
        m_deposit = deposit;
        setContentView(R.layout.dialog_deposit_withdraw);
        if (deposit)
            setTitle("How much would you like to deposit?");
        else
            setTitle("How much would you like to withdraw?");

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setText("100");

        TextView cash = (TextView) findViewById(R.id.textViewCash);
        cash.setText("Cash: $" + Integer.toString(m_player.cash()));
        
        TextView saving = (TextView) findViewById(R.id.textViewSaving);
        saving.setText("Saving: $" + Integer.toString(m_player.saving()));

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
    }

    private void setButtonActionCancel(Button clicked)
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
    
    private void setButtonActionOk(Button clicked)
    {
        if (m_deposit)
            clicked.setText("Deposit");
        else
            clicked.setText("Withdraw");
            
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                String countStr = m_quantity.getText().toString();
                int count = NoEtapUtility.parseInt(countStr);
                if (m_deposit)
                {
                    if (count > m_player.cash())
                        count = m_player.cash();
                    m_parent.element().visitor().deposit(count);
                }
                else
                {
                    if (count > m_player.saving())
                        count = m_player.saving();
                    m_parent.element().visitor().withdraw(count);
                }
                m_parent.playerView().invalidate();
                dismiss();
            }
        });
    }
}
