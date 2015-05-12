/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.manualutils;

import edu.agh.facebook.DateParser;
import edu.agh.facebook.FacebookCrawlerMain;
import java.io.IOException;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author marcin
 */
public class DateFatcher {
    public static void main(String args[]) throws IOException {
        //FacebookCrawlerMain.silentLogging();
        //FacebookCrawlerMain crawler = new FacebookCrawlerMain();
        //crawler.logOn();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM").withLocale(Locale.UK);
        String datestring = "11 April";
        DateTime datetime = new DateParser().parseDate(datestring);
        System.out.println(datetime.toString());
    }
}
