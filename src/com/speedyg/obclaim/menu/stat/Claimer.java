package com.speedyg.obclaim.menu.stat;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Claimer {

	public static ItemStack claimItemi() {
		ItemStack item = new ItemStack(Material.getMaterial("GOLD_SPADE"));
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName("§5Arsa Alım Aleti");
		imeta.setLore(Arrays.asList("", "§7Satın almak istediğiniz alanın bir ucuna sağ diğer",
				"§7ucuna sol tıklayarak kullanabilirsiniz."));
		item.setItemMeta(imeta);
		return item;
	}

}
