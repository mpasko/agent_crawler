/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.facebook;

import edu.agh.database.FacebookConnector;
import edu.agh.database.MyConnector;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openide.util.Exceptions;

/**
 *
 * @author marcin
 */
public class FacebookCrawlerMain {

    DefaultHttpClient httpclient;
    HttpGet httpget;
    HttpResponse response;
    HttpEntity entity;
    List<Cookie> cookies;
    HttpPost httpost;
    List<NameValuePair> nvps;
    //Queue<String> namesToVerify;
    //Set<String> alredyVisited;
    private final FacebookConnector connector;
    private final Random random;
    private DateParser dateParser;
    
    public FacebookCrawlerMain() {
        httpclient = new DefaultHttpClient();
        //namesToVerify = new LinkedList<String>();
        //alredyVisited = new HashSet<String>();
        connector = new FacebookConnector();
        random = new Random();
        dateParser = new DateParser();
    }

    public static void main(String[] args) throws Exception {
        silentLogging();
        new FacebookCrawlerMain().startCrawling();
    }

    public static String trim(String stringToTrim) {
        String answer = stringToTrim.replace(',', ' ');

        System.out.println(answer);
        return answer;
    }

    public void logOn() throws ParseException, UnsupportedEncodingException, IOException {
        httpget = new HttpGet("http://www.facebook.com/login.php");
        response = httpclient.execute(httpget);
        entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        System.out.println("Initial set of cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        httpost = new HttpPost("http://www.facebook.com/login.php");
        nvps = new ArrayList<NameValuePair>();
		/*
        nvps.add(new BasicNameValuePair("email", "krismiechowski333@gmail.com"));
        nvps.add(new BasicNameValuePair("pass", "kmiechowski333"));
		*/
		nvps.add(new BasicNameValuePair("email", "a2686248@trbvm.com"));
        nvps.add(new BasicNameValuePair("pass", "cheos2"));
		
        entity.getContent().close();
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
        response = httpclient.execute(httpost);
        entity = response.getEntity();
        System.out.println("Double check we've got right page " + EntityUtils.toString(entity));

        System.out.println("Login form get: " + response.getStatusLine());

        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        entity.getContent().close();
    }

    public void logOut() {
        try {
            Document doc = fetchDocument("http://m.facebook.com");
            Element logoutLink = allElementsByTagText(doc, "a", "Log Out").get(0);
            String url = String.format("https://m.facebook.com%s", logoutLink.attr("href"));
            System.out.println(url);
            httpclient.execute(new HttpGet(url));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
        
    public Document fetchDocument(final String url) throws IllegalStateException, IOException, ParseException, InterruptedException {
        /* */
        //To perform crawling culture ;)
        Thread.sleep(1000+random.nextInt(1000));
        HttpGet nw = new HttpGet(url);
        //System.out.println(String.format("Connecting with: %s", url));
        response = httpclient.execute(nw);
        entity = response.getEntity();
        final String entityString = EntityUtils.toString(entity);
        Document doc = Jsoup.parse(entityString);
        entity.getContent().close();
        return doc;
    }

    public void startCrawling() throws IOException, ParseException, IllegalStateException, InterruptedException {
        try {
            
            logOn();
            /* * /
             String idarray[] = new String[]{"100006273318136"};
             /* */
            
            /* */
			this.id = connector.getMaxOsobaId()+1;
            int usersToInvigilate = 10000;
            while (usersToInvigilate>0){
                List<String> idArray = connector.getUsersToExamine();
                for (String userName:idArray) {
                    try {
                        analyzeUser(userName);
                    } catch (Throwable th) {
                        Exceptions.printStackTrace(th);
                    }
                }
                usersToInvigilate-=idArray.size();
            }
        } finally {
            closeAll();
        }
    }
    
    private static int id = 0;

    public void analyzeUser(final String userName) throws IOException, InterruptedException, ParseException, IllegalStateException {
        connector.putUser(userName, id);
        System.out.println(String.format("\n------------------\nAnalyzing now: %s", userName));
         /* */
        final String url = String.format("https://m.facebook.com/%sv=info", userName);
        /* */
        TreeMap<String, String> friends = analyzeFriends(userName);
        connector.putFriends(id, friends);
        for (Entry<String, String> entry : friends.entrySet()) {
            String userid = entry.getValue();
            //if (!alredyVisited.contains(userid)&&!namesToVerify.contains(userid)) {
            //    namesToVerify.add(userid);
            //}
        }
        
        TreeMap<String, String> likes = analyzeLikes(userName);
        connector.putLikes(id, likes);
        
        TreeMap<String, String> timeline = analyzeTimeline(userName);
        //printMap(timeline, "timeline item");
        
        for (Entry<String, String> entry : timeline.entrySet()) {
            String timelineUrl = entry.getValue();
            TreeMap<String, String> content = analyzeTimelineContent(timelineUrl);
            DateTime parsedDate = dateParser.parseDate(entry.getKey());
            //System.out.println("date: "+parsedDate.toString());
            connector.putTimeline(id, parsedDate, content);
        }
        
        Document doc = fetchDocument(url);
        TreeMap<String, String> living = analyzeTopic(doc, "living");
        connector.putLiving(id, living);
        TreeMap<String, String> work = analyzeTopic(doc, "work");
        connector.putWork(id, work);
        TreeMap<String, String> edu = analyzeTopic(doc, "education");
        connector.putEdu(id, edu);
        
        //alredyVisited.add(userName);
        ++id;
    }

    public static TreeMap<String, String> analyzeTopic(Document doc, final String topic) {
        TreeMap<String, String> map = new TreeMap<String, String>();
        final Element topicTag = doc.getElementById(topic);
        if (topicTag == null) {
            return map;
        }
        Elements living = topicTag.getElementsByTag("a");
        for (Element link : living) {
            //System.out.print("\n\nNEXT LINK: ");
            //System.out.println(link.toString());
            String linkurl = link.attr("href");
            String name = link.text();
            String itemid = extractUserId(linkurl);
            //System.out.println(result);
            if (!hasPicture(link)&&!isCommonButton(link)) {
                map.put(name, itemid);
            }
        }
        return map;
    }

    public TreeMap<String, String> analyzeFriends(final String userName) throws IllegalStateException, ParseException, IOException, InterruptedException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        int startIndex = 0;
        int totall = Integer.MAX_VALUE;
        Element moreFriends;
        String friendsurl = String.format("https://m.facebook.com/%sv=friends&mutual&startindex=%d", userName, startIndex);
        do {
            Document friendsdoc = fetchDocument(friendsurl);
            Elements tbodies = friendsdoc.getElementsByTag("tbody");
            for (Element tbody : tbodies) {
                final Elements links = tbody.getElementsByTag("a");
                for (Element link : links) {
                    if (isValidLink(link)) {
                        String name = link.text();
                        String linkurl = link.attr("href");
                        String userid = extractUserId(linkurl);
                        map.put(name, userid);
                    }
                }
                //if (!links.isEmpty()) {
                //}
            }
            moreFriends = friendsdoc.getElementById("m_more_friends");
            if (moreFriends != null) {
                friendsurl = "https://m.facebook.com" + moreFriends.getElementsByTag("a").get(0).attributes().get("href");
            }
        } while (moreFriends != null);
        return map;
    }

    public TreeMap<String, String> analyzeLikes(String userName) throws InterruptedException, IOException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        int startIndex = 0;
        int totall = Integer.MAX_VALUE;
        Element moreLikes;
        String likesurl = String.format("https://m.facebook.com/%sv=likes&sectionid=9999&startindex=%d", userName, startIndex);
        do {
            Document friendsdoc = fetchDocument(likesurl);
            Elements links = friendsdoc.getElementsByTag("a");
            for (Element link : links) {
                if (!hasPicture(link)&&!isCommonButton(link)&&hasSpan(link)) {
                    String linkurl = link.attr("href");
                    String name = link.text();
                    String itemid = extractUserId(linkurl);
                    map.put(name, itemid);
                }
            }
            moreLikes = friendsdoc.getElementById("m_more_item");
            if (moreLikes != null) {
                likesurl = "https://m.facebook.com" + moreLikes.getElementsByTag("a").get(0).attributes().get("href");
            }
        } while (moreLikes != null);
        return map;
    }
    
    private TreeMap<String, String> analyzeTimeline(String userName) throws InterruptedException, IOException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        int startIndex = 0;
        int totall = Integer.MAX_VALUE;
        Element moreItems;
        String timelineUrl = String.format("https://m.facebook.com/%sv=timeline", userName);
        do {
            Document doc = fetchDocument(timelineUrl);
            //System.out.println("Diagnostics:");
            //System.out.println(doc.toString());
            List<Element> storyLinks = allElementsByTagText(doc, "a", "Full Story");
            
            //System.out.println(String.format("Number of links: %d", storyLinks.size()));
            for (Element link : storyLinks) {
                Element timelineItem = link.parent().parent();
                Elements abbrList = timelineItem.getElementsByTag("abbr");
                //System.out.println(String.format("Number of abbrs: %d", abbrList.size()));
                if (!abbrList.isEmpty()) {
                    Element abbr = abbrList.get(0);
                    map.put(abbr.text(), link.attr("href"));
                } else {
                    //System.out.println("Diagnostics:");
                    //System.out.println(timelineItem.toString());
                }
            }
            /*
            if (storyLinks.isEmpty()) {
                    System.out.println("Diagnostics:");
                    System.out.println(doc.toString());
            }
            */
            
            moreItems = getShowMoreTimelineLink(doc);
            if (moreItems != null) {
                timelineUrl = "https://m.facebook.com" + moreItems.getElementsByTag("a").get(0).attributes().get("href");
            }
        } while (moreItems != null);
        return map;
    }
    
