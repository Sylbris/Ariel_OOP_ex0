package ex1;

import ex0.node_data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, Serializable {
    private int edges;
    private HashMap<Integer,node_info> node_map;
    private int mode_count;
////////////////////////////////////////////////////////Start NodeInfo ///////////////////////////////////////////////
    private class NodeInfo implements node_info {
        private int key;
        private String info;
        private double tag;
        private HashMap<Integer,node_info> neighbours;// HashMap to store <Neighbour,Weight>;
        private HashMap<Integer, Double> edges;


        public NodeInfo(int key){//Constructor.
            this.key=key;
            this.info="blue";
            this.tag=0;
            neighbours=new HashMap<>();
            edges=new HashMap<>();
        }
        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         *
         * @return
         */
        @Override
        public int getKey() {

            return this.key;
        }
        public Collection<node_info> getNi() { // return the list of neighbours.
            return neighbours.values();

        }


        public boolean hasNi(int key){

            if(neighbours.containsKey(key))
                return true;
            else
                return false;

        }

        public void addNi(node_info t, double w) { //add a neibour
            if(t!=null ) {
                NodeInfo c=(NodeInfo)t;
                neighbours.put(t.getKey(), (NodeInfo)t);
                edges.put(t.getKey(),w);
            }
        }

        public double getEdgeV(int key){//returns the edge weight between 2 nodes.

            return edges.get(key);
        }


        public void removeNode(node_info node) { //removes a neighbour.

            if(neighbours.containsKey(node.getKey())) {//if neighbour is there.
                neighbours.remove(node.getKey(), node);//remove neighbour and remove the edge.
                edges.remove(node.getKey());
            }
        }

        /**
         * return the remark (meta data) associated with this node.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag=t;
        }
    }

    /////////////////////////////////////////////end NodeInfo/////////////////////////////////////////////////////////

    public WGraph_DS(){
        node_map=new HashMap<>();
        this.edges=0;
        this.mode_count=0;
    }
    /**
     * return the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {

        if(node_map.containsKey(key))
            return node_map.get(key);

        return null;

    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        NodeInfo c=(NodeInfo)node_map.get(node1);
        if((node_map.containsKey(node1) && node_map.containsKey(node2) && c.hasNi(node2)) || node1==node2 ) { //check if both nodes exist.
            return true;
        }
        return false;
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        NodeInfo c=(NodeInfo)node_map.get(node1);
        return c.getEdgeV(node2);
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(!node_map.containsKey(key)) {
            node_info newnode=new NodeInfo(key);
            node_map.put(key, newnode);
            mode_count++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     *
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 != node2 && !hasEdge(node1,node2) && node_map.containsKey(node1) && node_map.containsKey(node2) ) {//check if nodes are different, and the map contains both nodes.

            NodeInfo c1=(NodeInfo)node_map.get(node1); //cast them to use addNi method
            NodeInfo c2=(NodeInfo)node_map.get(node2); //cast them to use addNi method

            c1.addNi(node_map.get(node2), w); //add first neibour to second.
            c2.addNi(node_map.get(node1), w);//add second neibour to first.
            mode_count++;
            edges++;
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return node_map.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     *
     * @param node_id
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (node_map.get(node_id) == null) // if node doesn't exist, return null. otherwise get a collection of neibours.
            return null;

        NodeInfo c=(NodeInfo)node_map.get(node_id);

        return c.getNi();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (node_map.get(key) != null) {//in case the node was already deleted.
            node_info giveback = node_map.get(key);//We set a pointer in order to return it.
            int j = 0;// a counter for the amount of times we wiped a neibour from the hashmap=number of edges we wiped.

            NodeInfo c=(NodeInfo)node_map.get(key);//down cast the node to use getNi on next line.

            for (node_info i : c.getNi()) { //iterating through the neibours.

                NodeInfo c1=(NodeInfo)i;
                c1.removeNode(node_map.get(key)); // if the deleted node was its neibour, delete the edge between them.
                j++;

            }
            edges -= j;
            mode_count--;
            node_map.remove(key); // we remove the entry from the map.
            return giveback; //return the deleted node.
        }

        else
            return null;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(node1!=node2 && hasEdge(node1,node2)) { //check if both nodes are not equal and has an edge between them.

            NodeInfo c1=(NodeInfo) node_map.get(node1);
            NodeInfo c2=(NodeInfo) node_map.get(node2);

            c1.removeNode(node_map.get(node2));
            c2.removeNode(node_map.get(node1));
            mode_count++;
            edges--;
        }
    }

    /**
     * return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return node_map.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edges;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     *
     * @return
     */
    @Override
    public int getMC() {
        return mode_count;
    }
}
