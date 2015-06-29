package edu.agh.controller;

import edu.agh.commandgrammar.CommandTreeParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.antlr.runtime.tree.CommonTree;
import org.openide.util.Exceptions;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marcin
 */
public class Interpreter extends Thread {

    public Controller controller;

    public static void main(String[] args) {
        Interpreter contr = new Interpreter();
        contr.start();
    }

    public Interpreter() {
        controller = new Controller();
    }

    @Override
    public void run() {
        try {
            //Terminal terminal = TerminalFactory.create();
            InputStream term_in = //terminal.wrapInIfNeeded(System.in);
                    System.in;
            
            BufferedReader br = //new ConsoleReader();
                    new BufferedReader(new InputStreamReader(term_in));
            String input;
            InputParser parser = new InputParser();

            System.out.print("\n>");
            while ((input = br.readLine()) != null) {
                if (input.length() > 1) {
                    parseAndInvokeLine(input, parser, controller);
                }
                System.out.print("\n>");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void parseAndInvokeLine(String line, InputParser parser, Controller controller) throws NumberFormatException {
        CommonTree tree = parser.genericParse(line);
        int operationType = tree.getType();
        int argumentValue;
        int metric;
        String nick;
        Double result;
        switch (operationType) {
            case CommandTreeParser.METRIC_BYID:
                argumentValue = obtainNumericArgument(tree, 2);
                metric = tree.getChild(0).getType();
                metricById(metric, argumentValue);
                break;
            case CommandTreeParser.DRAW_GRAPH:
                controller.plotGraph();
                break;
            case CommandTreeParser.LIST_USERS:
                controller.listUsers();
                break;
            case CommandTreeParser.LIST_FRIENDS_BYID:
                argumentValue = obtainFirstNumericArgument(tree);
                controller.listFriendsOf(argumentValue);
                break;
            case CommandTreeParser.LIST_GROUPS_BYID:
                argumentValue = obtainFirstNumericArgument(tree);
                controller.listGroups(argumentValue);
                break;
            case CommandTreeParser.LIST_GROUPS:
                controller.listAllGroups();
                break;
            case CommandTreeParser.LIST_MEMBERS:
                nick = tree.getChild(0).getText();
                controller.listMembers(nick);
                break;
            case CommandTreeParser.DRAW_GRAPH_GROUP:
                nick = tree.getChild(0).getText();
                controller.plotGraphByGroup(nick);
                break;
            case CommandTreeParser.METRIC_BYGROUP:
                nick = tree.getChild(1).getText();
                metric = tree.getChild(0).getType();
                metricByGroup(metric, nick);
                break;
            case CommandTreeParser.GROUP_METRIC:
                metric = tree.getChild(0).getType();
                groupMetric(metric);
                break;
            case CommandTreeParser.METRIC_FORALL:
                metric = tree.getChild(0).getType();
                metricForAll(metric);
                break;
            case CommandTreeParser.HELP:
                System.out.println("Usage:\n"
                        + ">[metric] by id [id]\n"
                        + "   -computes [metric](*) for given person\n"
                        + ">[metric] by group [nick]\n"
                        + "   -computes [metric](*) for group members\n"
                        + ">[group_metric] for all\n"
                        + "   -computes [group_metric](**) for all groups\n"
                        + ">[metric] for all\n"
                        + "   -computes [metric](*) for all people\n"
                        + ">draw graph users\n"
                        + "   -plots a graph of whole network\n"
                        + ">draw graph by group [nick]\n"
                        + "   -plots a graph of members of group\n"
                        + ">list users\n"
                        + "   -lists all people\n"
                        + ">list groups\n"
                        + "   -lists all groups\n"
                        + ">list members by group [nick]\n"
                        + "   -lists members of given group\n"
                        + ">list friends by id [id]\n"
                        + "   -list friends of given person\n"
                        + ">list groups by id [id]\n"
                        + "   -list groups of given person\n"
                        + ">close <or> quit <or> exit\n"
                        + "   -to close the program\n"
                        + ">help\n"
                        + "   -prints this message\n"
                        + "-----------------------------------------------\n"
                        + " (*)  available metrices are:\n"
                        + "       -betweeness\n"
                        + "       -closeness\n"
                        + "       -page rank\n"
                        + "       -eigenvector\n"
                        + " (**) available group metrices are:\n"
                        + "       -density\n"
                        + "       -cohesion\n");
                break;
            case CommandTreeParser.EXIT:
                System.out.println("bye!\n");
                System.exit(0);
                break;
            default:
                System.out.println("Error: unknown command!\n type 'help'");
        }
        //System.out.println();
    }

    public int obtainFirstNumericArgument(CommonTree tree) throws NumberFormatException {
        return obtainNumericArgument(tree, 1);
    }

    private int obtainNumericArgument(CommonTree tree, int i) {
        CommonTree argument = (CommonTree) tree.getChild(i - 1);
        int argumentValue = Integer.parseInt(argument.getText());
        return argumentValue;
    }

    private void metricById(int metric, int argumentValue) {
        switch (metric) {
            case CommandTreeParser.BETWEENESS:
                controller.commandBetweenessById(argumentValue);
                break;
            case CommandTreeParser.CLOSENESS:
                controller.commandClosenessById(argumentValue);
                break;
            case CommandTreeParser.EIGENVECTOR:
                controller.commandEigenvectorById(argumentValue);
                break;
            case CommandTreeParser.PAGERANK:
                controller.commandPageRankById(argumentValue);
                break;
            default:
                System.out.println("Unsupported metric!");
        }
    }

    private void metricByGroup(int metric, String nick) {
        switch (metric) {
            case CommandTreeParser.BETWEENESS:
                controller.betweenessByGroup(nick);
                break;
            case CommandTreeParser.CLOSENESS:
                controller.closenessByGroup(nick);
                break;
            case CommandTreeParser.EIGENVECTOR:
                controller.eigenvectorByGroup(nick);
                break;
            case CommandTreeParser.PAGERANK:
                controller.pageRankByGroup(nick);
                break;
            default:
                System.out.println("Unsupported metric!");
        }
    }

    private void groupMetric(int metric) {
        switch (metric) {
            case CommandTreeParser.DENSITY:
                controller.densityByAllGroup();
                break;
            case CommandTreeParser.COHESION:
                controller.cohesionByAllGroup();
                break;
            default:
                System.out.println("Unsupported metric!");
        }
    }

    private void metricForAll(int metric) {
        switch (metric) {
            case CommandTreeParser.BETWEENESS:
                controller.commandBetweenessForAll();
                break;
            case CommandTreeParser.CLOSENESS:
                controller.commandClosenessForAll();
                break;
            case CommandTreeParser.EIGENVECTOR:
                controller.commandEigenvectorForAll();
                break;
            case CommandTreeParser.PAGERANK:
                controller.commandPageRankForAll();
                break;
            default:
                System.out.println("Unsupported metric!");
        }
    }
}
