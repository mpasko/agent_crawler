/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.database;

import edu.agh.manualutils.PrinterUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openide.util.Exceptions;

/**
 *
 * @author marcin
 */
public class TwitterConnector extends MyConnector{
    public TwitterConnector() {
        super("twitterdb");
    }
    
    public void putTwitterUser(int id, String link, String name, String place) {
        PreparedStatement statement=null;
        try {
            statement = connect
                .prepareStatement("insert into Osoba(id, nazwa, link, miejsce) values (?, ?, ?, ?)");
            int index=1;
            // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
            // Parameters start with 1
            statement.setInt(index++, id);
            statement.setString(index++, name);
            statement.setString(index++, link);
            statement.setString(index++, place);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
        }
    }
    
    public void putTwitterUser(int id, String link, String name, String place, Boolean celebryta) {
        PreparedStatement statement=null;
        try {
            statement = connect
                .prepareStatement("insert into Osoba(id, nazwa, link, miejsce, celebryta) values (?, ?, ?, ?, ?)");
            int index=1;
            // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
            // Parameters start with 1
            statement.setInt(index++, id);
            statement.setString(index++, name);
            statement.setString(index++, link);
            statement.setString(index++, place);
            statement.setBoolean(index++, celebryta);
            statement.executeUpdate();
            statement.close();
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
        PreparedStatement statement=null;
        try {
            final String query = "insert into Przyjaciel(id, nazwa, link, Osoba_id, typ) "+
                    "values (default, ?, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                int index=1;
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
        PreparedStatement statement=null;
        try {
            final String query = "insert into Tweet(id, link, czas, typ, Osoba_id) "+
                    "values (default, ?, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                int index=1;
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
        PreparedStatement statement=null;
        try {
            final String query = "insert into ListaDyskusyjna(id, nazwa, typ, Osoba_id) "+
                    "values (default, ?, ?, ?) ";
            statement = connect.prepareStatement(query);
            for (String item : items) {
                int index=1;
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
}
