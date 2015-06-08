/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.joda.time.DateTime;
import org.openide.util.Exceptions;

/**
 *
 * @author marcin
 */
public class FacebookConnector extends MyConnector{
    public FacebookConnector() {
        super("mydb");
    }

    public void putLikes(int id, TreeMap<String, String> likes) {
        putItems("CoLubi", likes, id);
    }

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
            statement = connect.prepareStatement(query);
            for (Entry<String, String> friend : content.entrySet()) {
                statement.setString(1, friend.getValue());
                statement.setDate(2, new Date(date.toDate().getTime()));
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

    public void putPlaceItems(String typ, TreeMap<String, String> items, int id) {
        if (items == null) {
            return;
        }
        PreparedStatement statement = null;
        try {
            String query = "insert into Miejsce(id, nazwa, link, typ, Osoba_id) values (default, ?, ?, ?, ?) ";
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
}
