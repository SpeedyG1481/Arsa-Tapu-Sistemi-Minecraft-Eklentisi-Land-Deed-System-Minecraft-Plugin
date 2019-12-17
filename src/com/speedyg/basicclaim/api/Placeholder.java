package com.speedyg.basicclaim.api;

import org.bukkit.OfflinePlayer;

import com.speedyg.basicclaim.Claim;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholder extends PlaceholderExpansion {

	private Claim main;
	private Claim_API api;

	public Placeholder(Claim lobi) {
		this.main = lobi;
		this.api = new Claim_API(this.main);
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "Creative Spects(YSÖ)";
	}

	@Override
	public String getIdentifier() {
		return "bclaims";
	}

	@Override
	public String getVersion() {
		return main.getDescription().getVersion();
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		long hak = 0;
		// %bclaims_player_ownedclaimblocks%
		if (identifier.contains("ownedclaimblocks")) {
			hak = api.getPlayerClaimBlocksSize(player);
		}
		// %bclaims_player_ownedclaims%
		if (identifier.contains("ownedclaims")) {
			hak = api.getPlayerClaimsSize(player);
		}

		// %bclaims_allremovedclaimssize%
		if (identifier.contains("allremovedclaimssize")) {
			hak = api.getRemovedClaimsSize();
		}

		// %bclaims_allclaimssize%
		if (identifier.contains("allclaimssize")) {
			hak = api.getAllClaims();
		}

		return String.valueOf(hak);
	}

}