/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.joda.time.DateTime;
import org.openide.util.Exceptions;

/**
 *
 * @author marcin
 */
public class MyConnector {

    public static void main(String[] args) {
    }
    private Connection connect = null;

    public MyConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/mydb?"
                    + "user=crawler&password=crawler");
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void putUser(String link, int id) {
        try {
            PreparedStatement statement = connect
                .prepareStatement("insert into Osoba(id, nazwa, link) values (?, ?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
            // Parameters start with 1
            statement.setInt(1, id);
            statement.setString(2, "");
            statement.setString(3, link);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /*
     * friends {nazwa, link}
     */
    public void putFriends(int id, TreeMap<String, String> friends) {
        putItems("Przyjaciel", friends, id);
    }

    public void putLikes(int id, TreeMap<String, String> likes) {
        putItems("CoLubi", likes, id);
    }

    /* 'szkola', 'praca','mieszkanie' */
    public void putLiving(int id, TreeMap<String, String> living) {
        putPlaceItems("mieszkanie", living, id);
    }

    public void putWork(int id, TreeMap<String, String> work) {
        putPlaceItems("praca", work, id);
    }

    public void putEdu(int id, TreeMap<String, String> edu) {
        putPlaceItems("szkola", edu, id);
    }

    public void putTimeline(int id, DateTime date, TreeMap<String, String> content) {
        if (content == null) {
            return;
        }
        try {
            String query = "insert into Interakcja(id, z_kim, czas, Osoba_id) values (default, ?, ?, ?)";
            //throw new UnsupportedOperationException("Not yet implemented");
            PreparedStatement statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : content.entrySet()) {
                statement.setString(1, friend.getValue());
                statement.setDate(2, new java.sql.Date(date.toDate().getTime()));
                statement.setInt(3, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void putItems(String table, TreeMap<String, String> items, int id) {
        if (items == null) {
            return;
        }
        try {
            String query = String.format("insert into %s(id, nazwa, link, Osoba_id) values (default, ?, ?, ?) ", table);
            //throw new UnsupportedOperationException("Not yet implemented");
            PreparedStatement statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                statement.setString(1, friend.getKey());
                statement.setString(2, friend.getValue());
                statement.setInt(3, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void putPlaceItems(String typ, TreeMap<String, String> items, int id) {
        if (items == null) {
            return;
        }
        try {
            String query = "insert into Miejsce(id, nazwa, link, typ, Osoba_id) values (default, ?, ?, ?, ?) ";
            //throw new UnsupportedOperationException("Not yet implemented");
            PreparedStatement statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                statement.setString(1, friend.getKey());
                statement.setString(2, friend.getValue());
                statement.setString(3, typ);
                statement.setInt(4, id);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public List<Entry<Integer, Integer>> getFriends(){
        LinkedList<Entry<Integer, Integer>> friends = new LinkedList<Entry<Integer, Integer>>();
        try {
            String query = "select kto.id, komu.id "
                    + "from Osoba as kto "
                    + "inner join Przyjaciel "
                    + "on kto.id = Przyjaciel.Osoba_id "
                    + "inner join Osoba as komu "
                    + "on Przyjaciel.link = komu.link;";
            PreparedStatement statement = connect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer from = resultSet.getInt(1);
                Integer to = resultSet.getInt(2);
                friends.add(new AbstractMap.SimpleEntry<Integer, Integer>(from, to));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        return friends;
    }
}
