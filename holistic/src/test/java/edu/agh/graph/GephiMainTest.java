/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.graph;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphModel;
import org.gephi.statistics.plugin.GraphDistance;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcin
 */
public class GephiMainTest {
    
    public GephiMainTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }
/*
    @Test
    public void testMakeGraphFromSource() {
        System.out.println("makeGraphFromSource");
        List<Entry<Integer, Integer>> edgesSource = null;
        GraphModel graphModel = null;
        DirectedGraph expResult = null;
        DirectedGraph result = GephiMain.makeGraphFromSource(edgesSource, graphModel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    @Test
    public void when_more_connections_then_more_betweeness() {
        System.out.println("when_more_connections_then_more_betweeness");
        List<Entry<Integer, Integer>> edgesSource = generateTestGraph();
        GephiMain instance = new GephiMain();
        Map<Integer, Double> result = instance.calculateGephiMetrics(edgesSource, GraphDistance.BETWEENNESS);
        assertTrue(result.get(0)>result.get(2));
        assertTrue(result.get(0)>result.get(1));
        assertTrue(result.get(1)>result.get(2));
        assertEquals(result.get(2), result.get(3));
        assertEquals(result.get(4), result.get(2));
    }

    public void putTestEdge(List<Entry<Integer, Integer>> edgesSource, Integer from, Integer to) {
        edgesSource.add(new AbstractMap.SimpleEntry<Integer, Integer>(from, to));
        edgesSource.add(new AbstractMap.SimpleEntry<Integer, Integer>(to, from));
    }

    public List<Entry<Integer, Integer>> generateTestGraph() {
        List<Entry<Integer, Integer>> edgesSource = new LinkedList<Entry<Integer, Integer>>();
        putTestEdge(edgesSource, 0, 1);
        putTestEdge(edgesSource, 0, 2);
        putTestEdge(edgesSource, 0, 3);
        putTestEdge(edgesSource, 1, 4);
        return edgesSource;
    }
}
