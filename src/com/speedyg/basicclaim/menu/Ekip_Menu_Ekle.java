package com.speedyg.basicclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import com.speedyg.basicclaim.mesajlar.Mesajlar;

public class Ekip_Menu_Ekle implements Listener {

	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int tut;
	private Player p;
	private Claim main;
	private int toplam;
	private List<Player> liste;
	private String arsaAdi;
	private File dosya;
	private JSONObject jo;
	private Calculate cal;

	public Ekip_Menu_Ekle(Claim main, Player p, String arsaAdi) {
		this.p = p;
		this.main = main;
		this.tut = 0;
		this.arsaAdi = arsaAdi;
		this.liste = new ArrayList<Player>();
		this.cal = new Calculate(this.main);
		dosya = cal.arsaBul(arsaAdi);
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
		List<String> ul = (List<String>) jo.get("Ekip-Liste");
		for (Player x : Bukkit.getServer().getOnlinePlayers()) {
			if (p != x && !ul.contains(x.getName())) {
				this.liste.add(x);
			}
		}

		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nEkip Ekle"));
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
		imeta.setLore(Arrays.asList("", "§eEkleme isteği göndermek için oyuncunun üzerine tıklayınız!"));
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
									oyuncuyuArsaTalebineEkle(p, arsaAdi, liste.get(i));
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private void oyuncuyuArsaTalebineEkle(Player p2, String arsaAdi2, Player player) {
		@SuppressWarnings("unchecked")
		int oyuncuarsa = ((List<String>) jo.get("Ekip-Liste")).size();
		if (oyuncuarsa < cal.oyuncuMaxEkip(p2)) {
			UUID isteyen = p2.getUniqueId();
			UUID istenen = player.getUniqueId();
			String istek = isteyen.toString() + ":" + arsaAdi2;
			List<String> oncekiIstekler;
			if (main.getConfig().getStringList("Arsa-Talepleri." + istenen.toString() + ".Istekler") != null) {
				oncekiIstekler = main.getConfig().getStringList("Arsa-Talepleri." + istenen.toString() + ".Istekler");
			} else {
				oncekiIstekler = new ArrayList<String>();
			}
			if (!oncekiIstekler.contains(istek)) {
				oncekiIstekler.add(istek);
				main.getConfig().set("Arsa-Talepleri." + istenen + ".Istekler", oncekiIstekler);
				main.saveConfig();
				p2.sendMessage(Mesajlar.istek_basariyla_gonderildi.replaceAll("<gonderilen>", player.getName()));
			} else {
				p2.sendMessage(Mesajlar.istek_basarisiz);
			}
		} else {
			p2.sendMessage(Mesajlar.bu_arsa_ekip_dolu);
		}
	}

}
