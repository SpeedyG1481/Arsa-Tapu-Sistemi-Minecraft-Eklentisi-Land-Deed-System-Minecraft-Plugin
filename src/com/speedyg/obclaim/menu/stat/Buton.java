package com.speedyg.obclaim.menu.stat;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.speedyg.obclaim.logo.Skull;

public class Buton {

	public static ItemStack geriDon() {
		ItemStack item = Skull.getCustomSkull("6e5b365c0a891c0c6a8582a8b193966c735ab201e6eea27709a04429b3ec322a");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§aGeri Dön");
		item.setItemMeta(imeta);
		return item;
	}

	public static ItemStack menuKapat() {
		ItemStack item = Skull.getCustomSkull("e93e2068617872c542ecda1d27df4ece91c699907bf327c4ddb85309412d3939");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cKapat");
		item.setItemMeta(imeta);
		return item;
	}

	public static ItemStack sonrakiSayfa() {
		ItemStack item = Skull.getCustomSkull("141ff6bc67a481232d2e669e43c4f087f9d2306665b4f829fb86892d13b70ca");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cSonraki Sayfa");
		item.setItemMeta(imeta);
		return item;
	}

	public static ItemStack oncekiSayfa() {
		ItemStack item = Skull.getCustomSkull("49b2bee39b6ef47e182d6f1dca9dea842fcd68bda9bacc6a6d66a8dcdf3ec");
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§cÖnceki Sayfa");
		item.setItemMeta(imeta);
		return item;
	}

}
