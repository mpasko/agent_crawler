
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
    public List<Map.Entry<Integer, Integer>> getListOfLinks();
    public List<Interaction> getInteractionTimeline();
}
