package common;

import com.codeborne.selenide.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

/****************************************************
 * Tarih: 2018-12-05
 * Proje: MayaNext Functional Test Automation
 * Class: 
 * Yazan: Emre Sencan
 ****************************************************/
public class AllureEnvironmentUtils {

    public static void create() {

        FileOutputStream fos = null;

        try {
            Properties props = new Properties();
            File propFile = new File("allure-results");
            propFile.mkdir();
            propFile = new File("allure-results/environment.properties");
            propFile.createNewFile();
            fos = new FileOutputStream("allure-results/environment.properties");

            ofNullable(Configuration.baseUrl).ifPresent(s -> props.setProperty("url", s));
            ofNullable(Configuration.browser).ifPresent(s -> props.setProperty("browser", s));
            ofNullable(Configuration.remote).ifPresent(s -> props.setProperty("hub", s));
            ofNullable(Configuration.timeout).ifPresent(s -> props.setProperty("timeout", String.valueOf(s / 1000) + " sec."));
            /*ofNullable(getProperty("Selenide.browser")).ifPresent(s -> props.setProperty("Browser", s));
            ofNullable(getProperty("Selenide.baseUrl")).ifPresent(s -> props.setProperty("Base url", s));*/
            ofNullable(getenv("BUILD_URL")).ifPresent(s -> props.setProperty("Jenkins build URL", s));

            props.store(fos, "See https://github.com/allure-framework/allure-app/wiki/Environment");

            fos.close();
        } catch (IOException e) {
            System.out.println("IO problem when writing allure properties file: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
