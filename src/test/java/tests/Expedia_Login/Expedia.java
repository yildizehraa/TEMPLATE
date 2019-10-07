package tests.Expedia_Login;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import pages.*;

import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.sleep;


@Feature("Expedia")
public class Expedia extends BaseTest {


    MainPage mainPage=new MainPage();



    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true, description = "TS0001 : Create Account")
    public void TS0001_CreateAccount(){

        mainPage.open();
        sleep(5000);
        takeScreenshot();

    }

}

