import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/* Tests for Social User:
 * As a social user
 * I want access to sharing features
 * So that I can share music with my friends
 */

public class SocialTest {
	// set up web driver
    static WebDriver driver = new FirefoxDriver();
    
    // helper method - login to Spotify
    public static void login(int user) {
        // navigate to home page
        driver.get("https://play.spotify.com");
        
        // click on "already have account?" link
        driver.findElement(By.id("has-account")).click();
        
        // enter username
        driver.findElement(By.id("login-usr")).clear();
        if (user == 0) {
        	driver.findElement(By.id("login-usr")).sendKeys("spotitest3");
        } else if (user == 1) {
        	driver.findElement(By.id("login-usr")).sendKeys("softwaretester1699");
        } else {
        	System.out.println("invalid user specified");
        	System.exit(1);
        }
        // enter password
        if (user == 0) {
        	driver.findElement(By.id("login-pass")).sendKeys("temporaryPass58()");
        } else if (user == 1) {
        	driver.findElement(By.id("login-pass")).sendKeys("temporaryPass58()");
        } else {
        	System.out.println("invalid user specified");
        	System.exit(1);
        }
        
        // submit form
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
    }
    
    // helper method - log out of Spotify
    public static void logout() throws InterruptedException {
        // navigate to settings page
        driver.get("https://play.spotify.com/settings");
        Thread.sleep(4000);
        
        // click on logout button
        driver.findElement(By.id("logout-settings")).click();
    }
    
    /*
     * Given that I am in a playlist
     * When I right-click and share a track
     * Then I am given the option to share it with users of my choosing
     */
    @Test
    public void userShareTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// navigate to track to test ("It Catches Up With You")
    	driver.get("https://play.spotify.com/track/2cryrajucGmbV6mwNUhQPB");
    	Thread.sleep(10000);
    	
    	// switch to player iframe
        driver.switchTo().frame("app-player");
        Thread.sleep(2000);
        
        // click on ellipsis button to open options for song
        driver.findElement(By.id("widget-more")).click();
        Thread.sleep(2000);
    	
    	// switch to context actions frame
        driver.switchTo().defaultContent(); // first go back to main frame
        driver.switchTo().frame("context-actions");
        Thread.sleep(2000);
        
        // click on "Share..." menu item
        driver.findElement(By.cssSelector("#share")).click();
        Thread.sleep(4000);
        
