package com.speedyg.obclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
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

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.logo.Skull;
import com.speedyg.obclaim.menu.stat.Buton;
import com.speedyg.obclaim.menu.stat.Liste;

public class Silinenler_Menu implements Listener {

	private Claim main;
	private Player p;
	private int tut = 0;
	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int toplam = 0;
	private List<File> liste;

	public Silinenler_Menu(Claim main, Player sender) {
		this.main = main;
		this.p = sender;
		liste = Liste.tumSilinenleriAl();
		this.toplam = (liste.size() / 45) + 1;
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nSilinen Arsalar"));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter.get(tut));

	}

	private void itemleriMenuyeYukle() {
		if (tut != 0)
			this.envanter.get(tut).setItem(45, Buton.oncekiSayfa());
		else
			this.envanter.get(tut).setItem(45, Buton.geriDon());

		if (tut < this.toplam - 1) {
			this.envanter.get(tut).setItem(53, Buton.sonrakiSayfa());
		} else {
			this.envanter.get(tut).setItem(53, Buton.menuKapat());
		}
		int sira = 45 * tut;
		for (int a = 0; a < this.envanter.get(tut).getSize() - 9; a++) {
			if (sira >= this.liste.size()) {
				break;
			}
			this.envanter.get(tut).setItem(a, okuyucu(liste.get(sira)));
			sira++;
		}
	}

	@SuppressWarnings("deprecation")
	private ItemStack okuyucu(File file) {
		JSONObject jo = null;
		try {
			FileReader reader = new FileReader(file);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		String tarihLong = file.getName().split("!")[0];
		Date tarih = new Date(Long.parseLong(tarihLong));
		ItemStack item = Skull.getCustomSkull("5d9c93f8b9f2f8f91aa4377551c2738002a78816d612f39f142fc91a3d713ad");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§e§lSİLİNME TARİHİ; §b§l" + tarih.toLocaleString());
		imeta.setLore(Arrays.asList(" ", " §8§l• §7Arsayı Silen; §e" + file.getName().split("!")[1],
				" §8§l• §7Arsayı Silen Yetkili Mi?; §e"
						+ (Boolean.parseBoolean(file.getName().split("!")[2]) == true ? "§aEvet" : "§cHayır"),
				"", " §8§l• §7Arsa Sahibi; §e" + jo.get("Arsa-Sahibi"), " §8§l• §7Alan Bilgisi; §e" + jo.get("Alan"),
				" §8§l• §7Ekip Listesi; §e" + jo.get("Ekip-Liste"),
				" §8§l• §7Uzaklaştırma Listesi; §e" + jo.get("Uzaklastirma-Liste"), " "));
		item.setItemMeta(imeta);
		return item;
	}

	@EventHandler
	private void tiklamaEtk(InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().equals(this.envanter.get(tut))) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null) {
					if (e.getCurrentItem().hasItemMeta()) {
						if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.geriDon().getItemMeta().getDisplayName())) {
								p.closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
								p.closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.oncekiSayfa().getItemMeta().getDisplayName())) {
								tut--;
								this.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.sonrakiSayfa().getItemMeta().getDisplayName())) {
								tut++;
								this.menuAc();
							}
						}
					}
				}
			}
		}
	}

}
