/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.graph;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;
import samples.SampleGenerator;

/**
 *
 * @author marcin
 */
public class JungMain {
    public static void main(String[] args) {
        SampleGenerator generator = new SampleGenerator();
        List<Entry<Integer, Integer>> generated = generator.generate(10);
        Graph<Integer, String> graph = generateGraphFromSources(generated);
        LinkedList<Double> result = computePageRank(graph);
        for (Double item : result) {
            System.out.println(item);
        }
        visualiseGraph(graph);
    }
    
    public static void visualiseGraph(Graph<Integer, String> graph){
        Layout<Integer, String> layout = new CircleLayout<Integer, String>(graph);
        layout.setSize(new Dimension(600, 600));
        BasicVisualizationServer server = new BasicVisualizationServer(layout);
        server.setPreferredSize(new Dimension(650, 650));
        Transformer<Integer, Paint> vertexTransformer = new Transformer<Integer, Paint>() {
            @Override
            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeTransformer = new Transformer<String, Stroke>() {
            @Override
            public Stroke transform(String i) {
                return edgeStroke;
            }
        };
        RenderContext renderContext = server.getRenderContext();
        renderContext.setVertexFillPaintTransformer(vertexTransformer);
        renderContext.setEdgeStrokeTransformer(edgeTransformer);
        renderContext.setVertexLabelTransformer(new ToStringLabeller());
        server.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        JFrame frame = new JFrame("Graph visualisation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(server);
        frame.pack();
        frame.setVisible(true);
    }

    public static Graph<Integer, String> generateGraphFromSources(List<Entry<Integer, Integer>> generated) {
        HashSet<Integer> usedVertexes = new HashSet<Integer>();
        Graph<Integer, String> graph = new DirectedSparseGraph<Integer, String>();
        for (Entry<Integer, Integer> link : generated) {
            Integer from = link.getKey();
            Integer to = link.getValue();
            putIfNotUsed(usedVertexes, from, graph);
            putIfNotUsed(usedVertexes, to, graph);
            String linkName = String.format("Connection from:%s to:%s", from, to);
            graph.addEdge(linkName, from, to);
        }
        return graph;
    }
    
    public static void putIfNotUsed(HashSet<Integer> usedVertexes, Integer from, Graph<Integer, String> graph) {
        if (!usedVertexes.contains(from)) {
            graph.addVertex(from);
            usedVertexes.add(from);
        }
    }

    public static LinkedList<Double> computePageRank(Graph<Integer, String> graph) {
        PageRank<Integer, String> pageRank = new PageRank<Integer, String>(graph, 0.1);
        pageRank.evaluate();
        LinkedList<Double> scores = new LinkedList<Double>();
        for (Integer vertex : graph.getVertices()) {
            scores.add(pageRank.getVertexScore(vertex));
        }
        return scores;
    }
}
