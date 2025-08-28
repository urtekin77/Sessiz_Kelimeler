# Sessiz Kelimeler - Mobil Tabanlı İşaret Dili Öğrenme Uygulaması

Sessiz Kelimeler, Türk İşaret Dili öğrenmek isteyen bireyler için geliştirilmiş, kullanıcı dostu ve etkileşimli bir mobil uygulamadır. Rekabetçi lig sistemi ve gamifikasyon öğeleriyle öğrenme sürecini daha eğlenceli ve motive edici hale getirir.

## 📱 Özellikler

### Temel Özellikler
- **Konu Bazlı Öğrenim**: Üniteler halinde düzenlenmiş işaret dili dersleri
- **İnteraktif Testler**: Video ve resim destekli çoktan seçmeli sorular
- **Genel Tekrar Sistemi**: Öğrenilen konuları pekiştirme testleri
- **Rekabetçi Lig Sistemi**: 8 farklı ligde kullanıcı sıralaması
- **Kapsamlı Sözlük**: Kelime, işaret ve alfabetik arama seçenekleri

### Gamifikasyon
- **Puan Sistemi**: Doğru cevaplara göre puan kazanma
- **Can Sistemi**: Her kullanıcıya 5 can hakkı (otomatik yenilenme)
- **Madalya Sistemi**: Aylık lig performansına göre altın, gümüş, bronz madalyalar
- **8 Farklı Lig**: Başlangıç seviyesinden Efsane Ligine kadar

### Kullanıcı Deneyimi
- **Otomatik Giriş**: Firebase Authentication ile güvenli oturum yönetimi
- **Şifre Kurtarma**: E-posta ile şifre sıfırlama
- **Profil Yönetimi**: Kullanıcı bilgileri güncelleme ve başarı takibi
- **Mesaj Sistemi**: Uygulama geliştiricilere geri bildirim gönderme

## 🛠️ Kullanılan Teknolojiler

| Kategori | Teknoloji | Versiyon | Açıklama |
|----------|-----------|----------|----------|
| **Geliştirme Platformu** | Android Studio | 4.0+ | Ana IDE |
| **Programlama Dili** | Java | JDK 8+ | Uygulama geliştirme dili |
| **Minimum SDK** | Android API | Level 21 (Android 5.0) | Desteklenen minimum versiyon |
| **Veritabanı** | Firebase Firestore | - | NoSQL bulut veritabanı |
| **Kimlik Doğrulama** | Firebase Authentication | - | Kullanıcı giriş sistemi |
| **Dosya Depolama** | Firebase Storage | - | Video/resim depolama |
| **UI Kütüphaneleri** | RecyclerView | - | Liste görünümü |
| **Navigasyon** | Fragment Navigation | - | Sayfa yönetimi |
| **Arka Plan İşlemleri** | WorkManager | - | Can yenileme, lig güncellemesi |
| **Mimari Pattern** | MVVM | - | Model-View-ViewModel |

## 📊 Veritabanı Koleksiyonları

| Koleksiyon | Amaç | Ana Alanlar |
|------------|------|-------------|
| **Kullanıcı** | Kullanıcı profili | E-posta, puan, lig, can |
| **İşaret** | İşaret dili içeriği | Video URL, açıklama, kategori |
| **Ünite** | Eğitim modülleri | Başlık, adım sayısı, zorluk |
| **Lig** | Lig bilgileri | Lig adı, renk, gereksinimler |
| **Madalya** | Başarı sistemi | Madalya türü, şartlar |
| **Alfabe** | İşaret alfabesi | Harf, görsel, video |
| **El** | El hareketleri | Hareket türü, açıklama |
| **Yer** | Vücut bölgeleri | Bölge adı, koordinatlar |
| **Mesajlar** | Geri bildirimler | Kullanıcı, mesaj, tarih |

## 📱 Ekran Görüntüleri

### Ana Menü ve Giriş Ekranları
<div align="center">
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/beklemeEkrani.png" alt="Bekleme Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/uyeKayit.png" alt="Üyelik Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/girisYap.png" alt="Giriş Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/sifreUnutma.png" alt="Şifre Unutma" width="150"/>
</div>

### Ana Sayfa 

<div align="center">
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/anaSayfa.png" alt="Ana Sayfa Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/uniteIcerik.png" alt="Ünite içerikleri" width="150"/>

</div>

