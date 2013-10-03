package com.noetap.titfl;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogPurchaseGoods 
{    
    private TITFLTownElementLayout m_parent;
    private TITFLGoods m_goods;
    private EditText m_quantity;
    private Dialog m_dialog;
    
    public DialogPurchaseGoods(TITFLGoods goods, TITFLTownElementLayout parent)
    {
        m_parent = parent;
        m_goods = goods;
    }

    public void show()
    {        
        m_dialog = new Dialog(m_parent.activity());
        m_dialog.setContentView(R.layout.purchase_goods);
        m_dialog.setTitle("Would you like to buy?");

        m_quantity = (EditText) m_dialog.findViewById(R.id.editTextQuantity);
        m_quantity.setText("1");

        TextView nameText = (TextView) m_dialog.findViewById(R.id.textViewName);
        nameText.setText(m_goods.name());
        
        TextView priceText = (TextView) m_dialog.findViewById(R.id.textViewPrice);
        priceText.setText("$" + m_goods.getPrice());

        Button add1Button = (Button) m_dialog.findViewById(R.id.buttonAdd1);
        setButtonActionAdd(add1Button, 1);
        
        Button add10Button = (Button) m_dialog.findViewById(R.id.buttonAdd10);
        setButtonActionAdd(add10Button, 10);

        Button add100Button = (Button) m_dialog.findViewById(R.id.buttonAdd100);
        setButtonActionAdd(add100Button, 100);

        Button cancelButton = (Button) m_dialog.findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) m_dialog.findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);

        m_dialog.show();
    }

    private void setButtonActionAdd(Button clicked, final int addition)
    {
        clicked.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                String countStr = m_quantity.getText().toString();
                int count = NoEtapUtility.parseInt(countStr);
                countStr = Integer.toString(count + addition);
                m_quantity.setText(countStr);
            }
        });
    }
    
    private void setButtonActionCancel(Button clicked)
    {
        clicked.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                m_dialog.dismiss();
            }
        });
    }
    
    private void setButtonActionOk(Button clicked)
    {
        clicked.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                String countStr = m_quantity.getText().toString();
                int count = NoEtapUtility.parseInt(countStr);
                for (int i = 0; i < count; i++)
                    m_parent.element().visitor().buy(m_goods, m_parent.element().town().currentWeek());
                m_parent.greetingText().setText(m_goods.greeting());
                m_parent.playerView().invalidate();
                m_dialog.dismiss();
            }
        });
    }
}