        // assert that "Send to..." option appears in share dialog
        // switch to share frame
        driver.switchTo().defaultContent(); // first go back to main frame
        WebElement shareFrame = driver.findElement(By.xpath("//iframe[@id='share']"));
        driver.switchTo().frame(shareFrame);
        // find "Send to..." link
        WebElement sendToLink = driver.findElement(By.xpath("//a[@data-string=\"sendTab\"]"));
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertTrue(sendToLink != null);
    }
    
    /*
     * Given that I am on an album page
     * When I right-click and share an album
     * Then I am given the option to share it with users of my choosing
     */
    @Test
    public void albumShareTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// navigate to album to test ("If You're Reading This It's Too Late")
    	driver.get("https://play.spotify.com/album/0ptlfJfwGTy0Yvrk14JK1I");
    	Thread.sleep(10000);
    	
    	// switch to album iframe
    	WebElement albumFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify:app:album')]"));
        driver.switchTo().frame(albumFrame);
        Thread.sleep(2000);
        
        // click on ellipsis button to open options for song
        driver.findElement(By.cssSelector(".spoticon-more-16")).click();
        Thread.sleep(2000);
    	
    	// switch to context actions frame
        driver.switchTo().defaultContent(); // first go back to main frame
        driver.switchTo().frame("context-actions");
        Thread.sleep(2000);
        
        // click on "Share..." menu item
        driver.findElement(By.cssSelector("#share")).click();
        Thread.sleep(4000);
        
        // assert that "Send to..." option appears in share dialog
        // switch to share frame
        driver.switchTo().defaultContent(); // first go back to main frame
        WebElement shareFrame = driver.findElement(By.xpath("//iframe[@id='share']"));
        driver.switchTo().frame(shareFrame);
        // find "Send to..." link
        WebElement sendToLink = driver.findElement(By.xpath("//a[@data-string=\"sendTab\"]"));
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertTrue(sendToLink != null);
    }
    
    /*
     * Given that I am on an artist's page
     * When I right-click and share an artist
     * Then I am given the option to share them with users of my choosing
     */
    @Test
    public void artistShareTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// navigate to artist to test (Mount Eerie)
    	driver.get("https://play.spotify.com/artist/4Sw0SFu1fFdYXdAEVdrqnO");
    	Thread.sleep(10000);
    	
    	// switch to artist iframe
    	WebElement artistFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify:app:artist')]"));
        driver.switchTo().frame(artistFrame);
        Thread.sleep(2000);
        
        // click on ellipsis button to open options for song
        driver.findElement(By.cssSelector(".spoticon-more-16")).click();
        Thread.sleep(2000);
    	
    	// switch to context actions frame
        driver.switchTo().defaultContent(); // first go back to main frame
        driver.switchTo().frame("context-actions");
        Thread.sleep(2000);
        
        // click on "Share..." menu item
        driver.findElement(By.cssSelector("#share")).click();
        Thread.sleep(4000);
        
        // assert that "Send to..." option appears in share dialog
        // switch to share frame
        driver.switchTo().defaultContent(); // first go back to main frame
        WebElement shareFrame = driver.findElement(By.xpath("//iframe[@id='share']"));
        driver.switchTo().frame(shareFrame);
        // find "Send to..." link
        WebElement sendToLink = driver.findElement(By.xpath("//a[@data-string=\"sendTab\"]"));
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertTrue(sendToLink != null);
    }
    
    /*
     * Given that I am in a playlist
     * When I right-click and share a track
     * Then I am given the option to share it globally with all my followers
     */
    @Test
    public void globalShareTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// navigate to track to test ("It Catches Up With You")
    	driver.get("https://play.spotify.com/track/2cryrajucGmbV6mwNUhQPB");
    	Thread.sleep(10000);
    	
    	// switch to player iframe
        driver.switchTo().frame("app-player");
        Thread.sleep(2000);
        
        // click on ellipsis button to open options for song
        driver.findElement(By.id("widget-more")).click();
        Thread.sleep(2000);
    	
    	// switch to context actions frame
        driver.switchTo().defaultContent(); // first go back to main frame
        driver.switchTo().frame("context-actions");
        Thread.sleep(2000);
        
        // click on "Share..." menu item
        driver.findElement(By.cssSelector("#share")).click();
        Thread.sleep(4000);
        
        // assert that "Post to Followers" option appears in share dialog
        // switch to share frame
        driver.switchTo().defaultContent(); // first go back to main frame
        WebElement shareFrame = driver.findElement(By.xpath("//iframe[@id='share']"));
        driver.switchTo().frame(shareFrame);
        // find "Post to Followers" link
        WebElement sendToLink = driver.findElement(By.xpath("//a[@data-string=\"postTab\"]"));
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertTrue(sendToLink != null);
    }
    
    /*
     * Given that I am on a user's page
     * When I click the Follow button
     * Then I should start following them
     * And my "Following" page will indicate that I followed them
     */
    @Test
    public void followTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// navigate to second user's page
    	driver.get("https://play.spotify.com/user/" + "softwaretester1699");
    	Thread.sleep(4000);
    	
    	// switch to user frame
    	WebElement userFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify:app:user:softwaretester1699')]"));
        driver.switchTo().frame(userFrame);
        
        // click "Follow" button
        driver.findElement(By.cssSelector(".button-add")).click();
        
        // navigate to first user's "Following" page
        driver.get("https://play.spotify.com/user");
        Thread.sleep(4000);
        
        // switch to user frame
    	userFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'user-app-spotify')]"));
        driver.switchTo().frame(userFrame);
        
        // click on "Following" tab
        driver.findElement(By.xpath("//*[@id='main']/header/div/div/div/ul/li[2]/a")).click();
        Thread.sleep(3000);
        
        // assert that the second user appears in the list
        WebElement secondUserLink = driver.findElement(By.xpath("//table[@data-uri=\"spotify:user:softwaretester1699\"]"));
        
        // unfollow them
        driver.findElement(By.cssSelector(".button-add")).click();
        
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertTrue(secondUserLink != null);
    }
    
    /*
     * Given that I am viewing a playlist made by another user
     * When I click on the user's name
     * Then I should see their profile page
     */
    @Test
    public void profileTest() throws InterruptedException {
    	// login to Spotify
    	Thread.sleep(2000);
    	login(1);
    	Thread.sleep(2000);
    	
    	// navigate to second user's playlist
    	driver.get("https://play.spotify.com/user/spotitest3/playlist/1aqYTCI9CUe3ppQtdM2D0T");
    	Thread.sleep(8000);
    	
    	// switch to playlist frame
    	WebElement playlistFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify:app:user:spotitest3:playlist')]"));
        driver.switchTo().frame(playlistFrame);
        Thread.sleep(2000);
    	
    	// click on playlist creator's name
        driver.findElement(By.cssSelector(".pull-left > span:nth-child(1) > a:nth-child(1)")).click();
        Thread.sleep(4000);
        
        // assert that we are on the second user's profile page
        driver.switchTo().defaultContent(); // return to outermost frame, which should have the profile page URL
        String pageURL = driver.getCurrentUrl();
        
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        assertEquals(pageURL, "https://play.spotify.com/user/spotitest3");
    }
    
    /*
     * Given that I am in the Messaging interface
     * When I enter a message and a recipient and click sen
     * Then the recipient should recieve that message
     */
    @Test
    public void messageTest() throws InterruptedException {
    	// login to Spotify with first user
    	Thread.sleep(2000);
    	login(0);
    	Thread.sleep(2000);
    	
    	// open messaging interface
    	/* note: inital message had to be sent using desktop client 
    	 * to begin conversation thread because web client
    	 * doesn't support sharing to non-facebook friends
    	 */
    	driver.get("https://play.spotify.com/messages");
    	Thread.sleep(3000);
    	// switch to messaging frame
    	WebElement messagingFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'messages-app-spotify')]"));
        driver.switchTo().frame(messagingFrame);
        Thread.sleep(2000);
    	
    	// open conversation with second user
    	driver.findElement(By.cssSelector(".list-group-item > a:nth-child(1)")).click();
    	
    	// generate timestamp string (so the test message is unique per test run)
    	String timeString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    	
    	// send string
    	driver.findElement(By.xpath("//*[@id=\"thread-body\"]/div[3]/textarea")).sendKeys(timeString);
    	driver.findElement(By.xpath("//*[@id=\"thread-body\"]/div[3]/textarea")).sendKeys(Keys.RETURN);
    	Thread.sleep(2000);
    	
    	// log out of first user's account
    	logout();
        Thread.sleep(2000);
        
        // log into second user's account
        Thread.sleep(2000);
    	login(1);
    	Thread.sleep(2000);
    	
    	// open messaging interface
    	driver.get("https://play.spotify.com/messages");
    	Thread.sleep(3000);
    	// switch to messaging frame
    	messagingFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'messages-app-spotify')]"));
        driver.switchTo().frame(messagingFrame);
        Thread.sleep(4000);
    	
    	// open conversation with first user
    	driver.findElement(By.cssSelector(".list-group-item > a:nth-child(1)")).click();
    	
    	// search all messaging frame text for timestamp string
        String messagingFrameText = driver.findElement(By.tagName("html")).getAttribute("innerHTML");
        
        // assert that the messaging frame contains the previously generated timestamp string
        logout();
        Thread.sleep(2000);
        assertTrue(messagingFrameText.contains(timeString));
    }
}
