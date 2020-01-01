package com.speedyg.basicclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.logo.Skull;
import com.speedyg.basicclaim.menu.stat.Buton;
import com.speedyg.basicclaim.mesajlar.Mesajlar;

public class Arsa_Admin_Sil_Onay_Menu implements Listener {

	private Player p;
	private Claim main;
	private File arsa;
	private Inventory envanter;
	JSONObject jo = null;

	public Arsa_Admin_Sil_Onay_Menu(Claim main, Player p, File arsaBul) {
		this.main = main;
		this.p = p;
		this.arsa = arsaBul;
		this.envanter = Bukkit.createInventory(null, 18, "§8§lArsa Silme Onayı");
		FileReader reader;
		try {
			reader = new FileReader(this.arsa);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter);
	}

	private void itemleriMenuyeYukle() {
		this.envanter.setItem(2, this.reddet());
		this.envanter.setItem(4, this.bilgiButonu());
		this.envanter.setItem(13, Buton.menuKapat());
		this.envanter.setItem(6, this.onayla());

	}

	private ItemStack onayla() {
		ItemStack item = Skull.getCustomSkull("56665f823e0e08d0800c7223a7625f1114ad18b7df3fd2d67de7ad70a806ca84");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§aSilmeyi Onayla");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack reddet() {
		ItemStack item = Skull.getCustomSkull("9bb0bfe495fa8b6eb026ad3e6043caf7a8ac25ac48bc0ecc6d4b1d9b10ae34bd");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cSilmeyi Reddet");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack bilgiButonu() {
		ItemStack item = Skull.getCustomSkull("c0cf74e2638ba5ad32237a37b1d76aa123d18546e7eb9a6b9961e4bf1c3a919");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Bilgi");
		imeta.setLore(Arrays.asList("",
				" §8• §7Silinen Arsa; §e" + ((String) jo.get("Alan")).replaceAll("&", " ").replaceAll(",", " "),
				" §8• §7Arsa Sahibi; §e" + jo.get("Arsa-Sahibi"), ""));
		item.setItemMeta(imeta);
		return item;
	}

	@EventHandler
	private void tiklamaEtk(InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().equals(this.envanter)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null) {
					if (e.getCurrentItem().hasItemMeta()) {
						if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.reddet().getItemMeta().getDisplayName())) {
								Claim_Listesi_Genel menu = new Claim_Listesi_Genel(main, p);
								menu.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.onayla().getItemMeta().getDisplayName())) {
								if (main.sil(arsa, p)) {
									p.sendMessage(Mesajlar.arsa_silindi_admin);
									p.closeInventory();
								}
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
								p.closeInventory();
							}
						}
					}
				}

			}
		}
	}

}
