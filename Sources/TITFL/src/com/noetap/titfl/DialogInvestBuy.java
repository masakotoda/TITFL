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

public class DialogInvestBuy extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLGoods m_goods;
    private ArrayList<TITFLGoods> m_allBuyable;
    private EditText m_quantity;
    
    public DialogInvestBuy(ArrayList<TITFLGoods> allBuyable, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_allBuyable = allBuyable;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_invest_buy);

        ListView list = (ListView) findViewById(R.id.listViewLoans);
        setActionListView(list);

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText("Investment");
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("Please select investment to purchase.");
        
        ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
        imageView.setVisibility(View.GONE);
        
        TextView textView = (TextView) findViewById(R.id.textViewQuantity);
        textView.setVisibility(View.GONE);

        m_quantity = (EditText) findViewById(R.id.editTextQuantity);
        m_quantity.setVisibility(View.GONE);

        LinearLayout buttonBar = (LinearLayout) findViewById(R.id.buttonBarQuantity);
        buttonBar.setVisibility(View.GONE); 

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
                if (m_goods != null)
                {
                    String countStr = m_quantity.getText().toString();
                    int count = NoEtapUtility.parseInt(countStr);
                    m_parent.element().visitor().buy(m_goods, count, m_parent.element().town().currentWeek());
                    m_parent.greetingText().setText(m_goods.greeting());
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
                TITFLGoods goods = m_allBuyable.get(position);
                m_goods = goods;

                TextView nameText = (TextView) findViewById(R.id.textViewName);
                nameText.setText(goods.name());
                
                TextView priceText = (TextView) findViewById(R.id.textViewPrice);
                priceText.setText("Price: $" + goods.getPrice());
                
                ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(goods.getBitmap());
                
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
