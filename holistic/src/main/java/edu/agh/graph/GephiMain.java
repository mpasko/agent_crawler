/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.graph;

import edu.agh.database.MyConnector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.AutoLayout.DynamicProperty;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.nodes.Node.Property;
import org.openide.util.Lookup;

/**
 *
 * @author marcin
 */
public class GephiMain {
    public static void main(String args[]) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        ArrayList<Edge> edges = new ArrayList<Edge>(10);
        ArrayList<Node> nodes = new ArrayList<Node>(11);
        for (int i=0; i<4; ++i) {
            Node n0 = graphModel.factory().newNode("n"+i);
            n0.setLabel("Node "+i);
            nodes.add(i, n0);
        }
        MyConnector connect = new MyConnector();
        for (Entry<Integer, Integer> entry: connect.getFriends()) {
            Node n0 = nodes.get(entry.getKey());
            Node n1 = nodes.get(entry.getValue());
            Edge e1 = graphModel.factory().newEdge(n0, n1);
            edges.add(e1);
        }
        
        DirectedGraph graph = graphModel.getDirectedGraph();
        graph.addAllNodes(nodes);
        graph.addAllEdges(edges);
        
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
        
        AttributeModel attributeModel = (AttributeModel) Lookup.getDefault().lookup(AttributeController.class).getModel();
        
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graph, attributeModel);
        
        AttributeColumn col = (AttributeColumn) attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        
        for (Node n : graph.getNodes()) {
            //centrality = (Double)n.getNodeData();
        }
        
        //TODO
        // http://www.slideshare.net/gephi/gephi-toolkit-tutorialtoolkit
        // http://gephi.github.io/developers/
    }
}
