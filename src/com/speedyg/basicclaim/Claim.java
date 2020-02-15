package com.speedyg.basicclaim;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.speedyg.basicclaim.api.Placeholder;
import com.speedyg.basicclaim.komut.Admin_Komut;
import com.speedyg.basicclaim.komut.Kirala_Komut;
import com.speedyg.basicclaim.komut.Silinenler_Komut;
import com.speedyg.basicclaim.menu.stat.Calculate;
import com.speedyg.basicclaim.mesajlar.Mesajlar;

import de.NeonnBukkit.CoinsAPI.API.CoinsAPI;
import net.milkbowl.vault.economy.Economy;

public class Claim extends JavaPlugin implements Listener {

    private File messageFile;
    private FileConfiguration mOptions;

    private File veri;
    private FileConfiguration veriayar;

    // public static OB_Sistem sis_api;
    public static Economy eco_api;
    public static CoinsAPI coins_api;
    public static Claim main;
    public HashMap<Player, Location> sagClick = new HashMap<Player, Location>();
    public HashMap<Player, Location> solClick = new HashMap<Player, Location>();
    public List<UUID> kullanim_suresi = new ArrayList<UUID>();
    private Econ_API api;
    private Calculate cal;

    @Override
    public void onEnable() {
        if (!this.lisansKontrol()) {
            Bukkit.getConsoleSender().sendMessage("§6§lUyari!§e Lisansiniz bulunamadi! Lutfen eklentiyi satin aliniz!");
            Bukkit.getConsoleSender().sendMessage("§eEger lisansiniz oldugu halde bu hatayi aliyorsaniz §4Discord: Yusuf#7761");
            Bukkit.getConsoleSender().sendMessage("§edestek alabilirsiniz!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        main = this;
        this.cal = new Calculate(this);
        this.saveDefaultConfig();
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§6§lUyari!§7 Vault eklentisi devre disi!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!loadPlaceHolder())
            Bukkit.getConsoleSender().sendMessage("§6§lUyari! §7PlaceholderAPI bulunamadı, placeholderler devre dışı!");
        Etkinlikler e = new Etkinlikler(this);
        e.etkKaydet();
        makeMessageFile();
        veriDosyasiOlustur();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("kirala").setExecutor(new Kirala_Komut(this));
        this.getCommand("kiralaadmin").setExecutor(new Admin_Komut(this));
        this.getCommand("silinenler").setExecutor(new Silinenler_Komut(this));
        Mesajlar.mesajlariYukle();
        Bukkit.getConsoleSender().sendMessage("§f[§bBasic-Claims§f] §fLoad complete!");
        Bukkit.getConsoleSender().sendMessage("§f[§bBasic-Claims§f] §a" + cal.genelListeyiAl().size()
                + " (Active) §fand §6" + cal.tumSilinenleriAl().size() + " (Removed) §fclaim loaded!");
    }

    private boolean lisansKontrol() {
        Lisans lisans = null;
        try {
            lisans = new Lisans(InetAddress.getLocalHost().getHostAddress(), this.getDescription().getName());
            return lisans.getControl();
        } catch (IOException e) {
            return false;
        }
    }

    private boolean loadPlaceHolder() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder().register();
            return true;
        }
        return false;
    }

    private boolean setupEconomy() {
        this.api = Econ_API.valueOf(main.getConfig().getString("ECON-API"));
        if (api != null) {
            switch (api) {
                case COINSAPI:
                    if (getServer().getPluginManager().getPlugin("CoinsAPINB") != null) {
                        return true;
                    }
                    return false;
                case VAULT:
                    if (getServer().getPluginManager().getPlugin("Vault") == null) {
                        return false;
                    }
                    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                            .getRegistration(Economy.class);
                    if (rsp == null) {
                        return false;
                    }
                    eco_api = (Economy) rsp.getProvider();
                    return eco_api != null;
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean alanKiralanmisMi(Location loc1, Location loc2) {
        File file = new File(this.getDataFolder() + "/arsalar");
        if (file.isDirectory()) {
            if (file.listFiles() != null)
                for (File listes : file.listFiles()) {
                    String[] bol_1 = listes.getName().split("!")[1].split("&");
                    String[] solBol = bol_1[1].split(",");
                    String[] sagBol = bol_1[2].split(",");
                    int solX = Integer.parseInt(solBol[0]);
                    int solY = Integer.parseInt(solBol[1]);
                    int solZ = Integer.parseInt(solBol[2]);
                    int sagX = Integer.parseInt(sagBol[0]);
                    int sagY = Integer.parseInt(sagBol[1]);
                    int sagZ = Integer.parseInt(sagBol[2]);

                    // Oyuncunun sectigi alan
                    int xPos1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
                    int zPos1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

                    int yPos1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
                    int yPos2 = Math.max(loc1.getBlockY(), loc2.getBlockY());

                    int xPos2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
                    int zPos2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

                    // listeden gelen alan
                    int listeXKucuk = Math.min(solX, sagX);
                    int listeZKucuk = Math.min(solZ, sagZ);

                    int listeYKucuk = Math.min(solY, sagY);
                    int listeYBuyuk = Math.max(solY, sagY);

                    int listeXBuyuk = Math.max(solX, sagX);
                    int listeZBuyuk = Math.max(solZ, sagZ);

                    final boolean b1 = ((listeXKucuk >= xPos1 && listeXKucuk <= xPos2)
                            && (listeZKucuk >= zPos1 && listeZKucuk <= zPos2))
                            || ((listeXBuyuk >= xPos1 && listeXBuyuk <= xPos2)
                            && (listeZBuyuk >= zPos1 && listeZBuyuk <= zPos2));
                    final boolean b4 = (xPos1 >= listeXKucuk && xPos1 <= listeXBuyuk)
                            && (zPos1 >= listeZKucuk && zPos1 <= listeZBuyuk);
                    boolean b3 = b4;
                    final boolean b2 = (xPos2 >= listeXKucuk && xPos2 <= listeXBuyuk)
                            && (zPos2 >= listeZKucuk && zPos2 <= listeZBuyuk);
                    if (main.getConfig().getBoolean("Yer-Gog-Sistemi")) {
                        // 3D Cuboid System Area Cross Check

                        // kenar köşegenler kontrolü
                        if ((b3
                                || b2
                                || b1)
                                && ((yPos1 >= listeYKucuk && yPos1 <= listeYBuyuk
                                || yPos2 >= listeYKucuk && yPos2 <= listeYKucuk)
                                || ((listeYBuyuk >= yPos1 && listeYBuyuk <= yPos2)
                                && (listeYKucuk >= zPos1 && listeYKucuk <= zPos2)))) {
                            if (main.getConfig().getBoolean("Alan-Gosterim-Efekti"))
                                alanCevrele(new Location(Bukkit.getWorld(bol_1[0]), solX, solY, solZ),
                                        new Location(Bukkit.getWorld(bol_1[0]), sagX, sagY, sagZ));
                            return true;
                        } else {
                            // Çeper kontrolü
                            for (int a = xPos1; a <= xPos2; a++)
                                for (int b = zPos1; b <= zPos2; b++)
                                    for (int c = yPos1; c < yPos2; c++)
                                        if (a >= listeXKucuk && a <= listeXBuyuk && b >= listeZKucuk && b <= listeZBuyuk
                                                && c >= listeYKucuk && c <= listeYBuyuk) {
                                            if (main.getConfig().getBoolean("Alan-Gosterim-Efekti"))
                                                alanCevrele(new Location(Bukkit.getWorld(bol_1[0]), solX, solY, solZ),
                                                        new Location(Bukkit.getWorld(bol_1[0]), sagX, sagY, sagZ));
                                            return true;
                                        }
                        }

                    } else {
                        // 2D Cuboid System Area Cross Check
                        // kenar köşegenler kontrolü
                        if (b4
                                || b2
                                || b1
                                || ((listeXKucuk >= xPos1 && listeXKucuk <= xPos2)
                                && (zPos1 >= listeZKucuk && zPos1 <= listeZBuyuk))
                                || (listeZKucuk >= zPos1 && listeZKucuk <= zPos2)
                                && (xPos1 >= listeXKucuk && xPos1 <= listeXBuyuk)) {
                            if (main.getConfig().getBoolean("Alan-Gosterim-Efekti"))
                                alanCevrele(new Location(Bukkit.getWorld(bol_1[0]), solX, solY, solZ),
                                        new Location(Bukkit.getWorld(bol_1[0]), sagX, sagY, sagZ));
                            return true;
                        }
                    }
                }
        }
        return false;
    }

    private void alanCevrele(Location loc1, Location loc2) {
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        Timer tmer = new Timer();
        TimerTask task = new TimerTask() {
            int sure = 0;

            @Override
            public void run() {
                try {
                    int x = minX;
                    int z = minZ;
                    int donus = 0;
                    int maxDonus = ((maxX - minX) * 2) + ((maxZ - minZ) * 2);
                    while (donus < maxDonus) {

                        if (x < maxX && z == minZ) {
                            x++;
                        } else if (x == maxX && z < maxZ) {
                            z++;
                        } else if (minX < x && z == maxZ) {
                            x--;
                        } else if (x == minX && minZ < z) {
                            z--;
                        }
                        loc1.getWorld().playEffect(
                                new Location(loc1.getWorld(), x, loc1.getWorld().getHighestBlockYAt(x, z), z),
                                Effect.MOBSPAWNER_FLAMES, 1);
                        donus++;
                    }
                } catch (Exception e) {
                    tmer.cancel();
                }
                if (++sure > 5) {
                    tmer.cancel();
                }
            }
        };
        tmer.schedule(task, 0, 600);

    }

    public FileConfiguration getMesajAyar() {
        return this.mOptions;
    }

    public FileConfiguration getVeriAyar() {
        return this.veriayar;
    }

    public void oyuncuClaimIstedi(Player p) {
        if (this.oyuncuParasiOgren(p) >= cal.maliyetBul(cal.blokSayisiBul(main.solClick.get(p), main.sagClick.get(p)))
                || p.isOp()) {
            if ((cal.oyuncununToplamAttigiClaim(p)
                    + cal.blokSayisiBul(main.solClick.get(p), main.sagClick.get(p))) <= cal.oyuncuMaxClaim(p)
                    || p.isOp()) {
                Location l1 = this.sagClick.get(p);
                Location l2 = this.solClick.get(p);
                if (this.alanKiralanmisMi(l1, l2)) {
                    p.sendMessage(Mesajlar.alan_kiralanmis);
                } else {
                    this.oyuncuAlaniAldi(p, l1, l2);
                    p.sendMessage(Mesajlar.alim_basarili);
                }
            } else {
                p.sendMessage(Mesajlar.kapasite_yetersiz);
            }
        } else {
            p.sendMessage(Mesajlar.paran_yok);
        }

        if (this.sagClick.containsKey(p))
            this.sagClick.remove(p);
        if (this.solClick.containsKey(p))
            this.solClick.remove(p);

    }

    private void makeMessageFile() {
        this.messageFile = new File(getDataFolder(), "messages.yml");
        if (!this.messageFile.exists()) {
            this.messageFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }
        this.mOptions = new YamlConfiguration();
        try {
            this.mOptions.load(this.messageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Mesajlar.mesajlariYukle();
    }

    private void veriDosyasiOlustur() {
        this.veri = new File(getDataFolder(), "veri.yml");
        if (!this.veri.exists()) {
            this.veri.getParentFile().mkdirs();
            saveResource("veri.yml", false);
        }
        this.veriayar = new YamlConfiguration();
        try {
            this.veriayar.load(this.veri);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void oyuncuAlaniAldi(Player p, Location l1, Location l2) {
        if (!p.isOp())
            this.oyuncudanParaCek(p, cal.maliyetBul(cal.blokSayisiBul(l1, l2)));
        String w = l1.getWorld().getName();
        int solx = l1.getBlockX();
        int soly = l1.getBlockY();
        int solz = l1.getBlockZ();

        int sagx = l2.getBlockX();
        int sagy = l2.getBlockY();
        int sagz = l2.getBlockZ();

        String str = w + "&" + solx + "," + soly + "," + solz + "&" + sagx + "," + sagy + "," + sagz;
        JSONObject json = new JSONObject();
        json.put("Alan", str);
        json.put("Ekip-Liste", new ArrayList<String>());
        json.put("Uzaklastirma-Liste", new ArrayList<String>());
        json.put("Arsa-Sahibi", p.getName());
        json.put("Genel-Blok-Koyma", false);
        json.put("Genel-Blok-Kirma", false);
        json.put("Genel-Item-Atma", false);
        json.put("Genel-Item-Alma", false);
        json.put("Genel-Balik-Tutma", false);
        json.put("Genel-Hasar-Verme", false);
        json.put("Genel-Konusma-Etkinligi", true);
        json.put("Genel-Yatak-Kullanimi", false);
        json.put("Genel-Isinlanma", false);
        json.put("Genel-Portal-Kullanimi", false);
        json.put("Genel-Aktivite", false);
        json.put("Ekip-Blok-Kirma", false);
        json.put("Ekip-Blok-Koyma", false);
        json.put("Ekip-Item-Atma", true);
        json.put("Ekip-Item-Alma", true);
        json.put("Ekip-Balik-Tutma", false);
        json.put("Ekip-Hasar-Verme", false);
        json.put("Ekip-Konusma-Etkinligi", true);
        json.put("Ekip-Yatak-Kullanimi", true);
        json.put("Ekip-Isinlanma", false);
        json.put("Ekip-Portal-Kullanimi", false);
        json.put("Ekip-Aktivite", true);

        File arsaDosyasi = new File(this.getDataFolder() + "/arsalar",
                (p.getUniqueId() + "!" + str + "!" + p.getName() + ".json"));
        try {
            arsaDosyasi.getParentFile().mkdirs();
            arsaDosyasi.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter writer;
        try {
            writer = new FileWriter(arsaDosyasi);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void yenile(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.load(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void kayit(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getMesajDosyasi() {
        return this.messageFile;
    }

    public void sistemleriYenile(CommandSender sender) {
        this.yenile(this.mOptions, this.messageFile);
        this.reloadConfig();
        sender.sendMessage("§a§lBilgi!§7 Sistemler başarıyla yenilendi!");

    }

    public boolean karsilastir(String s1, String s2) {
        if (s1 != null && s2 != null)
            return s1.equals(s2);
        else if (s1 != null && s2 == null)
            return false;
        else if (s1 == null && s2 != null)
            return false;
        else
            return true;
    }

    public void iadeEt(Player p, String arsa, double iademiktari, boolean iadeMi) {
        File arsaDosyasi = cal.arsaBul(arsa);
        if (arsaDosyasi != null) {
            if (sil(arsaDosyasi, p)) {
                if (iadeMi) {
                    p.sendMessage(Mesajlar.arsa_iade_edildi);
                    this.oyuncuyaParaVer(p, iademiktari);
                } else {
                    p.sendMessage(Mesajlar.arsaniz_silindi);
                }
            }
        }
    }

    public boolean sil(File arsaDosyasi, Player silen) {
        Date tarih = new Date();
        File git = new File(this.getDataFolder() + "/silinen-arsalar",
                tarih.getTime() + "!" + silen.getName() + "!" + silen.isOp() + "!" + arsaDosyasi.getName());
        if (!git.exists()) {
            try {
                git.getParentFile().mkdirs();
                git.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        JSONObject yaz = null;
        try {
            FileReader reader = new FileReader(arsaDosyasi);
            JSONParser oop = new JSONParser();
            yaz = (JSONObject) oop.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }

        FileWriter writer;
        try {
            writer = new FileWriter(git);
            writer.write(yaz.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (arsaDosyasi.exists()) {
            if (arsaDosyasi.delete()) {
                return true;
            }
        }
        return false;
    }

    public File getVeriDosyasi() {
        return this.veri;
    }

    public void oyuncuyaParaVer(Player p, double miktar) {
        switch (this.api) {
            case COINSAPI:
                CoinsAPI.addCoins(p.getUniqueId().toString(), (int) miktar);
                break;
            case VAULT:
                eco_api.depositPlayer(p, miktar);
                break;
            default:
                break;

        }

    }

    public double oyuncuParasiOgren(Player p) {
        switch (this.api) {
            case COINSAPI:
                return CoinsAPI.getCoins(p.getUniqueId().toString());
            case VAULT:
                return Claim.eco_api.getBalance(p);
            default:
                break;
        }

        return 0;
    }

    public void oyuncudanParaCek(Player p, double miktar) {
        switch (this.api) {
            case COINSAPI:
                CoinsAPI.removeCoins(p.getUniqueId().toString(), (int) miktar);
                break;
            case VAULT:
                Claim.eco_api.withdrawPlayer(p, miktar);
                break;
            default:
                break;

        }

    }

}
