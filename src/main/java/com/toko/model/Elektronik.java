package com.toko.model;

public class Elektronik extends Produk {
    public Elektronik(int id, String nama, double harga, int stok) {
        super(id, nama, harga, stok);
    }

    @Override
    public double hitungDiskon() {
        return getHarga() * 0.10; // 10% diskon untuk elektronik
    }
}
