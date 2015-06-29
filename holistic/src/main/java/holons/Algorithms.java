/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holons;

import edu.agh.controller.*;
import edu.agh.database.TwitterConnector;
import edu.agh.graph.GephiMain;
import edu.agh.graph.JungMain;
import edu.agh.utils.MapUtil;
import edu.uci.ics.jung.graph.Graph;
import interfaces.Cache;
import interfaces.IDataSource;
import interfaces.PersonGroup;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author marcin
 */
public class Algorithms {

    public static final double EPSILON = 0.0000001;
    private final IDataSource dataSource;

    //public List<Map.Entry<Integer, Integer>> edgesSource;
    public Algorithms() {
        //edgesSource = new SampleGenerator().generate(100);
        dataSource = new Cache(new TwitterConnector());
    }
    
    public Algorithms(IDataSource source) {
        dataSource = source;
    }

    public void commandBetweenessById(int identifier) {
        Map<Integer, Double> metricMap = computeBetweeness();
        printMetricByPerson(metricMap, identifier, "Betweeness");
    }

    public void commandClosenessById(int identifier) {
        Map<Integer, Double> metricMap = computeCloseness();
        printMetricByPerson(metricMap, identifier, "Closeness");
    }

    public void commandPageRankById(int identifier) {
        Map<Integer, Double> metricMap = computePageRank();
        printMetricByPerson(metricMap, identifier, "Page rank");
    }

    public void commandEigenvectorById(int identifier) {
        Map<Integer, Double> metricMap = computeEigenvector();
        printMetricByPerson(metricMap, identifier, "Eigenvector");
    }

    public void commandBetweenessForAll() {
        Map<Integer, Double> metric = computeBetweeness();
        metric = MapUtil.sortByValue(metric);
        this.printAllMetrics(metric, "betweeness");
    }

    public void commandClosenessForAll() {
        Map<Integer, Double> metric = computeCloseness();
        metric = MapUtil.sortByValue(metric);
        this.printAllMetrics(metric, "closeness");
    }

    public void commandEigenvectorForAll() {
        Map<Integer, Double> metric = computeEigenvector();
        metric = MapUtil.sortByValue(metric);
        this.printAllMetrics(metric, "eigenvector");
    }

    public void commandPageRankForAll() {
        Map<Integer, Double> metric = computePageRank();
        metric = MapUtil.sortByValue(metric);
        this.printAllMetrics(metric, "page rank");
    }

    public void plotGraph() {
        List<Entry<Integer, Integer>> edges = dataSource.getListOfLinks();
        Graph<Integer, String> graph = JungMain.generateGraphFromSources(edges);
        JungMain.plotGraph(graph);
    }

    public void listUsers() {
        Map<Integer, String> usernames = dataSource.getListOfPeople();
        for (Entry<Integer, String> user : usernames.entrySet()) {
            String line = String.format("%s %s", user.getKey(), user.getValue());
            System.out.println(line);
        }
    }

    public void listFriendsOf(Integer from) {
        List<Integer> friends = dataSource.getAllLinksFrom(from);
        for (Integer friend : friends) {
            System.out.println(nameDescription(friend));
            List<Integer> friendsOfFriend = dataSource.getAllLinksFrom(friend);
            this.printColumnOfUsers(friendsOfFriend, 1, from);
        }
    }

    public void listGroups(Integer from) {
        List<PersonGroup> groups = dataSource.determineGroupsByAspect(from, TwitterConnector.IS_FAN);
        printGroups(groups);
    }

    public void listAllGroups() {
        List<PersonGroup> groups = dataSource.getAllGroupsByAspect(TwitterConnector.IS_FAN);
        printGroups(groups);
    }

    public void printGroups(List<PersonGroup> groups) {
        ArrayList<PersonGroup> sortedGroups = new ArrayList<PersonGroup>(groups);
        Collections.sort(sortedGroups, new GroupComparator());
        for (PersonGroup group : sortedGroups) {
            int size = group.getMembers().size();
            String line = String.format("%s of type:%s total:%s members", group.getLink(), group.getType(), size);
            System.out.println(line);
            this.printColumnOfUsers(group.getMembers(), 1, null);
        }
    }

