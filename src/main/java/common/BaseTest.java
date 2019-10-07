package common;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import common.DriverEventListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.*;
import org.testng.annotations.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static io.qameta.allure.util.ResultsUtils.firstNonEmpty;

public class BaseTest extends BaseLibrary {

    static final int timeout = 200;
    public Locale turkishLocal;
    String driverPath="";
    private String parentFeatureId = null;
    public String testNameFromXml;
    protected static Map<String, String> parentFeatureMap = new HashMap();


    public void driverPath(String browserName){

        try {

            String OS= System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            driverPath=System.getProperty("user.dir")+"/drivers/";

            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {

                if (browserName.equalsIgnoreCase("chrome")){
                    driverPath= driverPath+"chromedriver";
                }else if (browserName.equalsIgnoreCase("firefox")){

                }


            } else if (OS.indexOf("win") >= 0) {

                if (browserName.equalsIgnoreCase("chrome")){
                    driverPath= driverPath+"chromedriver.exe";
                }else if (browserName.equalsIgnoreCase("firefox")){

                }else if(browserName.equalsIgnoreCase("ie")){
                    driverPath=driverPath+"IEDriverServer.exe";
                }


            } else if (OS.indexOf("nux") >= 0) {

                if (browserName.equalsIgnoreCase("chrome")){
                    driverPath= driverPath+"chromedriver";
                }else if (browserName.equalsIgnoreCase("firefox")){

                }

            } else {

                Assert.fail("Please Set Chrome Driver Path According to Detected operating system: "+OS );
            }


        }catch (Exception e){

            Assert.fail("Internet Explorer Driver Path Error: "+e.getMessage());
        }
    }


    @BeforeSuite(alwaysRun = true)
    public void driverSetUp() throws IOException {

        Properties properties=getProperty();

        String sysProperties = "";
        sysProperties += "Setup started";
        sysProperties += "\nfile.encoding: " + String.format("file.encoding: %s", System.getProperty("file.encoding"));
        sysProperties += "\ndefault charset=" + Charset.defaultCharset();
        sysProperties += "\njava.specification.version" + System.getProperty("java.specification.version");
        sysProperties += "\njava.runtime.version" + System.getProperty("java.runtime.version");
        sysProperties += "\nlocale default:" + Locale.getDefault();
        turkishLocal = new Locale("tr", "TR");

        if (!Locale.getDefault().equals(turkishLocal)) Locale.setDefault(turkishLocal);
        sysProperties += "\nlocale: " + Locale.getDefault();
        WebDriverRunner.addListener(new DriverEventListener());

        String browserName=properties.getProperty("browser");

        if (browserName.equalsIgnoreCase("ie")){
            driverPath("ie");
            System.setProperty("webdriver.ie.driver", driverPath);
            Configuration.browser=WebDriverRunner.INTERNET_EXPLORER;
        }else if (browserName.equalsIgnoreCase("chrome")){
            driverPath("chrome");
                System.setProperty("webdriver.chrome.driver", driverPath);

            Configuration.browser = (System.getProperty("browser") == null) ? "chrome" : System.getProperty("browser");
        }




        Configuration.driverManagerEnabled = false;
        Configuration.reportsFolder = "test-result/reports";
        Configuration.screenshots = Configuration.remote == null;
        Configuration.savePageSource = false;
        Configuration.collectionsTimeout = timeout * 1000;
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = timeout * 1000;
        Configuration.startMaximized = true;
        Configuration.pollingInterval = 100;
        Configuration.collectionsPollingInterval = 100;
        Configuration.headless = false;


//        setWaitForLoading(loadingTimeout);

        sysProperties += "\nremote: " + Configuration.remote;
        sysProperties += "\nbrowser: " + Configuration.browser;
        sysProperties += "\nbrowser.version: " + Configuration.browserVersion;
        sysProperties += "\nurl: " + Configuration.baseUrl;

        log.info(sysProperties);
    }

    @BeforeSuite(enabled = true)
    public void beforeSuite(ITestContext context) throws IOException {

        if (System.getProperty("buildName") != null && !System.getProperty("buildName").isEmpty())
            context.getSuite().getXmlSuite().setName(System.getProperty("buildName"));
        else
            context.getSuite().getXmlSuite().setName("Suite");

        ((TestRunner) context).getTest().setName("Tests");

        if (System.getProperty("buildName") != null && !System.getProperty("buildName").isEmpty())
            context.getSuite().getXmlSuite().setName(System.getProperty("buildName"));
        else
            context.getSuite().getXmlSuite().setName("Suite");

        ((TestRunner) context).getTest().setName("Tests");

    }

