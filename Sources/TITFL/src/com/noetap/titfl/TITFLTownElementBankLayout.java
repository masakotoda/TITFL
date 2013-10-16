package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class TITFLTownElementBankLayout extends TITFLTownElementLayout
{
    private ArrayList<TITFLGoods> m_allLoans;
    private ArrayList<TITFLGoods> m_allBuyables;
    private ArrayList<TITFLBelonging> m_allSellables;

    public TITFLTownElementBankLayout(Activity activity, TITFLTownElement element) 
    {
        super(activity, element);

        m_allLoans = new ArrayList<TITFLGoods>();
        m_allBuyables = new ArrayList<TITFLGoods>();
        m_allSellables = new ArrayList<TITFLBelonging>();
    }
    
    @Override
    public void invalidate() 
    {
        super.invalidate();
    }

    @Override
    public void initialize()
    {
        super.initialize();

        ListView buyable = (ListView) m_activity.findViewById(R.id.listViewBuyable);
        setBuyableAction(buyable);

        ListView sellable = (ListView) m_activity.findViewById(R.id.listViewSellable);
        setSellableAction(sellable);
        
        ListView loan = (ListView) m_activity.findViewById(R.id.listViewLoans);
        setLoanAction(loan);

        Button buttonDeposit = (Button) m_activity.findViewById(R.id.buttonDeposit);
        setDepositAction(buttonDeposit);

        Button buttonWithdraw = (Button) m_activity.findViewById(R.id.buttonWithdraw);
        setWithdrawAction(buttonWithdraw);
    }

    private void setBuyableAction(ListView list)
    {
        m_allBuyables.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (!g.isLoan())
                m_allBuyables.add(g);
        }
                
        final TITFLTownElementLayout layout = this;
        ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_allBuyables);
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLGoods goods = m_element.merchandise().get(position);
                DialogPurchaseGoods dialog = new DialogPurchaseGoods(goods, layout);
                dialog.show();
            }
        });
    }

    private void setLoanAction(ListView list)
    {
        m_allLoans.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (g.isLoan())
                m_allLoans.add(g);
        }
        
        final TITFLTownElementBankLayout layout = this;
        ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_allLoans);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                final TITFLGoods goods = m_allLoans.get(position);
                DialogApplyLoan dialog = new DialogApplyLoan(goods, layout);
                dialog.show();
            }
        });
    }

    private void setSellableAction(final ListView list)
    {
        m_allSellables.clear();
        
        for (TITFLBelonging b : m_element.visitor().belongings())
        {
            if (b.goodsRef().townelement() == m_element && !b.goodsRef().isLoan())
                m_allSellables.add(b);
        }
        
        final TITFLTownElementBankLayout layout = this;
        ArrayAdapter<TITFLBelonging> adapter = new ArrayAdapter<TITFLBelonging>(m_activity, android.R.layout.simple_list_item_1, m_allSellables);
        list.setAdapter(adapter);        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
                TITFLBelonging belonging = m_allSellables.get(position);
                DialogSellBelonging dialog = new DialogSellBelonging(belonging, layout);
                dialog.show();
            }
        });
    }
    
    @Override
    public void updateSellable()
    {
        ListView sellable = (ListView) m_activity.findViewById(R.id.listViewSellable);
        setSellableAction(sellable);
    }

    private void setDepositAction(Button clicked)
    {
        final TITFLTownElementLayout layout = this;
        clicked.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogDepositWithdraw dialog = new DialogDepositWithdraw(m_element.visitor(), true, layout);
                dialog.show();
            }
        });
    }

    private void setWithdrawAction(Button clicked)
    {
        final TITFLTownElementLayout layout = this;
        clicked.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogDepositWithdraw dialog = new DialogDepositWithdraw(m_element.visitor(), false, layout);
                dialog.show();
            }
        });
    }
}