    void plotGraphByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Integer> members = foundGroup.getMembers();
            List<Entry<Integer, Integer>> links = dataSource.getListOfLinks();
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(links, members);
            List<Entry<Integer, Integer>> outside = filterEdgegsOutgoing(links, members);
            //Map<Integer, String> names = dataSource.getListOfPeople();
            Graph<Integer, String> graph = JungMain.generateGraphFromSources(members, filtered, outside);
            JungMain.plotBisectGraph(graph, members, filtered);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public PersonGroup findGroupByNick(String nick) {
        List<PersonGroup> groups = dataSource.getAllGroupsByAspect(TwitterConnector.IS_FAN);
        PersonGroup foundGroup = null;
        for (PersonGroup group : groups) {
            if (group.getLink().equalsIgnoreCase(nick)) {
                foundGroup = group;
            }
        }
        return foundGroup;
    }

    public List<Entry<Integer, Integer>> filterEdgegsByMembership(List<Entry<Integer, Integer>> links, List<Integer> members) {
        List<Entry<Integer, Integer>> filtered = new LinkedList<Entry<Integer, Integer>>();
        for (Entry<Integer, Integer> link : links) {
            if (members.contains(link.getKey()) && members.contains(link.getValue())) {
                filtered.add(link);
            }
        }
        return filtered;
    }

    private List<Entry<Integer, Integer>> filterEdgegsOutgoing(List<Entry<Integer, Integer>> links, List<Integer> members) {
        List<Entry<Integer, Integer>> filtered = new LinkedList<Entry<Integer, Integer>>();
        for (Entry<Integer, Integer> link : links) {
            boolean condition = members.contains(link.getKey()) && !members.contains(link.getValue());
            condition |= !members.contains(link.getKey()) && members.contains(link.getValue());
            if (condition) {
                filtered.add(link);
            }
        }
        return filtered;
    }

