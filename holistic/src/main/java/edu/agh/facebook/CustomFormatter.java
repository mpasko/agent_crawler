/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.facebook;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author marcin
 */
public class CustomFormatter extends DateTimeFormatter{
    
    public CustomFormatter() {
        super(null, null);
    }
    
    @Override
    public DateTime parseDateTime(String dateTime){
        String[] parts = dateTime.split(" ");
        if (parts[1].equalsIgnoreCase("mins")) {
            DateTime.now().minusMinutes(Integer.parseInt(parts[0]));
        } else if (parts[1].equalsIgnoreCase("seconds")) {
            DateTime.now().minusSeconds(Integer.parseInt(parts[0]));
        } else if (parts[1].equalsIgnoreCase("hours")) {
            DateTime.now().minusHours(Integer.parseInt(parts[0]));
        }
        return null;
    }
}
