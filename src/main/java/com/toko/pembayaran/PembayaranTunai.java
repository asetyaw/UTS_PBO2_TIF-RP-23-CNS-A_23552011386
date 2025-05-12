package com.toko.pembayaran;

public class PembayaranTunai implements MetodePembayaran {
    @Override
    public void bayar(double jumlah) {
        System.out.println("Pembayaran tunai sebesar: Rp" + jumlah);
    }
}
