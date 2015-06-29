/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.database;

import edu.agh.manualutils.PrinterUtil;
import interfaces.IDataSource;
import interfaces.Interaction;
import interfaces.PersonGroup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openide.util.Exceptions;

/**
 *
 * @author marcin
 */
public class TwitterConnector extends MyConnector implements IDataSource {

    public static final String IS_FAN = "IS_FAN";

    public TwitterConnector() {
        super("twitterdb");
    }

    public void putTwitterUser(int id, String link, String name, String place) {
        PreparedStatement statement = null;
        try {
            statement = connect
                    .prepareStatement("insert into Osoba(id, nazwa, link, miejsce) values (?, ?, ?, ?)");
            int index = 1;
            statement.setInt(index++, id);
            statement.setString(index++, name);
            statement.setString(index++, link);
            statement.setString(index++, place);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    public void putTwitterUser(int id, String link, String name, String place, Boolean celebryta) {
        PreparedStatement statement = null;
        try {
            statement = connect
                    .prepareStatement("insert into Osoba(id, nazwa, link, miejsce, celebryta) values (?, ?, ?, ?, ?)");
            int index = 1;
            statement.setInt(index++, id);
            statement.setString(index++, name);
            statement.setString(index++, link);
            statement.setString(index++, place);
            statement.setBoolean(index++, celebryta);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    public void putTwitterMalformedUser(int id, String link) {
        PreparedStatement statement = null;
        try {
            statement = connect
                    .prepareStatement("insert into Osoba(id, link, uszkodzony) values (?, ?, ?)");
            int index = 1;
            statement.setInt(index++, id);
            statement.setString(index++, link);
            statement.setBoolean(index++, true);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    public void updateTwitterMalformedUser(int id, String link) {
        PreparedStatement statement = null;
        try {
            statement = connect
                    .prepareStatement("update table Osoba set uszkodzony = ? where id = ?");
            int index = 1;
            statement.setBoolean(index++, true);
            statement.setInt(index++, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    /**
     *
     * @param items [nazwa, link]
     * @param id osoba_id
     * @param type friend, follower
     */
    public void putTwitterFriendItems(Map<String, String> items, int id, String type) {
        if (items == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            final String query = "insert into Przyjaciel(id, nazwa, link, Osoba_id, typ) "
                    + "values (default, ?, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                int index = 1;
                statement.setString(index++, friend.getKey());
                statement.setString(index++, friend.getValue());
                statement.setInt(index++, id);
                statement.setString(index++, type);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            PrinterUtil.printMap(items, "MyConnector.putItems");
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    /**
     *
     * @param items -[klucz, typ]
     * @param id -osoba_id
     * @param date
     */
    public void putTweetItems(Map<String, String> items, int id, Date date) {
        if (items == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            final String query = "insert into Tweet(id, link, czas, typ, Osoba_id) "
                    + "values (default, ?, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                int index = 1;
                statement.setString(index++, friend.getKey());
                statement.setDate(index++, new java.sql.Date(date.getTime()));
                statement.setString(index++, friend.getValue());
                statement.setInt(index++, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            PrinterUtil.printMap(items, "MyConnector.putItems");
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    public void putTwitterListItems(List<String> items, int id, String type) {
        if (items == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            final String query = "insert into ListaDyskusyjna(id, nazwa, typ, Osoba_id) "
                    + "values (default, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (String item : items) {
                int index = 1;
                statement.setString(index++, item);
                statement.setString(index++, type);
                statement.setInt(index++, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            PrinterUtil.printList(items, "MyConnector.putItems");
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public Map<Integer, String> getListOfPeople() {
        Map<Integer, String> friends = new HashMap<Integer, String>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select kto.id, kto.link "
                    + "from Osoba as kto;";
            statement = connect.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int index = 1;
                Integer id = resultSet.getInt(index++);
                String link = resultSet.getString(index++);
                friends.put(id, link);
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return friends;
    }

    @Override
    public List<Entry<Integer, Integer>> getListOfLinks() {
        return this.getFriends();
    }

    @Override
    public List<Integer> getAllLinksFrom(Integer from) {
        LinkedList<Integer> friends = new LinkedList<Integer>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select komu.id "
                    + "from Przyjaciel "
                    + "inner join Osoba as komu "
                    + "on Przyjaciel.link = komu.link "
                    + "where Przyjaciel.Osoba_id = ?;";
            statement = connect.prepareStatement(query);
            statement.setInt(1, from);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer to = resultSet.getInt(1);
                friends.add(to);
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return friends;
    }

    @Override
    public List<Interaction> getInteractionTimeline() {
        LinkedList<Interaction> interactions = new LinkedList<Interaction>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select kto.osoba_id, komu.osoba_id, kto.czas "
                         + "from tweet as kto inner join tweet as komu "
                         + "on kto.link = komu.link where kto.osoba_id != komu.osoba_id;";
            statement = connect.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int index=1;
                Integer from = resultSet.getInt(index++);
                Integer to = resultSet.getInt(index++);
                java.sql.Date sql_date = resultSet.getDate(index++);
                Date date = new java.util.Date(sql_date.getTime());
                interactions.add(new Interaction(date, from, to));
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return interactions;
    }

    @Override
    public List<PersonGroup> determineGroupsByAspect(Integer from, String aspect) {
        if (aspect.equalsIgnoreCase(IS_FAN)) {
            return determineGroupsByBeingFan(from);
        } else {
            String message = String.format("Aspect %s is unsupported for:%s", aspect, this.getClass().getName());
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public List<String> getSupportedAspects() {
        return Arrays.asList(IS_FAN);
    }

    private List<PersonGroup> determineGroupsByBeingFan(Integer from) {
        List<String> celebrities = this.determineIdolsOf(from);
        return makeGroupsFromCelebrities(celebrities);
    }

    public LinkedList<String> determineIdolsOf(Integer from) {
        LinkedList<String> friends = new LinkedList<String>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select distinct(Przyjaciel.link) " + 
                    "from Przyjaciel " + 
                    "left outer join Osoba as celeb " + 
                    "on Przyjaciel.link = celeb.link " + 
                    "where celeb.link is NULL " + 
                    "and Przyjaciel.osoba_id = ?;";
            statement = connect.prepareStatement(query);
            statement.setInt(1, from);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String next = resultSet.getString(1);
                friends.add(next);
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return friends;
    }
    
    public LinkedList<Integer> determineFansOf(String celeb) {
        LinkedList<Integer> fans = new LinkedList<Integer>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select Osoba_id "
                    + "from Przyjaciel "
                    + "where Przyjaciel.link = ?;";
            statement = connect.prepareStatement(query);
            statement.setString(1, celeb);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer to = resultSet.getInt(1);
                fans.add(to);
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return fans;
    }

    @Override
    public List<PersonGroup> getAllGroupsByAspect(String aspect) {
        if (aspect.equalsIgnoreCase(IS_FAN)) {
            List<String> celebrities = this.getUsersToExamine();
            return makeGroupsFromCelebrities(celebrities);
        } else {
            String message = String.format("Aspect %s is unsupported for:%s", aspect, this.getClass().getName());
            throw new UnsupportedOperationException(message);
        }
    }

    public List<PersonGroup> makeGroupsFromCelebrities(List<String> celebrities) {
        List<PersonGroup> groups = new LinkedList<PersonGroup>();
        for (String celeb : celebrities) {
            LinkedList<Integer> fans = determineFansOf(celeb);
            if (fans.size()>1){
                PersonGroup group = new PersonGroup(IS_FAN, celeb, fans);
                groups.add(group);
            }
        }
        return groups;
    }
}
