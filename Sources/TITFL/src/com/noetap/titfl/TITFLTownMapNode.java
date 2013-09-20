package com.noetap.titfl;

import java.util.ArrayList;

public class TITFLTownMapNode 
{
	public int m_index;
	public int m_x;
	public int m_y;
	public ArrayList<TITFLTownMapNode> m_link;
	public boolean m_visited;
    public TITFLTownMapNode m_parent;
	
	TITFLTownMapNode()
	{
		m_link = new ArrayList<TITFLTownMapNode>();
	}

    public void unvisit()
    {
        m_visited = false;
        m_parent = null;
    }
}
