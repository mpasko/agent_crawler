/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.controller;

import edu.agh.graph.GephiMainTest;
import interfaces.IDataSource;
import interfaces.PersonGroup;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marcin
 */
public class ControllerTest {
    private static IDataSource mockedDataset;
    public static final String SOME_CELEBRITY = "Angelina Joile";
    public static final String IS_FAN = "IS_FAN";

    private static Map<Integer, String> generateMockedListOfPeople() {
        Map<Integer, String> mockedListOfPeople = new HashMap<Integer, String>();
        mockedListOfPeople.put(0, "First Parsons");
        mockedListOfPeople.put(1, "Second Parsons");
        mockedListOfPeople.put(2, "Third Parsons");
        mockedListOfPeople.put(3, "Fourth Parsons");
        mockedListOfPeople.put(4, "Fifth Parsons");
        return mockedListOfPeople;
    }

    private static List<PersonGroup> generateSampleGroup() {
        List<PersonGroup> groups = new LinkedList<PersonGroup>();
        List<Integer> members = Arrays.asList(0,2,4);
        groups.add(new PersonGroup(IS_FAN, SOME_CELEBRITY, members));
        return groups;
    }
    
    public ControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        mockedDataset = createMock(IDataSource.class);
        List<Entry<Integer, Integer>> mockedListOfLinks = GephiMainTest.generateTestGraph();
        expect(mockedDataset.getListOfLinks()).andReturn(mockedListOfLinks).anyTimes();
        expect(mockedDataset.getListOfPeople()).andReturn(generateMockedListOfPeople()).anyTimes();
        expect(mockedDataset.getAllLinksFrom(gt(2))).andReturn(Arrays.asList(0, 1, 2)).anyTimes();
        expect(mockedDataset.getAllLinksFrom(leq(2))).andReturn(Arrays.asList(3, 4)).anyTimes();
        expect(mockedDataset.determineGroupsByAspect(anyInt(), eq(IS_FAN))).andReturn(generateSampleGroup()).anyTimes();
        expect(mockedDataset.getAllGroupsByAspect(eq(IS_FAN))).andReturn(generateSampleGroup()).anyTimes();
        replay(mockedDataset);
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of commandBetweenessById method, of class Controller.
     */
    @Test
    public void testCommandBetweenessById() {
        System.out.println("commandBetweenessById");
        int identifier = 0;
        Controller instance = new Controller(mockedDataset);
        instance.commandBetweenessById(identifier);
    }

    /**
     * Test of commandClosenessById method, of class Controller.
     */
    @Test
    public void testCommandClosenessById() {
        System.out.println("commandClosenessById");
        int identifier = 0;
        Controller instance = new Controller(mockedDataset);
        instance.commandClosenessById(identifier);
    }

    /**
     * Test of commandPageRankById method, of class Controller.
     */
    @Test
    public void testCommandPageRankById() {
        System.out.println("commandPageRankById");
        int identifier = 0;
        Controller instance = new Controller(mockedDataset);
        instance.commandPageRankById(identifier);
    }

    /**
     * Test of commandEigenvectorById method, of class Controller.
     */
    @Test
    public void testCommandEigenvectorById() {
        System.out.println("commandEigenvectorById");
        int identifier = 0;
        Controller instance = new Controller(mockedDataset);
        instance.commandEigenvectorById(identifier);
    }

    /**
     * Test of commandBetweenessForAll method, of class Controller.
     */
    @Test
    public void testCommandBetweenessForAll() {
        System.out.println("commandBetweenessForAll");
        Controller instance = new Controller(mockedDataset);
        instance.commandBetweenessForAll();
    }

    /**
     * Test of commandClosenessForAll method, of class Controller.
     */
    @Test
    public void testCommandClosenessForAll() {
        System.out.println("commandClosenessForAll");
        Controller instance = new Controller(mockedDataset);
        instance.commandClosenessForAll();
    }

    /**
     * Test of commandEigenvectorForAll method, of class Controller.
     */
    @Test
    public void testCommandEigenvectorForAll() {
        System.out.println("commandEigenvectorForAll");
        Controller instance = new Controller(mockedDataset);
        instance.commandEigenvectorForAll();
    }

