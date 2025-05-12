package com.toko.pembayaran;

public class PembayaranKartu implements MetodePembayaran {
    @Override
    public void bayar(double jumlah) {
        System.out.println("Pembayaran kartu sebesar: Rp" + jumlah);
    }
}
