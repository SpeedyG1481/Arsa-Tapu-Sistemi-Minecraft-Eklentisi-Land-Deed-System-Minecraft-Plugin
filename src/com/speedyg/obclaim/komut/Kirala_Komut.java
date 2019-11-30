package com.speedyg.obclaim.komut;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.menu.Menu;

public class Kirala_Komut implements CommandExecutor {

	Claim main;

	public Kirala_Komut(Claim claim) {
		this.main = claim;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Menu menu = new Menu(main, p);
			menu.menuAc();
		}
		return false;
	}

}
