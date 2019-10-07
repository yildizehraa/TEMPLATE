package pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import common.BaseLibrary;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BaseLibrary {
    SelenideElement BTN_FLIGHT_XPATH = $(By.xpath("//button[@id='tab-flight-tab-hp']"));
    SelenideElement BTN_HOTEL_XPATH = $(By.xpath("//button[@id='tab-hotel-tab-hp']"));
    SelenideElement BTN_BUNDLE_AND_SAVE_XPATH = $(By.xpath("//button[@id='tab-package-tab-hp']"));
    SelenideElement TXT_GOING_TO_XPATH= $(By.xpath("//input[@class='clear-btn-input gcw-storeable text gcw-destination gcw-required ']"));
    SelenideElement TXT_CHECK_IN_XPATH=$(By.xpath("//input[@id='hotel-checkin-hp-hotel']"));
    SelenideElement BTN_RIGT_XPATH=$(By.xpath("//button[@class='datepicker-paging datepicker-next btn-paging btn-secondary next']"));
    SelenideElement BTN_FROM_MONTH_XPATH=$(By.xpath("//table[@class='datepicker-cal-weeks']//caption[text()='Aug 2020']"));
    SelenideElement TXT_CHECK_OUT_XPATH=$(By.xpath(" //input[@id='hotel-checkout-hp-hotel']"));

    SelenideElement BTN_FROMDAY_XPATH=$(By.xpath("(//tbody[@class='datepicker-cal-dates']//button[text()=' 15'])[1]"));
    SelenideElement BTN_TODAY_XPATH=$(By.xpath("(//tbody[@class='datepicker-cal-dates']//button[text()=' 1'])[2]"));
    SelenideElement BTN_SUBMIT_XPATH=$(By.xpath("(//button[@class='btn-primary btn-action  gcw-submit'])[1]"));
    SelenideElement LBL_SORT_XPATH=$(By.xpath("//legend[@id='sort-legend']"));

    @Step("Adres Bilgileri sayfası açılır.")
    public void open() {
        Configuration.baseUrl = ("https://www.expedia.com");
        WebDriverRunner.clearBrowserCache();
        Selenide.open("");
        maximazeBrowser();

    }


    @Step("Open Flight")
    public MainPage openFlight(){
        BTN_FLIGHT_XPATH.click();
        return this;
    }
    @Step("Open Hotel")
    public MainPage openHotel(){
        BTN_HOTEL_XPATH.click();
        return this;
    }
    @Step("Open Bunde And Save")
    public MainPage bundleAndSave(){
        BTN_BUNDLE_AND_SAVE_XPATH.click();
        return this;
    }
    @Step("Going To Hotel name,...")
    public MainPage going_to(){
        TXT_GOING_TO_XPATH.sendKeys(" Honolulu, Hawaii");
        return this;
    }

    @Step("Going To Data picker")
    public MainPage dataPickerCheckIn(){
        TXT_CHECK_IN_XPATH.click();
        do {
            BTN_RIGT_XPATH.click();
        }while(!(BTN_FROM_MONTH_XPATH.isDisplayed()));
        takeScreenshot();
        return this;
    }
    @Step("Going To Data picker")
    public MainPage chekinDay(){
        BTN_FROMDAY_XPATH.click();
        return this;
    }

    @Step("Going To Data picker")
    public MainPage chekoutDay(){
        TXT_CHECK_OUT_XPATH.click();
        BTN_TODAY_XPATH.click();
        return this;
    }

    @Step("Going To Data picker")
    public MainPage submit(){
        BTN_SUBMIT_XPATH.click();
      //  Assert.assertEquals(LBL_SORT_XPATH.isDisplayed(),"Hotel Serch sucsess");

        return this;
    }

}