    @BeforeMethod(alwaysRun = true, enabled = true)
    public void beforeMethod(ITestContext context, Method test) throws UnsupportedEncodingException {

            String testResults = "";
            String testName = firstNonEmpty(
                    test.getDeclaredAnnotation(org.testng.annotations.Test.class).description(),
                    test.getName())
                    .orElse("Unknown");

            final String desc = test.getDeclaredAnnotation(org.testng.annotations.Test.class).toString();
            Allure.addAttachment("Annotations", desc);
            testResults += "\n///////////////////////////////////////////////////////" + "\n";
            testResults += "\nTotal Tests: " + context.getAllTestMethods().length;
            testResults += "\nPassed Tests: " + context.getPassedTests().size();
            testResults += "\nFailed Tests: " + context.getFailedTests().size();
            testResults += "\nSkipped Tests: " + context.getSkippedTests().size();
            testResults += "\nLeft Tests: " + Integer.valueOf(context.getAllTestMethods().length - (context.getPassedTests().size() + context.getFailedTests().size() + context.getSkippedTests().size())).toString() + "\n";
            testResults += "\n///////////////////////////////////////////////////////";
            testResults += "\nTEST CLASS: " + test.getDeclaringClass().getSimpleName() + "\n";
            testResults += "\nTEST: " + testName + "\n";
            testResults += "\nSTATUS: Started: " + "\n";
            testResults += "\nTEST ANNOTATIONS: " + test.getDeclaredAnnotation(org.testng.annotations.Test.class).toString();
            testResults += "\n///////////////////////////////////////////////////////";
            testResults += "\n///////////////////////////////////////////////////////";
            log.info(testResults);

        }

    @AfterMethod(alwaysRun = true, enabled = true)
    public void afterMethod(ITestResult testResult)  {

        IRetryAnalyzer retry = testResult.getMethod().getRetryAnalyzer();
        if (retry != null) {
            if (testResult.getStatus() == 1) {
                testResult.getTestContext().getSkippedTests().removeResult(testResult.getMethod());
                testResult.getTestContext().getFailedTests().removeResult(testResult.getMethod());
            }

        }

        String testResults = "";
        String result = "unknown";
        switch (testResult.getStatus()) {
            case 1:
                result = "SUCCESS";
                break;
            case 2:
                result = "FAILURE";
                break;
            case 3:
                result = "SKIP";
                break;
            case 4:
                result = "SUCCESS_PERCENTAGE_FAILURE";
                break;
            case 16:
                result = "STARTED";
                break;
        }

        if (testResult.getStatus() == ITestResult.FAILURE)
            takeScreenshot();
        testResults += "///////////////////////////////////////////////////////";
        testResults += "///////////////////////////////////////////////////////";
        testResults += "\nTEST: " + testResult.getMethod().getMethodName() + "\n";
        testResults += "\nSTATUS: " + result + "\n";
        testResults += "\nDESCRIPTION: " + testResult.getMethod().getDescription() + "\n";
        if (testResult.getThrowable() != null) {
            testResults += "\nERROR: " + testResult.getThrowable().getMessage() + "\n";
        }
        testResults += "///////////////////////////////////////////////////////";
        testResults += "///////////////////////////////////////////////////////";
        log.info(testResults);

        try {
            Selenide.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass(alwaysRun = false)
    public void afterClass()  {
        Selenide.close();
        log.info("Browser has been closed.");
    }


    @BeforeClass(alwaysRun = true)
    public void beforeClass()  {

        Annotation[] annotations = this.getClass().getAnnotations();
        if (annotations.length > 0) {
            this.testNameFromXml = this.getClass().getName();
        } else {
            this.testNameFromXml = this.getClass().getName();
        }

        parentFeatureMap.put(this.testNameFromXml, this.parentFeatureId);
    }


    public Properties getProperty() {

        Properties properties= new Properties();

        try {

            properties.load(new FileInputStream("src/main/resources/config.properties"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return properties;
    }


}
