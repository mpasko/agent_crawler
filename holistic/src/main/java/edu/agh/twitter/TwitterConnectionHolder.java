/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.twitter;

import org.joda.time.DateTime;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author marcin
 */
public class TwitterConnectionHolder {
    public Twitter twitter;
    private DateTime lastLogin;

    public void reloginIfNeeded() {
        if (DateTime.now().getMinuteOfDay() - lastLogin.getMinuteOfDay() > 30) {
            relogin();
        }
    }

    public void relogin() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(SecretProvider.CONSUMER_KEY)
                .setOAuthConsumerSecret(SecretProvider.CONSUMER_KEY_SECRET)
                .setOAuthAccessToken(SecretProvider.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(SecretProvider.ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        lastLogin = DateTime.now();
        System.out.println("Connected to Twitter: " + lastLogin);
    }

    public Twitter getTwitter() {
        return twitter;
    }
    
}
