package com.speedyg.obclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.menu.stat.Buton;
import com.speedyg.obclaim.menu.stat.Liste;
import com.speedyg.obclaim.mesajlar.Mesajlar;

public class Istekler_Menu implements Listener {

	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private Player p;
	private Claim main;
	private int tut;
	private List<String> liste;
	private int toplam;

	public Istekler_Menu(Claim main, Player p) {
		this.main = main;
		this.p = p;

		if (main.getConfig().getStringList("Arsa-Talepleri." + p.getUniqueId().toString() + ".Istekler") != null)
			this.liste = main.getConfig().getStringList("Arsa-Talepleri." + p.getUniqueId().toString() + ".Istekler");
		else
			this.liste = new ArrayList<String>();
		this.tut = 0;
		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nArsa İstekler"));
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
			this.envanter.get(tut).setItem(a, this.okuyucu(liste.get(sira)));
			sira++;
		}
	}

	private ItemStack okuyucu(String arsa) {
		ItemStack item = new ItemStack(Material.getMaterial("PAPER"));
		ItemMeta imeta = item.getItemMeta();
		Bukkit.broadcastMessage("" + arsa);
		imeta.setDisplayName("§7Gönderen; §a" + Liste.getArsaSahibi(arsa.split(":")[1]));
		imeta.setLore(Arrays.asList("",
				" §8• §7Arsa Lokasyonu; §e" + arsa.split(":")[1].replaceAll("-", " ").replaceAll("&", ""), "",
				"§eOnaylamak için sağ tık!", "§eReddetmek için sol tık!"));
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
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.oncekiSayfa().getItemMeta().getDisplayName())) {
							tut--;
							this.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.geriDon().getItemMeta().getDisplayName())) {
							Menu menu = new Menu(main, p);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.sonrakiSayfa().getItemMeta().getDisplayName())) {
							tut++;
							this.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
							p.closeInventory();
						} else {
							for (int i = 0; i < liste.size(); i++) {
								if (e.getCurrentItem().getItemMeta().getDisplayName()
										.equals(this.okuyucu(liste.get(i)).getItemMeta().getDisplayName())) {
									if (e.getClick().equals(ClickType.RIGHT)
											|| e.getClick().equals(ClickType.SHIFT_RIGHT)) {
										this.onayla(liste.get(i));
										p.closeInventory();
										p.sendMessage("§a§l!§7 İstek başarıyla kabul edildi!");
									} else if (e.getClick().equals(ClickType.LEFT)
											|| e.getClick().equals(ClickType.SHIFT_LEFT)) {
										this.sil(liste.get(i));
										p.closeInventory();
										p.sendMessage("§a§l!§7 İstek başarıyla reddedildi!");
									}
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private void onayla(String string) {
		String arsaTipi = string.split(":")[1];
		if (Liste.arsaBul(arsaTipi) != null) {
			File dosya = Liste.arsaBul(arsaTipi);
			JSONObject jo = null;
			try {
				FileReader reader = new FileReader(dosya);
				JSONParser oop = new JSONParser();
				jo = (JSONObject) oop.parse(reader);
				reader.close();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
			@SuppressWarnings("unchecked")
			List<String> ekip = (List<String>) jo.get("Ekip-Liste");
			if (!ekip.contains(p.getName())) {
				ekip.add(p.getName());
				FileWriter writer;
				try {
					writer = new FileWriter(dosya);
					writer.write(jo.toString());
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				p.sendMessage(Mesajlar.arsa_giris_basarili.replaceAll("<sahip>", Liste.getArsaSahibi(arsaTipi)));
			}
		} else {
			p.sendMessage(Mesajlar.arsa_silinmis);
		}
		this.sil(string);
	}

	private void sil(String string) {
		this.liste.remove(string);
		if (!liste.isEmpty())
			this.main.getConfig().set("Arsa-Talepleri." + p.getUniqueId().toString() + ".Istekler", liste);
		else
			this.main.getConfig().set("Arsa-Talepleri." + p.getUniqueId().toString() + ".Istekler", null);
		this.main.saveConfig();

	}

}
