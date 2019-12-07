package solution;

import jigsaw.Jigsaw;
import jigsaw.JigsawNode;

import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * 在此类中填充算法，完成重拼图游戏（N-数码问题）
 */
public class Solution extends Jigsaw {
    Vector<JigsawNode> BFS_openList; // 用以保存已发现但未访问的节点
    Vector<JigsawNode> BFS_closeList; // 用以保存已访问的节点
    Set<JigsawNode> BFS_searchopenList;
    Set<JigsawNode> BFS_searchcloseList;
    int number = 0;
    /**
     * 拼图构造函数
     */
    public Solution() {
    }

    /**
     * 拼图构造函数
     * @param bNode - 初始状态节点
     * @param eNode - 目标状态节点
     */
    public Solution(JigsawNode bNode, JigsawNode eNode) {
        super(bNode, eNode);
    }

    /**
     *（实验一）广度优先搜索算法，求指定5*5拼图（24-数码问题）的最优解
     * 填充此函数，可在Solution类中添加其他函数，属性
     * @param bNode - 初始状态节点
     * @param eNode - 目标状态节点
     * @return 搜索成功时为true,失败为false
     */
    public boolean BFSearch(JigsawNode bNode, JigsawNode eNode) {
        this.BFS_openList = new Vector<JigsawNode>();
        this.BFS_closeList = new Vector<JigsawNode>();

        this.BFS_searchopenList = new HashSet<JigsawNode>();
        this.BFS_searchcloseList = new HashSet<JigsawNode>();

		this.beginJNode = new JigsawNode(bNode);
        this.endJNode = new JigsawNode(eNode);
        super.reset();

		// 把起始节点放进去
		BFS_openList.add(this.beginJNode);
        BFS_searchopenList.add(this.beginJNode);
        //BFS，进行层次遍历
        while (!BFS_openList.isEmpty()) {
            // 当前节点设置为openlist里的第一个元素
            this.currentJNode = BFS_openList.elementAt(0);
            // 如果当前节点等于目的节点，说明完成了，调用getPath函数
			if (this.currentJNode.equals(eNode)) {
				this.getPath();
				break;
			}
            else // 进行正常的层次遍历
            {
                // 头节点从未访问列表中移除，并移入访问过列表
                JigsawNode temp = BFS_openList.elementAt(0);
                this.BFS_openList.removeElementAt(0);
                this.BFS_searchopenList.remove(temp);

                this.BFS_closeList.addElement(this.currentJNode);
                this.BFS_searchcloseList.add(this.currentJNode);

                // 找到current的上下左右的未被访问过的邻接节点
                JigsawNode[] adjanceNodes = new JigsawNode[]{
                    new JigsawNode(this.currentJNode), new JigsawNode(this.currentJNode),
                    new JigsawNode(this.currentJNode), new JigsawNode(this.currentJNode)
                };


                // 如果移动成功了而且没有被访问过也没有被探查到就放入openlist
                for (int i = 0; i < 4; ++i) {
                    if (adjanceNodes[i].move(i) &&
                    !this.BFS_searchopenList.contains(adjanceNodes[i]) &&
                    !this.BFS_searchcloseList.contains(adjanceNodes[i])) {
                        BFS_openList.add(adjanceNodes[i]);
                        BFS_searchopenList.add(adjanceNodes[i]);
                    }
                }
                number++;
            }
		}
		
		System.out.println("Jigsaw AStar Search Result:");
        System.out.println("Begin state:" + this.getBeginJNode().toString());
        System.out.println("End state:" + this.getEndJNode().toString());
        System.out.println("Total number of searched nodes:" + number);
        System.out.println("Depth of the current node is:" + this.getCurrentJNode().getNodeDepth());
        return this.isCompleted();
    }


    /**
     *（Demo+实验二）计算并修改状态节点jNode的代价估计值:f(n)
     * 如 f(n) = s(n). s(n)代表后续节点不正确的数码个数
     * 此函数会改变该节点的estimatedValue属性值
     * 修改此函数，可在Solution类中添加其他函数，属性
     * @param jNode - 要计算代价估计值的节点
     */
    public void estimateValue(JigsawNode jNode) {
        int s = 0; // 后续节点不正确的数码个数
        int dimension = JigsawNode.getDimension();
        int manhattanDist = 0; // 设置曼哈顿距离，横向格子加上纵向格子
        int euclidean = 0; // 欧式距离。两点间直线距离

        //第n个节点的下一个节点的值 和 第n个节点的值加一 不相等，即为后续节点不正确的数码
        for (int index = 1; index < dimension * dimension; index++) {
            if (jNode.getNodesState()[index] + 1 != jNode.getNodesState()[index + 1]) {
                s++;
            }
        }

        for(int i=1;i<=dimension*dimension;++i)
        {
            for(int j=1;j<=dimension*dimension;++j)
            {
                if(jNode.getNodesState()[i] != 0 && jNode.getNodesState()[i] == this.endJNode.getNodesState()[j])
                {
                    //currentnode and endnode
                    int x1 = (i - 1) / 5;
                    int x2 = (j - 1) / 5;
                    int y1 = (i + 4) % 5;
                    int y2 = (j + 4) % 5;
                    manhattanDist += Math.abs(x1 - x2) + Math.abs(y1 - y2);
                    euclidean += Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2);
                    break;
                }
            }
        }
        jNode.setEstimatedValue(s*7+manhattanDist*6+euclidean*3);
    }
}
