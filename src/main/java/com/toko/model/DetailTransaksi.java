package com.toko.model;

public class DetailTransaksi {
    private Produk produk;
    private int jumlah;

    public DetailTransaksi(Produk produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Produk getProduk() { return produk; }
    public int getJumlah() { return jumlah; }

    public double subtotal() {
        return produk.hargaSetelahDiskon() * jumlah;
    }
}
