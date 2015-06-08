/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author marcin
 */
public class Cache implements IDataSource{
    private final IDataSource source;
    private Map<Integer, String> listofpeople;
    private List<Entry<Integer, Integer>> listoflinks;
    //private List<Integer> alllinksfrom;
    private List<Interaction> interactintimeline;
    //private List<PersonGroup> groupbyaspect;
    //private List<PersonGroup> allgroupbyaspect;
    private List<String> supportedaspect;

    public Cache(IDataSource source) {
        this.source = source;
    }
    
    @Override
    public Map<Integer, String> getListOfPeople() {
        if (listofpeople==null) {
            listofpeople = source.getListOfPeople();
        }
        return listofpeople;
    }

    @Override
    public List<Entry<Integer, Integer>> getListOfLinks() {
        if (listoflinks==null) {
            listoflinks = source.getListOfLinks();
        }
        return listoflinks;
    }

    @Override
    public List<Integer> getAllLinksFrom(Integer from) {
        /*
        if (alllinksfrom==null) {
            alllinksfrom = source.getAllLinksFrom(from);
        }
        return alllinksfrom;
        */
        return source.getAllLinksFrom(from);
    }

    @Override
    public List<Interaction> getInteractionTimeline() {
        if (interactintimeline==null) {
            interactintimeline = source.getInteractionTimeline();
        }
        return interactintimeline;
    }

    @Override
    public List<PersonGroup> determineGroupsByAspect(Integer from, String aspect) {
        /*
        if (groupbyaspect==null) {
            groupbyaspect = source.determineGroupsByAspect(from, aspect);
        }
        return groupbyaspect;
         
        */
        return source.determineGroupsByAspect(from, aspect);
    }

    @Override
    public List<PersonGroup> getAllGroupsByAspect(String aspect) {
        /*
        if (allgroupbyaspect==null) {
            allgroupbyaspect = source.getAllGroupsByAspect(aspect);
        }
        return allgroupbyaspect;
        
        */
        return source.getAllGroupsByAspect(aspect);
    }

    @Override
    public List<String> getSupportedAspects() {
        if (supportedaspect==null) {
            supportedaspect = source.getSupportedAspects();
        }
        return supportedaspect;
    }
    
}
