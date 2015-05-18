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
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterResponse;
import twitter4j.User;

/**
 *
 * @author marcin
 */
public abstract class PageableFetcher <T extends TwitterResponse>{
    private static final int MAX_RETRIES = 5;
    private static boolean retry;
    private static int consecutiveErrors = 0;
    
    public LinkedList<T> fetch(Twitter twitter, String userScreen) {
        LinkedList<T> friendIds = new LinkedList<T>();
        long cursor=-1;
        PagableResponseList<T> friends = null;
        do{
            do{
                retry = false;
                try {
                    friends = twitterApiMethod(twitter, userScreen, cursor);
                } catch (TwitterException ex) {
                    retry = true;
                    handleTwitterException(ex);
                }
            } while (retry);
            friendIds.addAll(friends);
            cursor = friends.getNextCursor();
        }while(friends.hasNext());
        return friendIds;
    }
    
    public static void handleTwitterException(TwitterException e) {
        if (e.exceededRateLimitation()) {
            int secondsToSleep = e.getRateLimitStatus().getSecondsUntilReset() + 1; // 1s slack
            int millisToSleep = 1000 * secondsToSleep;
            System.out.println("[" + new Date() + "] Sleeping for " + secondsToSleep + " seconds");
            long before = System.currentTimeMillis();
            try {
                if (millisToSleep>0)
                    Thread.sleep(millisToSleep);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            long now = System.currentTimeMillis();
            System.out.println("[" + new Date() + "] Woke up! Slept for " + (now - before) / 1000 + " seconds");
        } else {
            Exceptions.printStackTrace(e);
            
            if (consecutiveErrors++ > MAX_RETRIES) {
                retry = false; // already tried enough
                throw new RuntimeException(e);
            }
        }
    }

    public abstract PagableResponseList<T> twitterApiMethod(Twitter twitter, String userScreen, long cursor) throws TwitterException;
}
