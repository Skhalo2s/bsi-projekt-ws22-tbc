import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SeleniumTestLogin {

    private static WebDriver driver = null;
    private WebDriverWait webDriverWait = null;


    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\salah\\OneDrive - Hochschule Bonn-Rhein-Sieg\\Dokumente\\Hochschule\\Semester5\\IT-Projekt\\chromedriver.exe");
        driver = new ChromeDriver();
    }


    @Test
    public void testLogin() {
        webDriverWait = new WebDriverWait(driver, 100);
        driver.get("http://sepp-test.inf.h-brs.de:8080/WAC-0.0.1-SNAPSHOT/");
        //driver.get("localhost:8080");
        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/vaadin-app-layout/vaadin-vertical-layout[2]/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field//div/div[1]")));
        driver.findElement(By.xpath("/html/body/vaadin-app-layout/vaadin-vertical-layout[2]/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field//div/div[1]")).sendKeys("TomM");
        driver.findElement(By.id("/html/body/vaadin-app-layout/vaadin-vertical-layout[2]/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field//div/div[1]")).sendKeys("Test12345");
        driver.findElement(By.xpath("/html/body/vaadin-app-layout/vaadin-vertical-layout[2]/vaadin-vertical-layout/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-button//div")).click();
        //webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/vaadin-app-layout/vaadin-horizontal-layout/vaadin-horizontal-layout/h3")));
        //assertEquals(driver.findElement(By.xpath("/html/body/vaadin-app-layout/vaadin-horizontal-layout/vaadin-horizontal-layout/h3")).getText(), "Hallo!");

        //webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//[@id="button"]")));
        /**try{
            driver.findElement(By.xpath("")).click(); //Button aus irgendeinem Grund leider nicht interactable
        }catch (ElementNotInteractableException e) {
            WebElement webElement = driver.findElement(By.xpath(""));
            Actions actions = new Actions(driver);
            actions.moveToElement(webElement).click();
        }*/// hier auskomentieren





    }



    @AfterClass
    public static void tearDownClass(){
        driver.quit();
    }




}
