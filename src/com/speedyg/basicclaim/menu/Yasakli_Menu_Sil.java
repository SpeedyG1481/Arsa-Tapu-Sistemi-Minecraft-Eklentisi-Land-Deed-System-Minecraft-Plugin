package com.speedyg.basicclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.logo.Skull;
import com.speedyg.basicclaim.menu.stat.Buton;
import com.speedyg.basicclaim.menu.stat.Calculate;

public class Yasakli_Menu_Sil implements Listener {

	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private Player p;
	private Claim main;
	private String arsaNo;
	private List<String> liste = new ArrayList<String>();
	private int tut;
	private int toplam;
	private JSONObject jo = null;
	private File dosya;
	private Calculate cal;

	public Yasakli_Menu_Sil(Claim main, Player p, String arsaNO) {
		this.arsaNo = arsaNO;
		this.p = p;
		this.main = main;
		this.tut = 0;
		this.cal = new Calculate(this.main);
		dosya = cal.arsaBul(arsaNO);
		try {
			FileReader reader = new FileReader(dosya);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<String> ul = (List<String>) jo.get("Uzaklastirma-Liste");
		for (String s : ul)
			this.liste.add(s);
		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nYasaklı Çıkar"));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
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

	private ItemStack okuyucu(String player) {
		ItemStack item = Skull.getPlayerSkull(player);
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§c" + player);
		imeta.setLore(Arrays.asList("", "§eSilmek istediğiniz oyuncuya tıklayınız!"));
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
							Ekip_Menu menu = new Ekip_Menu(main, p, this.arsaNo);
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
									p.sendMessage("§a§l!§7 Arsanın uzaklaştırma listesinden §n" + liste.get(i)
											+ "§r§7 adlı oyuncu silindi!");
									oyuncuyuArsadanSil(liste.get(i));
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
	private void oyuncuyuArsadanSil(String string) {
		this.liste.remove(string);
		jo.put("Uzaklastirma-Liste", this.liste);
		FileWriter writer;
		try {
			writer = new FileWriter(dosya);
			writer.write(jo.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
