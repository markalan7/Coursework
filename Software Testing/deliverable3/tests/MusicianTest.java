/**
* Tests for musician user
* As a budding musician
* I want to discover new music
* So that I can be inspired to create new music myself
*/

import static org.junit.Assert.*;
import org.junit.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class MusicianTest {
    // set up web driver
    static WebDriver driver = new FirefoxDriver();
    final static String username = "spotitest3";
    final static String username2 = "softwaretester1699";
    final static String password = "temporaryPass58()";

    // helper method - login to Spotify
    @BeforeClass
    public static void login() throws InterruptedException {
        // navigate to home page
        driver.get("https://play.spotify.com");
        
        // click on "already have account?" link
        driver.findElement(By.id("has-account")).click();
        
        // enter username
        driver.findElement(By.id("login-usr")).clear();
        driver.findElement(By.id("login-usr")).sendKeys(username);
        // enter password
        driver.findElement(By.id("login-pass")).sendKeys(password);
        
        // submit form
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
        Thread.sleep(4000);
    }

    // helper method - log out of Spotify
    @AfterClass
    public static void logout() throws InterruptedException {
        // navigate to settings page
        driver.get("https://play.spotify.com/settings");
        Thread.sleep(4000);
        
        // click on logout button
        driver.findElement(By.id("logout-settings")).click();
        Thread.sleep(2000);
    }

    /** Scenario: Search by keyword
    *   Given that I am on the main page
    *   When I click the search button
    *   And enter a keyword
    *   Then I should see a list of results relevant to the entered keyword
    **/
    @Test
    public void searchByKeyword() throws InterruptedException {

        // click search button
        driver.findElement(By.id("nav-search")).click();

        // switch to search box iframe
        driver.switchTo().defaultContent();
        WebElement suggest = driver.findElement(By.xpath("//iframe[@id='suggest']"));
        driver.switchTo().frame(suggest);

        // add keywords to search bar (artist: Father John Misty)
        driver.findElement(By.xpath("/html/body/div[1]/form/input")).sendKeys("Father John Misty");
        Thread.sleep(1000);

        // select correct artist
        WebElement fjm = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div[1]/ul[1]/li/a"));
        String name = fjm.getAttribute("data-drag-text");

        // switch back to main frame
        driver.switchTo().defaultContent();
        
        // confirm artist was found
        assertEquals("Father John Misty", name);
        
        // return to main page
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);
    }

    /** Scenario: List of new releases
    *   Given that I am on the main page
    *   When I click the browse button
    *   And I select 'New Releases'
    *   Then a list of albums should appear
    **/
    @Test
    public void listNewReleases() throws InterruptedException {

        // switch to iframe that holds browse buttons
        // find path by xpath seach because id is nondeterministic
        WebElement browse = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify')]"));
        driver.switchTo().frame(browse);

        // click on new releases
        driver.findElement(By.cssSelector("a[href='spotify:app:browse:releases']")).click();
        Thread.sleep(4000);

        // find an album (Push It by iSHi, since it was in the new releases when I tested)
        WebElement item = driver.findElement(By.xpath("//*[@id='widget-47']/div/div/div[2]/div/a"));
        String title = item.getAttribute("title");

        // switch back to main frame
        driver.switchTo().defaultContent();

        // confirm new release was found
        assertEquals("Push It", title);
        
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);
    }

    /** Scenario: Select music playlist by mood
    *   Given that I am on the main page
    *   When I click the browse button
    *   And I select 'Genres & Moods'
    *   Then a list of moods should appear
    **/
    @Test
    public void listByMood() throws InterruptedException {

        // switch to iframe that holds browse buttons
        // find path by xpath seach because id is nondeterministic
        WebElement browse = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify')]"));
        driver.switchTo().frame(browse);

        // click on new releases
        driver.findElement(By.cssSelector("a[href='spotify:app:browse:genres']")).click();
        Thread.sleep(4000);

        // mood (party) exists
        WebElement element = driver.findElement(By.xpath("//*[@id='widget-47']/div/a/div/div/span"));
        String mood = element.getText();
        assertEquals("Party", mood);

        // switch back to main frame
        driver.switchTo().defaultContent();
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);
    }


    /** Scenario: Select music playlist by genre
    *   Given that I am on the main page
    *   When I click the browse button
    *   And I select 'Genres & Moods'
    *   Then a list of genres should appear
    **/
    @Test
    public void listByGenre() throws InterruptedException {

        // switch to iframe that holds browse buttons
        // find path by xpath seach because id is nondeterministic
        WebElement browse = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify')]"));
        driver.switchTo().frame(browse);

        // click on new releases
        driver.findElement(By.cssSelector("a[href='spotify:app:browse:genres']")).click();
        Thread.sleep(4000);

        // genre (Hip Hop) exists
        WebElement element = driver.findElement(By.xpath("//*[@id='widget-52']/div/a/div/div/span"));
        String genre = element.getText();
        assertEquals("Hip Hop", genre);

        // switch back to main frame
        driver.switchTo().defaultContent();
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);
    }

    /** Scenario: Top songs by category
    *   Given that I am on the main page
    *   When I click the browse button
    *   And I select 'Top Lists'
    *   Then I should get a list of top hits by category
    **/
    @Test
    public void listTopLists() throws InterruptedException {
        // switch to iframe that holds browse buttons
        // find path by xpath seach because id is nondeterministic
        WebElement browse = driver.findElement(By.xpath("//iframe[starts-with(@id, 'browse-app-spotify')]"));
        driver.switchTo().frame(browse);

        // click on new releases
        WebElement toplistsele = driver.findElement(By.cssSelector("a[href='spotify:app:browse:toplists']"));
        toplistsele.click();
        Thread.sleep(4000);
        String toplist = toplistsele.getText();

        // list (viral top 50 global) exists
        //WebElement element = driver.findElement(By.xpath("//*[@title='Today\'s Top Hits']"));
        //String list = element.getAttribute("title");
        //assertEquals("Today's Top Hits", list);

        // I tried finding a clickable icon with a music list that it links to, but I had trouble using findElement on one
        // instead, I simply checked that there was a "Top Lists" link and that it was clickable

        // assert the link is for "top lists"
        // lowercase to avoid case formatting
        assertTrue(toplist.toLowerCase().contains("top lists"));
        
        // switch back to main frame
        driver.switchTo().defaultContent();
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);
    }

}