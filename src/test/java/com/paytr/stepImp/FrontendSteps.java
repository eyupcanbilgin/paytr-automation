package com.paytr.stepImp;

import com.paytr.utils.ExcelHelper;
import com.thoughtworks.gauge.Step;
import com.paytr.base.BaseTest;
import com.paytr.model.ElementInfo;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class FrontendSteps extends BaseTest {
    public FrontendSteps() throws IOException {

    }

    public By getElementInfoBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        }
        return by;
    }

    WebElement findElement(String key) {

        By by = getElementInfoBy(findElementInfoByKey(key));
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", element);
        return element;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoBy(findElementInfoByKey(key)));
    }

    private void clickTo(WebElement element) {
        element.click();
    }

    private void sendKeysTo(WebElement element, String text) {
        element.sendKeys(text);
    }

    public void javaScriptClickTo(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

    }

    public WebElement findElementWithWait(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 20); // Bekleme süresini örneğin 30 saniyeye çıkarın.
        By by = getElementInfoBy(findElementInfoByKey(key));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by)); // Elementin mevcut olmasını bekleyin.
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    @Step("Çerezleri kabul et")
    public void acceptCookies() {
        try {
            // Elementi bul ve bekle
            WebElement acceptCookiesButton = findElementWithWait("acceptCookiesButton"); // Görünür olmasını bekle ve bul

            // Tıkla
            acceptCookiesButton.click();
            logger.info("Çerezler başarıyla kabul edildi.");
        } catch (Exception e) {
            logger.error("Çerezleri kabul etme sırasında bir hata oluştu: " + e.getMessage());
            throw new RuntimeException("Çerezleri kabul etme sırasında bir hata oluştu!", e);
        }
    }

    @Step("Sayfa doğru şekilde yüklendi mi kontrol et: <key>")
    public void checkPageIsLoaded(String key) {
        // Tarayıcı başlığını al
        String pageTitle = driver.getTitle();

        // Tarayıcı başlığını verilen değerle kıyasla
        Assertions.assertEquals(key, pageTitle, "Sayfa başlığı beklenenle uyuşmuyor!");
        logger.info(key + " başlığı doğru şekilde yüklendi.");
    }

    @Step("Elementin metni <key> <expectedText> mi kontrol et")
    public void checkElementText(String key, String expectedText) {
        WebElement element = findElement(key); // Elementi bul
        String actualText = element.getText(); // Elementin gerçek metnini al
        Assertions.assertEquals(expectedText, actualText, key + " elementinin metni beklenenle uyuşmuyor!");
        logger.info(key + " elementi bulundu ve metni doğrulandı: " + actualText);
    }

    @Step("<key> elementindeki başlıkların toplam sayısı <expectedCount> mi kontrol et")
    public void checkElementCount(String key, int expectedCount) {
        List<WebElement> elements = findElements(key); // Element listesini bul
        int actualCount = elements.size(); // Element listesindeki öğe sayısını al
        Assertions.assertEquals(expectedCount, actualCount, key + " elementinin toplam sayısı beklenenle uyuşmuyor!");
        logger.info(key + " elementinde toplam " + actualCount + " başlık bulundu ve doğrulandı.");
    }

    @Step("Menüye tıkla ve alt menü seçimini yap <mainMenuKey> ve <subMenuKey>")
    public void clickMenuAndSubmenu(String mainMenuKey, String subMenuKey) {
        try {
            // Ana menüyü bul
            WebElement mainMenu = findElement(mainMenuKey);
            Actions actions = new Actions(driver);

            // Hover yaparak menüyü aç
            actions.moveToElement(mainMenu).perform();
            logger.info(mainMenuKey + " menüsüne hover yapıldı.");

            // Alt menüyü bekle ve tıkla
            WebDriverWait wait = new WebDriverWait(driver, 20); // 20 saniye bekleme
            WebElement submenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(findElementInfoByKey(subMenuKey).getValue())));
            submenu.click();
            logger.info(subMenuKey + " alt menüsüne tıklandı.");
        } catch (Exception e) {
            logger.error("Menü veya alt menü seçimi sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException("Menü veya alt menü tıklanamadı!", e);
        }
    }

    @Step("<key> başlığı mevcut mu kontrol et")
    public void checkSingleHeadingExists(String key) {
        WebElement element = findElement(key); // JSON'daki key'e göre elementi bul
        Assertions.assertTrue(element.isDisplayed(), key + " başlığı sayfada bulunamadı!"); // Görünürlüğü kontrol et
        logger.info(key + " başlığı sayfada mevcut.");
    }

    @Step("Excel dosyasından form bilgileri okunur")
    public void readFormDataFromExcel() throws IOException {
        String filePath = "src/test/resources/formData.xlsx"; // Excel dosyasının yolu
        String sheetName = "FormBilgileri"; // Sayfa adı
        Map<String, String> formData = ExcelHelper.readExcelData(filePath, sheetName); // Verileri oku

        // Logger ile bilgileri göster
        formData.forEach((key, value) -> logger.info("Key: " + key + ", Value: " + value));

        // Form alanlarını doldur
        sendKeysTo(findElement("adField"), formData.get("Yetkili Adı"));
        sendKeysTo(findElement("soyadField"), formData.get("Yetkili Soyadı"));
        sendKeysTo(findElement("emailField"), formData.get("Yetkili E-Posta"));
        sendKeysTo(findElement("telefonField"), formData.get("Yetkili Telefon"));
        sendKeysTo(findElement("websiteField"), formData.get("Website"));

        logger.info("Form bilgileri Excel'den başarıyla dolduruldu.");
    }

    @Step("Dropdown seçimi yap <dropdownKey> ve <value>")
    public void selectDropdownOption(String dropdownKey, String value) {
        try {
            // Dropdown'u bul ve tıkla
            WebElement dropdown = findElement(dropdownKey);
            dropdown.click();
            logger.info(dropdownKey + " dropdown'una tıklandı.");

            // Açılan menüde ilgili seçeneği bul ve tıkla
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'select-items')]/div[text()='" + value + "']")
            ));
            option.click();
            logger.info(value + " seçeneği dropdown'dan seçildi.");
        } catch (Exception e) {
            logger.error("Dropdown seçiminde hata oluştu: " + e.getMessage());
            throw new RuntimeException("Dropdown seçiminde hata oluştu!", e);
        }
    }

    @Step("Checkbox'a tıkla <checkboxKey>")
    public void clickCheckbox(String checkboxKey) {
        try {
            // JSON'daki locator'ı kullanarak dinamik bir yapı oluşturuyoruz
            WebElement checkbox = findElement(checkboxKey);

            // Checkbox görünür ve etkileşimli hale geldiğinde tıkla
            if (!checkbox.isSelected()) {
                checkbox.click();
                logger.info(checkboxKey + " checkbox'ı işaretlendi.");
            } else {
                logger.info(checkboxKey + " checkbox zaten işaretli.");
            }
        } catch (Exception e) {
            logger.error("Checkbox'a tıklama sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException("Checkbox'a tıklama sırasında hata oluştu!", e);
        }
    }

    @Step("Butona tıkla <buttonKey>")
    public void clickButton(String buttonKey) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 20);
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(findElement(buttonKey)));
            button.click();
            logger.info(buttonKey + " butonuna tıklandı.");
        } catch (Exception e) {
            logger.error("Butona tıklama sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException("Butona tıklama sırasında hata oluştu!", e);
        }
    }

    @Step("<key> alanına <value> yaz")
    public void fillField(String key, String value) {
        try {
            // JSON'dan element bilgilerini al
            ElementInfo elementInfo = findElementInfoByKey(key);

            // Elementi bul
            WebElement element = findElement(key);

            // Elemente değer yaz
            element.clear();
            element.sendKeys(value);

            logger.info(key + " alanına '" + value + "' yazıldı.");
        } catch (Exception e) {
            logger.error(key + " alanına yazma sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException(key + " alanına yazma sırasında hata oluştu!", e);
        }
    }

    @Step("<seconds> saniye bekle")
    public void waitForSeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000L); // Saniyeyi milisaniyeye çevirerek bekleme işlemi
        } catch (InterruptedException e) {
            logger.error("Bekleme sırasında hata oluştu: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Step("<key> elementi içerisindeki referans numarasını dosyaya kaydet: <fileName>")
    public void saveDynamicReferenceToFile(String key, String fileName) {
        try {
            // Elementi bul ve içeriği al
            WebElement element = findElement(key); // JSON'dan alınan key ile elementi buluyor
            String elementText = element.getText();

            // Referans numarasını ayıkla
            String referenceNumber = elementText.split(":")[1].trim();

            // Dosyaya yazma işlemi
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/" + fileName));
            writer.write(referenceNumber); // Sadece referans numarasını yazıyoruz
            writer.close();

            logger.info(key + " elementi içerisindeki referans numarası '" + referenceNumber + "' dosyaya kaydedildi: " + fileName);
        } catch (IOException e) {
            logger.error("Dosya yazma sırasında hata oluştu: " + e.getMessage());
            throw new RuntimeException("Dosya yazma sırasında hata oluştu!", e);
        }
    }

}