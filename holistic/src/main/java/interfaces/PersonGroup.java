/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author marcin
 */
public class PersonGroup {
    private String type;
    private String link;
    private List<Integer> members;

    public PersonGroup(String type, String link, List<Integer> members) {
        this.type = type;
        this.link = link;
        this.members = members;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the members
     */
    public List<Integer> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<Integer> members) {
        this.members = members;
    }
    
}
