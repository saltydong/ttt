public class SparseGridNode{
    private Object occupant;
    private int col;
    private SparseGridNode next;

    //首先是构造函数
    public SparseGridNode(Object oc, int co, SparseGridNode next){
        occupant = oc;
        col = co;
        this.next = next;
    }

    //需要有获取到三个private变量的方法
    public Object getOccupant()
    {
        return occupant;
    }

    public int getCol()
    {
        return col;
    }

    public SparseGridNode getNextNode()
    {
        return next;
    }

    //需要有设置三个private变量的方法

    public void setOccupant(Object oc)
    {
        occupant = oc;
    }

    public void setCol(int co)
    {
        col = co;
    }

    public void setNextNode(SparseGridNode temp)
    {
        next = temp;
    }
}