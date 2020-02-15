package com.speedyg.basicclaim.api;

import org.bukkit.OfflinePlayer;

import com.speedyg.basicclaim.Claim;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholder extends PlaceholderExpansion {

    private Claim_API api;

    public Placeholder() {
        this.api = new Claim_API();
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
        return Claim.main.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String hak = "";

        // %bclaims_player_ownedclaimblocks%
        if (identifier.contains("ownedclaimblocks")) {
            hak = "" + api.getPlayerClaimBlocksSize(player);
        }
        // %bclaims_player_ownedclaims%
        if (identifier.contains("ownedclaims")) {
            hak = "" + api.getPlayerClaimsSize(player);
        }

        // %bclaims_allremovedclaimssize%
        if (identifier.contains("allremovedclaimssize")) {
            hak = "" + api.getRemovedClaimsSize();
        }

        // %bclaims_allclaimssize%
        if (identifier.contains("allclaimssize")) {
            hak = "" + api.getAllClaimsSize();
        }

        // %bclaims_claimowner%
        if (player.isOnline())
            if (identifier.contains("claimowner")) {
                Player p = (Player) player;
                hak = api.getClaimOwner(p.getLocation());
            }

        return hak;
    }

}