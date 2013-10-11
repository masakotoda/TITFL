package com.noetap.titfl;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogPurchaseGoods extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLGoods m_goods;
    private EditText m_quantity;
    
    public DialogPurchaseGoods(TITFLGoods goods, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_goods = goods;

        setContentView(R.layout.purchase_goods);
        setTitle("Would you like to buy?");

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setText("1");

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText(m_goods.name());
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("$" + m_goods.getPrice());

        Button add1Button = (Button) findViewById(R.id.buttonAdd1);
        setButtonActionAdd(add1Button, 1);
        
        Button add10Button = (Button) findViewById(R.id.buttonAdd10);
        setButtonActionAdd(add10Button, 10);

        Button add100Button = (Button) findViewById(R.id.buttonAdd100);
        setButtonActionAdd(add100Button, 100);

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);

        ImageView imageGoods = (ImageView) findViewById(R.id.imageViewGoods);
        Bitmap bm = NoEtapUtility.getBitmap(m_parent.activity(), m_goods.id() + ".png");
        if (bm != null)
            imageGoods.setImageBitmap(bm);
        
    }

    private void setButtonActionAdd(Button clicked, final int addition)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
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
                m_parent.element().visitor().buy(m_goods, count, m_parent.element().town().currentWeek());
                m_parent.greetingText().setText(m_goods.greeting());
                m_parent.playerView().invalidate();
                m_parent.updateSellable();
                dismiss();
            }
        });
    }
}
