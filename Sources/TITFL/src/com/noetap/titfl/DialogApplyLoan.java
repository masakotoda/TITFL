package com.noetap.titfl;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogApplyLoan extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLGoods m_goods;
    
    public DialogApplyLoan(TITFLGoods goods, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_goods = goods;
        setContentView(R.layout.dialog_apply_loan);
        setTitle("Would you like to apply for loan?");

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText(goods.name());
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("Fee: $" + goods.getPrice());

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
                m_parent.element().visitor().buy(m_goods, 1, m_parent.element().town().currentWeek());
                m_parent.playerView().invalidate();
                dismiss();
            }
        });
    }
}
