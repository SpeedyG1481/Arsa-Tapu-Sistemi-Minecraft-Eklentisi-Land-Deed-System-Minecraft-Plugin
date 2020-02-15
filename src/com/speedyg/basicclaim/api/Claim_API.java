package com.speedyg.basicclaim.api;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.speedyg.basicclaim.Claim;
import com.speedyg.basicclaim.menu.stat.Calculate;

public final class Claim_API {

    private Claim main;
    private Calculate cal;

    public Claim_API() {
        this.main = Claim.main;
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

    public long getAllClaimsSize() {
        return cal.genelListeyiAl().size();
    }

    public String getClaimOwner(Location loc){
    	return cal.oyuncuLokasyondaMi(loc);
	}



}
