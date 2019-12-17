package com.speedyg.basicclaim.api;

import org.bukkit.OfflinePlayer;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.menu.stat.Calculate;

public final class Claim_API {

	private Claim main;
	private Calculate cal;

	public Claim_API(Claim main) {
		this.main = main;
		cal = new Calculate(this.main);
	}

	public long getPlayerClaimBlocksSize(OfflinePlayer p) {
		return cal.oyuncununToplamAttigiClaim(p);
	}

	public int getPlayerClaimsSize(OfflinePlayer p) {
		return cal.oyuncuListesiniAl(p).size();
	}

	public int getRemovedClaimsSize() {
		return cal.tumSilinenleriAl().size();
	}

	public long getAllClaims() {
		return cal.genelListeyiAl().size();
	}

}