    public TreeMap<String, String> analyzeTimelineContent(String url) throws InterruptedException, IOException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        String fullurl = String.format("https://m.facebook.com%s", url);
        Document doc = fetchDocument(fullurl);
        Entry<String, String> place = getPlaceFromTimeline(doc);
        if (place!=null) {
            map.put(place.getKey(), place.getValue());
        }
        TreeMap<String, String> comments = getCommentsFromTimeline(doc);
        map.putAll(comments);
        TreeMap<String, String> likes = getStoryLikes(doc);
        map.putAll(likes);
        return map;
    }
    
    public Entry<String, String> getPlaceFromTimeline(Document doc) throws InterruptedException, IOException {
        Element voiceReplaceId = doc.getElementById("voice_replace_id");
        if (voiceReplaceId==null) {
            return null;
        }
        Elements spans = voiceReplaceId.getElementsByTag("span");
        List<Element> links = allElementsByTag(spans, "a");
        if (!links.isEmpty()) {
            Element place = links.get(0);
            //System.out.println(String.format("place: %s link: %s", place.text(), place.attr("href")));
            Entry<String, String> placeEntry = new AbstractMap.SimpleEntry<String, String>(place.text(), place.attr("href"));
            return placeEntry;
        }
        return null;
    }
    
    public TreeMap<String, String> getCommentsFromTimeline(Document doc) throws InterruptedException, IOException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        Elements headers = doc.getElementsByTag("h3");
        List<Element> links = allElementsByTag(headers, "a");
        for (Element item: links) {
            if (!isCommonButton(item)) {
                String name = item.text();
                String url = item.attr("href");
                map.put(name, url);
            }
        }
        return map;
    }
        
    public static boolean hasPicture(Element link) {
        return link.getElementsByTag("img").size() > 0;
    }

    private static boolean hasSpan(Element link) {
        return link.getElementsByTag("span").size() > 0;
    }

    public static void silentLogging() {
        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
    }

    public static boolean isCommonButton(Element link) {
        String name = link.text();
        LinkedList<String> keywords = new LinkedList<String>();
        keywords.add("See More");
        keywords.add("Help");
        keywords.add("Follow");
        keywords.add("Settings & Privacy");
        keywords.add("Report a Problem");
        keywords.add("Terms & Policies");
        keywords.add("Log Out");
        keywords.add("Add Friend");
        keywords.add("Message");
        keywords.add("Find Friends");
        keywords.add("Edit");
        keywords.add("Add ");
        for (String key : keywords) {
            if (name.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidLink(Element link) {
        return !hasPicture(link) && !hasSpan(link) && !isCommonButton(link);
    }

    private static String extractUserId(final String linkurl) {
        LinkedList<String> keywords = new LinkedList<String>();
        keywords.add("\\/");
        keywords.add("fref=fr_tab");
        keywords.add("fref=pb");
        keywords.add("fref=none");
        keywords.add("refid=17");
        keywords.add("fref=*");
        keywords.add("refid=*");
        //keywords.add("profile\\.php");
        String result = linkurl;
        for (String key : keywords) {
            result = result.replaceAll(key, "");
        }
        return result;
    }

    private Element getShowMoreTimelineLink(Document doc) {
        String parentId = "structured_composer_async_container";
        String tag = "a";
        String text = "Show more";
        return findElementByIdTagText(doc, parentId, tag, text);
    }

    public Element findElementByIdTagText(Document doc, String parentId, String tag, String text) {
        Element moreItems = null;
        Element composer = doc.getElementById(parentId);
        if (composer==null) {
            System.out.println("[WARNING] null "+parentId+" !!!");
            return null;
        }
        Elements compLinks = composer.getElementsByTag(tag);
        for (Element link : compLinks) {
            if (link.text().equalsIgnoreCase(text)){
                moreItems = link;
            }
        }
        return moreItems;
    }
    
    public List<Element> allElementsByClassTagText(Document doc, String parentId, String tag, String text) {
        LinkedList<Element> moreItems = new LinkedList<Element>();
        Elements composer = doc.getElementsByClass(parentId);
        for (Element element: composer) {
            List<Element> found = allElementsByTagText(element, tag, text);
            moreItems.addAll(found);
        }
        return moreItems;
    }

    public List<Element> allElementsByTagText(Element element, String tag, String text) {
        LinkedList<Element> moreItems = new LinkedList<Element>();
        Elements compLinks = element.getElementsByTag(tag);
        for (Element link : compLinks) {
            if (link.text().equalsIgnoreCase(text) || link.text().contains(text)){
                moreItems.add(link);
            }
        }
        return moreItems;
    }
    
    public List<Element> allElementsByTag(Elements compLinks, String tag) {
        LinkedList<Element> moreItems = new LinkedList<Element>();
        for (Element link : compLinks) {
            Elements tags = link.getElementsByTag(tag);
            moreItems.addAll(tags);
        }
        return moreItems;
    }

    private TreeMap<String, String> getStoryLikes(Document doc) throws IllegalStateException, IOException, ParseException, InterruptedException {
        TreeMap<String, String> likes = new TreeMap<String, String>();
        Elements spans = doc.getElementsContainingOwnText("like this");
        List<Element> links = allElementsByTag(spans, "a");
        if (!links.isEmpty()) {
            String likesBtn = links.get(0).attr("href");
            Map<String, String> result = analyzeStoryLikesPage(likesBtn);
            likes.putAll(result);
        }
        return likes;
    }

    private Map<String, String> analyzeStoryLikesPage(String shortUrl) throws IllegalStateException, IOException, ParseException, InterruptedException {
        TreeMap<String, String> likes = new TreeMap<String, String>();
        String fullUrl = String.format("https://m.facebook.com%s", shortUrl);
        Document doc = fetchDocument(fullUrl);
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkurl = link.attr("href");
            String name = link.text();
            if ((!isCommonButton(link)) && !name.isEmpty()) {
                //System.out.println(String.format("Next like: name:%s id:%s", name, linkurl));
                likes.put(name, linkurl);
            }
        }
        Elements nextBtns = doc.getElementsContainingOwnText("See More");
        if (!nextBtns.isEmpty()) {
            Element link = nextBtns.get(0).parent();
            //System.out.println(link.toString());
            String nextUrl = link.attr("href");
            //System.out.println("Going to:"+nextUrl);
            Map<String, String> nextPortion = analyzeStoryLikesPage(nextUrl);
            likes.putAll(nextPortion);
        }
        return likes;
    }

    public void closeAll() {
        logOut();
        httpclient.getConnectionManager().shutdown();
        connector.close();
    }
}
