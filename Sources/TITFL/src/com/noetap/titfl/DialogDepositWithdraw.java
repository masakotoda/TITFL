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

        TextView quantityText = (TextView) findViewById(R.id.textViewQuantity);        
        if (deposit)
        {
            setTitle("Deposit");
            quantityText.setText("Amount to Deposit $.");
        }
        else
        {
            setTitle("Withdraw");
            quantityText.setText("Cash: $" + Integer.toString(m_player.cash()));
            quantityText.setText("Amount to Withdraw $.");
        }

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setText("100");

        //TextView cash = (TextView) findViewById(R.id.textViewCash);
        //cash.setText("Cash: $" + Integer.toString(m_player.cash()));

        TextView saving = (TextView) findViewById(R.id.textViewSaving);
        saving.setText(Integer.toString(m_player.saving()));

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
        
        Button resetButton = (Button) findViewById(R.id.buttonReset);
        setButtonActionAmount(resetButton, -1);
        
        Button add20Button = (Button) findViewById(R.id.buttonAdd20);
        setButtonActionAmount(add20Button, 20);

        Button add100Button = (Button) findViewById(R.id.buttonAdd100);
        setButtonActionAmount(add100Button, 100);

        Button add1000Button = (Button) findViewById(R.id.buttonAdd1000);
        setButtonActionAmount(add1000Button, 1000);
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

    private void setButtonActionAmount(Button clicked, final int amount)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                if (amount < 0)
                {
                    m_quantity.setText("0");
                }
                else
                {
                    String countStr = m_quantity.getText().toString();
                    int count = NoEtapUtility.parseInt(countStr);
                    m_quantity.setText(Integer.toString(count + amount));
                }
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
                    if (m_player.cash() <= 0)
                    {
                        NoEtapUtility.showAlert(m_parent.activity(), "Sorry", "You have no cash to make deposit. Make some money and come again!");
                    }
                    else
                    {
                        if (count > m_player.cash())
                            count = m_player.cash();
                        m_parent.element().visitor().deposit(count);
                    }
                }
                else
                {
                    if (m_player.saving() <= 0)
                    {
                        NoEtapUtility.showAlert(m_parent.activity(), "Sorry", "You have no money in your account. Save today, withdraw tomorrow!");
                    }
                    else
                    {
                        if (count > m_player.saving())
                            count = m_player.saving();
                        m_parent.element().visitor().withdraw(count);
                    }
                }
                m_parent.playerView().invalidate();
                dismiss();
            }
        });
    }
}
