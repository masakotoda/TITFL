package com.noetap.titfl;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AssetManager;
import android.util.Xml;

public class TITFLTownMap 
{
	private ArrayList<TITFLTownMapNode> m_nodes;
	private String m_name;
	private int m_id;
	
	final static int num_columns = 4;
	final static int num_rows = 6;
	final static int num_nodes = num_columns * num_rows;
	
	private static String tag_root = "TITFL";
	private static String tag_item = "townmap";
	private static String atr_id = "townmap_id";
	private static String atr_name = "name";

	public TITFLTownMap()
	{
		m_nodes = new ArrayList<TITFLTownMapNode>();
		for (int i = 0; i < num_nodes; i++)
		{
			m_nodes.add(new TITFLTownMapNode());
		}
	}
	
	public ArrayList<TITFLTownMapNode> nodes()
	{
		return m_nodes;
	}
	
	public String name()
	{
		 return m_name;
	}
	
	public int id()
	{
		return m_id;
	}
	
	public static ArrayList<TITFLTownMap> loadTownMaps(AssetManager am)
	{
		ArrayList<TITFLTownMap> ret = new ArrayList<TITFLTownMap>();		
		try
		{
			String ns = "";
			InputStream in = am.open("default_townmap.xml");
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
 			
			parser.require(XmlPullParser.START_TAG, ns, tag_root);
			while (parser.next() != XmlPullParser.END_TAG) 
			{
				if (parser.getEventType() != XmlPullParser.START_TAG) 
			    	continue;
			    
				String name = parser.getName();
			    if (!name.equals(tag_item))
			    	continue;

	    		ArrayList<String> rows_up = new ArrayList<String>();
	    		ArrayList<String> rows_lr = new ArrayList<String>();
	    		ArrayList<String> rows_dn = new ArrayList<String>();
	    		ArrayList<String> slots = new ArrayList<String>();

		    	TITFLTownMap townMap = new TITFLTownMap();
		    	for (int i = 0; i < parser.getAttributeCount(); i++)
		    	{
		    		String attribName = parser.getAttributeName(i);
		    		String attribValue = parser.getAttributeValue(i);
		    		if (attribName.equals(atr_id))
		    			townMap.m_id = Integer.parseInt(attribValue);
		    		else if (attribName.equals(atr_name))
		    			townMap.m_name = attribValue;
		    		else if (attribName.contains("rowup"))
		    			rows_up.add(attribValue);
		    		else if (attribName.contains("rowlr"))
		    			rows_lr.add(attribValue);
		    		else if (attribName.contains("rowdn"))
		    			rows_dn.add(attribValue);
		    		else if (attribName.contains("slot"))
		    			slots.add(attribValue);
		    	}
		    	

		    	for (int i = 0; i < num_nodes; i++)
		    	{
		    		TITFLTownMapNode node = townMap.m_nodes.get(i);
		    		int x = i % num_columns;
		    		int y = i / num_columns;
		    		node.setX(x);
		    		node.setY(y);
		    		node.setIndex(i);
		    		boolean up = getFlag(rows_up.get(y), 2 * x);
		    		boolean left = getFlag(rows_lr.get(y), 2 * x);
		    		boolean right = getFlag(rows_lr.get(y), 2 * x + 1);
		    		boolean down = getFlag(rows_dn.get(y), 2 * x);
		    		boolean slot = getFlag(slots.get(y), 2 * x);
		    		if (up)
		    		{
		    			TITFLTownMapNode link = townMap.getUp(x, y);
		    			if (link != null)
		    				node.link().add(link);
		    		}
		    		if (left)
		    		{
		    			TITFLTownMapNode link = townMap.getLeft(x, y);
		    			if (link != null)
		    				node.link().add(link);
		    		}
		    		if (right)
		    		{
		    			TITFLTownMapNode link = townMap.getRight(x, y);
		    			if (link != null)
		    				node.link().add(link);
		    		}
		    		if (down)
		    		{
		    			TITFLTownMapNode link = townMap.getDown(x, y);
		    			if (link != null)
		    				node.link().add(link);
		    		}
		    		node.setOccupied(slot);
		    	}
		    	ret.add(townMap);
		    	parser.next();
			}
		}
		catch (Exception e)
		{
		}
		
		return ret;
	}
	
	static boolean getFlag(String str, int index)
	{
		String flag = str.substring(index, index + 1);
		if (flag.equals(" "))
			return false;
		else
			return true;
	}
	
	private int xyToIndex(int x, int y)
	{
		return (y * num_columns + x);		
	}
	
	private TITFLTownMapNode getUp(int x, int y)
	{
		TITFLTownMapNode slot = null;
		if (y > 0)
		{
			int index = xyToIndex(x, y - 1);
			slot = m_nodes.get(index);
		}
		return slot;
	}

	private TITFLTownMapNode getLeft(int x, int y)
	{
		TITFLTownMapNode slot = null;
		if (x > 0)
		{
			int index = xyToIndex(x - 1, y);
			slot = m_nodes.get(index);
		}
		return slot;
	}

	private TITFLTownMapNode getDown(int x, int y)
	{
		TITFLTownMapNode slot = null;
		if (y < num_rows - 1)
		{
			int index = xyToIndex(x, y + 1);
			slot = m_nodes.get(index);
		}
		return slot;
	}

	private TITFLTownMapNode getRight(int x, int y)
	{
		TITFLTownMapNode slot = null;
		if (x < num_columns - 1)
		{
			int index = xyToIndex(x + 1, y);
			slot = m_nodes.get(index);
		}
		return slot;
	}
}
