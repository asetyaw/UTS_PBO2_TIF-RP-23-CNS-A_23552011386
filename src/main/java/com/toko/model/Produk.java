package com.toko.model;

public abstract class Produk {
    private int id;
    private String nama;
    private double harga;
    private int stok;

    public Produk(int id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void kurangiStok(int jumlah) { this.stok -= jumlah; }

    // Polymorphic method
    public abstract double hitungDiskon();

    public double hargaSetelahDiskon() {
        return harga - hitungDiskon();
    }

    public void setStok(int stok) {
        this.stok = stok;
    }


    public String getKategori() {
        return this.getClass().getSimpleName(); // akan mengembalikan "Elektronik" atau "Pakaian"
    }


}
