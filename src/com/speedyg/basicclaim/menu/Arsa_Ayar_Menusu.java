package com.speedyg.basicclaim.menu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.logo.Skull;
import com.speedyg.basicclaim.menu.stat.Buton;
import com.speedyg.basicclaim.menu.stat.Calculate;
import com.speedyg.basicclaim.mesajlar.Mesajlar;

public class Arsa_Ayar_Menusu implements Listener {

	private Claim main;
	private Player p;
	private Inventory envanter;
	private String arsaAdi;
	private Calculate cal;
	private JSONObject jo = null;

	public Arsa_Ayar_Menusu(Claim main, Player p, String arsaAdi) {
		this.main = main;
		this.p = p;
		this.arsaAdi = arsaAdi;
		this.cal = new Calculate(this.main);
		this.envanter = Bukkit.createInventory(null, 45, "§8§nArsa Ayarları");
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
		FileReader reader = null;
		try {
			reader = new FileReader(cal.arsaBul(this.arsaAdi));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONParser oop = new JSONParser();
		try {
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter);

	}

	private void itemleriMenuyeYukle() {
		this.envanter.setItem(36, Buton.geriDon());
		this.envanter.setItem(44, Buton.menuKapat());
		this.envanter.setItem(39, this.ekipBilgiButonu());
		this.envanter.setItem(40, this.ekipListeneOyuncuEkle());
		this.envanter.setItem(41, this.genelBilgiButonu());
		this.envanter.setItem(22, sagSolEkipGenelButon());
		this.envanter.setItem(31, this.iadeButonu());
		int x = 0;
		int y = 5;
		FileReader reader = null;
		try {
			reader = new FileReader(cal.arsaBul(this.arsaAdi));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONParser oop = new JSONParser();
		JSONObject jo = null;
		try {
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		if (x < this.envanter.getSize() - 9) {
			for (Object o : jo.keySet()) {
				String icerik = (String) o;
				if (icerik.contains("Ekip") && !icerik.contains("Liste")) {
					this.envanter.setItem(x, this.okuyucu((boolean) jo.get(o), icerik));
					x++;
					if (x == 4)
						x = 9;
					else if (x == 13)
						x = 18;
					else if (x == 22)
						x = 27;
				} else if (icerik.contains("Genel")) {
					this.envanter.setItem(y, this.okuyucu((boolean) jo.get(o), icerik));
					y++;
					if (y == 9)
						y = 14;
					else if (y == 18)
						y = 23;
					else if (y == 27)
						y = 32;
				}
			}
		}

	}

	private ItemStack iadeButonu() {
		ItemStack item = Skull.getCustomSkull("e887cc388c8dcfcf1ba8aa5c3c102dce9cf7b1b63e786b34d4f1c3796d3e9d61");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§e§lCLAİM İADE");
		imeta.setLore(Arrays.asList("",
				"§7Satın aldığın alanı %" + (int) ((int) main.getConfig().getDouble("Iade-Yuzdesi")) + " ücretine",
				"§7geri iade edebilirsin.", "", "§8§l► §eİade etmek için tıkla."));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack sagSolEkipGenelButon() {
		ItemStack item = Skull.getCustomSkull("49ae88a7d03f474558a05692e5f5c3ade312ddf1072166ad0426334ef5174b87");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§e§lIŞINLAYICI");
		imeta.setLore(Arrays.asList("", "§7Arsana tam ortasına gitmek", "§7için bubutonu kullanabilirsin.", "",
				"§e► Tıkla ve arsana ışınlan!", ""));
		item.setItemMeta(imeta);
		return item;
	}

	@SuppressWarnings("unchecked")
	private ItemStack ekipListeneOyuncuEkle() {
		ItemStack item = Skull.getCustomSkull("63f4d555fb3b9357af7582f273800f1da4ac69b04210c3aa34e63db2a4235bbf");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eOyuncu Ayarları");
		imeta.setLore(Arrays.asList("", "§7Arsana eklemek istediğin", "§7oyuncuyu bu kısımdan ekliyebilir",
				"§7veya arsandan oyuncu yasaklayabilirsin.", "",
				" §8• §7Bu Arsa İçin Kalan Ekip Hakkın; §e"
						+ (cal.oyuncuMaxEkip(p) - (((ArrayList<String>) jo.get("Ekip-Liste"))).size()) + " Kişi",
				" §8• §7Bu Arsa İçin Kalan Yasaklama Hakkın; §e" + (cal.oyuncuMaxYasakliOyuncu(p)
						- (((ArrayList<String>) jo.get("Uzaklastirma-Liste"))).size()) + " Kişi",
				"", "§e► Tıkla ve ayarları yap!"));
		item.setItemMeta(imeta);
		return item;
	}

	@SuppressWarnings("unchecked")
	private ItemStack genelBilgiButonu() {
		ItemStack item = Skull.getCustomSkull("36505b1befba242170a46e8947b52aea54a59060f3e1c36f21cebb44690f8b0c");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName(" §8• §7Genel Oyuncu İzinleri");
		imeta.setLore(Arrays.asList("   §8► §7Blok Kırma; " + this.aktiflik((boolean) jo.get("Genel-Blok-Kirma")),
				"   §8► §7Blok Koyma; " + this.aktiflik((boolean) jo.get("Genel-Blok-Koyma")),
				"   §8► §7Item Atma; " + this.aktiflik((boolean) jo.get("Genel-Item-Atma")),
				"   §8► §7Item Alma; " + this.aktiflik((boolean) jo.get("Genel-Item-Alma")),
				"   §8► §7Balık Tutma; " + this.aktiflik((boolean) jo.get("Genel-Balik-Tutma")),
				"   §8► §7Hasar Verme; " + this.aktiflik((boolean) jo.get("Genel-Hasar-Verme")),
				"   §8► §7Konuşma; " + this.aktiflik((boolean) jo.get("Genel-Konusma-Etkinligi")),
				"   §8► §7Yatak Kullanımı; " + this.aktiflik((boolean) jo.get("Genel-Yatak-Kullanimi")),
				"   §8► §7Işınlanma; " + this.aktiflik((boolean) jo.get("Genel-Isinlanma")),
				"   §8► §7Portal Kullanımı; " + this.aktiflik((boolean) jo.get("Genel-Portal-Kullanimi")),
				"   §8► §7Aktivite; " + this.aktiflik((boolean) jo.get("Genel-Aktivite")), "",
				"   §8► §7Yasaklı Oyuncular; " + (ArrayList<String>) jo.get("Uzaklastirma-Liste")/**/));
		item.setItemMeta(imeta);
		return item;
	}

	@SuppressWarnings("unchecked")
	private ItemStack ekipBilgiButonu() {
		ItemStack item = Skull.getCustomSkull("d53fa1b57e4f784d16e5a2daa2f746b2ecfe624ccd74a4d4acc6a2e6a083f54e");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName(" §8• §7Ekip Oyuncuları İzinleri");
		imeta.setLore(Arrays.asList("   §8► §7Blok Kırma; " + this.aktiflik((boolean) jo.get("Ekip-Blok-Kirma")),
				"   §8► §7Blok Koyma; " + this.aktiflik((boolean) jo.get("Ekip-Blok-Koyma")),
				"   §8► §7Item Atma; " + this.aktiflik((boolean) jo.get("Ekip-Item-Atma")),
				"   §8► §7Item Alma; " + this.aktiflik((boolean) jo.get("Ekip-Item-Alma")),
				"   §8► §7Balık Tutma; " + this.aktiflik((boolean) jo.get("Ekip-Balik-Tutma")),
				"   §8► §7Hasar Verme; " + this.aktiflik((boolean) jo.get("Ekip-Hasar-Verme")),
				"   §8► §7Konuşma; " + this.aktiflik((boolean) jo.get("Ekip-Konusma-Etkinligi")),
				"   §8► §7Yatak Kullanımı; " + this.aktiflik((boolean) jo.get("Ekip-Yatak-Kullanimi")),
				"   §8► §7Işınlanma; " + this.aktiflik((boolean) jo.get("Ekip-Isinlanma")),
				"   §8► §7Portal Kullanımı; " + this.aktiflik((boolean) jo.get("Ekip-Portal-Kullanimi")),
				"   §8► §7Aktivite; " + this.aktiflik((boolean) jo.get("Ekip-Aktivite")), "",
				"   §8► §7Ekip Listen; " + (ArrayList<String>) jo.get("Ekip-Liste")/**/));
		item.setItemMeta(imeta);
		return item;
	}

	private String aktiflik(boolean tr) {
		return tr ? "§aAktif" : "§cDeaktif";
	}

	private ItemStack okuyucu(boolean tip, String ismi) {
		if (tip) {
			ItemStack item = Skull.getCustomSkull("1b67edb6ab2129a3b439dde98869e05af81c86d94815e37d31359bcd3ddf458d");
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName("§7" + ismi.replaceAll("-", " ") + " §cDeaktif Et");
			item.setItemMeta(imeta);
			return item;
		} else {
			ItemStack item = Skull.getCustomSkull("884e92487c6749995b79737b8a9eb4c43954797a6dd6cd9b4efce17cf475846");
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName("§7" + ismi.replaceAll("-", " ") + " §aAktif Et");
			item.setItemMeta(imeta);
			return item;
		}

	}

	@EventHandler
	private void tiklamaEtk(InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().equals(this.envanter)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null) {
					if (e.getCurrentItem().hasItemMeta()) {
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.geriDon().getItemMeta().getDisplayName())) {
							Claim_Listesi_Ozel menu = new Claim_Listesi_Ozel(main, p);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
							p.closeInventory();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.ekipListeneOyuncuEkle().getItemMeta().getDisplayName())) {
							Ekip_Menu menu = new Ekip_Menu(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.iadeButonu().getItemMeta().getDisplayName())) {
							Arsa_Sil_Onay menu = new Arsa_Sil_Onay(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.sagSolEkipGenelButon().getItemMeta().getDisplayName())) {
							World w = Bukkit.getWorld(this.arsaAdi.split("&")[0]);
							int x = Integer.parseInt(this.arsaAdi.split("&")[1].split(",")[0]);
							int y = Integer.parseInt(this.arsaAdi.split("&")[1].split(",")[1]);
							int z = Integer.parseInt(this.arsaAdi.split("&")[1].split(",")[2]);

							int x1 = Integer.parseInt(this.arsaAdi.split("&")[2].split(",")[0]);
							int y1 = Integer.parseInt(this.arsaAdi.split("&")[2].split(",")[1]);
							int z1 = Integer.parseInt(this.arsaAdi.split("&")[2].split(",")[2]);

							int kucukX = x < x1 ? x : x1;
							int kucukY = y < y1 ? y : y1;
							int kucukZ = z < z1 ? z : z1;

							int buyukX = x > x1 ? x : x1;
							int buyukY = y > y1 ? y : y1;
							int buyukZ = z > z1 ? z : z1;

							double isinlanX = (((kucukX + buyukX) / 2));
							double isinlanY = (kucukY + buyukY) / 2;
							double isinlanZ = (((kucukZ + buyukZ) / 2));
							Location loc = new Location(w, isinlanX, isinlanY, isinlanZ);
							p.teleport(loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5, 0, 0.5));
							p.sendMessage(Mesajlar.arsaniza_isinlandiniz);
						} else {
							FileReader reader = null;
							try {
								reader = new FileReader(cal.arsaBul(this.arsaAdi));
							} catch (FileNotFoundException ex) {
								ex.printStackTrace();
							}
							JSONParser oop = new JSONParser();
							JSONObject jo = null;
							try {
								jo = (JSONObject) oop.parse(reader);
								reader.close();
							} catch (IOException | ParseException xxx) {
								xxx.printStackTrace();
							}
							for (Object o : jo.keySet()) {
								String icerik = (String) o;
								if (jo.get(o) instanceof Boolean)
									if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
											this.okuyucu((boolean) jo.get(o), icerik).getItemMeta().getDisplayName())) {
										this.tipDegistir((boolean) jo.get(o), icerik, jo);
										this.menuAc();
									}
							}

						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void tipDegistir(boolean bool, String tip, JSONObject jo) {
		jo.put(tip, !bool);
		FileWriter writer;
		try {
			writer = new FileWriter(cal.arsaBul(arsaAdi));
			writer.write(jo.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
