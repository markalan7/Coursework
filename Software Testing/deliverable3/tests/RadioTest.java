import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;

/* Tests for Radio Lover:
 * As a radio lover
 * I want an extensive radio feature
 * So that I can explore new artists and genres
 */

public class RadioTest {
    // set up web driver
    static WebDriver driver = new FirefoxDriver();
    
    // helper method - login to Spotify
    public static void login() {
        // navigate to home page
        driver.get("https://play.spotify.com");
        
        // click on "already have account?" link
        driver.findElement(By.id("has-account")).click();
        
        // enter username
        driver.findElement(By.id("login-usr")).clear();
        driver.findElement(By.id("login-usr")).sendKeys("spotitest3");
        
        // enter password
        driver.findElement(By.id("login-pass")).clear();
        driver.findElement(By.id("login-pass")).sendKeys("temporaryPass58()");
        
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
     * Given that I am listening to a song
     * When I right-click the title and click "create radio station"
     * Then a new radio station is started seeded from that song
     */
    @Test
    public void createRadioTest() throws InterruptedException {
        // login to Spotify
        Thread.sleep(2000);
        login();
        
        /* wait for authentication.
         * Thread.sleep() is used over Selenium's implicit wait because 
         * the latter seems to arbitrarily fail.
         * Sleeping is used frequently to combat AJAX page load times.
         */
        Thread.sleep(2000);
        
        // navigate to track to test ("Turret Redemption Line")
        driver.get("https://play.spotify.com/track/2jQ9mEFHBtjjiSjzTYYjNE");
        Thread.sleep(8000);
        
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
        
        // click on "Start Radio" menu item
        driver.findElement(By.cssSelector("#start-radio")).click();
        Thread.sleep(4000);
        
        // switch to radio frame
        driver.switchTo().defaultContent(); // first go back to main frame
        // have to find frame with xpath search because the radio frame id is nondeterministic
        WebElement radioFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'radio-app-spotify')]"));
        driver.switchTo().frame(radioFrame);
        Thread.sleep(2000);
        
        // get radio seed title
        String seedTitle = driver.findElement(By.xpath("//a[@data-ta=\"seed-title\"]")).getAttribute("innerHTML");
        
        /* values are saved and compared after logging out of Spotify
         * because assert statments terminate the test, so logouts
         * placed after assertions will not run
         */
        
        // logout of Spotify
        logout();
        Thread.sleep(2000);
        
