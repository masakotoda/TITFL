package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DialogApplyLoan extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLItem m_goods;
    private ArrayList<TITFLItem> m_allLoans;
    
    public DialogApplyLoan(ArrayList<TITFLItem> allLoans, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_allLoans = allLoans;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_apply_loan);

        ListView list = (ListView) findViewById(R.id.listViewLoans);
        setActionListView(list);

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText("Apply Loan");
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("Please select type of loan to apply.");
        
        ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
        imageView.setVisibility(View.GONE);

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
                    TITFLGoods goods = (TITFLGoods)m_goods;
                    m_parent.element().visitor().buy(goods, 1, m_parent.element().town().currentWeek());
                    m_parent.activity().speakOut(goods.greeting(), m_parent.element().speechPitch(), m_parent.element().speechRate());
                    m_parent.playerView().invalidate();
                    dismiss();
                }
            }
        });
    }

    void setActionListView(ListView list)
    {
        ListAdapterGoods adapter = new ListAdapterGoods(m_parent.activity(), m_allLoans);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLItem goods = m_allLoans.get(position);
                m_goods = goods;
                TextView nameText = (TextView) findViewById(R.id.textViewName);
                nameText.setText(goods.name());
                
                TextView priceText = (TextView) findViewById(R.id.textViewPrice);
                priceText.setText("Fee: $" + goods.getPrice());
                
                ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(goods.getBitmap());
            }
        });
    }
}
