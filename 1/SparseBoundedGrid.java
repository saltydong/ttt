/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Alyce Brady
 * @author APCS Development Committee
 * @author Cay Horstmann
 */

import info.gridworld.grid.Grid;
import info.gridworld.grid.AbstractGrid;
import info.gridworld.grid.Location; 
import java.util.ArrayList;

/**
 * A <code>SparseBoundedGrid</code> is a rectangular grid with a finite number of
 * rows and columns. <br />
 * The implementation of this class is testable on the AP CS AB exam.
 */
public class SparseBoundedGrid<E> extends AbstractGrid<E>
{
    private SparseGridNode[] occupantArray; // 存储所有的grid，每一行都是一个链表

    private int colNum;    //列数
    private int rowNum;    //行数
    /**
     * Constructs an empty bounded grid with the given dimensions.
     * (Precondition: <code>rows > 0</code> and <code>cols > 0</code>.)
     * @param rows number of rows in SparseBoundedGrid
     * @param cols number of columns in SparseBoundedGrid
     */
    public SparseBoundedGrid(int rows, int cols)
    {
        if (rows <= 0)
            throw new IllegalArgumentException("rows <= 0");
        if (cols <= 0)
            throw new IllegalArgumentException("cols <= 0");
        colNum = cols;
        rowNum = rows;
        occupantArray = new SparseGridNode[rows];
    }

    public int getNumRows()
    {
        return occupantArray.length;
    }

    public int getNumCols()
    {
        return colNum;
    }

    public boolean isValid(Location loc)
    {
        return 0 <= loc.getRow() && loc.getRow() < getNumRows()
                && 0 <= loc.getCol() && loc.getCol() < getNumCols();
    }

    public ArrayList<Location> getOccupiedLocations()
    {
        ArrayList<Location> theLocations = new ArrayList<Location>();

        // Look at all grid locations.
        for (int r = 0; r < getNumRows(); r++)
        {
            SparseGridNode linkedlist = occupantArray[r]; //这里就可以取到每一行的链表
            //接下来对链表进行遍历，找到所有不为null的格子
            for (int c = 0; c < getNumCols(); c++)
            while(linkedlist != null)
            {
                theLocations.add(new Location(r, linkedlist.getCol()));
                linkedlist = linkedlist.getNextNode();
            }
        }
        return theLocations;
    }

    public E get(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        SparseGridNode linkedlist = occupantArray[loc.getRow()];  //获取该行链表
        while(linkedlist != null)
        {
            if(linkedlist.getCol() == loc.getCol())
            {
                return (E) linkedlist.getOccupant();// 拿到位于loc的对象
            }
            linkedlist = linkedlist.getNextNode();
        }
        return null;
    }

    public E put(Location loc, E obj)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        if (obj == null)
            throw new NullPointerException("obj == null");
        //先把老的先移除掉
        E oldOccupant = remove(loc); 
        // Add the object to the grid.
        SparseGridNode linkedlist = occupantArray[loc.getRow()];
        occupantArray[loc.getRow()] = new SparseGridNode(obj, loc.getCol(), linkedlist);
        return oldOccupant;
    }

    public E remove(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        
        // Remove the object from the grid.
        E r = get(loc);

        if(r == null)//找不到就返回null
            return null;
        
        SparseGridNode linkedlist = occupantArray[loc.getRow()];
        if(linkedlist != null)
        {
            if(linkedlist.getCol() == loc.getCol())
            {
                // 如果要移除的是这一行的第一个，我们就把这个链表的头节点给第二个节点，再把第一个删掉
                occupantArray[loc.getRow()] = linkedlist.getNextNode();
            }
            else
            {
                SparseGridNode temp = linkedlist.getNextNode();
                while(temp != null && temp.getCol() != loc.getCol())
                {
                    linkedlist = linkedlist.getNextNode();
                    temp = temp.getNextNode();
                }
                //有两种情况，一种是一整行都没有找到，在第一个条件的时候跳出来了；一种是找到了，在第二个条件的时候跳出来了
                if(temp != null)
                {
                    linkedlist.setNextNode(temp.getNextNode());
                }
            }
        }
        return r;
    }

    
}
