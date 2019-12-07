package solution;

import java.util.ArrayList;
import java.util.HashSet;

import jigsaw.Jigsaw;
import jigsaw.JigsawNode;


/**
 * 在此类中填充算法，完成重拼图游戏（N-数码问题）
 */
public class Solution extends Jigsaw {

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
        //this.visitedList = new HashSet<>(1000);
        //this.exploreList = new Queue<>(500);
        ArrayList<JigsawNode> openList = new ArrayList<JigsawNode>();
        HashSet<JigsawNode> open_hash = new HashSet<JigsawNode>();
        HashSet<JigsawNode> close_hash = new HashSet<JigsawNode>();

        this.beginJNode = new JigsawNode(bNode);
        this.endJNode = new JigsawNode(eNode);
        this.currentJNode = null;

        final int DIRS = 4;

        // 重置求解标记
        int mySearchedNodesNum = 0;

        // (1)将起始节点放入openList中
        openList.add(this.beginJNode);
        open_hash.add(this.beginJNode);

        // (2) 如果exploreList为空，则搜索失败，问题无解;否则循环直到求解成功
        while (!openList.isEmpty()) {
            mySearchedNodesNum++;

            // (2-1)取出exploreList的第一个节点N，置为当前节点currentJNode
            //      若currentJNode为目标节点，则搜索成功，计算解路径，退出
            this.currentJNode = openList.get(0);
            if (this.currentJNode.equals(eNode)) {
                this.getPath();
                break;
            }

            JigsawNode[] nextNodes = new JigsawNode[]{
                new JigsawNode(this.currentJNode), new JigsawNode(this.currentJNode),
                new JigsawNode(this.currentJNode), new JigsawNode(this.currentJNode)
            };

            // (2-2)寻找所有与currentJNode邻接且未曾被发现的节点，将它们插入openList中
            for (int i = 0; i < DIRS; i++) {
                if (nextNodes[i].move(i) && !open_hash.contains(nextNodes[i]) && !close_hash.contains(nextNodes[i])) {
                            openList.add(nextNodes[i]);
                            open_hash.add(nextNodes[i]);
                }
            }
            
            // (2-3)在openList中删除当前节点，加入到closeList中
            openList.remove(currentJNode);
            open_hash.remove(currentJNode);
            close_hash.add(currentJNode);    
            
        }

        System.out.println("Jigsaw AStar Search Result:");
        System.out.println("Begin state:" + this.getBeginJNode().toString());
        System.out.println("End state:" + this.getEndJNode().toString());
        // System.out.println("Solution Path: ");
        // System.out.println(this.getSolutionPath());
        System.out.println("Total number of searched nodes:" + mySearchedNodesNum);
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
        int mhDis = 0;
        int euDis = 0;
        int dimension = JigsawNode.getDimension();
        for (int i = 1; i < dimension * dimension; i++) {
        	if (jNode.getNodesState()[i] + 1 != jNode.getNodesState()[i + 1]) {
                s++;
            }
        	
        	for (int j = 1; j < dimension * dimension; j++) {
        		if (jNode.getNodesState()[i] != 0 
        				&& jNode.getNodesState()[i] == endJNode.getNodesState()[j]) {
        			int x1 = (i - 1)/dimension, y1 = (i - 1)%dimension,
        				x2 = (j - 1)/dimension, y2 = (j - 1)%dimension;
        			mhDis += Math.abs(x1 - x2) + Math.abs(y1 - y2);
        			euDis += Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        			break;
        		}
        	}        
        }
        
        int weight = s * 5 + mhDis * 4 + euDis * 1;
        jNode.setEstimatedValue(weight);
    }
}
