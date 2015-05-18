/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.twitter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Exceptions;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterResponse;
import twitter4j.User;

/**
 *
 * @author marcin
 */
public abstract class SimpleFetcher <T extends TwitterResponse>{
    private static final int MAX_RETRIES = 5;
    private static boolean retry;
    private static int consecutiveErrors = 0;
    
    public LinkedList<T> fetch(Twitter twitter, String userScreen) {
        LinkedList<T> friendIds = new LinkedList<T>();
        ResponseList<T> friends = null;
        do{
            retry = false;
            try {
                friends = twitterApiMethod(twitter, userScreen);
            } catch (TwitterException ex) {
                retry = true;
                PageableFetcher.handleTwitterException(ex);
            }
        } while (retry);
        friendIds.addAll(friends);
        return friendIds;
    }

    public abstract ResponseList<T> twitterApiMethod(Twitter twitter, String userScreen) throws TwitterException;
}
