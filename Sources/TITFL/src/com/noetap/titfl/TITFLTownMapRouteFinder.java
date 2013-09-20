package com.noetap.titfl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TITFLTownMapRouteFinder
{
    ArrayList<TITFLTownMapNode> m_nodes;

    TITFLTownMapRouteFinder(ArrayList<TITFLTownMapNode> nodes)
    {
        m_nodes = nodes;
    }

    ArrayList<TITFLTownMapNode> findRoute(TITFLTownMapNode origination, TITFLTownMapNode destination)
    {
        unvisit();

        travel(origination, destination);

        ArrayList<TITFLTownMapNode> route = new ArrayList<TITFLTownMapNode>();

        TITFLTownMapNode current = destination;
        while (current != null)
        {
            route.add(current);
            current = current.m_parent;
        }

        return route;
    }

    void unvisit()
    {
        for (int i = 0; i < m_nodes.size(); i++)
        {
            m_nodes.get(i).unvisit();
        }
    }    

    TITFLTownMapNode travel(TITFLTownMapNode origination, TITFLTownMapNode destination)
    {
        if (origination == null)
            return null;

        Queue<TITFLTownMapNode> q = new LinkedList<TITFLTownMapNode>();
        q.add(origination);

        while (!q.isEmpty())
        {
            TITFLTownMapNode p = q.poll();

            if (p == destination)
                return p;

            if (p.m_visited)
                continue;

            for (int i = 0; i < p.m_link.size(); i++)
            {
                TITFLTownMapNode link = p.m_link.get(i);
                if (link.m_visited)
                    continue;

                link.m_parent = p;
                q.add(link);                
            }

            p.m_visited = true;
        }

        return null;
    }
}
