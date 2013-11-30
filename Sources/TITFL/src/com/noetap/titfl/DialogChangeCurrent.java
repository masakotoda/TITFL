package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class DialogChangeCurrent extends Dialog
{    
    private TITFLTownElementHomeLayout m_parent;
    private TITFLBelonging m_item;
    private ArrayList<TITFLItem> m_allItems;
    
    public DialogChangeCurrent(String button, ArrayList<TITFLItem> allItems, TITFLTownElementHomeLayout parent)
    {
        super(parent.activity());
        m_parent = parent;
        m_allItems = allItems;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_current);

        ListView list = (ListView) findViewById(R.id.listViewGoods);
        setActionListView(list);

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        setButtonActionCancel(cancelButton);

        Button okButton = (Button) findViewById(R.id.buttonOK);
        setButtonActionOk(okButton);
        okButton.setText(button);
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

                if (m_item.goodsRef().isOutfit())
                    m_parent.element().visitor().setOutfit((TITFLGoods)m_item.goodsRef());
                
                if (m_item.goodsRef().isTransportation())
                    m_parent.element().visitor().setTransportation((TITFLGoods)m_item.goodsRef());

                m_parent.playerView().invalidate();
                m_parent.updateOutfit();
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
            }
        });
    }
}