        // assert that the text in the "seed-title" element is "Turret Redemption Line", i.e. the clicked song
        assertEquals("Turret Redemption Line", seedTitle);
    }
    
    /*
     * Given that I am listening to a radio station
     * When I thumbs-up a track
     *Then the track should be added to my "liked from radio" playlist
     */
    @Test
    public void thumbsUpTest() throws InterruptedException {
        // login to Spotify
        Thread.sleep(2000);
        login();
        Thread.sleep(2000);
        
        // switch to radio tab
        driver.get("https://play.spotify.com/radio");
        Thread.sleep(4000);
        
        // switch to radio frame
        WebElement radioFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'radio-app-spotify')]"));
        driver.switchTo().frame(radioFrame);
        Thread.sleep(2000);
        
        // start the Black Metal station
        driver.findElement(By.cssSelector("button.main:nth-child(2)")).click();
        Thread.sleep(4000);
        
        // save current track name
        String currentTrack = driver.findElement(By.cssSelector("div.cover:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).getAttribute("innerHTML");
        currentTrack = currentTrack.replace("<span class=\"ellipsis\">&nbsp;</span>", ""); // remove ellipsis from track name
        currentTrack = currentTrack.trim(); // remove newlines and whitespace
        
        // thumbs-up current track
        driver.findElement(By.xpath("//button[@class=\"button button-icon-only spoticon-thumbs-up-32\"]")).click();
        
        // go to "Liked From Radio" playlist
        driver.get("https://play.spotify.com/user/spotitest0/playlist/5eF9dqNfk2YPFwo1qXYEnJ");
        Thread.sleep(8000);
        
        /* make sure the liked track's name appears on this page.
         * even if the track is still playing in the sidebar, this should
         * not cause a false positive since the "now playing" sidebar
         * is in a different frame.
         */
        
        // switch to playlist frame
        WebElement playlistFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'collection-app-spotify')]"));
        driver.switchTo().frame(playlistFrame);
        
        // get full text of frame
        String body = driver.findElement(By.tagName("html")).getAttribute("innerHTML");
        
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        
        // assert that playlist frame text contains liked track name
        assertTrue(body.contains(currentTrack));
    }
    
    /*
     * Given that I am listening to a radio station
     * When I thumbs-down a track
     * Then the current track should be skipped
     */
    @Test
    public void thumbsDownTest() throws InterruptedException {
        // login to Spotify
        Thread.sleep(2000);
        login();
        Thread.sleep(2000);
        
        // switch to radio tab
        driver.get("https://play.spotify.com/radio");
        Thread.sleep(4000);
        
        // switch to radio frame
        WebElement radioFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'radio-app-spotify')]"));
        driver.switchTo().frame(radioFrame);
        Thread.sleep(4000);
        
        // start the Alternative radio station. Note that this test will fail if the track has previously been thumbs-up'd.
        driver.findElement(By.cssSelector("button.main:nth-child(1)")).click();
        Thread.sleep(4000);
        
        // save current track name
        String oldTrack = driver.findElement(By.cssSelector("div.cover:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).getAttribute("innerHTML");
        oldTrack = oldTrack.replace("<span class=\"ellipsis\">&nbsp;</span>", ""); // remove ellipsis from track name
        oldTrack = oldTrack.trim(); // remove newlines and whitespace
        
        // thumbs-down current track
        driver.findElement(By.xpath("//button[@class=\"button button-icon-only spoticon-thumbs-down-32\"]")).click();
        Thread.sleep(8000);
        
        // get the new track name
        String newTrack = driver.findElement(By.cssSelector("div.cover:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).getAttribute("innerHTML");
        newTrack = newTrack.replace("<span class=\"ellipsis\">&nbsp;</span>", ""); // remove ellipsis from track name
        newTrack = newTrack.trim(); // remove newlines and whitespace
        
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        
        // make sure new and old tracks are not the same (i.e. that the old track has been skipped)
        System.out.println(oldTrack);
        System.out.println(newTrack);
        assertFalse(oldTrack.equals(newTrack));
    }
    
    /*
     * Given that I am listening to a radio station
     * When I click the skip button
     * Then the current track should be skipped
     */
    @Test
    public void skipTest() throws InterruptedException {
        // login to Spotify
        Thread.sleep(2000);
        login();
        Thread.sleep(2000);
        
        // switch to radio tab
        driver.get("https://play.spotify.com/radio");
        Thread.sleep(4000);
        
        // switch to radio frame
        WebElement radioFrame = driver.findElement(By.xpath("//iframe[starts-with(@id, 'radio-app-spotify')]"));
        driver.switchTo().frame(radioFrame);
        Thread.sleep(4000);
        
        // start the Blues radio station.
        driver.findElement(By.cssSelector("button.main:nth-child(2)")).click();
        Thread.sleep(4000);
        
        // save current track name
        String oldTrack = driver.findElement(By.cssSelector("div.cover:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).getAttribute("innerHTML");
        oldTrack = oldTrack.replace("<span class=\"ellipsis\">&nbsp;</span>", ""); // remove ellipsis from track name
        oldTrack = oldTrack.trim(); // remove newlines and whitespace
        
        // skip current track
        // switch to player iframe
        driver.switchTo().defaultContent();
        driver.switchTo().frame("app-player");
        Thread.sleep(2000);
        // click "Next" button
        driver.findElement(By.cssSelector("#next")).click();
        Thread.sleep(8000);
        
        // switch back to radio frame
        driver.switchTo().defaultContent();
        driver.switchTo().frame(radioFrame);
        
        // get the new track name
        String newTrack = driver.findElement(By.cssSelector("div.cover:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).getAttribute("innerHTML");
        newTrack = newTrack.replace("<span class=\"ellipsis\">&nbsp;</span>", ""); // remove ellipsis from track name
        newTrack = newTrack.trim(); // remove newlines and whitespace
        
        // log out of Spotify
        logout();
        Thread.sleep(2000);
        
        // make sure new and old tracks are not the same (i.e. that the old track has been skipped)
        System.out.println(oldTrack);
        System.out.println(newTrack);
        assertFalse(oldTrack.equals(newTrack));
    }
}
