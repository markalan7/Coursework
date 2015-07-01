/**
* Tests for the general user with a free account
*
* As a general user
* I want free access to Spotify's music library
* So that I can try out their service and listen to music I like
*/

import static org.junit.Assert.*;
import org.junit.Test;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class GeneralTest {
    // set up web driver
    static WebDriver driver = new FirefoxDriver();
    final static String username = "spotitest3";
    final static String username2 = "softwaretester1699";
    final static String password = "temporaryPass58()";
    static boolean logged = false;

    // helper method - login to Spotify
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
        logged = true;
    }

    // helper method - log out of Spotify
    public static void logout() throws InterruptedException {
        // navigate to settings page
        driver.get("https://play.spotify.com/settings");
        Thread.sleep(4000);
        
        // click on logout button
        driver.findElement(By.id("logout-settings")).click();
        Thread.sleep(2000);
        logged = false;
    }

    /** Scenario: Create an account with an email that is already in use
    *   Given I am on the Spotify website
    *   And I am not currently signed in to an account
    *   And I select the 'Sign up with your email address' option
    *   When I enter the following valid credentials: username, password, date of birth, and gender
    *   But the email address I entered is associated with another account
    *   And I select 'Sign up'
    *   Then a new account is not created
    *   And I am informed that the email address is already in use
    **/
    @Test
    public void createAccountWithTakenEmailTest() throws InterruptedException {
        // navigate to spotify
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);

        // click on "sign up with your email addres"
        driver.findElement(By.id("signup-spotify")).click();

        // enter username
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("superuniquecertainlydoesnotexist55555");

        // enter email (already associated with another user account)
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("testingspotifytesting@mailinator.com");

        // enter email confirm (already associated with another user account)
        driver.findElement(By.id("email-confirm")).clear();
        driver.findElement(By.id("email-confirm")).sendKeys("testingspotifytesting@mailinator.com");
    
        // enter password
        driver.findElement(By.id("password")).sendKeys("doesn'tmatter.jpg");

        // enter age
        driver.findElement(By.id("age-year")).sendKeys("1960");
        driver.findElement(By.id("age-month")).sendKeys("2");
        driver.findElement(By.id("age-day")).sendKeys("13");

        // enter gender
        List<WebElement> radios = driver.findElements(By.name("gender"));
        radios.get(0).click();

        //submit form
        driver.findElement(By.xpath("//*[@id='signup-form']/div/form/p[9]/button")).click();
        Thread.sleep(1000);

        // check error message
        String error = driver.findElement(By.xpath("//*[@id='wrap-email']/div")).getText();
        assertEquals(error, "There's already an account associated with this email address");
    }

    /** Scenario: Successful sign-in given correct credentials
    *   Given a correct username
    *   And a correct password
    *   When I try to log in with those credentials
    *   Then I should see the home page of http://play.spotify.com
    **/
    @Test
    public void loginTest() throws InterruptedException {
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);

        // click on "already have account"
        driver.findElement(By.id("has-account")).click();

        // enter username
        driver.findElement(By.id("login-usr")).clear();
        driver.findElement(By.id("login-usr")).sendKeys(username);

        // enter password
        driver.findElement(By.id("login-pass")).sendKeys(password);

        // submit
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
        Thread.sleep(4000);

        // check the username is displayed on screen
        String name = driver.findElement(By.xpath("//*[@id='nav-profile']/span/div[2]")).getText();
        logout();
        assertEquals(name, username);
    }

    /** Scenario: Unsuccessful sign-in given incorrect password
    *   Given a correct username
    *   And an incorrect password
    *   When I try to log in with those credentials
    *   Then I should see an error message indicating incorrect credentials
    **/
    @Test
    public void loginWrongPasswordTest() throws InterruptedException {
        driver.get("https://play.spotify.com");
        Thread.sleep(2000);

        // click on "already have account"
        driver.findElement(By.id("has-account")).click();

        // enter username
        driver.findElement(By.id("login-usr")).clear();
        driver.findElement(By.id("login-usr")).sendKeys(username);

        // enter password
        driver.findElement(By.id("login-pass")).sendKeys("thisisn'tit");

        // submit
        driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
        Thread.sleep(4000);

        // check the username is displayed on screen
        String error = driver.findElement(By.xpath("//*[@id='login-error']")).getText();
        assertEquals(error, "Incorrect username and password");
    }

    /** Scenario: Language can be selected
    *   Given that I am on the settings page
    *   When I click the language dropbox
    *   And I select a new language
    *   And I reload the page
    *   Then the page should reappear in the selected language
    **/
    @Test
    public void ichSprecheDeutsch() throws InterruptedException {
        // log in if you aren't
        if(!logged) {
            login();
        }
        driver.get("https://play.spotify.com/settings");
        Thread.sleep(4000);

        // select language dropdown
        Select lang = new Select(driver.findElement(By.xpath("//*[@id='lang-select']/select")));
        lang.selectByValue("de");
        driver.navigate().refresh();
        Thread.sleep(4000);

        // check that language is in german
        String test = driver.findElement(By.xpath("//*[@id='fbconnect']/div[1]")).getText();
        
        // change back to english
        Select eng = new Select(driver.findElement(By.xpath("//*[@id='lang-select']/select")));
        eng.selectByValue("en");
        driver.navigate().refresh();
        Thread.sleep(4000);

        logout();
        assertEquals(test, "Mein Konto mit Facebook verbinden");
    }

    /** Scenario: Successful log-out
    *   Given I am signed in
    *   When I click the setting button
    *   And I click the "Log Out" button
    *   Then I should be logged out of my account
    **/
    @Test
    public void logOutTest() throws InterruptedException {
        if(!logged) {
            login();
        }

        // go to settings page
        driver.findElement(By.id("nav-settings")).click();
        Thread.sleep(1500);
        
        // click on logout button
        driver.findElement(By.id("logout-settings")).click();
        Thread.sleep(2000);

        // get log-in element, which is only displayed on log-in screen
        String login = driver.findElement(By.id("has-account")).getText();
        assertEquals(login, "Already have an account? Log in here.");
    }
}