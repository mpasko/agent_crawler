/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author marcin
 */
public class SampleGenerator {

    public List<Map.Entry<Integer, Integer>> generate(int size) {
        List<Map.Entry<Integer, Integer>> web = new LinkedList<Map.Entry<Integer, Integer>>();
        Map<Integer, Integer> counted = new HashMap<Integer, Integer>();
        Random random = new Random();
        int global = 0;
        makeNode(web, counted, 0, 1);
        ++global;
        for (int i = 2; i < size; ++i) {
            int webSize = web.size();
            int capacityPerNode = (webSize - 1);
            int capacity = webSize * capacityPerNode / 2;
            double filled = global / (double) capacity;
            for (int j = 0; j < size - 1; ++j) {
                Integer cnt = getValue(counted, j);
                double localFilled = cnt / (double) capacityPerNode;
                int randInt = random.nextInt(100+(int)(100*filled));
                if (randInt < 50+50*localFilled) {
                    makeNode(web, counted, j, i);
                    ++global;
                }
            }
        }
        return web;
    }

    private void increment(Map<Integer, Integer> counted, int index) {
        Integer current = getValue(counted, index);
        counted.put(index, current + 1);
    }

    private void makeNode(List<Entry<Integer, Integer>> web, Map<Integer, Integer> counted, int from, int to) {
        web.add(new AbstractMap.SimpleEntry<Integer, Integer>(from, to));
        web.add(new AbstractMap.SimpleEntry<Integer, Integer>(to, from));
        increment(counted, from);
        increment(counted, to);
    }

    private Integer getValue(Map<Integer, Integer> counted, int index) {
        Integer current = counted.get(index);
        if (current == null) {
            current = 0;
        }
        return current;
    }
}
