# SOYO Tek Onay v1.8

Bu sürüm, gönderilen **Önerilen** ve **Murat sohbet** ekran görüntülerindeki düzene göre ayarlandı.

## Akış

1. Bir kez Android **Ayarlar → Erişilebilirlik → SOYO Tek Onay** servisini aç.
2. Yardımcı uygulamada mesaj şablonunu seç. Varsayılan: `{isim} {hitap} selamlarrrr`.
3. SOYO'yu aç ve **Önerilen** sekmesine gel.
4. Servis ekrandaki sarı **Sohbet** düğmelerini bulur ve aynı satırın solundaki kullanıcı adını eşleştirir.
5. İlk uygun satırın sohbetini açar.
6. Sohbet ekranında mesaj kutusuna örneğin `Murat Bey selamlarrrr` yazar.
7. Normal modda ekranın altındaki `ONAYLA` düğmesiyle gönderim yapılır.
8. **Tam Otomatik Mod** açıksa onay beklemeden gönderir, listeye geri döner, sıradaki kişiyi açar; görünür uygun kişi kalmadığında listeyi ileri kaydırır.
9. Tam Otomatik Mod ana ekrandaki kutudan anında açılıp kapatılabilir ve varsayılan olarak kapalıdır.

## v1.8 düzeltmeleri

- Tam Otomatik Mod açıkken `ONAYLA` penceresine düşme kaldırıldı. Gönder düğmesi geç oluşursa otomatik yeniden denenir ve mesaj gerektiğinde yeniden yazılır.
- Sohbetten çıkışta çift geri basma kaldırıldı. Listeye dönüş tamamlanana kadar yeni sohbet açılması kilitlenir.
- Kaydırma için `canPerformGestures` açıldı ve erişilebilirlik `scroll` eylemi çalışmazsa gerçek yukarı kaydırma gesture'ı eklendi.
- Sürüm `1.8` / `versionCode 9` olarak güncellendi.

## Ekran görüntülerine özel algılama

- İsim, `Sohbet` düğmesinin solunda ve aynı satırda aranır.
- `VIP1`, `VIP3`, `125km`, yaş değerleri, `Önerilen`, `Yeniler`, `Ana Sayfa`, `Mesaj` gibi yazılar isim kabul edilmez.
- Aynı servis oturumunda gönderilen/atlanmış kişi, Önerilenler'e dönüldüğünde hemen tekrar açılmaz.
- Tam otomatik modda gönderimden sonra listeye dönülür; ekrandaki kişiler işlendiğinde öneri alanı gerçek kaydırma hareketiyle yukarı kaydırılır. Erişilebilirlik listesi kaydırma desteği vermezse gesture yedeği devreye girer.
- Mesaj gönderilince sohbet ekranındaki sol üst geri oku bulunup **yalnızca bir kez** tıklanır. Ok erişilebilir değilse Android geri işlemi **tek kez** kullanılır; ikinci otomatik geri komutu gönderilmez.
- Listeye dönüşten sonra sıradaki işlenmemiş kişi otomatik açılır. Görünür aday kalmadığında öneri listesi ileri kaydırılır ve tarama sürer.
- Sohbet ekranında ad üst orta başlıktan da okunabilir; `Murat VIP2` tek metin olarak gelirse `VIP2` otomatik temizlenip `Murat` kullanılır.
- Alt kısımdaki gri mesaj alanı düzenlenebilir kutu olarak aranır.
- Gönder düğmesinin erişilebilirlik etiketi yoksa, mesaj kutusunun sağındaki tıklanabilir ikonlardan **en sağdaki kâğıt uçak konumuna** öncelik verilir; emoji ikonunun seçilmemesi için konum puanlaması uygulanır.
- `Turkey`, `Kişilik benzerliği`, hazır mesaj önerileri, `Çevrimiçi`, `VIP` ve sayısal rozetler isim kabul edilmez.
- Süslü Unicode adlar (ör. `𝓜𝓾𝓻𝓪𝓽`), emoji/taç çerçeveleri ve aksan süsleri sadeleştirilir; `[VIP3]`, `(Lv.12)`, `@etiket`, `#etiket`, rozet ve seviye metinleri mesaja yazılmaz.
- SOYO arayüzü ileride değişirse satır eşleştirme kurallarının yeniden ayarlanması gerekebilir.

## APK üretme

Projeyi Android Studio ile açıp **Build → Build APK(s)** kullanabilirsin.

Alternatif olarak GitHub'a yükleyip **Actions → Build Android APK → Run workflow** çalıştır. Çıktı artifact olarak `app-debug.apk` olur.

## Not

Erişilebilirlik servisleri Android tarafından güçlü izinler olarak değerlendirilir. Bu proje yalnızca `com.haflla.soulu` ve `com.haflla.soulu.lite` paketleriyle sınırlandırılmıştır.
