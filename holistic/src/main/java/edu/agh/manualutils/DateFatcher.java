/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.manualutils;

import edu.agh.facebook.FacebookCrawlerMain;
import java.io.IOException;

/**
 *
 * @author marcin
 */
public class DateFatcher {
    public static void main(String args[]) throws IOException {
        FacebookCrawlerMain.silentLogging();
        FacebookCrawlerMain crawler = new FacebookCrawlerMain();
        crawler.logOn();
        
        
    }
}
