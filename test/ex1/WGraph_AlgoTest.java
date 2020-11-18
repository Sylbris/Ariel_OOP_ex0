package ex1;

import ex1.WGraph_Algo;
import ex1.node_info;
import ex1.weighted_graph;
import ex1.weighted_graph_algorithms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

  /**
   * Test for graph connectivity, notice that the minimum edges required for graph to be connected is n-1.
   */
  @Test
  void isConnected() {
    weighted_graph g0 = WGraph_DSTest1.graph_creator(0,0,1);
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    Assertions.assertTrue(ag0.isConnected());

    g0 = WGraph_DSTest1.graph_creator(1,0,1);
    ag0 = new WGraph_Algo();
    ag0.init(g0);
    Assertions.assertTrue(ag0.isConnected());

    g0 = WGraph_DSTest1.graph_creator(2,0,1);
    ag0 = new WGraph_Algo();
    ag0.init(g0);
    Assertions.assertFalse(ag0.isConnected());

    g0 = WGraph_DSTest1.graph_creator(2,1,1);
    ag0 = new WGraph_Algo();
    ag0.init(g0);
    Assertions.assertTrue(ag0.isConnected());

    g0 = WGraph_DSTest1.graph_creator(10,30,1);
    ag0.init(g0);
    boolean b = ag0.isConnected();
    Assertions.assertTrue(b);

    g0 = WGraph_DSTest1.graph_creator(10,8,1);
    ag0.init(g0);
    boolean c = ag0.isConnected();
    Assertions.assertFalse(c);


  }

  /**
   * Check run time for creating a graph with 50,000 nodes and checking its connected.
   * Should be less than 10 seconds.
   */
  @Test
  void runtime(){
    weighted_graph g0 = WGraph_DSTest1.graph_creator(0,0,1);
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    long start = new Date().getTime();
    g0 = WGraph_DSTest1.graph_creator(50000,49000,1);
    ag0.init(g0);
    boolean c = ag0.isConnected();
    Assertions.assertFalse(c);
    long finish = new Date().getTime();
    double timeElapsed = (finish - start)/1000;
    boolean time=timeElapsed<10;
    Assertions.assertEquals(true,time);
  }
  @Test
  void copy(){
    weighted_graph g0 = small_graph();
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    weighted_graph g1= ag0.copy();
    Assertions.assertEquals(g1,g0);
    g1.removeNode(0);
    Assertions.assertEquals(false,g1.equals(g0));
  }

  @Test
  void shortestPathDist() {
    weighted_graph g0 = small_graph();
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    Assertions.assertTrue(ag0.isConnected());
    double d = ag0.shortestPathDist(0,10);
    Assertions.assertEquals(d, 5.1);
    g0.removeEdge(7,10);
    ag0.init(g0);
    Assertions.assertTrue(ag0.isConnected());
    double e=ag0.shortestPathDist(0,10);
    Assertions.assertEquals(e,33);
  }

  @Test
  void shortestPath() {
    weighted_graph g0 = small_graph();
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    List<node_info> sp = ag0.shortestPath(0,10);
    //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
    int[] checkKey = {0, 1, 5, 7, 10};
    int i = 0;
    for(node_info n: sp) {
      //assertEquals(n.getTag(), checkTag[i]);
      Assertions.assertEquals(n.getKey(), checkKey[i]);
      i++;
    }
  }

  @Test
  void save_load() {
    weighted_graph g0 = WGraph_DSTest1.graph_creator(10,30,1);
    weighted_graph_algorithms ag0 = new WGraph_Algo();
    ag0.init(g0);
    String str = "g0.obj";
    ag0.save(str);
    weighted_graph g1 = WGraph_DSTest1.graph_creator(10,30,1);
    ag0.load(str);
    Assertions.assertEquals(g0,g1);
    g0.removeNode(0);
    Assertions.assertNotEquals(g0,g1);
  }

  private weighted_graph small_graph() {
    weighted_graph g0 = WGraph_DSTest1.graph_creator(11,0,1);
    g0.connect(0,1,1);
    g0.connect(0,2,2);
    g0.connect(0,3,3);

    g0.connect(1,4,17);
    g0.connect(1,5,1);
    g0.connect(2,4,1);
    g0.connect(3, 5,10);
    g0.connect(3,6,100);
    g0.connect(5,7,1.1);
    g0.connect(6,7,10);
    g0.connect(7,10,2);
    g0.connect(6,8,30);
    g0.connect(8,10,10);
    g0.connect(4,10,30);
    g0.connect(3,9,10);
    g0.connect(8,10,10);

    return g0;
  }
}