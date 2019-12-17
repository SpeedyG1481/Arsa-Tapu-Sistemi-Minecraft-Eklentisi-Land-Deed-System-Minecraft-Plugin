package com.speedyg.basicclaim.mesajlar;

import com.speedyg.basicclaim.Claim;

public final class Mesajlar {

	public static String alan_kiralanmis;
	public static String alim_basarili;
	public static String kapasite_yetersiz;
	public static String paran_yok;
	public static String yetkin_yok;
	public static String arsa_girisin_banli;
	public static String sag_tik;
	public static String sol_tik;
	public static String aktivite_engelli_ekip;
	public static String genel_aktivite_engeli;
	public static String arsa_iade_edildi;
	public static String banli_dunya;
	public static String arsa_silinmis;
	public static String arsa_giris_basarili;
	public static String arsaniza_isinlandiniz;
	public static String arsaniz_silindi;
	public static String hatali_secim;
	public static String kullanim_suresi;
	public static String arsadan_ayrildiniz;
	public static String arsa_silindi_admin;
	public static String min_blok_gecersiz;
	public static String bu_arsa_ekip_dolu;
	public static String istek_basariyla_gonderildi;
	public static String istek_basarisiz;
	public static String max_yasakli_oyuncu;
	public static String yasaklama_basarili;
	public static String zaten_yasaklanmis;
	public static String oyuncu_arsasina_isinlandiniz;

