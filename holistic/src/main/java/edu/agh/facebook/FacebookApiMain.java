/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

/**
 *
 * @author marcin
 */
public class FacebookApiMain {
    public static void main(String[] args) {
        // DefaultFacebookClient is the FacebookClient implementation
        // that ships with RestFB. You can customize it by passing in
        // custom JsonMapper and WebRequestor implementations, or simply
        // write your own FacebookClient instead for maximum control.
        String MY_ACCESS_TOKEN = "CAACEdEose0cBAFipfAl9gfjbsUbQH2tZC"
                                 + "Xs66hMAUUmWWMIkhah6s5EGGdT67FIkgb4YAyFmmi"
                                 + "VGpHTaGCKkEA5JhVA8ewthxbMWKuAnd5GxOMdfB45"
                                 + "4rIfgp2EPZBv1ZA08drQs5LYlhxv96XQYWZBoIMAg"
                                 + "TxNsBEwXYszGjoZCP34EMElQGTU0ZBvGKbOZCjsxw"
                                 + "kZAVyk7Wqccd96ZBYOYNoq1aUXdD7zYZD";
        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
        User user = facebookClient.fetchObject("me", User.class);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        for (User friend: myFriends.getData()) {
            Connection<User> friendsOfFriends = facebookClient.fetchConnection(friend.getId()+"/friends", User.class);
            for (User fof: friendsOfFriends.getData()) {
                System.out.print(fof.getId());
                System.out.print(fof.getName());
                System.out.print(fof.getLastName());
                System.out.println();
            }
        }
        
// It's also possible to create a client that can only access
// publicly-visible data - no access token required. 
// Note that many of the examples below will not work unless you supply an access token! 

        //FacebookClient publicOnlyFacebookClient = new DefaultFacebookClient();

// Get added security by using your app secret:

//        FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN, MY_APP_SECRET);
    }
}
