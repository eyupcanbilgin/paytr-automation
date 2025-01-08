# PayTR Test Automation Framework

## Proje Hakkında
PayTR Test Automation Framework, **Gauge** tabanlı bir otomasyon framework'üdür. Bu proje, PayTR web uygulamasının testlerini kolaylaştırmak ve farklı senaryoları dinamik olarak çalıştırabilmek için tasarlanmıştır. Test adımları, JSON formatında tanımlanan element locator'ı kullanılarak çalışr ve Excel dosyalarından alınan dinamik veri ile desteklenir.

---

## Kullanılan Teknolojiler

- **Gauge**: Test senaryolarını kolay ve okunabilir bir şekilde yazmak için.
- **Selenium**: Web elementlerini bulmak ve etkileşimde bulunmak için.
- **Java**: Kodlama dili olarak.
- **JSON**: Element locator'ını dinamik olarak saklamak için.
- **Excel**: Test verilerini saklamak ve okumak için.
- **Log4j**: Loglama için.

---

## Proje Yapısı

```plaintext
├── src
│   ├── test
│   │   ├── java
│   │   │   ├── com.paytr
│   │   │   │   ├── base
│   │   │   │   │   └── BaseTest.java
│   │   │   │   ├── model
│   │   │   │   │   └── ElementInfo.java
│   │   │   │   ├── stepImp
│   │   │   │   │   └── FrontendSteps.java
│   │   │   │   ├── utils
│   │   │   │   │   ├── ExcelHelper.java
│   │   │   │   │   └── ElementHelper.java
│   │   ├── resources
│   │   │   ├── elementValues
│   │   │   │   └── paytr.json
│   │   │   ├── formData.xlsx
│   │   │   ├── log4j.properties
│   │   │   └── referansNumarasi.txt
```

---

## Projede Yer Alan Sınıflar

### 1. **BaseTest.java**
- Selenium WebDriver nesnesini başlatır.
- Tarayıcı konfigürasyonlarını yönetir.

### 2. **FrontendSteps.java**
- Gauge senaryolarında kullanılan adımların implementasyonunu içerir.
- JSON dosyasından element bilgilerini okur.
- Web elementlerini bulur, veri girer, tıklama yapar ve doğrulamalar gerçekleştirir.

### 3. **ElementInfo.java**
- JSON formatındaki locator bilgilerini saklamak için bir model sınıfıdır.
- Locator türünü (`css`, `xpath`, `id`) ve locator değerini içerir.

### 4. **ExcelHelper.java**
- Excel dosyalarını okuyarak dinamik veri sağlar.
- Test verilerinin dış kaynaklı bir dosyadan çekilmesini destekler.

### 5. **paytr.json**
- Element locator'ını saklar.
- Örnek:

```json
{
  "key": "acceptCookiesButton",
  "type": "css",
  "value": ".accept-cookie-btn"
}
```

---

## Kurulum ve Çalıştırma Talimatları

1. **Gereksinimler:**
    - Java 11 veya üzeri
    - Maven
    - ChromeDriver (Chrome tarayıcı sürümüne uygun)

2. **Depoyu Klonlayın:**
   ```bash
   git clone https://github.com/eyupcanbilgin/paytr-automation.git
   cd paytr-automation
   ```

3. **Bağımlılıkları Kurun:**
   ```bash
   mvn clean install
   ```

4. **Testleri Çalıştırın:**
   ```bash
   gauge run specs
   ```

5. **Sonuçları İnceleyin:**
    - `reports/html-report/index.html` dosyasını tarayıcıda açarak test sonuçlarını görüntüleyebilirsiniz.

---

## Önemli Gauge Adımları

1. **Çerezleri Kabul Et:**
   ```
   * Çerezleri kabul et
   ```

2. **Sayfanın Yüklenmesini Kontrol Et:**
   ```
   * Sayfa doğru şekilde yüklendi mi kontrol et: "Sanal Ödeme Çözümleri & Fiziksel Ödeme Sistemleri | PayTR"
   ```

3. **Form Alanlarını Doldur:**
   ```
   * "isletmeUnvani" alanına "Test Şirketi" yaz
   * "vergiNumarasi" alanına "1234567890" yaz
   * "aylikCiro" alanına "100000" yaz
   ```

4. **Başarı Mesajını Kontrol Et:**
   ```
   * "basariliBasvuru" başlığı mevcut mu kontrol et
   ```

5. **Referans Numarasını Kaydet:**
   ```
   * "referansNumarasi" elementi içerisindeki referans numarasını dosyaya kaydet: "referansNumarasi.txt"
   ```

---

## Test Senaryosu

### **Senaryo: Başarılı Linkle Ödeme Başvurusu Numara Kontrolü**

```gauge
Scenario: Basarili Linkle Odeme Basvurusu Numara Kontrolu
* Çerezleri kabul et
* Sayfa doğru şekilde yüklendi mi kontrol et: "Sanal Ödeme Çözümleri & Fiziksel Ödeme Sistemleri | PayTR"
* Elementin metni "headerTitle" "Ödemeler İçin İhtiyacınız Olan Her Şey." mi kontrol et
* "mainSections" elementindeki başlıkların toplam sayısı "12" mi kontrol et
* "urunleriMagazaPanelinizdenYonetin" başlığı mevcut mu kontrol et
* Menüye tıkla ve alt menü seçimini yap "productsTab" ve "linkleOdeme"
* Excel dosyasından form bilgileri okunur
* Dropdown seçimi yap "isletmeTipiDropdown" ve "Limited Şirket"
* Checkbox'a tıkla "aydinlatmaMetniCheckbox"
* Butona tıkla "onBasvuruButton"
* "referansNumarasi" elementi içerisindeki referans numarasını dosyaya kaydet: "referansNumarasi.txt"
```

---

## Önemli Notlar

- **Dinamik Veri:**
    - Tüm veriler JSON veya Excel'den çekilerek dinamik hale getirilmiştir.

- **Loglama:**
    - Loglama işlemleri Log4j ile yapılmaktadır.

- **Error Handling:**
    - Her adımda oluşabilecek hatalar için `try-catch` blokları kullanılmıştır ve hatalar loglanmaktadır.

---

Bu framework, PayTR uygulamasının uçtan uca testlerini gerçekleştirmek ve kolay genişletilebilir bir yapı sunmak için tasarlanmıştır. Geri bildirimleriniz doğrultusunda geliştirme yapılabilir. Teşekküler!