	public static void mesajlariYukle() {
		alan_kiralanmis = Claim.main.getMesajAyar().getString("Alan_Zaten_Kiralanmis") != null
				? Claim.main.getMesajAyar().getString("Alan_Zaten_Kiralanmis").replaceAll("&", "§")
				: "§c§l!§7 Seçtiğiniz bölgede zaten satın alınmış bir arsa bulunuyor bulunuyor!";
		alim_basarili = Claim.main.getMesajAyar().getString("Basariyla_Satin_Aldiniz") != null
				? Claim.main.getMesajAyar().getString("Basariyla_Satin_Aldiniz").replaceAll("&", "§")
				: "§a§l!§7 Seçtiğiniz alanı başarıyla aldınız!";
		kapasite_yetersiz = Claim.main.getMesajAyar().getString("Kapasiteniz_Yetersiz") != null
				? Claim.main.getMesajAyar().getString("Kapasiteniz_Yetersiz").replaceAll("&", "§")
				: "§c§l!§7 Seçtiğin blokları satın alabilcek kadar arsa kapasiteniz kalmadı!";
		paran_yok = Claim.main.getMesajAyar().getString("Paran_Yetersiz") != null
				? Claim.main.getMesajAyar().getString("Paran_Yetersiz").replaceAll("&", "§")
				: "§c§l!§7 Alanı satın alabilmek için yeterli paraya sahip değilsiniz!";
		yetkin_yok = Claim.main.getMesajAyar().getString("Yetkin_Yok") != null
				? Claim.main.getMesajAyar().getString("Yetkin_Yok").replaceAll("&", "§")
				: "§c§l!§7 Yetkiniz yok!";
		arsa_girisin_banli = Claim.main.getMesajAyar().getString("Arsadan_Banlanmissin") != null
				? Claim.main.getMesajAyar().getString("Arsadan_Banlanmissin").replaceAll("&", "§")
				: "§c§l!§7 Bu arsaya giriş yetkiniz yok!";
		sag_tik = Claim.main.getMesajAyar().getString("Sag_Secim_Basarili") != null
				? Claim.main.getMesajAyar().getString("Sag_Secim_Basarili").replaceAll("&", "§")
				: "§c§l!§7 Seçim başarılı!";
		sol_tik = Claim.main.getMesajAyar().getString("Sol_Secim_Basarili") != null
				? Claim.main.getMesajAyar().getString("Sol_Secim_Basarili").replaceAll("&", "§")
				: "§c§l!§7 Seçim başarılı!";
		aktivite_engelli_ekip = Claim.main.getMesajAyar().getString("Ekip_Etkinlik_Engeli") != null
				? Claim.main.getMesajAyar().getString("Ekip_Etkinlik_Engeli").replaceAll("&", "§")
				: "§c§l!§7 Arsa sahibiniz §a" + "<arsasahibi>"
						+ " §7 tarafından bu arsada bu etkinliği yapmanız engellendi!";
		genel_aktivite_engeli = Claim.main.getMesajAyar().getString("Genel_Etkinlik_Engeli") != null
				? Claim.main.getMesajAyar().getString("Genel_Etkinlik_Engeli").replaceAll("&", "§")
				: "§c§l!§7 Bu arsada bunu yapamazsınız!";
		arsa_iade_edildi = Claim.main.getMesajAyar().getString("Arsa_Iade_Edildi") != null
				? Claim.main.getMesajAyar().getString("Arsa_Iade_Edildi").replaceAll("&", "§")
				: "§c§l!§7 Arsanız iade edildi!";
		banli_dunya = Claim.main.getMesajAyar().getString("Banli_Dunya") != null
				? Claim.main.getMesajAyar().getString("Banli_Dunya").replaceAll("&", "§")
				: "§c§l!§7 Bu dünyadan arsa kiralayamazsınız!";
		arsa_silinmis = Claim.main.getMesajAyar().getString("Arsa_Silinmis") != null
				? Claim.main.getMesajAyar().getString("Arsa_Silinmis").replaceAll("&", "§")
				: "§c§l!§7 Bu arsa silinmiş!";
		arsa_giris_basarili = Claim.main.getMesajAyar().getString("Arsa_Giris_Basarili") != null
				? Claim.main.getMesajAyar().getString("Arsa_Giris_Basarili").replaceAll("&", "§")
				: "§c§l! §3<sahip>§7 adlı oyuncunun arsasına başarıyla dahil oldun!";
		arsaniza_isinlandiniz = Claim.main.getMesajAyar().getString("Arsa_Isinlanma_Basarili") != null
				? Claim.main.getMesajAyar().getString("Arsa_Isinlanma_Basarili").replaceAll("&", "§")
				: "§c§l! §7Arsanıza ışınlandınız!";
		arsaniz_silindi = Claim.main.getMesajAyar().getString("Uzun_Sure_Silindi") != null
				? Claim.main.getMesajAyar().getString("Uzun_Sure_Silindi").replaceAll("&", "§")
				: "§c§l! §7Uzun süre giriş yapmadığınız için arsanız silindi!";
		hatali_secim = Claim.main.getMesajAyar().getString("Yanlis_Secim") != null
				? Claim.main.getMesajAyar().getString("Yanlis_Secim").replaceAll("&", "§")
				: "§c§l! §7Böyle bir arsa seçimi yapamazsınız!";
		kullanim_suresi = Claim.main.getMesajAyar().getString("Kullanim_Suresi") != null
				? Claim.main.getMesajAyar().getString("Kullanim_Suresi").replaceAll("&", "§")
				: "§c§l! §7Arsa kiralama aletini §3<sure>§7 saniyede bir defa kullanabilirsiniz!";
		arsadan_ayrildiniz = Claim.main.getMesajAyar().getString("Arsadan_Ayrildin") != null
				? Claim.main.getMesajAyar().getString("Arsadan_Ayrildin").replaceAll("&", "§")
				: "§c§l! §aBaşarıyla §3<sahip>§a adlı oyuncunun arsasından ayrıldınız!";
		arsa_silindi_admin = Claim.main.getMesajAyar().getString("Arsa_Silme_Basarili") != null
				? Claim.main.getMesajAyar().getString("Arsa_Silme_Basarili").replaceAll("&", "§")
				: "§c§l! §aİstediğiniz arsa başarıyla silindi!";
		min_blok_gecersiz = Claim.main.getMesajAyar().getString("Min_Blok_Gecersiz") != null
				? Claim.main.getMesajAyar().getString("Min_Blok_Gecersiz").replaceAll("&", "§")
				: "§c§l! §cBu kadar az blok içeren bir arsa kiralama olamaz!";
		bu_arsa_ekip_dolu = Claim.main.getMesajAyar().getString("Arsa_Ekip_Dolu") != null
				? Claim.main.getMesajAyar().getString("Arsa_Ekip_Dolu").replaceAll("&", "§")
				: "§c§l! §cBu arsaya alabileceğiniz maximum kişi sayısına ulaşmışsınız!";
		istek_basariyla_gonderildi = Claim.main.getMesajAyar().getString("Istek_Gonderildi") != null
				? Claim.main.getMesajAyar().getString("Istek_Gonderildi").replaceAll("&", "§")
				: "§c§l! §aIstek başarılı bir şekilde gönderildi!";
		istek_basarisiz = Claim.main.getMesajAyar().getString("Zaten_Istek_Gonderildi") != null
				? Claim.main.getMesajAyar().getString("Zaten_Istek_Gonderildi").replaceAll("&", "§")
				: "§c§l! §aBu kişiye zaten ekip daveti göndermişsiniz!";
		max_yasakli_oyuncu = Claim.main.getMesajAyar().getString("Arsa_Yasakli_Dolu") != null
				? Claim.main.getMesajAyar().getString("Arsa_Yasakli_Dolu").replaceAll("&", "§")
				: "§c§l! §aBu arsada yasaklayabileceğiniz maximum oyuncu sayısına ulaşmışsınız!";
		yasaklama_basarili = Claim.main.getMesajAyar().getString("Arsa_Yasaklama_Basarili") != null
				? Claim.main.getMesajAyar().getString("Arsa_Yasaklama_Basarili").replaceAll("&", "§")
				: "§c§l! §aBaşarılı bir şekilde §c<yasaklanan>§a adlı oyuncuyu arsanızdan uzaklaştırdınız!";
		zaten_yasaklanmis = Claim.main.getMesajAyar().getString("Zaten_Arsa_Yasaklama_Yapilmis") != null
				? Claim.main.getMesajAyar().getString("Zaten_Arsa_Yasaklama_Yapilmis").replaceAll("&", "§")
				: "§c§l! §cBu oyuncuyu zaten bu arsadan uzaklaştırmışsınız!";
		oyuncu_arsasina_isinlandiniz = Claim.main.getMesajAyar().getString("Oyuncu_Arsasina_Isinlandiniz") != null
				? Claim.main.getMesajAyar().getString("Oyuncu_Arsasina_Isinlandiniz").replaceAll("&", "§")
				: "§c§l! §aBaşarılı bir şekilde oyuncu arsasına ışınlandınız!";
	}

}