### Test ve Quiz Ekranları
<div>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/quizEkran.png" alt="Quiz Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/genelTekrar1.png" alt="Quiz Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/canBitmesi.png" alt="Can Bitmesi Durumu" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/score.png" alt="Skor Ekranı" width="150"/>
</div>

### Sözlük ve Arama
<div>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/aramaSayfasiOrnek.png" alt="Arama Sayfası" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/alfabeSorgu.png" alt="Alfabe ile Sorgu" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/isaretSorgu.png" alt="İşareler ile Arama" width="150"/>
</div>

### Profil ve Lig Sistemi
<div align="center">
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/profil.png" alt="Profil Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/profilS2.png" alt="Profil Ekranı" width="150"/>
  <img src="https://github.com/urtekin77/Sessiz_Kelimeler/blob/master/images/ligGumus.png" alt="Lig Ekranı" width="150"/>

</div>



## 🚀 Kurulum

### Gereksinimler
- Android Studio 4.0+
- JDK 8+
- Android SDK 21+
- Firebase projesi

### Kurulum Adımları

1. **Projeyi klonlayın**
   ```bash
   git clone https://github.com/urtekin77/Sessiz_Kelimeler.git
   ```

2. **Android Studio'da açın**
   - Android Studio'yu açın
   - "Open an existing project" seçeneğini tıklayın
   - Klonladığınız proje klasörünü seçin

3. **Firebase Konfigürasyonu**
   - [Firebase Console](https://console.firebase.google.com)'da yeni proje oluşturun
   - Android uygulaması ekleyin
   - `google-services.json` dosyasını `app/` dizinine ekleyin

4. **Firebase Servislerini Etkinleştirin**
   - Authentication (Email/Password)
   - Cloud Firestore
   - Storage

5. **Bağımlılıkları Yükleyin**
   ```bash
   ./gradlew build
   ```

6. **Uygulamayı Çalıştırın**
   - Android cihaz veya emülatör bağlayın
   - Run butonuna tıklayın



## 📖 Kullanım

### İlk Kurulum
1. Uygulamayı açın
2. "Üye Ol" ile hesap oluşturun
3. E-posta ve güçlü şifre belirleyin
4. Giriş yapın ve öğrenmeye başlayın

### Öğrenme Süreci
1. **Ana Sayfa**: Üniteleri takip edin
2. **Test Çözme**: Her ünite 9 adımdan oluşur
3. **İlerleme**: Başarılı testlerle yeni üniteleri açın
4. **Tekrar**: Genel tekrar testleriyle bilginizi pekiştirin

### Sözlük Kullanımı
- **Kelime Arama**: Türkçe kelime yazarak ara
- **İşaret Arama**: El hareketi ve vücut bölgesine göre ara
- **Alfabetik Arama**: Harflere göre kategorize edilmiş arama

## 🏆 Lig Sistemi

### Ligler (Alt → Üst)
1. **Başlangıç Seviyesi** - Gri
2. **Bronz Ligi** - Bronz
3. **Gümüş Ligi** - Gümüş
4. **Altın Ligi** - Altın
5. **Elmas Ligi** - Mavi
6. **Master Ligi** - Mor
7. **Şampiyon Ligi** - Kırmızı
8. **Efsane Ligi** - Gökkuşağı

### Lig Kuralları
- Her ay ilk 3 kullanıcı üst lige çıkar
- Son 3 kullanıcı alt lige düşer
- Aylık puan sıfırlaması (mevcut puanın 1/4'ü kalır)
- Madalya sistemi: 1., 2., 3. sıra için özel madalyalar

## 🎯 Puan Sistemi

### Puan Kazanma
- **Doğru Cevap**: 10 + (kalan saniye × 2) puan
- **Lig Puanı**: (10 + kalan saniye × 2) × 2

### Puan Kaybetme
- **Yanlış Cevap**: -5 puan, -10 lig puanı, -1 can
- **Test Bırakma**: -1000 puan, -250 lig puanı

## 🔧 Katkıda Bulunma

1. Projeyi fork edin
2. Yeni branch oluşturun (`git checkout -b feature/YeniOzellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik eklendi'`)
4. Branch'inizi push edin (`git push origin feature/YeniOzellik`)
5. Pull Request oluşturun


## 📞 İletişim

- **Geliştirici**: Eda Nur Urtekin
- **E-posta**: urtekinedanur77@gmail.com
- **APk Linki**:[APK](https://github.com/user-attachments/files/16063379/APK.zip)
