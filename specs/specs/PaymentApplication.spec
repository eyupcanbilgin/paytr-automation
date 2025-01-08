PayTRTest
=========
     
Basarili Linkle Odeme Basvurusu
-------------------------------
tags: @paymentApplication @mainPage @EndToEnd @DataDriven

Scenario: Basarili Linkle Odeme Basvurusu Numara Kontrolu

* Çerezleri kabul et
* Sayfa doğru şekilde yüklendi mi kontrol et: "Sanal Ödeme Çözümleri & Fiziksel Ödeme Sistemleri | PayTR"
* Elementin metni "headerTitle" "Ödemeler İçin İhtiyacınız Olan Her Şey ." mi kontrol et
* "mainSections" elementindeki başlıkların toplam sayısı "12" mi kontrol et
* "urunleriMagazaPanelinizdenYonetin" başlığı mevcut mu kontrol et
* "gelistiricilerIcin" başlığı mevcut mu kontrol et
* "nedenBiziTercihEdiyorlar" başlığı mevcut mu kontrol et
* "isOrtaklari" başlığı mevcut mu kontrol et
* "sikcaSorulanSorular" başlığı mevcut mu kontrol et
* Menüye tıkla ve alt menü seçimini yap "productsTab" ve "linkleOdeme"
* Sayfa doğru şekilde yüklendi mi kontrol et: "Linkle Ödeme: Link Oluştur, Kolay ve Hızlı Ödeme Al | PayTR"
* Excel dosyasından form bilgileri okunur
* Dropdown seçimi yap "isletmeTipiDropdown" ve "Limited Şirket"
* Checkbox'a tıkla "aydinlatmaMetniCheckbox"
* Butona tıkla "onBasvuruButton"
* "isletmeUnvani" alanına "Test Şirketi" yaz
* "vergiNumarasi" alanına "1234567890" yaz
* "vergiDairesi" alanına "Kadıköy" yaz
* "aylikCiro" alanına "100000" yaz
* Butona tıkla "onBasvuruYapButton"
* "2" saniye bekle
* "basariliBasvuru" başlığı mevcut mu kontrol et
* "referansNumarasi" elementi içerisindeki referans numarasını dosyaya kaydet: "referansNumarasi.txt"
