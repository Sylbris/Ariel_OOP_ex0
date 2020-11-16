package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph wg;
    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
    wg=g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return wg;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * Uses the WGraph_DS deep copy method in order to do so.
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph copy_graph = new WGraph_DS(wg); // make the designated copied graph
        return copy_graph;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     *
     * The algorithm goes over each node using BFS, marks it and then goes over its neighbours.
     * by the end of the algorithm all nodes should have been visited if the graph is connected.
     * Otherwise we return false as some nodes cannot be reached.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (wg.nodeSize() == 0 || wg.nodeSize() == 1) // if our graph contains 0/1 nodes its connected.
            return true;

        for (node_info n : wg.getV()) { // set the distance to max for all nodes from the source node.
            n.setInfo("blue"); // lets mark all nodes as blue for unvisited.
        }

        Queue<node_info> queue = new LinkedList<>(); // we define a queue to save all nodes.

        queue.add(wg.getV().iterator().next());//add the first none null node to the queue.

        int count=0;//we mark the amount of nodes visited.

        while (!queue.isEmpty()) { //bfs

            node_info curr = queue.poll(); //pull the 1st node from the queue.

            for (node_info adjacent : wg.getV(curr.getKey())) {//iterate all of currs neibours.

                if (adjacent.getInfo()=="blue") {//if we haven't seen the neibour we put it in the queue.
                    adjacent.setInfo("red");
                    count++;
                    queue.add(adjacent);

                }

            }

        }
        if (count == wg.nodeSize()) { //we've implemented BFS meaning seen contains all nodes in the graph so their size must be equal.
            return true;

        }

        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * Here we use Dijkstra's algorithm to traverse the graph:
     *
     * https://www.youtube.com/watch?v=pVfj6mxhdMw&ab_channel=ComputerScience
     * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue
     *
     * I used a priority queue to draw the node with the minimal distance each time(using comparator).
     * Each time you reach a node you store the distance from the source node to that node if the new distance is smaller.
     *
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        if (wg.getNode(src) == null || wg.getNode(dest) == null) // if our graph can't reach either of them.
            return -1;

        if (src == dest)// in case we get the same node.
            return 0;

        wg.getNode(src).setTag(0);

        PriorityQueue<node_info> queue = new PriorityQueue<>(11, new Comparator<node_info>() {//we define a comparator.
            @Override
            public int compare(node_info o1, node_info o2) {
                if(o1.getTag()<o2.getTag())
                    return -1;
                if(o1.getTag()>o2.getTag())
                    return 1;
                else
                    return 0;
            }
        });

        for (node_info n : wg.getV()) { // set the distance to max for all nodes from the source node.

            if(n.getKey()!=src)
            n.setTag(Integer.MAX_VALUE);// this will be the distance from src node.
            n.setInfo("blue"); // lets mark all nodes as blue for unvisited.
        }

        queue.add(wg.getNode(src));//we add the src node to the queue.

        while (!queue.isEmpty()) {

            node_info curr = queue.remove();//pull the first from the queue.

            curr.setInfo("red");

            for (node_info adjacent : wg.getV(curr.getKey())) {//iterate all of currs neibours.
                if (adjacent.getInfo() == "blue") { // lets check all unvisited children

                    if(adjacent.getTag()>(curr.getTag() + wg.getEdge(adjacent.getKey(), curr.getKey()))) {//if new distance is smaller, update the it.
                        adjacent.setTag(curr.getTag() + wg.getEdge(adjacent.getKey(), curr.getKey()));//set the tag of the node to be the previous Weight + new Weight.
                        queue.add(adjacent);
                    }


                }
            }
        }
        return wg.getNode(dest).getTag(); //if we haven't found it return -1.
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * Here we use our previous algorithm shortestPathDist to calculate the distance to each node.
     * once done, all we left is to backtrack our way by choosing a neighbour that has a distance equal to our current node's - the weight of the edge between them.
     *
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(wg.getNode(src)==null || wg.getNode(dest)==null)//if either of them doesn't exist we return null.
            return null;

        shortestPathDist(src,dest);//lets mark the distance between all nodes and the src.

        Queue<node_info> queue = new LinkedList<>(); // we define a queue to save all nodes.

        List<node_info> path = new LinkedList<>();//a list to return the path.

        path.add(wg.getNode(dest));//add the last to the list.

        if(src==dest)
            return path;

        queue.add(wg.getNode(dest)); //add the last to the queue.

        while (!queue.isEmpty()) { //commence bfs.

            node_info curr = queue.poll();//pull the first from the queue.

            for (node_info adjacent : wg.getV(curr.getKey())) {//iterate all of currs neibours.

              /*  System.out.println("adjacent neib distance: " + adjacent.getTag());
                System.out.println("curr.getTag() is : " + curr.getTag());
                System.out.println("edge between them : "+ wg.getEdge(curr.getKey(),adjacent.getKey()));
                System.out.println("the diff is : " + (curr.getTag() - wg.getEdge(curr.getKey(),adjacent.getKey())));
                System.out.println("");
*/
                if (curr.getTag() == adjacent.getTag() + wg.getEdge(curr.getKey(),adjacent.getKey())) { //only move if the distance is one less.

                    path.add(adjacent);//add it to the list.
                    queue.add(adjacent);//add the node to the queue.

                    if (adjacent.getKey() == src) {// if we reached the source we stop.
                        Collections.reverse(path);
                        return path;
                    }
                    break; //we can stop iterating the neighbours.
                }
            }
        }
        return path;
    }


    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fileOstrm=new FileOutputStream(file);
            ObjectOutputStream out= new ObjectOutputStream(fileOstrm);

            out.writeObject(wg);

            out.close();
            fileOstrm.close();

            return true;

        } catch (IOException ex) {
            return false;
        }

    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        weighted_graph wg1=null;//create the graph to be loaded into.
        try {
            FileInputStream fileIstrm = new FileInputStream(file);
            ObjectInputStream in=new ObjectInputStream(fileIstrm);

            wg1=(weighted_graph)in.readObject();

            in.close();
            fileIstrm.close();
            wg=wg1;
            return true;
        }
        catch (IOException | ClassNotFoundException ex){

        return false;

        }



    }
}
