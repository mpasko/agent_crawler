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
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
        Set<Entry<Integer, Double>> result = computePageRank(graph, 0.1).entrySet();
        for (Entry<Integer, Double> item : result) {
            System.out.println(item.getValue());
        }
        plotGraph(graph);
    }

    public static void plotCustomizedGraph(final Graph<Integer, String> graph,
            Transformer<Integer, Paint> vertexTransformer,
            Transformer<String, Stroke> edgeTransformer) {
        new WindowThread(graph, vertexTransformer, edgeTransformer).start();
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

    public static Map<Integer, Double> computePageRank(Graph<Integer, String> graph, Double alpha) {
        PageRank<Integer, String> pageRank = new PageRank<Integer, String>(graph, alpha);
        pageRank.evaluate();
        HashMap<Integer, Double> scores = new HashMap<Integer, Double>();
        for (Integer vertex : graph.getVertices()) {
            Double value = pageRank.getVertexScore(vertex);
            scores.put(vertex, value);
        }
        return scores;
    }

    public static Map<Integer, Double> computeEigenvector(Graph<Integer, String> graph) {
        return computePageRank(graph, 0.0);
    }

    public static Graph<Integer, String> generateGraphFromSources(List<Integer> members, List<Entry<Integer, Integer>> filtered, List<Entry<Integer, Integer>> outside) {
        HashSet<Integer> usedVertexes = new HashSet<Integer>();
        Graph<Integer, String> graph = new DirectedSparseGraph<Integer, String>();
        List<Entry<Integer, Integer>> generated = new LinkedList<Entry<Integer, Integer>>(filtered);
        generated.addAll(outside);
        for (Entry<Integer, Integer> link : generated) {
            Integer from = link.getKey();
            Integer to = link.getValue();
            putIfNotUsed(usedVertexes, from, graph);
            putIfNotUsed(usedVertexes, to, graph);
            String linkName = String.format("%s->%s", from, to);
            graph.addEdge(linkName, from, to);
        }
        return graph;
    }

    public static void plotGraph(Graph<Integer, String> graph) {
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
        plotCustomizedGraph(graph, vertexTransformer, edgeTransformer);
    }

    public static void plotBisectGraph(Graph<Integer, String> graph, final List<Integer> set, final List<Entry<Integer, Integer>> inner) {
        Transformer<Integer, Paint> vertexTransformer = new Transformer<Integer, Paint>() {
            @Override
            public Paint transform(Integer i) {
                if (set.contains(i)) {
                    return Color.GREEN;
                } else {
                    return Color.LIGHT_GRAY;
                }
            }
        };
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f, 3.0f}, 0.1f);
        final Stroke secondStrole = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{15.0f}, 0.5f);
        Transformer<String, Stroke> edgeTransformer = new Transformer<String, Stroke>() {
            @Override
            public Stroke transform(String i) {
                String[] items = i.split("->");
                Integer from = Integer.parseInt(items[0]);
                Integer to = Integer.parseInt(items[1]);
                Entry<Integer, Integer> pattern = new AbstractMap.SimpleEntry<Integer, Integer>(from, to);
                if (inner.contains(pattern)) {
                    return edgeStroke;
                } else {
                    return secondStrole;
                }
            }
        };
        plotCustomizedGraph(graph, vertexTransformer, edgeTransformer);
    }

    public static class WindowThread extends Thread {

        private final Graph<Integer, String> graph;
        private final Transformer<Integer, Paint> vertexTransformer;
        private final Transformer<String, Stroke> edgeTransformer;

        private WindowThread(Graph<Integer, String> graph,
                Transformer<Integer, Paint> vertexTransformer,
                Transformer<String, Stroke> edgeTransformer) {
            this.graph = graph;
            this.vertexTransformer = vertexTransformer;
            this.edgeTransformer = edgeTransformer;
        }

        @Override
        public void run() {
            plotCustomizedGraph(graph, vertexTransformer, edgeTransformer);
        }

        public void plotCustomizedGraph(Graph<Integer, String> graph,
                Transformer<Integer, Paint> vertexTransformer,
                Transformer<String, Stroke> edgeTransformer) {
            Layout<Integer, String> layout = new CircleLayout<Integer, String>(graph);
            layout.setSize(new Dimension(600, 600));
            BasicVisualizationServer server = new BasicVisualizationServer(layout);
            server.setPreferredSize(new Dimension(650, 650));
            RenderContext renderContext = server.getRenderContext();
            renderContext.setVertexFillPaintTransformer(vertexTransformer);
            renderContext.setEdgeStrokeTransformer(edgeTransformer);
            renderContext.setVertexLabelTransformer(new ToStringLabeller());
            server.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

            JFrame frame = new JFrame("Graph visualisation");
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.getContentPane().add(server);
            frame.pack();
            frame.setVisible(true);
        }
    }
}
