package interfaces;


import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marcin
 */
public class Interaction {
    private Date date_time;
    private Integer from;
    private Integer to;

    
    public Interaction(){}
    
    public Interaction(Date time, Integer from, Integer to) {
        this.date_time = time;
        this.from = from;
        this.to = to;
    }
    /**
     * @return the date_time
     */
    public Date getDateTime() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDateTime(Date date_time) {
        this.date_time = date_time;
    }

    /**
     * @return the from
     */
    public Integer getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Integer getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Integer to) {
        this.to = to;
    }
}
