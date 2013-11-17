package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DialogLoanPay extends Dialog
{    
    private TITFLTownElementLayout m_parent;
    private TITFLBelonging m_item;
    private ArrayList<TITFLItem> m_allItems;
    private EditText m_payment;
    
    public DialogLoanPay(ArrayList<TITFLItem> allItems, TITFLTownElementLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_allItems = allItems;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loan_pay);

        ListView list = (ListView) findViewById(R.id.listViewLoans);
        setActionListView(list);

        TextView nameText = (TextView) findViewById(R.id.textViewName);
        nameText.setText("Loan");
        
        TextView priceText = (TextView) findViewById(R.id.textViewPrice);
        priceText.setText("Please select loan type to make payment.");
        
        ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
        imageView.setVisibility(View.GONE);
        
        TextView textView = (TextView) findViewById(R.id.textViewPayment);
        textView.setVisibility(View.GONE);

        m_payment = (EditText) findViewById(R.id.editTextPayment);
        m_payment.setVisibility(View.GONE);

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
                if (m_item == null)
                    return;
                
                String paymentStr = m_payment.getText().toString();
                int payment = NoEtapUtility.parseInt(paymentStr);
                m_parent.element().visitor().makePayment(m_item, payment);
                m_parent.playerView().invalidate();
                dismiss();
            }
        });
    }

    void setActionListView(ListView list)
    {
        ListAdapterGoods adapter = new ListAdapterGoods(m_parent.activity(), m_allItems);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                m_item = (TITFLBelonging) m_allItems.get(position);

                TextView nameText = (TextView) findViewById(R.id.textViewName);
                nameText.setText(m_item.name());
                
                int remaining = m_item.loanAmount() - m_item.completedPayment();
                TextView priceText = (TextView) findViewById(R.id.textViewPrice);
                priceText.setText("Principal Balance: $" + remaining + " / " + m_item.loanAmount());
                
                ImageView imageView = (ImageView) findViewById(R.id.imageViewGoods);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(m_item.getBitmap());
                
                TextView textView = (TextView) findViewById(R.id.textViewPayment);
                textView.setVisibility(View.VISIBLE);

                m_payment.setVisibility(View.VISIBLE);
                m_payment.setText(Integer.toString(Math.min(100, remaining)));                
            }
        });
    }
}
