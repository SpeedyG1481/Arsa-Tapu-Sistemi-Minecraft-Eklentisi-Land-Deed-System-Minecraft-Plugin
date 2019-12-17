package com.speedyg.basicclaim;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.speedyg.basicclaim.menu.Claim_Onayla;
import com.speedyg.basicclaim.menu.stat.Calculate;
import com.speedyg.basicclaim.menu.stat.Claimer;
import com.speedyg.basicclaim.mesajlar.Mesajlar;

@SuppressWarnings("deprecation")
public class Etkinlikler implements Listener {

	private Claim main;
	private Calculate cal;

	public Etkinlikler(Claim main) {
		this.main = main;
		this.cal = new Calculate(this.main);
	}

	public void etkKaydet() {
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	private void girisEtk(PlayerJoinEvent e) {
		Date tarih = new Date();
		if (main.getVeriAyar().isSet(e.getPlayer().getUniqueId().toString())) {
			long kayitliTarih = main.getVeriAyar().getLong(e.getPlayer().getUniqueId().toString());
			Date gTarih = new Date(kayitliTarih);
			if (Math.abs(((tarih.getTime() - gTarih.getTime())
					/ (1000 * 60 * 60 * 24))) >= (main.getConfig().getInt("Silinme-Suresi") > 0
							? main.getConfig().getInt("Silinme-Suresi")
							: 60)) {
				List<String> oyuncuArsalari = cal.oyuncuListesiniAl(e.getPlayer());
				for (String s : oyuncuArsalari) {
					main.iadeEt(e.getPlayer(), s, 0, false);
				}
			}
		} else {
			main.getVeriAyar().set(e.getPlayer().getUniqueId().toString(), tarih.getTime());
			main.kayit(main.getVeriAyar(), main.getVeriDosyasi());
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	private void claimAtmaEtk(PlayerInteractEvent e) {
		if (e.isCancelled())
			return;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
			if (e.getClickedBlock() != null)
				if (e.getItem() != null) {
					if (e.getItem().hasItemMeta()) {
						if (e.getItem().getItemMeta().getDisplayName() != null)
							if (e.getItem().getItemMeta().getDisplayName()
									.equals(Claimer.claimItemi().getItemMeta().getDisplayName())
									&& e.getItem().getItemMeta().getLore()
											.equals(Claimer.claimItemi().getItemMeta().getLore())
									|| e.getItem().getItemMeta().equals(Claimer.claimItemi().getItemMeta())) {
								e.setCancelled(true);
								if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
									main.sagClick.put(e.getPlayer(), e.getClickedBlock().getLocation());
									e.getPlayer().sendMessage(Mesajlar.sag_tik);
								} else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
									main.solClick.put(e.getPlayer(), e.getClickedBlock().getLocation());
									e.getPlayer().sendMessage(Mesajlar.sol_tik);
								}

								if (main.sagClick.get(e.getPlayer()) != null
										&& main.solClick.get(e.getPlayer()) != null) {
									if (!main.getConfig().getStringList("Claim-Yasak-Dunyalar")
											.contains(main.sagClick.get(e.getPlayer()).getWorld().getName())) {
										if (main.solClick.get(e.getPlayer()).getWorld()
												.equals(main.sagClick.get(e.getPlayer()).getWorld())) {
											if (!main.getConfig().getBoolean("Yer-Gog-Sistemi")) {
												Location loc = main.sagClick.get(e.getPlayer());
												Location loc_1 = main.solClick.get(e.getPlayer());
												loc.setY(0);
												loc_1.setY(loc_1.getWorld().getMaxHeight());
												main.sagClick.put(e.getPlayer(), loc);
												main.solClick.put(e.getPlayer(), loc_1);
											}
											int sure = main.getConfig().getInt("Arsa-Aleti-Kullanim-Suresi") > 0
													? main.getConfig().getInt("Arsa-Aleti-Kullanim-Suresi")
													: 120;
											int minBlok = main.getConfig().getInt("Minimum-Arsa-Blok-Sayisi") > 0
													? main.getConfig().getInt("Minimum-Arsa-Blok-Sayisi")
													: 10000;
											if (!main.kullanim_suresi.contains(e.getPlayer().getUniqueId())
													|| e.getPlayer().isOp()) {
												if (cal.blokSayisiBul(main.sagClick.get(e.getPlayer()),
														main.solClick.get(e.getPlayer())) > minBlok
														|| e.getPlayer().isOp()) {
													Claim_Onayla menu = new Claim_Onayla(main, e.getPlayer(),
															main.solClick.get(e.getPlayer()),
															main.sagClick.get(e.getPlayer()));
													menu.menuAc();
													if (!main.kullanim_suresi.contains(e.getPlayer().getUniqueId())) {
														main.kullanim_suresi.add(e.getPlayer().getUniqueId());
														BukkitScheduler scheduler = Claim.main.getServer()
																.getScheduler();
														scheduler.scheduleSyncDelayedTask(Claim.main, new Runnable() {
															@Override
															public void run() {
																if (main.kullanim_suresi
																		.contains(e.getPlayer().getUniqueId()))
																	main.kullanim_suresi
																			.remove(e.getPlayer().getUniqueId());
															}
														}, sure * 20L);
													}
												} else {
													e.getPlayer().sendMessage(Mesajlar.min_blok_gecersiz
															.replaceAll("<minblok>", String.valueOf(minBlok)));
												}
											} else {
												e.getPlayer().sendMessage(Mesajlar.kullanim_suresi.replaceAll("<sure>",
														String.valueOf(sure)));
												if (main.sagClick.get(e.getPlayer()) != null)
													main.sagClick.remove(e.getPlayer());
												if (main.solClick.get(e.getPlayer()) != null)
													main.solClick.remove(e.getPlayer());
											}
										} else {
											if (main.sagClick.get(e.getPlayer()) != null)
												main.sagClick.remove(e.getPlayer());
											if (main.solClick.get(e.getPlayer()) != null)
												main.solClick.remove(e.getPlayer());
											e.getPlayer().sendMessage(Mesajlar.hatali_secim);
										}
									} else {
										if (main.sagClick.get(e.getPlayer()) != null)
											main.sagClick.remove(e.getPlayer());
										if (main.solClick.get(e.getPlayer()) != null)
											main.solClick.remove(e.getPlayer());
										e.getPlayer().sendMessage(Mesajlar.banli_dunya);
									}

								}
							}
					}
				}
	}

	@EventHandler
	private void oyuncuAlanaGirmeEtk(PlayerMoveEvent e) {
		Location onceki = e.getFrom();
		Location son = e.getTo();
		List<String> liste = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory()) {
			if (file.listFiles() != null) {
				if (!Claim.main.karsilastir(cal.oyuncuLokasyondaMi(onceki), cal.oyuncuLokasyondaMi(son))) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(son))) {
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								JSONObject jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (ArrayList<String>) jo.get("Uzaklastirma-Liste");
								for (String s : ul)
									liste.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							boolean girebilirMi = liste != null ? !liste.contains(e.getPlayer().getName()) : true;
							if (!girebilirMi) {
								e.getPlayer().sendMessage(Mesajlar.arsa_girisin_banli.replaceAll("<arsasahibi>",
										cal.getArsaSahibi(cal.oyuncuLokasyondaMi(son))));
								e.setCancelled(true);
								return;
							}
							break;

						}
					}
					if (cal.oyuncuLokasyondaMi(son) == null) {
						e.getPlayer().sendTitle(
								main.getMesajAyar().getString("Arsa-Giris.Sahipsiz.Title").replaceAll("&", "§"),
								main.getMesajAyar().getString("Arsa-Giris.Sahipsiz.Sub").replaceAll("&", "§"));
					} else {
						e.getPlayer().sendTitle(
								main.getMesajAyar().getString("Arsa-Giris.Sahipli.Title").replaceAll("&", "§"),
								main.getMesajAyar().getString("Arsa-Giris.Sahipli.Sub").replaceAll("&", "§")
										.replaceAll("<sahip>", cal.getArsaSahibi(cal.oyuncuLokasyondaMi(son))));
					}

				}
			}
		}
	}

	@EventHandler
	private void blokKirma(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getBlock().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Blok-Kirma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Blok-Kirma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void blokKoyma(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getBlock().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Blok-Koyma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Blok-Koyma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void itemAtma(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Item-Atma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Item-Atma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void itemAlma(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Item-Alma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Item-Alma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void isinlanmaEtk(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Isinlanma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Isinlanma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void yatakEtk(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getBed().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Yatak-Kullanimi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Yatak-Kullanimi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void konusmaEtk(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Konusma-Etkinligi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Konusma-Etkinligi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void hasarEtk(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			Location blok = p.getLocation();
			List<String> ekipListesi = new ArrayList<String>();
			File file = new File(Claim.main.getDataFolder() + "/arsalar");
			if (file.isDirectory())
				if (file.listFiles() != null)
					if (cal.oyuncuLokasyondaMi(blok) != null) {
						for (File listes : file.listFiles()) {
							if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
								String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
								JSONObject jo = null;
								try {
									FileReader reader = new FileReader(listes);
									JSONParser oop = new JSONParser();
									jo = (JSONObject) oop.parse(reader);
									reader.close();
									@SuppressWarnings("unchecked")
									List<String> ul = (List<String>) jo.get("Ekip-Liste");
									for (String s : ul)
										ekipListesi.add(s);
								} catch (Exception ex) {
									ex.printStackTrace();
								}

								if (ekipListesi.contains(p.getName())) {
									boolean blokKirma = (boolean) jo.get("Ekip-Hasar-Verme");
									if (!blokKirma && !p.isOp()
											&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
										e.setCancelled(true);
										p.sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
												cal.getArsaSahibi(lokasyonAdi)));
									}
								} else {
									boolean blokKirma = (boolean) jo.get("Genel-Hasar-Verme");
									if (!blokKirma && !p.isOp()
											&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
										e.setCancelled(true);
										p.sendMessage(Mesajlar.genel_aktivite_engeli);
									}
								}
								break;
							}
						}
					}
		}
	}

	@EventHandler
	private void balikEtk(PlayerFishEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Balik-Tutma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Balik-Tutma");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void aktiviteEtk(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getClickedBlock() != null ? e.getClickedBlock().getLocation() : p.getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Aktivite");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Aktivite");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

	@EventHandler
	private void portalEtk(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		Location blok = e.getPlayer().getLocation();
		List<String> ekipListesi = new ArrayList<String>();
		File file = new File(Claim.main.getDataFolder() + "/arsalar");
		if (file.isDirectory())
			if (file.listFiles() != null)
				if (cal.oyuncuLokasyondaMi(blok) != null) {
					for (File listes : file.listFiles()) {
						if (listes.getName().split("!")[1].equals(cal.oyuncuLokasyondaMi(blok))) {
							String lokasyonAdi = cal.oyuncuLokasyondaMi(blok);
							JSONObject jo = null;
							try {
								FileReader reader = new FileReader(listes);
								JSONParser oop = new JSONParser();
								jo = (JSONObject) oop.parse(reader);
								reader.close();
								@SuppressWarnings("unchecked")
								List<String> ul = (List<String>) jo.get("Ekip-Liste");
								for (String s : ul)
									ekipListesi.add(s);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							if (ekipListesi.contains(p.getName())) {
								boolean blokKirma = (boolean) jo.get("Ekip-Portal-Kullanimi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.aktivite_engelli_ekip.replaceAll("<arsasahibi>",
											cal.getArsaSahibi(lokasyonAdi)));
								}
							} else {
								boolean blokKirma = (boolean) jo.get("Genel-Portal-Kullanimi");
								if (!blokKirma && !p.isOp()
										&& !cal.claimSahibiBul(lokasyonAdi).equals(p.getUniqueId())) {
									e.setCancelled(true);
									e.getPlayer().sendMessage(Mesajlar.genel_aktivite_engeli);
								}
							}
							break;
						}
					}
				}
	}

}
