package com.speedyg.basicclaim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Lisans {

    private boolean lisans = false;

    public Lisans(final String ip, final String pluginName) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            socket = new Socket("178.20.229.149", 1453);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Baglanti hatasi!");
            System.out.println("Lutfen discord uzerinden yetkili ile iletisime geciniz!");
            System.out.println("Discord; Yusuf#7761");
            return;
        }


        String gonder = ip + "_" + pluginName + "_";
        String al = null;
        if (ip != null && pluginName != null) {
            out.println(gonder);
            al = in.readLine();

            if (al != null)
                this.lisans = Boolean.parseBoolean(al);
            else
                this.lisans = false;
        }
        out.close();
        in.close();
        socket.close();

    }


    public boolean getControl() {
        return this.lisans;
    }

}
