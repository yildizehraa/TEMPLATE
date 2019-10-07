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

//import org.apache.log4j.Logger;

/****************************************************
 * Tarih: 2018-12-05
 * Proje: MayaNext Functional Test Automation
 * Class: 
 * Yazan: Emre Sencan
 ****************************************************/

public class BaseTest extends BaseLibrary {

    static final int timeout = 200;
    static final int loadingTimeout = 200;
    String explorerPath="";
    public Locale turkishLocal;
    String driverPath="";
    private String parentFeatureId = null;
    public String testNameFromXml;
    protected static Map<String, String> parentFeatureMap = new HashMap();
//    private static final Logger LOGGER = Logger.getLogger(TestBase.class);
    protected static boolean ISWEBSERVICE = false;


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

    public void useFirefox()
    {
        try {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability(CapabilityType.VERSION, Configuration.browserVersion);

            EventFiringWebDriver driver;
            if (Configuration.remote == null) {
                WebDriver firefox = new FirefoxDriver();
                driver = new EventFiringWebDriver(firefox).register(new DriverEventListener());
            } else {
                RemoteWebDriver firefox = new RemoteWebDriver(new URL(Configuration.remote), firefoxOptions);
                firefox.setFileDetector(new LocalFileDetector());
                driver = new EventFiringWebDriver(firefox).register(new DriverEventListener());
            }

            if (WebDriverRunner.hasWebDriverStarted())
                WebDriverRunner.getWebDriver().quit();

            WebDriverRunner.setWebDriver(driver);

        } catch (Exception e) {
            throw new RuntimeException(String.format("Error new RemoteWebDriver: %s error %s", Configuration.remote, e.getMessage()), e);
        }

        //System.out.println("Browser: " + getCapabilities().getBrowserName());
    }

    public void useIE()
    {
        try {
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.setCapability(CapabilityType.VERSION, Configuration.browserVersion);
            driverPath("ie");
            EventFiringWebDriver driver;
            if (Configuration.remote == null) {
                System.setProperty("webdriver.ie.driver", driverPath);
                WebDriver ie = new InternetExplorerDriver();
                driver = new EventFiringWebDriver(ie).register(new DriverEventListener());
            } else {
                RemoteWebDriver ie = new RemoteWebDriver(new URL(Configuration.remote), internetExplorerOptions);
                ie.setFileDetector(new LocalFileDetector());
                driver = new EventFiringWebDriver(ie).register(new DriverEventListener());
            }

            if (WebDriverRunner.hasWebDriverStarted())
                WebDriverRunner.getWebDriver().quit();

            WebDriverRunner.setWebDriver(driver);

        } catch (Exception e) {
            throw new RuntimeException(String.format("Error new RemoteWebDriver: %s error %s", Configuration.remote, e.getMessage()), e);
        }

        //System.out.println("Browser: " + getCapabilities().getBrowserName());
    }
    public void useChrome()

    {
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(CapabilityType.VERSION, Configuration.browserVersion);
            driverPath("chrome");
            EventFiringWebDriver driver;
            if (Configuration.remote == null) {
                System.setProperty("webdriver.chrome.driver", driverPath);
                WebDriver chromeDriver = new ChromeDriver();
                driver = new EventFiringWebDriver(chromeDriver).register(new DriverEventListener());
            } else {
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(Configuration.remote), chromeOptions);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                driver = new EventFiringWebDriver(remoteWebDriver).register(new DriverEventListener());
            }

            if (WebDriverRunner.hasWebDriverStarted())
                WebDriverRunner.getWebDriver().quit();

            WebDriverRunner.setWebDriver(driver);

        } catch (Exception e) {
            throw new RuntimeException(String.format("Error new RemoteWebDriver: %s error %s", Configuration.remote, e.getMessage()), e);
        }

        //System.out.println("Browser: " + getCapabilities().getBrowserName());
    }


    @BeforeSuite(alwaysRun = true)
   // @Parameters({"browserName"})
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

//        String driverPath=System.getProperty("user.dir")+"/drivers/";
        String browserName=properties.getProperty("browser");

        if (browserName.equalsIgnoreCase("ie")){
            driverPath("ie");
            System.setProperty("webdriver.ie.driver", driverPath);
            Configuration.browser=WebDriverRunner.INTERNET_EXPLORER;
        }else if (browserName.equalsIgnoreCase("chrome")){

            if (properties.getProperty("remote").equalsIgnoreCase("false")){
                driverPath("chrome");
                System.setProperty("webdriver.chrome.driver", driverPath);
            }else{
                Configuration.remote = properties.getProperty("gridUrl");//"http://100.64.15.255:4444/wd/hub";

            }

            Configuration.browser = (System.getProperty("browser") == null) ? "chrome" : System.getProperty("browser");
        }




        Configuration.driverManagerEnabled = false;
//        Configuration.remote = System.getProperty("hub");
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

//        TestBase testBase= new TestBase();
        Properties properties=getProperty();

//        testBase.beforeSuite(context,properties.getProperty("TestBase.ENV"),
//                properties.getProperty("moduleId"),
//                properties.getProperty("serviceId"),
//                properties.getProperty("gridUrl"),
//                properties.getProperty("testEnabled"),
//                "");

        if (System.getProperty("buildName") != null && !System.getProperty("buildName").isEmpty())
            context.getSuite().getXmlSuite().setName(System.getProperty("buildName"));
        else
            context.getSuite().getXmlSuite().setName("Suite");

        ((TestRunner) context).getTest().setName("Tests");


        Iterator var8 = context.getSuite().getAllMethods().iterator();

//        while(var8.hasNext()) {
//            ITestNGMethod method = (ITestNGMethod)var8.next();
//            method.setRetryAnalyzer(new ReTryTestCase());
//        }

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
        int SUCCESS = 1;
        int FAILURE = 2;
        int SKIP = 3;
        int SUCCESS_PERCENTAGE_FAILURE = 4;
        int STARTED = 16;
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
        //        System.out.println("Test Annotations: " + testResult.getMethod().getMethod().getDeclaredAnnotation(org.testng.annotations.Test.class).toString());
        testResults += "///////////////////////////////////////////////////////";
        testResults += "///////////////////////////////////////////////////////";

        //Parallelde hatası vermemesi WebDriverRunner.closeWebDriver() eklendi.
        //login da WebDriverRunner.clearBrowserCache(); eklendi
        //Selenide.close();
        //WebDriverRunner.getAndCheckWebDriver().quit();
        log.info(testResults);

        try {
            Selenide.close();
            //WebDriverRunner.getWebDriver().quit();
            //WebDriverRunner.closeWebDriver();
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
    public void beforeClass() throws  UnsupportedEncodingException {

        Annotation[] annotations = this.getClass().getAnnotations();
        if (annotations.length > 0) {
            //this.testNameFromXml = ((Name)annotations[0]).testName();
            this.testNameFromXml = this.getClass().getName();
        } else {
            this.testNameFromXml = this.getClass().getName();
        }

        parentFeatureMap.put(this.testNameFromXml, this.parentFeatureId);
    }

    @Step("Test Numarası : {testid} {status} ")
    public void testStatus(String testid, String status) {
    }

    @Step("{name} : {description}")
    public void step(String name, String description) {
    }

    public Properties getProperty() throws IOException {

        Properties properties= new Properties();

        try {

            properties.load(new FileInputStream("src/main/resources/config.properties"));

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        return properties;
    }


}
