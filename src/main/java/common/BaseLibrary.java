package common;

import com.codeborne.selenide.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;


public class BaseLibrary  {

    protected static final Logger log = Logger.getLogger(BaseLibrary.class.getName());
    private long waitForLoading = 60;


    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        byte[] bytes = new byte[]{};
        try {
            bytes = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            log.warning("Take screenshot error:" + e.getMessage());
        }
        return bytes;
    }


    private long getWaitForLoading() {
        return waitForLoading;
    }


    public void waitForLoadingJS(WebDriver driver, long timeoutSec) {
        AtomicInteger isJsFinished = new AtomicInteger();
        AtomicInteger isAjaxFinished = new AtomicInteger();
        final int jsCompleteCheck = 3;
        new WebDriverWait(driver, timeoutSec, 10).until(d -> {
            JavascriptExecutor js = (JavascriptExecutor) d;
            try {
                if ((boolean) js.executeScript("return (document.readyState == \"complete\" || document.readyState == \"interactive\")"))
                    isJsFinished.set(jsCompleteCheck);
            } catch (Exception e) {
                isJsFinished.getAndIncrement();
            }

            try {
                if ((boolean) js.executeScript("var result = true; try { result = (typeof jQuery != 'undefined') ? jQuery.active == 0 : true } catch (e) {}; return result;"))
                    isAjaxFinished.set(jsCompleteCheck);
            } catch (Exception e) {
                isAjaxFinished.getAndIncrement();
            }

            boolean loaderVisible = false;
            if (loaderVisible) {
                isJsFinished.set(0);
                isAjaxFinished.set(0);
            }
            return isJsFinished.get() >= jsCompleteCheck && isAjaxFinished.get() >= jsCompleteCheck && !loaderVisible;
        });
    }

    public void waitForLoadingJS(WebDriver driver) {
        long timeout = getWaitForLoading();
        waitForLoadingJS(driver, timeout);
    }


    public void maximazeBrowser() {
        try {
            if (Configuration.browserSize != null) {
                try {
                    String[] size = Configuration.browserSize.split("x");
                    int width = Integer.parseInt(size[0]);
                    int height = Integer.parseInt(size[1]);
                    Dimension browserSize = new Dimension(width, height);
                    WebDriverRunner.getWebDriver().manage().window().setSize(browserSize);
                } catch (NumberFormatException e) {
                    WebDriverRunner.getWebDriver().manage().window().maximize();
                }
            } else {
                try {
                    WebDriverRunner.getWebDriver().manage().window().maximize();
                    System.out.println("manage().window().maximize()");
                } catch (Exception e) {
                    System.out.println("maximize:" + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("maximize:" + e.getMessage());
        }
    }


    public static String stringOlusturma(Method test) {
        String testName = null;
        String testDescripton = null;
        String[] depenthsOnMethod = null;
        String method = null;
        String feature = null;
        String[] feature2;
        String text = "Bos";


        testName = test.getName();

        if (testName.contains("TS0")) {
            testDescripton = test.getDeclaredAnnotation(org.testng.annotations.Test.class).description();
            depenthsOnMethod = test.getDeclaredAnnotation(org.testng.annotations.Test.class).dependsOnMethods();
            if (depenthsOnMethod.length == 0)
                method = "";
            else
                method = depenthsOnMethod[0];


            feature = test.getDeclaringClass().getDeclaredAnnotations()[0].toString();
            feature2 = feature.split("=\"");
            feature = feature.split("\"")[1];

            text = "{\"testClass\":\"" + feature + "\",\"test\":\"" + testName + "\",\"description\":\"" + testDescripton + "\",\"methodsDependsOn\":[\"" + method + "\"]}";
        }

        return text;

    }

    public void openLastPage() {
        Set<String> windows = WebDriverRunner.getWebDriver().getWindowHandles();
        int i = 0;
        for (String handle : windows) {
            i++;
            if (i == windows.size()) {
                switchTo().window(handle);

            }

        }
        maximazeBrowser();
    }
    public void close() {
        WebDriverRunner.getWebDriver().close();

    }

    public void switchToFrame(WebElement element){

        switchTo().frame(element);

    }

}
