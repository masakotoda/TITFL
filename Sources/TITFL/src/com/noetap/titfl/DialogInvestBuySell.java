package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogInvestBuySell extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLItem m_item;
    private ArrayList<TITFLItem> m_allBuyable;
    private EditText m_quantity;
    private boolean m_buy;
    
    public DialogInvestBuySell(ArrayList<TITFLItem> allBuyable, TITFLTownElementLayout parent, boolean buy)
    {
        super(parent.activity());
        m_parent = parent;
        m_allBuyable = allBuyable;
        m_buy = buy;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_invest_buy);

        ListView list = (ListView) findViewById(R.id.listViewLoans);
        setActionListView(list);

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText("Investment");
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        if (m_buy)
            priceText.setText("Please select investment to purchase.");
        else
            priceText.setText("Please select investment to sell.");
        
        ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
        imageView.setVisibility(View.GONE);
        
        TextView textView = (TextView) findViewById(R.id.textViewQuantity);
        textView.setVisibility(View.GONE);

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setVisibility(View.GONE);

        //int maxUnits = 500;
        Button resetButton = (Button) findViewById(R.id.buttonReset);
        setButtonActionReset(resetButton);
        
        Button add1Button = (Button) findViewById(R.id.buttonAdd1);
        setButtonActionAdd(add1Button, 1);
        
        Button add10Button = (Button) findViewById(R.id.buttonAdd10);
        setButtonActionAdd(add10Button, 10);
      
        Button add100Button = (Button) findViewById(R.id.buttonAdd100);
        setButtonActionAdd(add100Button, 100);
                
        LinearLayout buttonBar = (LinearLayout) findViewById(R.id.buttonBarQuantity);
        buttonBar.setVisibility(View.GONE); 

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
    }

    private void setButtonActionReset(Button clicked)
    {
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	m_quantity.setText("1");
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
        if (m_buy)
            clicked.setText("Buy");
        else
            clicked.setText("Sell");
            
        clicked.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                if (m_item == null)
                    return;
                
                String countStr = m_quantity.getText().toString();
                int count = NoEtapUtility.parseInt(countStr);

                if (m_buy)
                {
                    TITFLGoods goods = (TITFLGoods) m_item;
                    m_parent.element().visitor().buy(goods, count, m_parent.element().town().currentWeek());
                    m_parent.greetingText().setText(goods.greeting());
                    m_parent.playerView().invalidate();
                    m_parent.updateSellable();
                    dismiss();
                }
                else
                {
                    TITFLBelonging belonging = (TITFLBelonging) m_item;
                    if (count > belonging.unit())
                        count = belonging.unit();
                    m_parent.element().visitor().sell(belonging, count);
                    m_parent.playerView().invalidate();
                    m_parent.updateSellable();
                    dismiss();
                }
            }
        });
    }

    void setActionListView(ListView list)
    {
        ListAdapterGoods adapter = new ListAdapterGoods(m_parent.activity(), m_allBuyable);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                m_item = m_allBuyable.get(position);

                TextView nameText = (TextView) findViewById(R.id.textViewName);
                nameText.setText(m_item.name());
                
                TextView priceText = (TextView) findViewById(R.id.textViewPrice);
                priceText.setText("Price: $" + m_item.getPrice());
                
                ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(m_item.getBitmap());
                
                TextView textView = (TextView) findViewById(R.id.textViewQuantity);
                textView.setVisibility(View.VISIBLE);

                m_quantity.setVisibility(View.VISIBLE);
                m_quantity.setText("1");
                
                LinearLayout buttonBar = (LinearLayout) findViewById(R.id.buttonBarQuantity);
                buttonBar.setVisibility(View.VISIBLE); 
            }
        });
    }
}
