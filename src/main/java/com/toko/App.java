package com.toko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.toko.model.DetailTransaksi;
import com.toko.model.Elektronik;
import com.toko.model.Pakaian;
import com.toko.model.Produk;
import com.toko.model.Transaksi;
import com.toko.pembayaran.MetodePembayaran;
import com.toko.pembayaran.PembayaranKartu;
import com.toko.pembayaran.PembayaranTunai;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Ambil daftar produk dari database
        List<Produk> daftarProduk = ambilProdukDariDatabase();

        // Tampilkan daftar produk
        System.out.println("=== DAFTAR PRODUK ===");
        for (Produk p : daftarProduk) {
            System.out.printf("%d. %s - Rp%.0f (%s, Stok: %d)%n",
                    p.getId(), p.getNama(), p.getHarga(), p.getKategori(), p.getStok());
        }

        // Mulai transaksi
        List<DetailTransaksi> keranjang = new ArrayList<>();
        String lanjut;
        do {
            System.out.print("\nMasukkan ID produk yang ingin dibeli: ");
            int id = input.nextInt();
            System.out.print("Jumlah: ");
            int jumlah = input.nextInt();

            // Cari produk berdasarkan ID
            Produk produkDipilih = null;
            for (Produk p : daftarProduk) {
                if (p.getId() == id) {
                    produkDipilih = p;
                    break;
                }
            }

            if (produkDipilih == null) {
                System.out.println("Produk tidak ditemukan.");
            } else if (jumlah > produkDipilih.getStok()) {
                System.out.println("Stok tidak cukup.");
            } else {
                produkDipilih.kurangiStok(jumlah);
                keranjang.add(new DetailTransaksi(produkDipilih, jumlah));
                System.out.println("Produk ditambahkan ke keranjang.");
            }

            System.out.print("Tambah produk lain? (y/n): ");
            lanjut = input.next();
        } while (lanjut.equalsIgnoreCase("y"));

        // Pilih metode pembayaran
        System.out.print("Metode pembayaran (1 = Tunai, 2 = Kartu): ");
        int metodeInput = input.nextInt();
        MetodePembayaran metode = (metodeInput == 1) ? new PembayaranTunai() : new PembayaranKartu();

        // Buat transaksi
        Transaksi transaksi = new Transaksi(1, metode);
        for (DetailTransaksi dt : keranjang) {
            transaksi.tambahDetail(dt);
        }

        // Cetak struk
        System.out.println("\n=== STRUK PEMBAYARAN ===");
        for (DetailTransaksi dt : keranjang) {
            Produk p = dt.getProduk();
            double hargaAwal = p.getHarga();
            double diskon = p.hitungDiskon();
            double hargaSetelahDiskon = hargaAwal - diskon;

            System.out.printf("%s (%s)%n", p.getNama(), p.getKategori());
            System.out.printf("  Harga Satuan : Rp%.0f%n", hargaAwal);
            System.out.printf("  Diskon       : Rp%.0f%n", diskon);
            System.out.printf("  Harga Akhir  : Rp%.0f%n", hargaSetelahDiskon);
            System.out.printf("  Jumlah       : %d%n", dt.getJumlah());
            System.out.printf("  Subtotal     : Rp%.0f%n%n", dt.subtotal());
        }

        double total = transaksi.hitungTotal();
        System.out.printf("TOTAL BAYAR: Rp%.0f%n", total);
        transaksi.prosesPembayaran();

        // Update stok di database
        for (DetailTransaksi dt : keranjang) {
            Produk p = dt.getProduk();
            updateStokDiDatabase(p.getId(), dt.getJumlah());
        }
    }

    // Ambil produk dari database
    public static List<Produk> ambilProdukDariDatabase() {
        List<Produk> daftar = new ArrayList<>();
        String query = "SELECT * FROM produk";
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                String kategori = rs.getString("kategori");

                Produk p;
                if (kategori.equalsIgnoreCase("Elektronik")) {
                    p = new Elektronik(id, nama, harga, stok);
                } else {
                    p = new Pakaian(id, nama, harga, stok);
                }
                daftar.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return daftar;
    }

    // Update stok di database
    public static void updateStokDiDatabase(int idProduk, int jumlahDibeli) {
        String query = "UPDATE produk SET stok = stok - ? WHERE id = ?";
        try (Connection conn = database.getConnection()) {
            if (conn == null || conn.isClosed()) {
                System.out.println("Koneksi database tidak tersedia saat update stok.");
            return;
            }
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, jumlahDibeli);
                stmt.setInt(2, idProduk);
                stmt.executeUpdate();
            }
        }       catch (Exception e) {
            System.out.println("Gagal update stok di database: " + e.getMessage());
        }
    }
}
