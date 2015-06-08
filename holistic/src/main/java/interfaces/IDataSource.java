package interfaces;


import java.util.List;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marcin
 */
public interface IDataSource {
    public Map<Integer, String> getListOfPeople();
    public List<Map.Entry<Integer, Integer>> getListOfLinks();
    public List<Integer> getAllLinksFrom(Integer from);
    public List<Interaction> getInteractionTimeline();
    public List<PersonGroup> determineGroupsByAspect(Integer from, String aspect);
    public List<PersonGroup> getAllGroupsByAspect(String aspect);
    public List<String> getSupportedAspects();
}
