package com.speedyg.obclaim.menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.logo.Skull;
import com.speedyg.obclaim.menu.stat.Buton;
import com.speedyg.obclaim.menu.stat.Liste;

public class Arsa_Sil_Onay implements Listener {

	Inventory envanter;
	Player p;
	String arsa;
	Claim main;
	double iademiktari;

	public Arsa_Sil_Onay(Claim main, Player p, String arsa) {
		this.arsa = arsa;
		this.p = p;
		this.main = main;
		this.envanter = Bukkit.createInventory(null, 18, "§8§nSilme Onay Menüsü");
		String[] bol = arsa.split("&");
		String w = bol[0];
		int x1 = Integer.parseInt(bol[1].split(",")[0]);
		int y1 = Integer.parseInt(bol[1].split(",")[1]);
		int z1 = Integer.parseInt(bol[1].split(",")[2]);

		int x2 = Integer.parseInt(bol[2].split(",")[0]);
		int y2 = Integer.parseInt(bol[2].split(",")[1]);
		int z2 = Integer.parseInt(bol[2].split(",")[2]);
		iademiktari = (Liste
				.maliyetBul(Liste.blokSayisiBul(new Location(Bukkit.getWorld(w), x1, y1, z1),
						new Location(Bukkit.getWorld(w), x2, y2, z2)))
				* main.getConfig().getDouble("Iade-Yuzdesi") / 100);
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	private void tiklamaEtkinligi(InventoryClickEvent e) {
		if (e.getWhoClicked() != null)
			if (e.getWhoClicked().equals(this.p))
				if (e.getInventory() != null) {
					if (e.getInventory().equals(this.envanter)) {
						e.setCancelled(true);
						if (e.getCurrentItem() != null) {
							if (e.getCurrentItem().hasItemMeta()) {
								if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
									if (e.getCurrentItem().getItemMeta().getDisplayName()
											.equals(this.onayla().getItemMeta().getDisplayName())) {
										main.iadeEt(p, arsa, iademiktari, true);
										p.closeInventory();
									} else if (e.getCurrentItem().getItemMeta().getDisplayName()
											.equals(this.reddet().getItemMeta().getDisplayName())) {
										Arsa_Ayar_Menusu menu = new Arsa_Ayar_Menusu(main, p, arsa);
										menu.menuAc();
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
		imeta.setDisplayName("§aARSA İADESİNİ ONAYLA");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack reddet() {
		ItemStack item = Skull.getCustomSkull("9bb0bfe495fa8b6eb026ad3e6043caf7a8ac25ac48bc0ecc6d4b1d9b10ae34bd");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cARSA İADESİNİ REDDET");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack bilgiButonu() {
		ItemStack item = Skull.getCustomSkull("c0cf74e2638ba5ad32237a37b1d76aa123d18546e7eb9a6b9961e4bf1c3a919");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Bilgi");
		imeta.setLore(Arrays.asList("", "§7Arsa iadesinden alınacak para miktarı; §b" + iademiktari + " TL", ""));
		item.setItemMeta(imeta);
		return item;
	}

}
