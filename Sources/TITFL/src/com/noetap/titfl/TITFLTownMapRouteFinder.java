package com.noetap.titfl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TITFLTownMapRouteFinder
{
    private ArrayList<TITFLTownMapNode> m_nodes;

    public TITFLTownMapRouteFinder(ArrayList<TITFLTownMapNode> nodes)
    {
        m_nodes = nodes;
    }

    public ArrayList<TITFLTownMapNode> findRoute(TITFLTownMapNode origination, TITFLTownMapNode destination)
    {
        unvisit();

        travel(origination, destination);

        ArrayList<TITFLTownMapNode> route = new ArrayList<TITFLTownMapNode>();

        TITFLTownMapNode current = destination;
        while (current != null)
        {
            route.add(current);
            current = current.parent();
        }

        return route;
    }

    private void unvisit()
    {
        for (int i = 0; i < m_nodes.size(); i++)
        {
            m_nodes.get(i).unvisit();
        }
    }    

    private TITFLTownMapNode travel(TITFLTownMapNode origination, TITFLTownMapNode destination)
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

            if (p.visited())
                continue;

            for (int i = 0; i < p.link().size(); i++)
            {
                TITFLTownMapNode link = p.link().get(i);
                if (link.visited())
                    continue;

                link.setParent(p);
                q.add(link);                
            }

            p.setVisited(true);
        }

        return null;
    }
}
