package com.paytr.base;

import com.paytr.utils.ElementHelper;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.paytr.model.ElementInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.thoughtworks.gauge.ExecutionContext;


public class BaseTest {

    protected ElementHelper elementHelper;

    public BaseTest() throws IOException {
        elementHelper = new ElementHelper();
        String workingDir = System.getProperty("user.dir");
        List<File> fileList = ElementHelper.getFileList(workingDir + "/src");
        elementHelper.initMap(fileList);
    }

    public ElementInfo findElementInfoByKey(String key) {
        return elementHelper.findElementInfoByKey(key);
    }

    public By getElementInfoBy(ElementInfo elementInfo) {
        return elementHelper.getElementInfoBy(elementInfo);
    }


    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static Actions action;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    DesiredCapabilities capabilities;
    ChromeOptions chromeOptions;
    FirefoxOptions firefoxOptions;
    String browserName = "chrome";
    String selectPlatform = System.getProperty("os.name").toLowerCase().contains("win") ? "windows" : "mac";
    String testUrl = "https://www.paytr.com/";

    public ChromeOptions chromeOptions() { //chrome tarayıcısının ayarlarını özelleştirilmesi
        chromeOptions = new ChromeOptions();
        capabilities = DesiredCapabilities.chrome(); // chrome için özel yetenekler oluşturur
        Map<String, Object> prefs = new HashMap<>(); // tarayıcı tercihlerini bir map 'in içine atıyoruz
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--kiosk"); // tarayıcıyı tam ekran boyutunda başlatır
        chromeOptions.addArguments("--disable-notifications"); // web bildirimlerini devre dışı bırakır
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/web_driver/chromedriver.exe");//Selenium'un chrome tarayıcısını kontrol etmesi için, zorunlu
        chromeOptions.merge(capabilities); //DesiredCapalities nesnesini ChromeOptions ile birleştirir.
        return chromeOptions;

    }

    public FirefoxOptions firefoxOptions() { // firefox tarayıcısının ayarlarını özelleştirilmesi
        firefoxOptions = new FirefoxOptions();
        capabilities = DesiredCapabilities.firefox(); // firefox için özel yetenekler oluşturur
        Map<String, Object> preference = new HashMap<>(); // tarayıcı tercihlerini bir map 'in içine atıyoruz
        preference.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("preference", preference);
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--kiosk"); // tarayıcıyı tam ekran boyutunda başlatır
        chromeOptions.addArguments("--disable-notifications"); // web bildirimlerini devre dışı bırakır
        chromeOptions.addArguments("--start-fullscreen");
        System.setProperty("webdriver.firefox.driver", "web_driver/chromedriver"); //Selenium'un chrome tarayıcısını kontrol etmesi için, zorunlu
        firefoxOptions.merge(capabilities); //DesiredCapalities nesnesini ChromeOptions ile birleştirir.
        return firefoxOptions;
    }

    private void initializeDriver() {
        switch (browserName.toLowerCase()) {
            case "chrome":                                 //Eğer browserName chrome ise bana ChromeDriver nesnesi oluşturur.
                driver = new ChromeDriver(chromeOptions());
                break;
            case "firefox":
                driver = new FirefoxDriver(firefoxOptions());
                break;
            default:
                throw new IllegalStateException("Desteklenmeyen tarayıcı");
        }
    }

    private void setupBrowser() {
        if ("mac".equalsIgnoreCase(selectPlatform) || "windows".equalsIgnoreCase(selectPlatform)) {
            initializeDriver();
            driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS); //tarayıcının bekleyeceği max süre


            driver.get(testUrl);
            logger.info(testUrl + " adresi açılıyor.");

            if ("windows".equalsIgnoreCase(selectPlatform)) {
                action = new Actions(driver);
            }
        }

    }

    @BeforeScenario
    public void startSetup(ExecutionContext context) {
        String scenarioName = context.getCurrentScenario().getName();
        if (scenarioName == null || !scenarioName.toLowerCase().contains("api")) {
            try {
                logger.info("Cihazda " + selectPlatform + " ortamında " + browserName + " browserinda test ayağa kalkacak");
                setupBrowser();
            } catch (Exception e) {
                logger.info("Driver başlatılırken hata oluştu " + e.getMessage());
            }
        }
    }

    @AfterScenario
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}

                  /*                                                               ÖZET
                    Bu sınıf, testlerimizin koşmaya başlamadan önce tanımlamalar yaptığımız ve her testten önce gerçekleşmesini istediğimiz genel kuralları belirler.
                    Driver'ın ayağ kalkması
                    browser'ın seçilmesi ve modifiye edilmesi
                    Test senaryolarından sonra driver'ın kapatılması gibi işlemleri gerçekleştiriyoruz.
                  */