    public void betweenessByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Map<Integer, Double> betweeness = GephiMain.computeBetweeness(filtered);
            String metric = "betweeness";
            printAllMetrics(betweeness, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void closenessByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Map<Integer, Double> closeness = GephiMain.computeCloseness(filtered);
            String metric = "closeness";
            printAllMetrics(closeness, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void pageRankByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Graph<Integer, String> graph = JungMain.generateGraphFromSources(filtered);
            Map<Integer, Double> pageRank = JungMain.computePageRank(graph, 0.1);
            String metric = "page rank";
            printAllMetrics(pageRank, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void eigenvectorByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Graph<Integer, String> graph = JungMain.generateGraphFromSources(filtered);
            Map<Integer, Double> eigenvector = JungMain.computeEigenvector(graph);
            String metric = "eigenvector";
            printAllMetrics(eigenvector, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void cohesionByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Graph<Integer, String> graph = JungMain.generateGraphFromSources(filtered);
            Map<Integer, Double> cohesion = JungMain.computeEigenvector(graph);
            String metric = "cohesion";
            printAllMetrics(cohesion, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void densityByGroup(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            List<Entry<Integer, Integer>> filtered = filterEdgegsByMembership(dataSource.getListOfLinks(), foundGroup.getMembers());
            Graph<Integer, String> graph = JungMain.generateGraphFromSources(filtered);
            Map<Integer, Double> density = JungMain.computeEigenvector(graph);
            String metric = "density";
            printAllMetrics(density, metric);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void densityByAllGroup() {
        List<PersonGroup> groups = dataSource.getAllGroupsByAspect(TwitterConnector.IS_FAN);
        List<Entry<Integer, Integer>> allConnections = dataSource.getListOfLinks();
        Map<PersonGroup, Double> metric = new HashMap<PersonGroup, Double>();
        for (PersonGroup group : groups) {
            List<Integer> members = group.getMembers();
            List<Entry<Integer, Integer>> connectionsInGroup = filterEdgegsByMembership(allConnections, members);
            int membersCount = members.size();
            double groupDensity = connectionsInGroup.size() / ((double) membersCount * (membersCount - 1));
            metric.put(group, groupDensity);
        }
        metric = MapUtil.sortByValue(metric);
        printGroupMetric(metric, "density");
    }

    public void cohesionByAllGroup() {
        List<PersonGroup> groups = dataSource.getAllGroupsByAspect(TwitterConnector.IS_FAN);
        List<Entry<Integer, Integer>> allConnections = dataSource.getListOfLinks();
        Map<PersonGroup, Double> metric = new HashMap<PersonGroup, Double>();
        for (PersonGroup group : groups) {
            List<Integer> members = group.getMembers();
            List<Entry<Integer, Integer>> connectionsInGroup = filterEdgegsByMembership(allConnections, members);
            List<Entry<Integer, Integer>> connectionsOutGroup = filterEdgegsOutgoing(allConnections, members);
            int membersCount = members.size();
            double groupDensity = connectionsInGroup.size() / ((double) membersCount * (membersCount - 1));
            double denominator = connectionsOutGroup.size() / ((double) membersCount * (allConnections.size() - membersCount));
            double cohesion = groupDensity / denominator;
            metric.put(group, cohesion);
        }
        metric = MapUtil.sortByValue(metric);
        printGroupMetric(metric, "cohesion");
    }

    public void printAllMetrics(Map<Integer, Double> values, String metric) {
        for (Entry<Integer, Double> item : values.entrySet()) {
            Integer userid = item.getKey();
            System.out.println(String.format("for %s %s is %s", nameDescription(userid), metric, item.getValue()));
        }
    }

    public void listMembers(String nick) {
        PersonGroup foundGroup = findGroupByNick(nick);
        if (foundGroup != null) {
            printColumnOfUsers(foundGroup.getMembers(), 0, null);
        } else {
            System.out.println(String.format("Group '%s' not found!", nick));
        }
    }

    public void printColumnOfUsers(List<Integer> friends, int tab, Integer filter) {
        StringBuilder b = new StringBuilder();
        int i;
        for (i = 0; i < tab * 4; ++i) {
            b.append(' ');
        }
        if (tab > 0) {
            b.append("* ");
        }
        i = 0;
        for (Integer friend : friends) {
            if ((filter == null) || ((int)filter != (int)friend)) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(nameDescription(friend));
                ++i;
            }
        }
        System.out.println(b.toString());
    }

    public void printMetricByPerson(Map<Integer, Double> metricMap, int identifier, String metric) {
        StringBuilder b = new StringBuilder();
        b.append(String.format("%s for %s is %s", metric, nameDescription(identifier), metricMap.get(identifier)));
        List<Integer> friends = dataSource.getAllLinksFrom(identifier);
        b.append(String.format("%n----%n%s for friends:%n", metric));
        for (Integer friend : friends) {
            b.append(String.format("%s: %s%n", nameDescription(friend), metricMap.get(friend)));
        }
        System.out.println(b.toString());
    }

    public String nameDescription(int identifier) {
        return String.format("%s (%s)", identifier, dataSource.getListOfPeople().get(identifier));
    }

    public void printGroupMetric(Map<PersonGroup, Double> metric, String metricName) {
        StringBuilder b = new StringBuilder();
        for (Entry<PersonGroup, Double> entry : metric.entrySet()) {
            Double cohesion = entry.getValue();
            PersonGroup group = entry.getKey();
            int membersCount = group.getMembers().size();
            if (cohesion >= EPSILON) {
                String format = "for group: %s of size: %s %s is: %s%n";
                b.append(String.format(format, group.getLink(), membersCount, metricName, cohesion));
            }
        }
        System.out.println(b.toString());
    }

    public Map<Integer, Double> computeBetweeness() {
        List<Entry<Integer, Integer>> edges = dataSource.getListOfLinks();
        Map<Integer, Double> metricMap = GephiMain.computeBetweeness(edges);
        return metricMap;
    }

    public Map<Integer, Double> computeCloseness() {
        List<Entry<Integer, Integer>> edges = dataSource.getListOfLinks();
        Map<Integer, Double> metricMap = GephiMain.computeCloseness(edges);
        return metricMap;
    }

    public Map<Integer, Double> computePageRank() {
        List<Entry<Integer, Integer>> edges = dataSource.getListOfLinks();
        Graph<Integer, String> graph = JungMain.generateGraphFromSources(edges);
        Map<Integer, Double> metricMap = JungMain.computePageRank(graph, 0.1);
        return metricMap;
    }

    public Map<Integer, Double> computeEigenvector() {
        List<Entry<Integer, Integer>> edges = dataSource.getListOfLinks();
        Graph<Integer, String> graph = JungMain.generateGraphFromSources(edges);
        Map<Integer, Double> metricMap = JungMain.computeEigenvector(graph);
        return metricMap;
    }

    private static class GroupComparator implements Comparator<PersonGroup> {

        public GroupComparator() {
        }

        @Override
        public int compare(PersonGroup o1, PersonGroup o2) {
            return o1.getMembers().size() - o2.getMembers().size();
        }
    }
}
