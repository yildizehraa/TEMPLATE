package common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.sql.Timestamp;
import java.util.Arrays;

/****************************************************
 * Tarih: 2018-12-05
 * Proje: MayaNext Functional Test Automation
 * Class: 
 * Yazan: Emre Sencan
 ****************************************************/
public class DriverEventListener extends BaseLibrary implements WebDriverEventListener {

    private static boolean log = false;

    public void beforeAlertAccept(WebDriver driver) {

    }

    public void afterAlertAccept(WebDriver driver) {

    }

    public void afterAlertDismiss(WebDriver driver) {

    }

    public void beforeAlertDismiss(WebDriver driver) {

    }

    public void beforeNavigateTo(String url, WebDriver driver) {

    }

    public void afterNavigateTo(String url, WebDriver driver) {

    }

    public void beforeNavigateBack(WebDriver driver) {

    }

    public void afterNavigateBack(WebDriver driver) {

    }

    public void beforeNavigateForward(WebDriver driver) {

    }

    public void afterNavigateForward(WebDriver driver) {

    }

    public void beforeNavigateRefresh(WebDriver driver) {

    }

    public void afterNavigateRefresh(WebDriver driver) {

    }

    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        waitForLoadingJS(driver);
//        if (!driver.findElement(by).isDisplayed())
//            Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", driver.findElement(by));

    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
//        waitForLoadingJS(driver);
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    found element: " + by.toString());
        }
    }

    public void beforeClickOn(WebElement element, WebDriver driver) {
//        waitForLoadingJS(driver);
    }

    public void afterClickOn(WebElement element, WebDriver driver) {
        waitForLoadingJS(driver);
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    clicked: " + element.toString());
        }
    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        waitForLoadingJS(driver);
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    Change value: " + Arrays.toString(keysToSend) + "    element: " + element.toString());
        }
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    value chaged: " + element.toString());
        }
    }

    public void beforeScript(String script, WebDriver driver) {
//        waitForLoadingJS(driver);
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    before script: " + script);
        }
    }

    public void afterScript(String script, WebDriver driver) {
        if (log) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + "    after script: " + script);
        }
    }

    public void beforeSwitchToWindow(String s, WebDriver webDriver) {

    }

    public void afterSwitchToWindow(String s, WebDriver webDriver) {

    }

    public void onException(Throwable throwable, WebDriver driver) {

    }

}
