package com.speedyg.basicclaim.menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.menu.stat.Buton;
import com.speedyg.basicclaim.menu.stat.Calculate;
import com.speedyg.basicclaim.mesajlar.Mesajlar;

public class Claim_Listesi_Genel implements Listener {

	private Claim main;
	private Player p;
	private HashMap<Integer, Inventory> envanter = new HashMap<Integer, Inventory>();
	private int tut;
	private int toplam;
	private List<String> liste;
	private Calculate cal;

	public Claim_Listesi_Genel(Claim main, Player p) {
		this.main = main;
		this.p = p;
		this.tut = 0;
		this.cal = new Calculate(this.main);
		liste = cal.genelListeyiAl();
		this.toplam = ((liste.size() / 45) + 1);
		for (int i = 0; i < toplam; i++) {
			this.envanter.put(i, Bukkit.createInventory(null, 54, "§8§nKiralanan Arsalar"));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
	}

	public void menuAc() {
		this.itemleriMenuyeYukle();
		this.p.openInventory(envanter.get(tut));
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
			this.envanter.get(tut).setItem(a, okuyucu(this.liste.get(sira), cal.arsaBul(liste.get(sira)), sira));
			sira++;
		}

	}

	@SuppressWarnings("unchecked")
	private ItemStack okuyucu(String arsa, File xfile, int sira) {
		FileReader reader;
		JSONObject jo = null;
		try {
			reader = new FileReader(xfile);
			JSONParser oop = new JSONParser();
			jo = (JSONObject) oop.parse(reader);
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		String[] bol = arsa.split("&");
		String w = bol[0];
		int x1 = Integer.parseInt(bol[1].split(",")[0]);
		int y1 = Integer.parseInt(bol[1].split(",")[1]);
		int z1 = Integer.parseInt(bol[1].split(",")[2]);

		int x2 = Integer.parseInt(bol[2].split(",")[0]);
		int y2 = Integer.parseInt(bol[2].split(",")[1]);
		int z2 = Integer.parseInt(bol[2].split(",")[2]);
		ItemStack item = new ItemStack(Material.getMaterial("EMPTY_MAP"));
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§7Tapu Numarası: §a" + (sira + 1));
		imeta.setLore(Arrays.asList("", " §8• §7Tapu Sahibi; §a" + cal.getArsaSahibi(arsa),
				" §8• §7Lokasyon Bilgisi; §a" + w + "," + x1 + "," + y1 + "," + z1 + "|" + x2 + "," + y2 + "," + z2,
				" §8• §7Toplam Blok Sayısı; §a" + cal.blokSayisiBul(new Location(Bukkit.getWorld(w), x1, y1, z1),
						new Location(Bukkit.getWorld(w), x2, y2, z2)) + " Adet",
				" §8• §7Ekip Büyüklüğü; §a" + ((ArrayList<String>) jo.get("Ekip-Liste")).size() + " Kişi",
				" §8• §7Uzaklaştırılan Sayısı; §a" + ((ArrayList<String>) jo.get("Uzaklastirma-Liste")).size()
						+ " Kişi",
				(p.isOp() ? "§eBu arsaya ışınlanmak için sol tıklayın!" : ""),
				(p.isOp() ? "§eBu arsayı silmek için sağ tıklayın!" : "")));
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
								if (p.isOp()) {
									for (int i = 0; i < liste.size(); i++) {
										if (e.getCurrentItem().getItemMeta().getDisplayName()
												.equals(this.okuyucu(liste.get(i), cal.arsaBul(liste.get(i)), i)
														.getItemMeta().getDisplayName())) {
											if (e.getClick().equals(ClickType.RIGHT)
													|| e.getClick().equals(ClickType.SHIFT_RIGHT)) {
												Arsa_Admin_Sil_Onay_Menu menu = new Arsa_Admin_Sil_Onay_Menu(main, p,
														cal.arsaBul(liste.get(i)));
												menu.menuAc();
											} else if (e.getClick().equals(ClickType.LEFT)
													|| e.getClick().equals(ClickType.SHIFT_LEFT)) {
												World w = Bukkit.getWorld(this.liste.get(i).split("&")[0]);
												int x = Integer.parseInt(this.liste.get(i).split("&")[1].split(",")[0]);
												int y = Integer.parseInt(this.liste.get(i).split("&")[1].split(",")[1]);
												int z = Integer.parseInt(this.liste.get(i).split("&")[1].split(",")[2]);

												int x1 = Integer
														.parseInt(this.liste.get(i).split("&")[2].split(",")[0]);
												int y1 = Integer
														.parseInt(this.liste.get(i).split("&")[2].split(",")[1]);
												int z1 = Integer
														.parseInt(this.liste.get(i).split("&")[2].split(",")[2]);

												int kucukX = x < x1 ? x : x1;
												int kucukY = y < y1 ? y : y1;
												int kucukZ = z < z1 ? z : z1;

												int buyukX = x > x1 ? x : x1;
												int buyukY = y > y1 ? y : y1;
												int buyukZ = z > z1 ? z : z1;

												double isinlanX = (((kucukX + buyukX) / 2));
												double isinlanY = (kucukY + buyukY) / 2;
												double isinlanZ = (((kucukZ + buyukZ) / 2));
												Location loc = new Location(w, isinlanX, isinlanY, isinlanZ);
												p.teleport(loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5,
														0, 0.5));
												p.sendMessage(Mesajlar.oyuncu_arsasina_isinlandiniz);
											}
											break;
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

}
