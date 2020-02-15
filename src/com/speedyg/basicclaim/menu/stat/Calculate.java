package com.speedyg.basicclaim.menu.stat;

import com.speedyg.basicclaim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class Calculate {

    public Claim main;

    public Calculate(Claim main) {
        this.main = main;
    }

    public List<String> genelListeyiAl() {
        List<String> don = new ArrayList<String>();
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles())
                    don.add(listes.getName().split("!")[1]);

        return don;
    }

    public File arsaBul(String arsa) {
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    if (listes.getName().split("!")[1].equals(arsa))
                        return listes;
                }
        return null;
    }

    public File arsaBul(UUID uuid) {
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    if (listes.getName().split("!")[0].equals(uuid.toString()))
                        return listes;
                }
        return null;
    }

    public List<String> oyuncuListesiniAl(OfflinePlayer p) {
        List<String> don = new ArrayList<String>();
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    if (listes.getName().split("!")[0].equals(p.getUniqueId().toString())) {
                        don.add(listes.getName().split("!")[1]);
                    }
                }
        return don;
    }

    public List<File> tumSilinenleriAl() {
        List<File> don = new ArrayList<File>();
        File file = new File(main.getDataFolder() + "/silinen-arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    don.add(listes);
                }
        return don;
    }

    public UUID claimSahibiBul(String s) {
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles())
                    if (listes.getName().split("!")[1].equals(s)) {
                        return UUID.fromString(listes.getName().split("!")[0]);
                    }
        return null;
    }

    public int blokSayisiBul(Location loc1, Location loc2) {
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        return (topBlockX - bottomBlockX != 0 ? (topBlockX - bottomBlockX + 1) : 1)
                * (topBlockY - bottomBlockY != 0 ? (topBlockY - bottomBlockY + 1) : 1)
                * (topBlockZ - bottomBlockZ != 0 ? (topBlockZ - bottomBlockZ + 1) : 1);
    }

    public boolean blokSayisiBul(Location loc1, Location loc2, int minCarpan) {
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        return (topBlockX - bottomBlockX) >= minCarpan && (topBlockZ - bottomBlockZ) >= minCarpan;
    }

    public String oyuncuLokasyondaMi(Location loc) {
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File dosyalar : file.listFiles()) {
                    String[] bol = dosyalar.getName().split("!")[1].split("&");
                    World w = Bukkit.getWorld(bol[0]);
                    int x1 = Integer.parseInt(bol[1].split(",")[0]);
                    int y1 = Integer.parseInt(bol[1].split(",")[1]);
                    int z1 = Integer.parseInt(bol[1].split(",")[2]);

                    int x2 = Integer.parseInt(bol[2].split(",")[0]);
                    int y2 = Integer.parseInt(bol[2].split(",")[1]);
                    int z2 = Integer.parseInt(bol[2].split(",")[2]);

                    int topBlockX = (Math.max(x1, x2));
                    int bottomBlockX = (Math.min(x1, x2));

                    int topBlockY = (Math.max(y1, y2));
                    int bottomBlockY = (Math.min(y1, y2));

                    int topBlockZ = (Math.max(z1, z2));
                    int bottomBlockZ = (Math.min(z1, z2));

                    if (w.getName().equals(loc.getWorld().getName()) && bottomBlockX <= loc.getBlockX()
                            && loc.getBlockX() <= topBlockX && bottomBlockY <= loc.getBlockY()
                            && loc.getBlockY() <= topBlockY && bottomBlockZ <= loc.getBlockZ()
                            && loc.getBlockZ() <= topBlockZ) {
                        return (dosyalar.getName().split("!")[1]);
                    }

                }

        return null;
    }

    public long oyuncununToplamAttigiClaim(final OfflinePlayer p) {
        long don = 0;
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    if (listes.getName().split("!")[0].equals(p.getUniqueId().toString())) {
                        String[] bol = listes.getName().split("!")[1].split("&");
                        World w = Bukkit.getWorld(bol[0]);

                        long x1 = Long.parseLong(bol[1].split(",")[0]);
                        long y1 = Long.parseLong(bol[1].split(",")[1]);
                        long z1 = Long.parseLong(bol[1].split(",")[2]);

                        long x2 = Long.parseLong(bol[2].split(",")[0]);
                        long y2 = Long.parseLong(bol[2].split(",")[1]);
                        long z2 = Long.parseLong(bol[2].split(",")[2]);
                        don += this.blokSayisiBul(new Location(w, x1, y1, z1), new Location(w, x2, y2, z2));
                    }
                }

        return don;
    }

    public String getArsaSahibi(final String arsa) {
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            for (File listes : file.listFiles())
                if (listes.getName().split("!")[1].equals(arsa)) {
                    try {
                        FileReader reader = new FileReader(listes);
                        JSONParser oop = new JSONParser();
                        JSONObject jo = (JSONObject) oop.parse(reader);
                        reader.close();
                        return (String) jo.get("Arsa-Sahibi");
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
        return null;
    }

    public long oyuncuMaxClaim(final Player p) {
        long hak = 57825;
        List<String> liste = main.getConfig().getStringList("Claim-Sinirlari");
        for (int i = liste.size() - 1; i > 0; i--) {
            if (p.hasPermission(liste.get(i).split(":")[0])) {
                hak = Long.parseLong(liste.get(i).split(":")[1]);
                return hak;
            }
        }

        if (hak == -1 || p.isOp())
            return Long.MAX_VALUE;
        return hak;
    }

    public short oyuncuMaxEkip(final Player p) {
        short hak = 5;
        if (main.getConfig().getConfigurationSection("Maximum") != null) {
            if (main.getConfig().getConfigurationSection("Maximum.Ekip") != null) {
                Set<String> aliste = main.getConfig().getConfigurationSection("Maximum.Ekip").getKeys(false);
                List<String> liste = new ArrayList<String>(aliste.size());
                for (String x : aliste) {
                    liste.add(x);
                }
                for (int i = liste.size() - 1; i > 0; i--) {
                    if (p.hasPermission(liste.get(i))) {
                        hak = (short) main.getConfig().getInt("Maximum.Ekip." + liste.get(i));
                        return hak;
                    }
                }
            }
        }
        if (hak == -1 || p.isOp())
            return Short.MAX_VALUE;
        return hak;
    }

    public short oyuncuMaxYasakliOyuncu(final Player p) {
        short hak = 5;
        if (main.getConfig().getConfigurationSection("Maximum") != null) {
            if (main.getConfig().getConfigurationSection("Maximum.Yasakli-Oyuncu") != null) {
                Set<String> aliste = main.getConfig().getConfigurationSection("Maximum.Yasakli-Oyuncu").getKeys(false);
                List<String> liste = new ArrayList<String>(aliste.size());
                for (String x : aliste) {
                    liste.add(x);
                }
                for (int i = liste.size() - 1; i > 0; i--) {
                    if (p.hasPermission(liste.get(i))) {
                        hak = (short) main.getConfig().getInt("Maximum.Yasakli-Oyuncu." + liste.get(i));
                        return hak;
                    }
                }
            }
        }
        if (hak == -1 || p.isOp())
            return Short.MAX_VALUE;
        return hak;
    }

    public double maliyetBul(final long blokSayisi) {
        double maliyet = 10;
        if (main.getConfig().getDouble("Blok-Fiyati") >= 0) {
            maliyet = main.getConfig().getDouble("Blok-Fiyati");
        }

        return (maliyet * blokSayisi);
    }

    public List<String> oyuncununEkipteOlduguArsalar(final Player p) {
        List<String> liste = new ArrayList<String>();
        File file = new File(main.getDataFolder() + "/arsalar");
        if (file.isDirectory())
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    try {
                        FileReader reader = new FileReader(listes);
                        JSONParser oop = new JSONParser();
                        JSONObject jo = (JSONObject) oop.parse(reader);
                        reader.close();
                        @SuppressWarnings("unchecked")
                        List<String> ekip = (List<String>) jo.get("Ekip-Liste");
                        if (ekip.contains(p.getName()))
                            liste.add(listes.getName().split("!")[1]);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }

                }

        return liste;
    }

}
