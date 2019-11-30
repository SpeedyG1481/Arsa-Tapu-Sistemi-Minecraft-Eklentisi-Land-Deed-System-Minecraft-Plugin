package com.speedyg.obclaim.komut;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.speedyg.obclaim.Claim;
import com.speedyg.obclaim.mesajlar.Mesajlar;

public class Admin_Komut implements CommandExecutor {

	Claim main;

	public Admin_Komut(Claim claim) {
		this.main = claim;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("bclaim.admin")) {
			main.sistemleriYenile(sender);
		}else {
			sender.sendMessage(Mesajlar.yetkin_yok);
		}
		return false;
	}

}
