package com.noetap.titfl;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogSellBelonging extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLBelonging m_belonging;
    private EditText m_quantity;
    
    public DialogSellBelonging(TITFLBelonging belonging, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_belonging = belonging;
        setContentView(R.layout.dialog_sell_belonging);
        setTitle("Would you like to sell?");

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setText(Integer.toString(m_belonging.unit()));

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText(m_belonging.goodsRef().name());
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("$" + m_belonging.goodsRef().getPrice());

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
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                String countStr = m_quantity.getText().toString();
                int count = NoEtapUtility.parseInt(countStr);
                if (count > m_belonging.unit())
                    count = m_belonging.unit();
                m_parent.element().visitor().sell(m_belonging, count);
                m_parent.playerView().invalidate();
                m_parent.updateSellable();
                dismiss();
            }
        });
    }
}
