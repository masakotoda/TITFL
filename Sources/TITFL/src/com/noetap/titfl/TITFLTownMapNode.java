package com.noetap.titfl;

import java.util.ArrayList;

public class TITFLTownMapNode 
{
    private int m_index;
    private int m_x;
    private int m_y;
    private boolean m_occupied;
    private ArrayList<TITFLTownMapNode> m_link;
    private boolean m_visited;
    private TITFLTownMapNode m_parent;
    
    public TITFLTownMapNode()
    {
        m_link = new ArrayList<TITFLTownMapNode>();
    }

    public void unvisit()
    {
        m_visited = false;
        m_parent = null;
    }
    
    public boolean visited()
    {
        return m_visited;
    }
    
    public void setVisited(boolean visited)
    {
        m_visited = visited;
    }
    
    public TITFLTownMapNode parent()
    {
        return m_parent;
    }
    
    public void setParent(TITFLTownMapNode parent)
    {
        m_parent = parent;
    }
    
    public int index()
    {
        return m_index;
    }
    
    public void setIndex(int index)
    {
        m_index = index;
    }
    
    public int x()
    {
        return m_x;
    }
    
    public void setX(int x)
    {
        m_x = x;
    }
    
    public int y()
    {
        return m_y;
    }
    
    public void setY(int y)
    {
        m_y = y;
    }

    public ArrayList<TITFLTownMapNode> link()
    {
        return m_link;
    }
    
    public boolean occupied()
    {
        return m_occupied;
    }
    
    public void setOccupied(boolean occupied)
    {
        m_occupied = occupied;
    }
}
