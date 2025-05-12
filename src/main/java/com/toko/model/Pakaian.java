package com.toko.model;

public class Pakaian extends Produk {
    public Pakaian(int id, String nama, double harga, int stok) {
        super(id, nama, harga, stok);
    }

    @Override
    public double hitungDiskon() {
        return getHarga() * 0.20; // 20% diskon untuk pakaian
    }
}
