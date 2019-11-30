package com.speedyg.obclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
import com.speedyg.obclaim.mesajlar.Mesajlar;

public class Ekipten_Ayril_Menu implements Listener {

	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int tut;
	private Player p;
	private Claim main;
	private int toplam;
	private List<String> liste;

	public Ekipten_Ayril_Menu(Claim main, Player p) {
		this.main = main;
		this.p = p;
		this.tut = 0;
		this.liste = Liste.oyuncununEkipteOlduguArsalar(p);
		this.toplam = ((Liste.oyuncununEkipteOlduguArsalar(p).size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nEkip Arsaları"));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter.get(tut));
	}

	private void itemleriMenuyeYukle() {
		if (tut != 0) {
			this.envanter.get(tut).setItem(45, Buton.oncekiSayfa());
		} else {
			this.envanter.get(tut).setItem(45, Buton.geriDon());
		}
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
		JSONObject jo = null;
		File dosya = Liste.arsaBul(arsa);
		try {
			FileReader reader = new FileReader(dosya);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		ItemStack item = Skull.getCustomSkull("8eada85c17ae58637dae2b86f3f6966a77352abf214bc4493e65cf671c28a204");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Arsa Sahibi; §a" + Liste.getArsaSahibi(arsa));
		imeta.setLore(Arrays.asList("", " §8• §7Ekip; §a" + jo.get("Ekip-Liste"), "",
				"§eTıklayarak bu arsa ekibinden ayrılabilirsiniz!"));
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
									p.closeInventory();
									oyuncuyuArsadanSil(p, liste.get(i));
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void oyuncuyuArsadanSil(Player p2, String string) {
		JSONObject jo = null;
		File dosya = Liste.arsaBul(string);
		try {
			FileReader reader = new FileReader(dosya);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		List<String> liste = (List<String>) jo.get("Ekip-Liste");
		if (liste.contains(p2.getName()))
			liste.remove(p2.getName());
		jo.put("Ekip-Liste", liste);
		FileWriter writer;
		try {
			writer = new FileWriter(dosya);
			writer.write(jo.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		p2.sendMessage(Mesajlar.arsadan_ayrildiniz.replaceAll("<sahip>", (String) jo.get("Arsa-Sahibi")));
	}

}
