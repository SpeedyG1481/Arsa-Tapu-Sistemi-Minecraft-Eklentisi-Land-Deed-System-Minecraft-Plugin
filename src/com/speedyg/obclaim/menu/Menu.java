package com.speedyg.obclaim.menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import com.speedyg.obclaim.menu.stat.Claimer;

public class Menu implements Listener {

	private Claim main;
	private Player p;
	private Inventory envanter;

	public Menu(Claim claim, Player p) {
		this.main = claim;
		this.p = p;
		this.envanter = Bukkit.createInventory(null, 36, "§8§nClaim Menü");
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter);
	}

	private void itemleriMenuyeYukle() {
		for (int i = 9; i < 18; i++)
			this.envanter.setItem(i, cam());
		this.envanter.setItem(20, this.varOlanBaskaClaimleriGoruntuler());
		this.envanter.setItem(21, this.varOlanKendiClaimleriGoruntuler());
		this.envanter.setItem(22, this.claimSatinAl());
		this.envanter.setItem(23, this.isteklerButonu());
		this.envanter.setItem(24, this.ekipButonu());
		this.envanter.setItem(31, Buton.menuKapat());
	}

	@SuppressWarnings("deprecation")
	private ItemStack cam() {
		ItemStack item = new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 15);
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName(" ");
		item.setItemMeta(imeta);
		return item;

	}

	private ItemStack varOlanKendiClaimleriGoruntuler() {
		ItemStack item = Skull.getCustomSkull("6d821092ce5e7557451c723a0341e90b93e0564e2b01481de81eea271f04c5b6");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eAlanların");
		imeta.setLore(Arrays.asList("", "§7Sunucudaki kiraladığın", "§7Bütün alanları bu kısımda",
				"§7kolaylıkla bulabilirsin.", "", "§e► Tıkla ve bilgi al"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack varOlanBaskaClaimleriGoruntuler() {
		ItemStack item = Skull.getCustomSkull("438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eTüm Alanlar");
		imeta.setLore(Arrays.asList("", "§7Sunucudaki kiralanan", "§7Bütün alanları bu kısımda",
				"§7kolaylıkla bulabilirsin.", "", "§e► Tıkla ve bilgi al"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack claimSatinAl() {
		ItemStack item = Skull.getCustomSkull("212a03a4c11b4d472472e7e4593d2e126a6259e33cc81f44eb05cf042d076967");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eAlan Satın Al");
		imeta.setLore(Arrays.asList("", "§7Sunucudaki herkes gibi", "§7Sende alan kolaylıkla", "§7Kiralayabilirsin.",
				"", "§e► Tıkla ve satın al"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack isteklerButonu() {
		ItemStack item = Skull.getCustomSkull("f8e6bdfae6b71dd2a1578511f987d592c0bacb8c793c6abd058c7af5872dd41c");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eAlan İstekler");
		imeta.setLore(Arrays.asList("", "§7Alanlarına katılmak", "§7İstediğin oyuncuların",
				"§7İsteklerini değerlendirebilirsin.", "", "§e► Tıkla ve değerlendir"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack ekipButonu() {
		ItemStack item = Skull.getCustomSkull("ad2c0cedfc32e3beee955cabd66fd4d765eeda3adcc84bc451c9fdbecf3cb7c2");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eEkip Arsaları");
		imeta.setLore(Arrays.asList("", "§7Ekip arkadaşlarının", "§7Kiraladığı ve sana yetki", "§7Verdikleri arsaları",
				"§7Kontrol edebilirsin.", "", "§e► Tıkla ve bilgi al"));
		item.setItemMeta(imeta);
		return item;
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
									.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
								e.getWhoClicked().closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.claimSatinAl().getItemMeta().getDisplayName())) {
								if (e.getWhoClicked().getInventory().contains(Claimer.claimItemi())) {
									e.getWhoClicked().sendMessage(
											"§a§l!§7 İstediğiniz alanın uçlarına sağ ve sol tık ile seçim yapınız.");
								} else {
									e.getWhoClicked().getInventory().addItem(Claimer.claimItemi());
									e.getWhoClicked().sendMessage("§a§l!§7 Arsa alım aleti envanterinize eklendi!");
									e.getWhoClicked().sendMessage(
											"§a§l!§7 İstediğiniz alanın uçlarına sağ ve sol tık ile seçim yapınız.");
								}
								e.getWhoClicked().closeInventory();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.varOlanBaskaClaimleriGoruntuler().getItemMeta().getDisplayName())) {
								Claim_Listesi_Genel menu = new Claim_Listesi_Genel(main, (Player) e.getWhoClicked());
								menu.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.varOlanKendiClaimleriGoruntuler().getItemMeta().getDisplayName())) {
								Claim_Listesi_Ozel menu = new Claim_Listesi_Ozel(main, (Player) e.getWhoClicked());
								menu.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.isteklerButonu().getItemMeta().getDisplayName())) {
								Istekler_Menu menu = new Istekler_Menu(main, p);
								menu.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(this.ekipButonu().getItemMeta().getDisplayName())) {
								Ekipten_Ayril_Menu menu = new Ekipten_Ayril_Menu(main, p);
								menu.menuAc();
							}
						}
					}
				}
			}
		}
	}

}
