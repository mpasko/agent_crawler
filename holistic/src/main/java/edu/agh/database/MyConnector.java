/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.database;

import edu.agh.manualutils.PrinterUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
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
    protected Connection connect = null;

    public MyConnector(String dbname) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = String.format("jdbc:mysql://localhost/%s?user=crawler&password=crawler", dbname);
            connect = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void putUser(String link, int id) {
        PreparedStatement statement=null;
        try {
            statement = connect
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
        } finally {
            closeStatement(statement);
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
        PreparedStatement statement = null;
        try {
            String query = "insert into Interakcja(id, z_kim, czas, Osoba_id) values (default, ?, ?, ?)";
            //throw new UnsupportedOperationException("Not yet implemented");
            statement = connect.prepareStatement(query);
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
        } finally {
            closeStatement(statement);
        }
    }

    public void putItems(String table, TreeMap<String, String> items, int id) {
        if (items == null) {
            return;
        }
        PreparedStatement statement=null;
        try {
            final String query = String.format("insert into %s(id, nazwa, link, Osoba_id) values (default, ?, ?, ?) ", table);
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : items.entrySet()) {
                statement.setString(1, friend.getKey());
                statement.setString(2, friend.getValue());
                statement.setInt(3, id);
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
    
    public void putPlaceItems(String typ, TreeMap<String, String> items, int id) {
        if (items == null) {
            return;
        }
        PreparedStatement statement=null;
        try {
            String query = "insert into Miejsce(id, nazwa, link, typ, Osoba_id) values (default, ?, ?, ?, ?) ";
            //throw new UnsupportedOperationException("Not yet implemented");
            statement = connect.prepareStatement(query);
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
        } finally {
            closeStatement(statement);
        }
    }
    
    public List<Entry<Integer, Integer>> getFriends(){
        LinkedList<Entry<Integer, Integer>> friends = new LinkedList<Entry<Integer, Integer>>();
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            String query = "select kto.id, komu.id "
                    + "from Osoba as kto "
                    + "inner join Przyjaciel "
                    + "on kto.id = Przyjaciel.Osoba_id "
                    + "inner join Osoba as komu "
                    + "on Przyjaciel.link = komu.link;";
            statement = connect.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer from = resultSet.getInt(1);
                Integer to = resultSet.getInt(2);
                friends.add(new AbstractMap.SimpleEntry<Integer, Integer>(from, to));
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return friends;
    }
    
    
    public int getMaxOsobaId() {
        String query = "select max(id) from Osoba;";
        int result = getSingleValue(query);
        return result;
    }
    
    public int getCrawledSoFar() {
        String query = "select count(id) from Osoba;";
        int result = getSingleValue(query);
        return result;
    }

    public void close() {
        try {
            connect.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected void closeStatement(PreparedStatement statement) {
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    protected void closeResultSet(ResultSet resultSet) {
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private int getSingleValue(String query) {
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        int result=-1;
        try {
            statement = connect.prepareStatement(query);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            closeStatement(statement);
            closeResultSet(resultSet);
        }
        return result;
    }

    public List<String> getUsersToExamine() {
        LinkedList<String> friends = new LinkedList<String>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "select distinct(Przyjaciel.link) " + 
                    "from Przyjaciel " + 
                    "left outer join Osoba " + 
                    "on Przyjaciel.link = Osoba.link " + 
                    "where Osoba.link is NULL;";
            statement = connect.prepareStatement(query);
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
}
