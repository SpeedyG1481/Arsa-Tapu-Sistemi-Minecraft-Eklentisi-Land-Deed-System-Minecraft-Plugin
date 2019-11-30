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

public class Yasakli_Menu_Ekle implements Listener {

	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int tut;
	private Player p;
	private Claim main;
	private int toplam;
	private List<Player> liste = new ArrayList<Player>();
	private String arsaAdi;
	private JSONObject jo = null;
	private File dosya;

	public Yasakli_Menu_Ekle(Claim main, Player p, String arsaAdi) {
		this.p = p;
		this.main = main;
		this.tut = 0;
		dosya = Liste.arsaBul(arsaAdi);
		this.arsaAdi = arsaAdi;
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
		for (Player x : Bukkit.getServer().getOnlinePlayers()) {
			if (p != x && !ul.contains(x.getName())) {
				this.liste.add(x);
			}
		}

		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nYasaklı Ekle"));
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

	private ItemStack okuyucu(Player player) {
		ItemStack item = Skull.getPlayerSkull(player.getName());
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§c" + player.getName());
		imeta.setLore(Arrays.asList("", "§eBu oyuncuyu arsanızdan uzaklaştırmak için tıklayın!"));
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
							Ekip_Menu menu = new Ekip_Menu(main, p, arsaAdi);
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
									oyuncuyuBanla(p, arsaAdi, liste.get(i));
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private void oyuncuyuBanla(Player p2, String arsaAdi, Player player) {
		@SuppressWarnings("unchecked")
		List<String> banlilar = (List<String>) jo.get("Uzaklastirma-Liste");
		if (banlilar.size() < Liste.oyuncuMaxYasakliOyuncu(p2)) {
			if (!banlilar.contains(player.getName())) {
				banlilar.add(player.getName());
				FileWriter writer;
				try {
					writer = new FileWriter(dosya);
					writer.write(jo.toString());
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				p2.sendMessage(Mesajlar.yasaklama_basarili);
			} else {
				p2.sendMessage(Mesajlar.zaten_yasaklanmis);
			}
		} else {
			p2.sendMessage(Mesajlar.max_yasakli_oyuncu);
		}
	}

}
