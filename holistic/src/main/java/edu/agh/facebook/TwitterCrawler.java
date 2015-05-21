/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.facebook;

import edu.agh.database.TwitterConnector;
import edu.agh.twitter.PageableFetcher;
import edu.agh.twitter.SecretProvider;
import edu.agh.twitter.SimpleFetcher;
import edu.agh.twitter.TwitterConnectionHolder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import org.joda.time.DateTime;
import org.openide.util.Exceptions;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author marcin
 */
public class TwitterCrawler {

    public static void main(String args[]) {
        try {
            TwitterCrawler crawler = new TwitterCrawler();
            crawler.doJob();
        } catch (TwitterException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    private PageableFetcher<User> friendsFetcher;
    private PageableFetcher<User> followersFetcher;
    private PageableFetcher<UserList> membershipFetcher;
    private PageableFetcher<UserList> subscriptionFetcher;
    private SimpleFetcher<Status> timelineFetcher;
    private TwitterConnector connect;
    private final TwitterConnectionHolder holder;

    public TwitterCrawler() {
        holder = new TwitterConnectionHolder();
        holder.relogin();
        friendsFetcher = new FriendsFetcher();
        followersFetcher = new FollowersFetcher();
        membershipFetcher = new MembershipFetcher();
        subscriptionFetcher = new SubscriptionFetcher();
        timelineFetcher = new TimelineFetcher();

        connect = new TwitterConnector();
        System.out.println("Connected to Database");
    }

    public void doJob() throws TwitterException {
        System.out.println("Starting crawling: " + DateTime.now());
        userId = connect.getMaxOsobaId() + 1;

        System.out.println("Starting with:" + userId);
        if (userId == 1) {
            System.out.println("No users found -initializing");
            String[] startScreens = new String[]{"KarolinaOchmanx", "DariaBladzinska"};

            for (int i = 0; i < startScreens.length; ++i) {
                User startUser = PageableFetcher.wrapShowUser(holder, startScreens[i]);
                examineUser(holder, startUser);
            }
        }

        do {
            examineNextPortionOfFriends();
        } while (true);
    }

    public void printUser(User friend) {
        StringBuilder build = new StringBuilder("Examining: ");
        build.append(friend.getScreenName());
        build.append("\n");
        build.append("Total friends: ");
        build.append(friend.getFriendsCount());
        build.append("\n");
        build.append(friend.getLocation());
        build.append("\n");
        Status status = friend.getStatus();
        //examineStatus(build, status);
        System.out.println(build.toString());
    }

    static int userId = 0;

    private void examineUser(TwitterConnectionHolder twitter, User user) {
        System.out.println("Examining: " + user.getName());
        connect.putTwitterUser(userId, user.getScreenName(), user.getName(), user.getLocation());
        LinkedList<User> friendIds = friendsFetcher.fetch(twitter, user.getScreenName());
        System.out.println("FRIENDS:");
        TreeMap<String, String> friends = new TreeMap<String, String>();
        for (User friend : friendIds) {
            friends.put(friend.getName(), friend.getScreenName());
        }
        connect.putTwitterFriendItems(friends, userId, "friend");
        System.out.println("FOLLOWERS:");
        friends = new TreeMap<String, String>();
        LinkedList<User> followersIds = followersFetcher.fetch(twitter, user.getScreenName());
        for (User follower : followersIds) {
            friends.put(follower.getName(), follower.getScreenName());
        }
        connect.putTwitterFriendItems(friends, userId, "follower");
        System.out.println("MEMBERSHIPS:");
        LinkedList<String> discussionList = new LinkedList<String>();
        LinkedList<UserList> memberships = membershipFetcher.fetch(twitter, user.getScreenName());
        for (UserList membership : memberships) {
            discussionList.add(membership.getFullName());
        }
        connect.putTwitterListItems(discussionList, userId, "member");
        System.out.println("SUBSCRIPTIONS:");
        discussionList = new LinkedList<String>();
        LinkedList<UserList> subscriptions = subscriptionFetcher.fetch(twitter, user.getScreenName());
        for (UserList membership : subscriptions) {
            discussionList.add(membership.getFullName());
        }
        connect.putTwitterListItems(discussionList, userId, "subscriber");
        LinkedList<Status> timeline = timelineFetcher.fetch(twitter, user.getScreenName());
        System.out.println("TIMELINE");
        for (Status status : timeline) {
            TreeMap<String, String> tweets = examineStatus(status);
            if (tweets != null) {
                connect.putTweetItems(tweets, userId, status.getCreatedAt());
            }
        }
        ++userId;
    }

    private TreeMap<String, String> examineStatus(Status status) {
        if (status == null) {
            return null;
        }
        TreeMap<String, String> tweets = new TreeMap<String, String>();
        if (status.isRetweeted()) {
            tweets.put(status.getRetweetedStatus().getUser().getScreenName(), "retweet");
        }
        SymbolEntity[] symbolEntities = status.getSymbolEntities();
        for (SymbolEntity entity : symbolEntities) {
            tweets.put(entity.getText(), "symbol");
        }
        URLEntity[] urlEntities = status.getURLEntities();
        for (URLEntity entity : urlEntities) {
            tweets.put(entity.getURL(), "url");
        }
        UserMentionEntity[] userMention = status.getUserMentionEntities();
        for (UserMentionEntity entity : userMention) {
            tweets.put(entity.getScreenName(), "user");
        }
        return tweets;
    }

    private PlaceStatus verifyLocation(String location) {
        if (location != null && !location.isEmpty()) {
            GeoQuery geo = new GeoQuery((String) null);
            geo.setQuery(location);
            GeoLocation loc = geo.getLocation();
            if (loc == null) {
                return PlaceStatus.INVALID;
            }
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            boolean inPoland = (latitude > 48) && (latitude < 55.2) && (longitude > 13.3) && (longitude < 24.5);
            if (inPoland) {
                return PlaceStatus.POLAND;
            } else {
                return PlaceStatus.OUTSIDE;
            }
        } else {
            return PlaceStatus.INVALID;
        }
    }

    private void examineNextPortionOfFriends() throws TwitterException {
        List<String> nextUsers = connect.getUsersToExamine();
        for (String screen : nextUsers) {
            holder.reloginIfNeeded();
            User nextUser;
            try {
                nextUser = PageableFetcher.wrapShowUser(holder, screen);
            } catch (RuntimeException ex) {
                connect.putTwitterMalformedUser(userId, screen);
                throw ex;
            }
            int followersCount = nextUser.getFollowersCount();
            int friendsCount = nextUser.getFriendsCount();
            boolean isCelebrity = (followersCount > 300) || (friendsCount > 500);
            if (!isCelebrity) {
                try {
                    examineUser(holder, nextUser);
                } catch (RuntimeException ex) {
                    connect.updateTwitterMalformedUser(userId, screen);
                    throw ex;
                }
            }
        }
    }

    private static enum PlaceStatus {

        POLAND, OUTSIDE, INVALID;
    }

    private static class FriendsFetcher extends PageableFetcher<User> {

        @Override
        public PagableResponseList<User> twitterApiMethod(Twitter twitter, String userScreen, long cursor) throws TwitterException {
            return twitter.getFriendsList(userScreen, cursor);
        }
    }

    private static class FollowersFetcher extends PageableFetcher<User> {

        public FollowersFetcher() {
        }

        @Override
        public PagableResponseList<User> twitterApiMethod(Twitter twitter, String userScreen, long cursor) throws TwitterException {
            return twitter.getFollowersList(userScreen, cursor);
        }
    }

    private static class MembershipFetcher extends PageableFetcher<UserList> {

        public MembershipFetcher() {
        }

        @Override
        public PagableResponseList<UserList> twitterApiMethod(Twitter twitter, String userScreen, long cursor) throws TwitterException {
            return twitter.getUserListMemberships(userScreen, cursor);
        }
    }

    private static class SubscriptionFetcher extends PageableFetcher<UserList> {

        public SubscriptionFetcher() {
        }

        @Override
        public PagableResponseList<UserList> twitterApiMethod(Twitter twitter, String userScreen, long cursor) throws TwitterException {
            return twitter.getUserListSubscriptions(userScreen, cursor);
        }
    }

    private static class TimelineFetcher extends SimpleFetcher<Status> {

        public TimelineFetcher() {
        }

        @Override
        public ResponseList<Status> twitterApiMethod(Twitter twitter, String userScreen) throws TwitterException {
            return twitter.getUserTimeline(userScreen);
        }
    }
}
