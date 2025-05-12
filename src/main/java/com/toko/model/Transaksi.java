package com.toko.model;

import java.util.ArrayList;
import java.util.List;

import com.toko.pembayaran.MetodePembayaran;

public class Transaksi {
    private int id;
    private List<DetailTransaksi> detailList;
    private MetodePembayaran metodePembayaran;

    public Transaksi(int id, MetodePembayaran metodePembayaran) {
        this.id = id;
        this.metodePembayaran = metodePembayaran;
        this.detailList = new ArrayList<>();
    }

    public void tambahDetail(DetailTransaksi detail) {
        detailList.add(detail);
    }

    public double hitungTotal() {
        double total = 0;
        for (DetailTransaksi dt : detailList) {
            total += dt.subtotal();
        }
        return total;
    }

    public void prosesPembayaran() {
        double total = hitungTotal();
        metodePembayaran.bayar(total);
    }
}
