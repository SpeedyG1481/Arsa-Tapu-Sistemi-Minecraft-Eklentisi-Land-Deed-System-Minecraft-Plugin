package com.speedyg.obclaim.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import com.speedyg.obclaim.menu.stat.Liste;

public class Claim_Listesi_Ozel implements Listener {

	private Claim main;
	private Player p;
	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int tut;
	private List<String> liste;
	private int toplam;

	public Claim_Listesi_Ozel(Claim main, Player p) {
		this.main = main;
		this.p = p;
		this.tut = 0;
		this.liste = Liste.oyuncuListesiniAl(p);
		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nKiraladığın Arsalar"));
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

		this.envanter.get(tut).setItem(49, this.bilgiButonu());
		int sira = 45 * tut;
		for (int a = 0; a < this.envanter.get(tut).getSize() - 9; a++) {
			if (sira >= this.liste.size()) {
				break;
			}
			this.envanter.get(tut).setItem(a,
					okuyucu(this.liste.get(sira), sira, Liste.claimSahibiBul(this.liste.get(sira))));
			sira++;
		}

	}

	private ItemStack bilgiButonu() {
		ItemStack item = Skull.getCustomSkull("47a374e21b81c0b21abeb8e97e13e077d3ed1ed44f2e956c68f63a3e19e899f6");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Bilgi");
		imeta.setLore(Arrays.asList("", " §8• §7Maximum Blok Hakkınız; §a" + Liste.oyuncuMaxClaim(p) + " Adet",
				" §8• §7Toplam Sahip Olduğunuz Blok; §a" + Liste.oyuncununToplamAttigiClaim(p) + " Adet",
				" §8• §7Kalan Blok Hakkınız; " + (Liste.oyuncuMaxClaim(p) - Liste.oyuncununToplamAttigiClaim(p) > 0
						? ("§a" + (Liste.oyuncuMaxClaim(p) - Liste.oyuncununToplamAttigiClaim(p)))
						: "§c" + (Liste.oyuncuMaxClaim(p) - Liste.oyuncununToplamAttigiClaim(p))) + " Adet",
				""));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack okuyucu(String string, int sira, UUID uuid) {
		String[] bol = string.split("&");
		String w = bol[0];
		int x1 = Integer.parseInt(bol[1].split(",")[0]);
		int y1 = Integer.parseInt(bol[1].split(",")[1]);
		int z1 = Integer.parseInt(bol[1].split(",")[2]);

		int x2 = Integer.parseInt(bol[2].split(",")[0]);
		int y2 = Integer.parseInt(bol[2].split(",")[1]);
		int z2 = Integer.parseInt(bol[2].split(",")[2]);
		ItemStack item = new ItemStack(Material.getMaterial("EMPTY_MAP"));
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Tapu Numaranız: §a" + (sira + 1));
		imeta.setLore(Arrays.asList("",
				" §8• §7Toplam İçerdiği Blok; §a"
						+ String.valueOf((Liste.blokSayisiBul(new Location(Bukkit.getWorld(w), x1, y1, z1),
								new Location(Bukkit.getWorld(w), x2, y2, z2))))
						+ " Adet",
				" §8• §7Değeri; §a" + Liste.maliyetBul(Liste.blokSayisiBul(new Location(Bukkit.getWorld(w), x1, y1, z1),
						new Location(Bukkit.getWorld(w), x2, y2, z2))) + " TL",
				" §8• §7Lokasyon Bilgisi; §a" + w + "," + x1 + "," + y1 + "," + z1 + " - " + x2 + "," + y2 + "," + z2,
				"", "§eArsa ayarları için tıklayınız!"));
		item.setItemMeta(imeta);
		return item;
	}

	@EventHandler
	private void tiklamaEtk(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			if (e.getInventory() != null) {
				if (e.getInventory().equals(this.envanter.get(tut))) {
					e.setCancelled(true);
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().hasItemMeta()) {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.geriDon().getItemMeta().getDisplayName())) {
								Menu menu = new Menu(Claim.main, p);
								menu.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.sonrakiSayfa().getItemMeta().getDisplayName())) {
								tut++;
								this.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.oncekiSayfa().getItemMeta().getDisplayName())) {
								tut--;
								this.menuAc();
							} else if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
								p.closeInventory();
							} else {
								for (int i = 0; i < liste.size(); i++) {
									if (e.getCurrentItem().equals(
											okuyucu(this.liste.get(i), i, Liste.claimSahibiBul(this.liste.get(i))))) {
										Arsa_Ayar_Menusu menu = new Arsa_Ayar_Menusu(main, p, liste.get(i));
										menu.menuAc();
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
