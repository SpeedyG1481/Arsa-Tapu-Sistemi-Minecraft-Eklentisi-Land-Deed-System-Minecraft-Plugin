package com.speedyg.obclaim.komut;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.menu.Silinenler_Menu;
import com.speedyg.obclaim.mesajlar.Mesajlar;

public class Silinenler_Komut implements CommandExecutor {
	Claim main;

	public Silinenler_Komut(Claim claim) {
		this.main = claim;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("bclaim.admin")) {
				Silinenler_Menu menu = new Silinenler_Menu(main, (Player) sender);
				menu.menuAc();
			} else {
				sender.sendMessage(Mesajlar.yetkin_yok);
			}
		}
		return false;
	}

}