    /**
     * Test of commandPageRankForAll method, of class Controller.
     */
    @Test
    public void testCommandPageRankForAll() {
        System.out.println("commandPageRankForAll");
        Controller instance = new Controller(mockedDataset);
        instance.commandPageRankForAll();
    }

    /**
     * Test of plotGraph method, of class Controller.
     */
    @Test
    public void testPlotGraph() {
        System.out.println("plotGraph");
        Controller instance = new Controller(mockedDataset);
        instance.plotGraph();
    }

    /**
     * Test of listUsers method, of class Controller.
     */
    @Test
    public void testListUsers() {
        System.out.println("listUsers");
        Controller instance = new Controller(mockedDataset);
        instance.listUsers();
    }

    /**
     * Test of listFriendsOf method, of class Controller.
     */
    @Test
    public void testListFriendsOf() {
        System.out.println("listFriendsOf");
        Integer from = 0;
        Controller instance = new Controller(mockedDataset);
        instance.listFriendsOf(from);
    }

    /**
     * Test of listGroups method, of class Controller.
     */
    @Test
    public void testListGroups() {
        System.out.println("listGroups");
        Integer from = 0;
        Controller instance = new Controller(mockedDataset);
        instance.listGroups(from);
    }

    /**
     * Test of listAllGroups method, of class Controller.
     */
    @Test
    public void testListAllGroups() {
        System.out.println("listAllGroups");
        Controller instance = new Controller(mockedDataset);
        instance.listAllGroups();
    }

    /**
     * Test of findGroupByNick method, of class Controller.
     */
    @Test
    public void testFindGroupByNick() {
        System.out.println("findGroupByNick");
        String nick = "";
        Controller instance = new Controller(mockedDataset);
        PersonGroup expResult = null;
        PersonGroup result = instance.findGroupByNick(nick);
    }

    /**
     * Test of betweenessByGroup method, of class Controller.
     */
    @Test
    public void testBetweenessByGroup() {
        System.out.println("betweenessByGroup");
        String nick = SOME_CELEBRITY;
        Controller instance = new Controller(mockedDataset);
        instance.betweenessByGroup(nick);
    }

    /**
     * Test of closenessByGroup method, of class Controller.
     */
    @Test
    public void testClosenessByGroup() {
        System.out.println("closenessByGroup");
        String nick = SOME_CELEBRITY;
        Controller instance = new Controller(mockedDataset);
        instance.closenessByGroup(nick);
    }

    /**
     * Test of pageRankByGroup method, of class Controller.
     */
    @Test
    public void testPageRankByGroup() {
        System.out.println("pageRankByGroup");
        String nick = SOME_CELEBRITY;
        Controller instance = new Controller(mockedDataset);
        instance.pageRankByGroup(nick);
    }

    /**
     * Test of eigenvectorByGroup method, of class Controller.
     */
    @Test
    public void testEigenvectorByGroup() {
        System.out.println("eigenvectorByGroup");
        String nick = SOME_CELEBRITY;
        Controller instance = new Controller(mockedDataset);
        instance.eigenvectorByGroup(nick);
    }

    /**
     * Test of densityByAllGroup method, of class Controller.
     */
    @Test
    public void testDensityByAllGroup() {
        System.out.println("densityByAllGroup");
        Controller instance = new Controller(mockedDataset);
        instance.densityByAllGroup();
    }

    /**
     * Test of cohesionByAllGroup method, of class Controller.
     */
    @Test
    public void testCohesionByAllGroup() {
        System.out.println("cohesionByAllGroup");
        Controller instance = new Controller(mockedDataset);
        instance.cohesionByAllGroup();
    }

    /**
     * Test of listMembers method, of class Controller.
     */
    @Test
    public void testListMembers() {
        System.out.println("listMembers");
        String nick = SOME_CELEBRITY;
        Controller instance = new Controller(mockedDataset);
        instance.listMembers(nick);
    }

}
