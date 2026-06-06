package io.github.killiansra.javacman.ai;

import io.github.killiansra.javacman.main.GamePanel;

import java.util.ArrayList;

public class Pathfinder
{
    private GamePanel gp;
    private Node[][] nodes;
    private ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode;
    private int step;
    private boolean goalReached = false;

    public Pathfinder(GamePanel gp)
    {
        this.gp = gp;
        instantiateNodes();
    }
    /**
     * Instantiates a 2D array of nodes representing the grid for the A* pathfinding algorithm.
     * Each node is initialized with its respective column and row index.
     * <p>
     * The method iterates through all columns and rows within the grid dimensions
     * defined by the {@link GamePanel} and creates a new {@link Node} object for each cell.
     */
    private void instantiateNodes()
    {
        this.nodes = new Node[gp.maxScreenCol][gp.maxScreenRow];

        int col = 0;
        int row = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            nodes[col][row] = new Node(col, row);
            col++;
            if(col == gp.maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Resets the state of all nodes in the grid.
     */
    private void resetNodes()
    {
        int col = 0;
        int row = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            //Reset open, checked and solid state
            nodes[col][row].open = false;
            nodes[col][row].checked = false;
            nodes[col][row].solid = false;

            col++;
            if(col == gp.maxScreenCol)
            {
                col = 0;
                row++;
            }
        }

        //Reset others settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    /**
     * Initializes the nodes by setting the start, goal, and solid nodes,
     * and calculating the cost for each node in the grid.
     *
     * @param startCol the column index of the starting node
     * @param startRow the row index of the starting node
     * @param goalCol the column index of the goal node
     * @param goalRow the row index of the goal node
     */
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow)
    {
        resetNodes();

        //Set start and goal nodes
        startNode = nodes[startCol][startRow];
        currentNode = startNode;
        goalNode = nodes[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;
        while(col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            //Set solid nodes
            int tileNum = gp.tileManager.mapTileNum[col][row];
            if(gp.tileManager.tiles[tileNum].isCollision())
            {
                nodes[col][row].solid = true;
            }

            //Set cost
            getCost(nodes[col][row]);

            col++;
            if(col == gp.maxScreenCol)
            {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Calculates the G, H, and F costs for the specified node.
     *
     * <p>The costs are calculated as follows:
     * <ul>
     *   <li><b>G cost:</b> The distance from the starting node to the current node.</li>
     *   <li><b>H cost:</b> The estimated distance from the current node to the goal node (heuristic).</li>
     *   <li><b>F cost:</b> The sum of the G and H costs, representing the total cost of the path through this node.</li>
     * </ul>
     * </p>
     *
     * @param node the node for which the costs are being calculated
     */
    private void getCost(Node node)
    {
        //G cost
        int xDist = Math.abs(node.col - startNode.col);
        int yDist = Math.abs(node.row - startNode.row);
        node.gCost = xDist + yDist;

        //H cost
        xDist = Math.abs(node.col - goalNode.col);
        yDist = Math.abs(node.row - goalNode.row);
        node.hCost = xDist + yDist;

        //F cost
        node.fCost = node.gCost + node.hCost;
    }

    /**
     * Executes the A* search algorithm to find the shortest path from the start node to the goal node.
     *
     * <p>The method iteratively explores the nodes on the grid to determine the optimal path using
     * the A* algorithm. It evaluates neighboring nodes, calculates their costs, and selects the best
     * candidate for the next step based on F cost (and G cost in case of ties).</p>
     *
     * @return {@code true} if the goal node is successfully reached, {@code false} otherwise.
     */
    public boolean search()
    {
        boolean exit = false;
        while(!goalReached && step < 500 && !exit)
        {
            int col = currentNode.col;
            int row = currentNode.row;

            //Check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //Open the up node
            if(row - 1 >= 0)
            {
                openNode(nodes[col][row - 1]);
            }
            //Open the left node
            if(col - 1 >= 0)
            {
                openNode(nodes[col - 1][row]);
            }
            //Open the down node
            if(row + 1 < gp.maxScreenRow)
            {
                openNode(nodes[col][row + 1]);
            }
            //Open the right node
            if(col + 1 < gp.maxScreenCol)
            {
                openNode(nodes[col + 1][row]);
            }

            //Find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for(int i = 0; i < openList.size(); i++)
            {
                //Check if this node's F cost is better
                if(openList.get(i).fCost < bestNodeFCost)
                {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                //If F Cost is equal, check the G Cost
                else if(openList.get(i).fCost == bestNodeFCost)
                {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost)
                    {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.isEmpty())
            {
                exit = true;
            }

            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode)
            {
                goalReached = true;
                trackPath();
            }
            step++;
        }


        return goalReached;
    }

    /**
     * Marks a node as open and adds it to the open list if it is traversable and has not been processed.
     *
     * @param node The node to be processed and potentially opened for traversal.
     */
    private void openNode(Node node)
    {
        if(!node.open && !node.checked && !node.solid)
        {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    /**
     * Tracks the optimal path from the goal node to the start node and stores it in the path list.
     */
    public void trackPath()
    {
        Node current = goalNode;

        while(current != startNode)
        {
            pathList.addFirst(current);
            current = current.parent;
        }
    }
}