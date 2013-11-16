package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

public class TITFLTownElementBankLayout extends TITFLTownElementLayout
{
    private ArrayList<TITFLItem> m_allLoans;
    private ArrayList<TITFLItem> m_allBuyables;
    private ArrayList<TITFLItem> m_allSellables;

    public TITFLTownElementBankLayout(Activity activity, TITFLTownElement element) 
    {
        super(activity, element);

        m_allLoans = new ArrayList<TITFLItem>();
        m_allBuyables = new ArrayList<TITFLItem>();
        m_allSellables = new ArrayList<TITFLItem>();
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

        GridView gridView = (GridView) m_activity.findViewById(R.id.gridView);
        setGridView(gridView);
            
        /*
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
        */
    }

    /*
    private void setBuyableAction(ListView list)
    {
        m_allBuyables.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (!g.isLoan())
                m_allBuyables.add(g);
        }
                
        final TITFLTownElementLayout layout = this;
        //ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_allBuyables);
        ListAdapterGoods adapter = new ListAdapterGoods(m_activity, m_allBuyables);
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
        //ArrayAdapter<TITFLGoods> adapter = new ArrayAdapter<TITFLGoods>(m_activity, android.R.layout.simple_list_item_1, m_allLoans);
        ListAdapterGoods adapter = new ListAdapterGoods(m_activity, m_allLoans);
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
        //ListView sellable = (ListView) m_activity.findViewById(R.id.listViewSellable);
        //setSellableAction(sellable);
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
    */
    
    private void setGridView(GridView gridView)
    {
        ArrayList<ListAdapterPicture.PictureItem> actions = new ArrayList<ListAdapterPicture.PictureItem>();

        ListAdapterPicture.PictureItem item;
        Bitmap bmHappy = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.event_happy);
        Bitmap bmUnhappy = BitmapFactory.decodeResource(m_activity.getResources(), R.drawable.event_unhappy);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Deposit";
        item.m_picture = bmHappy;
        actions.add(item);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Withdraw";
        item.m_picture = bmUnhappy;
        actions.add(item);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Apply Loan";
        item.m_picture = bmHappy;
        actions.add(item);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Loan Payment";
        item.m_picture = bmUnhappy;
        actions.add(item);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Invest - Buy";
        item.m_picture = bmHappy;
        actions.add(item);

        item = new ListAdapterPicture.PictureItem();
        item.m_label = "Invest - Sell";
        item.m_picture = bmUnhappy;
        actions.add(item);

        ListAdapterPicture adapter = new ListAdapterPicture(m_activity, actions);
            
        gridView.setNumColumns(2);
        gridView.setAdapter((ListAdapter)adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
            {
                switch (arg2)
                {
                case 0:
                    deposit();
                    break;
                case 1:
                    withdraw();
                    break;
                case 2:
                    applyLoan();
                    break;
                case 3:
                    loanPayment();
                    break;
                case 4:
                    investBuy();
                    break;
                case 5:
                    investSell();
                    break;
                }                
            }            
        });
    }
    
    private void deposit()
    {
        DialogDepositWithdraw dialog = new DialogDepositWithdraw(m_element.visitor(), true, this);
        dialog.show();
    }
    
    private void withdraw()
    {
        DialogDepositWithdraw dialog = new DialogDepositWithdraw(m_element.visitor(), false, this);
        dialog.show();
    }
    
    private void applyLoan()
    {
        m_allLoans.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (g.isLoan())
                m_allLoans.add(g);
        }
        
        DialogApplyLoan dialog = new DialogApplyLoan(m_allLoans, this);
        dialog.show();
    }
    
    private void loanPayment()
    {
        
    }
    
    private void investBuy()
    {
        m_allBuyables.clear();
        for (TITFLGoods g : m_element.merchandise())
        {
            if (!g.isLoan())
                m_allBuyables.add(g);
        }

        DialogInvestBuySell dialog = new DialogInvestBuySell(m_allBuyables, this, true);
        dialog.show();
    }
    
    private void investSell()    
    {
        m_allSellables.clear();
        
        for (TITFLBelonging b : m_element.visitor().belongings())
        {
            if (b.goodsRef().townelement() == m_element && !b.goodsRef().isLoan())
                m_allSellables.add(b);
        }

        if (m_allSellables.size() == 0)
        {
            NoEtapUtility.showAlert(m_activity, "Sorry", "You have nothing to sell.");
            return;
        }

        DialogInvestBuySell dialog = new DialogInvestBuySell(m_allSellables, this, false);
        dialog.show();
    }
}
