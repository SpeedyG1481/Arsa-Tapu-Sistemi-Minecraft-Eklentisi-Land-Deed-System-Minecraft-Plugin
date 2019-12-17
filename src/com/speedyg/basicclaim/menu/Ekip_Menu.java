package com.speedyg.basicclaim.menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.logo.Skull;
import com.speedyg.basicclaim.menu.stat.Buton;

public class Ekip_Menu implements Listener {

	private Claim main;
	private Player p;
	private Inventory envanter;
	private String arsaAdi;

	public Ekip_Menu(Claim main, Player p, String arsa) {
		this.main = main;
		this.p = p;
		this.arsaAdi = arsa;
		this.envanter = Bukkit.createInventory(null, 9, "§8§nOyuncu Ayarları");
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(this.envanter);
	}

	private void itemleriMenuyeYukle() {
		this.envanter.setItem(0, Buton.geriDon());
		this.envanter.setItem(3, this.ekipOyuncuEkle());
		this.envanter.setItem(2, this.yasakliOyuncuEkle());
		this.envanter.setItem(5, this.ekipOyuncuSil());
		this.envanter.setItem(6, this.yasakliOyuncuSil());
		this.envanter.setItem(8, Buton.menuKapat());
	}

	private ItemStack ekipOyuncuSil() {
		ItemStack item = Skull.getCustomSkull("e72b4a5e3599106121faf93d4b2ca7da99840c0a80ae4b6d4a3aa5f5ac42cb0f");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eEkip Üyesi Çıkar");
		imeta.setLore(Arrays.asList("", "§7Arsanda olan ekip", "§7Arkadaşlarını bu kısımdan", "§7Çıkartabilirsin.", "",
				"§e► Tıkla ve çıkar"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack ekipOyuncuEkle() {
		ItemStack item = Skull.getCustomSkull("217b6a0677ef2f5ac4303182a4d6a6da8618ce7c026a4abecafcd75da5262c98");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eEkip Üyesi Ekle");
		imeta.setLore(Arrays.asList("", "§7Arsana ekliyeceğin", "§7Ekip arkadaşlarını bu ",
				"§7Kısımdan ekliyebilirsin.", "", "§e► Tıkla ve ekle"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack yasakliOyuncuSil() {
		ItemStack item = Skull.getCustomSkull("7682aba81cd3ddfd4e89675863634e5cf81fa0f495cebd3ac3efa3d5edb15");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eYasaklı Oyuncu Çıkar");
		imeta.setLore(Arrays.asList("", "§7Arsana yasakladığın", "§7Oyuncuyu bu kısımdan ",
				"§7Yasağını kaldırabilirsin.", "", "§e► Tıkla ve çıkar"));
		item.setItemMeta(imeta);
		return item;
	}

	private ItemStack yasakliOyuncuEkle() {
		ItemStack item = Skull.getCustomSkull("399c3bce75c59c6ec3a8e8265aed86980f4cda523e1922add6ceb5b9ef");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§eYasaklı Oyuncu Ekle");
		imeta.setLore(Arrays.asList("", "§7Arsana yasaklıyacağın", "§7Oyuncuyu bu kısımdan ", "§7Yasaklıyabilirsin.",
				"", "§e► Tıkla ve yasakla"));
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
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.ekipOyuncuEkle().getItemMeta().getDisplayName())) {
							Ekip_Menu_Ekle menu = new Ekip_Menu_Ekle(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.ekipOyuncuSil().getItemMeta().getDisplayName())) {
							Ekip_Menu_Cikar menu = new Ekip_Menu_Cikar(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.menuKapat().getItemMeta().getDisplayName())) {
							p.closeInventory();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.yasakliOyuncuEkle().getItemMeta().getDisplayName())) {
							Yasakli_Menu_Ekle menu = new Yasakli_Menu_Ekle(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(this.yasakliOyuncuSil().getItemMeta().getDisplayName())) {
							Yasakli_Menu_Sil menu = new Yasakli_Menu_Sil(main, p, arsaAdi);
							menu.menuAc();
						} else if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(Buton.geriDon().getItemMeta().getDisplayName())) {
							Arsa_Ayar_Menusu menu = new Arsa_Ayar_Menusu(main, p, arsaAdi);
							menu.menuAc();
						}
					}
				}
			}
		}
	}

}
