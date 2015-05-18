/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.manualutils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author marcin
 */
public class PrinterUtil {

    public static void printMap(Map<String, String> likes, String text) {
        for (Entry<String, String> entry : likes.entrySet()) {
            String like = entry.getKey();
            String link = entry.getValue();
            System.out.println(String.format("Next %s: name:%s id:%s", text, like, link));
        }
    }

    public static void printList(List<String> items, String text) {
        for (String item : items) {
            System.out.println(String.format("Next %s: %s", text, item));
        }
    }
    
}
