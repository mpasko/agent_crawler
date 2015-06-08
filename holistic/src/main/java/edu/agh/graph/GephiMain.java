/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.graph;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;
import samples.SampleGenerator;

/**
 *
 * @author marcin
 */
public class GephiMain {

    public static void main(String args[]) {
        List<Entry<Integer, Integer>> edgesSource = new SampleGenerator().generate(100);
        Map<Integer, Double> results = computeBetweeness(edgesSource);
        System.out.println("Betweeness");
        for (Entry<Integer, Double> value : results.entrySet()) {
            System.out.println(value);
        }
        results = computeCloseness(edgesSource);
        System.out.println("Closeness");
        for (Entry<Integer, Double> value : results.entrySet()) {
            System.out.println(value);
        }
        //new GephiMain().script();
    }

    public static DirectedGraph makeGraphFromSource(List<Entry<Integer, Integer>> edgesSource, GraphModel graphModel) {
        GraphFactory factory = graphModel.factory();
        NodeRepository repo = new NodeRepository();
        LinkedList<Edge> edges = new LinkedList<Edge>();
        DirectedGraph graph = graphModel.getDirectedGraph();
        for (Entry<Integer, Integer> entry : edgesSource) {
            Integer from = entry.getKey();
            Integer to = entry.getValue();
            Node n0 = repo.getNodeFromRepository(from, factory);
            Node n1 = repo.getNodeFromRepository(to, factory);
            Edge e1 = factory.newEdge(n0, n1);
            edges.add(e1);
        }
        LinkedList<Node> nodes = repo.getAllValues();
        
        for (Node node : nodes) {
            graph.addNode(node);
        }
        for (Edge edge : edges) {
            graph.addEdge(edge);
        }
        
        //graph.addAllNodes(nodes);
        //graph.addAllEdges(edges);
        return graph;
    }

    private static void printGraph(List<Entry<Integer, Integer>> edgesSource) {
        for (Entry<Integer, Integer> entry : edgesSource) {
            System.out.println(String.format("i:%s j:%s", entry.getKey(), entry.getValue()));
        }
    }

    private static void manualTest(GraphModel graphModel) {
        Node n1 = graphModel.factory().newNode("N1");
        n1.getNodeData().setLabel("Node 1");
        Node n2 = graphModel.factory().newNode("N2");
        n1.getNodeData().setLabel("Node 2");
        Edge e1 = graphModel.factory().newEdge(n2, n1, 1, true);
        DirectedGraph graph2 = graphModel.getDirectedGraph();
        graph2.addNode(n1);
        graph2.addNode(n2);
        graph2.addEdge(e1);
    }

    public static Map<Integer, Double> computeBetweeness(List<Entry<Integer, Integer>> edgesSource) {
        Map<Integer, Double> results = new GephiMain().calculateGephiMetrics(edgesSource, GraphDistance.BETWEENNESS);
        return results;
    }

    public static Map<Integer, Double> computeCloseness(List<Entry<Integer, Integer>> edgesSource) {
        Map<Integer, Double> results;
        results = new GephiMain().calculateGephiMetrics(edgesSource, GraphDistance.CLOSENESS);
        return results;
    }

    public Map<Integer, Double> calculateGephiMetrics(List<Entry<Integer, Integer>> edgesSource, String metric) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        
        AttributeController attributeController =
                Lookup.getDefault().lookup(AttributeController.class);
                //new AttributeControllerImpl();
        
        //manualTest(graphModel);
        
        
        /*
         List<Entry<Integer, Integer>> edgesSource = new MyConnector("mydb").getFriends();
         */
        //printGraph(edgesSource);

        DirectedGraph graph = makeGraphFromSource(edgesSource, graphModel);

        /*
         AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
         autoLayout.setGraphModel(graphModel);
         YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
         ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
        
         DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("Adjust by Sizes", Boolean.TRUE, 0.1f);
         DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("Repulsion strength", new Double(500.), 0f);
        
         autoLayout.addLayout(firstLayout, 0.5f);
         autoLayout.addLayout(secondLayout, 0.5f, new AutoLayout.DynamicProperty[]{adjustBySizeProperty, repulsionProperty});
         autoLayout.execute();*/

        AttributeModel attributeModel = (AttributeModel) attributeController.getModel(workspace);
        
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel, attributeModel);

        //AttributeColumn col = (AttributeColumn) attributeModel.getNodeTable().getColumn(metric);
        
        HashMap<Integer, Double> output = new HashMap<Integer, Double>();
        for (Node n : graph.getNodes()) {
            Double centrality = (Double) n.getNodeData().getAttributes().getValue(metric);
            output.put(Integer.parseInt(n.getNodeData().getLabel()), centrality);
            //System.out.println(centrality);
        }

        //TODO
        // http://www.slideshare.net/gephi/gephi-toolkit-tutorialtoolkit
        // http://gephi.github.io/developers/
        return output;
    }

    public static class NodeRepository {
        HashMap<Integer, Node> nodesRepository = new HashMap<Integer, Node>();
        
        public Node getNodeFromRepository(Integer from, GraphFactory factory) {
            Node n0;
            if (nodesRepository.get(from) == null) {
                n0 = factory.newNode("n" + from);
                n0.getNodeData().setLabel(from.toString());
                nodesRepository.put(from, n0);
            } else {
                n0 = nodesRepository.get(from);
            }
            return n0;
        }

        public LinkedList<Node> getAllValues() {
            LinkedList<Node> nodes = new LinkedList<Node>(nodesRepository.values());
            return nodes;
        }
    }
    
    public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get controllers and models
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

        //Import file
        Container container;
        try {
            File file = new File("polblogs.gml");
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force DIRECTED
            container.setAllowAutoNode(false);  //Don't create missing nodes
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        //List node columns
        AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
        AttributeModel model = ac.getModel();
        for (AttributeColumn col : model.getNodeTable().getColumns()) {
            System.out.println(col);
        }

        //Add boolean column
        AttributeColumn testCol = model.getNodeTable().addColumn("test", AttributeType.BOOLEAN);

        //Write values to nodes
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        for (Node n : graphModel.getGraph().getNodes()) {
            n.getNodeData().getAttributes().setValue(testCol.getIndex(), Boolean.TRUE);
        }

        //Iterate values - fastest
        AttributeColumn sourceCol = model.getNodeTable().getColumn("source");
        for (Node n : graphModel.getGraph().getNodes()) {
            System.out.println(n.getNodeData().getAttributes().getValue(sourceCol.getIndex()));
        }

        //Iterate values - normal
        for (Node n : graphModel.getGraph().getNodes()) {
            System.out.println(n.getNodeData().getAttributes().getValue("source"));
        }
    }
}
