package com.speedyg.basicclaim.menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.logo.Skull;
import com.speedyg.basicclaim.menu.stat.Calculate;

public class Claim_Onayla implements Listener {

	private Claim main;
	private Player p;
	private Inventory envanter;
	private Location l1;
	private Location l2;
	private Calculate cal;

	public Claim_Onayla(Claim main, Player p, Location l1, Location l2) {
		this.main = main;
		this.p = p;
		this.l1 = l1;
		this.l2 = l2;
		this.cal = new Calculate(this.main);
		this.envanter = Bukkit.createInventory(null, 18, "§8§nArsa Alım Menü");
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter);

	}

	private void itemleriMenuyeYukle() {
		this.envanter.setItem(11, this.reddet());
		this.envanter.setItem(15, this.onayla());
		if (main.getConfig().getBoolean("Yer-Gog-Sistemi"))
			this.envanter.setItem(13, this.yerdenGoge());
		this.envanter.setItem(4, this.bilgiButonu());
	}

	private ItemStack bilgiButonu() {
		ItemStack item = Skull.getCustomSkull("c0cf74e2638ba5ad32237a37b1d76aa123d18546e7eb9a6b9961e4bf1c3a919");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Bilgi");
		imeta.setLore(Arrays.asList("",
				" §8• §7Satın Alınacak Arsa Lokasyonu; §b" + l1.getBlockX() + "," + l1.getBlockY() + ","
						+ l1.getBlockZ() + "|" + l2.getBlockX() + "," + l2.getBlockY() + "," + l2.getBlockZ(),
				" §8• §7Satın Alınacak Blok Sayısı; §b" + cal.blokSayisiBul(l1, l2) + " Adet",
				" §8• §7Satın Alım Maliyeti; §b" + (cal.maliyetBul(cal.blokSayisiBul(l1, l2))) + " TL",
				" §8• §7Ekonomi Durumunuz; " + (main.oyuncuParasiOgren(p) >= cal.maliyetBul(cal.blokSayisiBul(l1, l2))
						? "§a" + main.oyuncuParasiOgren(p)
						: "§c" + main.oyuncuParasiOgren(p)) + " TL"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack onayla() {
		ItemStack item = Skull.getCustomSkull("56665f823e0e08d0800c7223a7625f1114ad18b7df3fd2d67de7ad70a806ca84");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§aSATIN ALIMI ONAYLA");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack reddet() {
		ItemStack item = Skull.getCustomSkull("9bb0bfe495fa8b6eb026ad3e6043caf7a8ac25ac48bc0ecc6d4b1d9b10ae34bd");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cSATIN ALIMI REDDET");
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack yerdenGoge() {
		ItemStack item = Skull.getCustomSkull("8449b9318e33158e64a46ab0de121c3d40000e3332c1574932b3c849d8fa0dc2");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7YERDEN GÖĞE SATIN AL");
		item.setItemMeta(imeta);
		return item;
	}

	@EventHandler
	private void envanterKapatma(InventoryCloseEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().equals(this.envanter)) {
				if (e.getPlayer().equals(this.p)) {
					if (main.sagClick.get(p) != null)
						main.sagClick.remove(p);
					if (main.solClick.get(p) != null)
						main.solClick.remove(p);
				}
			}
		}
	}

	@EventHandler
	private void tiklamaEtk(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			if (e.getInventory() != null) {
				if (e.getInventory().equals(this.envanter)) {
					e.setCancelled(true);
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().hasItemMeta()) {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.onayla().getItemMeta().getDisplayName())) {
								main.oyuncuClaimIstedi(p);
								p.closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.reddet().getItemMeta().getDisplayName())) {
								if (main.sagClick.get(p) != null)
									main.sagClick.remove(p);
								if (main.solClick.get(p) != null)
									main.solClick.remove(p);
								p.closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.yerdenGoge().getItemMeta().getDisplayName())) {
								p.closeInventory();
								this.l1.setY(l1.getWorld().getMaxHeight());
								this.l2.setY(0);
								main.solClick.put(p, l1);
								main.sagClick.put(p, l2);
								Claim_Onayla menu = new Claim_Onayla(main, p, main.sagClick.get(p),
										main.solClick.get(p));
								menu.menuAc();
							}
						}
					}
				}
			}
		}
	}

}
