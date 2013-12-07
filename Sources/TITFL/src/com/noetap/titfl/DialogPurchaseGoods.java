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

        setContentView(R.layout.dialog_purchase_goods);
        setTitle("Would you like to buy?");

        int maxUnits = defaultPurchaseUnits(goods.maxUnits());
        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setText("1");

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText(m_goods.name());
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("$" + m_goods.getPrice());

        // If maxUnits is 1, no need to show quantity. (Masako)
        if (goods.maxUnits() == 1)
        {
            View buttonBar = findViewById(R.id.buttonBarQuantity);
            buttonBar.setVisibility(View.INVISIBLE);
            m_quantity.setVisibility(View.INVISIBLE);
            findViewById(R.id.textViewQuantity).setVisibility(View.INVISIBLE);
        }

        Button resetButton = (Button) findViewById(R.id.buttonReset);
        setButtonActionReset(resetButton);
        
        Button add1Button = (Button) findViewById(R.id.buttonAdd1);
        setButtonActionAdd(add1Button, 1);
        
        Button add10Button = (Button) findViewById(R.id.buttonAdd10);
        if (maxUnits >= 10)
        {
        	setButtonActionAdd(add10Button, 10);
        	m_quantity.setText("10");
        }
        else
        {
        	add10Button.setVisibility(View.GONE);
        }

        Button add100Button = (Button) findViewById(R.id.buttonAdd100);
        if (maxUnits >= 100)
        {
        	setButtonActionAdd(add100Button, 100);
        	m_quantity.setText("100");
        }
        else
        {
        	add100Button.setVisibility(View.GONE);
        }

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);

        ImageView imageGoods = (ImageView) findViewById(R.id.imageViewGoods);
        Bitmap bm = NoEtapUtility.getBitmap(m_parent.activity(), TITFLActivity.pathGoods + m_goods.id() + ".png");
        if (bm != null)
            imageGoods.setImageBitmap(bm);
        
    }

    private int defaultPurchaseUnits(int maxUnits)
    {
    	int units = 1;
    	
    	if (maxUnits >= 10 && maxUnits <= 99) units = 10;
    	else if (maxUnits >= 100 ) units = 100;
    	
    	return units;
    }
    
    private void setButtonActionReset(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	int maxUnits = m_goods.maxUnits();
            	if (maxUnits < 10)
                   	m_quantity.setText("1");
            	else if (maxUnits >= 10 && maxUnits < 100)
                   	m_quantity.setText("10");
            	else if (maxUnits >= 100)
                   	m_quantity.setText("100");
            }
        });
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
                //countStr = Integer.toString(count + addition);
                //m_quantity.setText(countStr);
                int maxUnits = m_goods.maxUnits();
                if (count + addition <= maxUnits)
                {
                	countStr = Integer.toString(count + addition);
	                m_quantity.setText(countStr);
                }
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
                m_parent.activity().speakOut(m_goods.greeting(), m_parent.element().speechPitch(), m_parent.element().speechRate());
                m_parent.playerView().invalidate();
                m_parent.updateOutfit();
                m_parent.updateSellable();
                dismiss();
            }
        });
    }
}
