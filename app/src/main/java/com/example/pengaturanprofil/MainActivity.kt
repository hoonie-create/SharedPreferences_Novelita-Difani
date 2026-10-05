package com.example.pengaturanprofil

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.doOnTextChanged

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NAMA_PREFS = "profil_prefs"

        private const val KEY_NAMA = "nama"
        private const val KEY_EMAIL = "email"
        private const val KEY_BIO = "bio"
        private const val KEY_AVATAR = "avatar"
        private const val KEY_GELAP = "mode_gelap"

        // ================= SOAL NOMOR 1 =================
        private const val KEY_NAMA_PANGGILAN = "nama_panggilan"

        // ================= SOAL NOMOR 2 =================
        // Key baru untuk menyimpan kota
        private const val KEY_KOTA = "kota"

        // ================= SOAL NOMOR 3 =================
        // Key untuk menyimpan jumlah pembukaan aplikasi
        private const val KEY_JUMLAH_BUKA = "jumlah_buka"

        private val DAFTAR_AVATAR = listOf(
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3
        )
    }

    private val prefs by lazy {
        getSharedPreferences(NAMA_PREFS, MODE_PRIVATE)
    }

    private var indeksAvatar = 0

    private lateinit var layoutUtama: View
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNamaKartu: TextView
    private lateinit var tvEmailKartu: TextView

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBio: EditText
    private lateinit var swGelap: SwitchCompat

    // ================= SOAL NOMOR 1 =================
    private lateinit var etNamaPanggilan: EditText
    private lateinit var btnSimpanNamaPanggilan: Button
    private lateinit var tvNamaPanggilan: TextView

    // ================= SOAL NOMOR 2 =================
    // Komponen untuk Kota
    private lateinit var etKota: EditText
    private lateinit var btnSimpanKota: Button
    private lateinit var tvKota: TextView

    // ================= SOAL NOMOR 3 =================
    private lateinit var tvJumlahBuka: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ================= AMBIL VIEW =================

        layoutUtama = findViewById(R.id.layoutUtama)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvNamaKartu = findViewById(R.id.tvNamaKartu)
        tvEmailKartu = findViewById(R.id.tvEmailKartu)

        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etBio = findViewById(R.id.etBio)
        swGelap = findViewById(R.id.swGelap)

        // ================= SOAL NOMOR 1 =================
        // Menghubungkan komponen nama panggilan

        etNamaPanggilan = findViewById(R.id.etNamaPanggilan)
        btnSimpanNamaPanggilan = findViewById(R.id.btnSimpanNamaPanggilan)
        tvNamaPanggilan = findViewById(R.id.tvNamaPanggilan)


        // ================= SOAL NOMOR 2 =================
        // Menghubungkan komponen Kota dengan XML

        etKota = findViewById(R.id.etKota)
        btnSimpanKota = findViewById(R.id.btnSimpanKota)
        tvKota = findViewById(R.id.tvKota)

        // ================= SOAL NOMOR 3 =================
        // Menghubungkan TextView jumlah pembukaan
        tvJumlahBuka = findViewById(R.id.tvJumlahBuka)

        // ================= TOMBOL PROFIL =================

        findViewById<Button>(R.id.btnGantiAvatar).setOnClickListener {
            gantiAvatar()
        }

        findViewById<Button>(R.id.btnSimpan).setOnClickListener {
            simpanProfil()
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            hapusSemuaData()
        }

        // ================= SOAL NOMOR 1 =================
        // Tombol simpan nama panggilan

        btnSimpanNamaPanggilan.setOnClickListener {
            simpanNamaPanggilan()
        }


        // ================= SOAL NOMOR 2 =================
        // Tombol simpan kota

        btnSimpanKota.setOnClickListener {
            simpanKota()
        }

        // ================= UPDATE KARTU =================

        etNama.doOnTextChanged { _, _, _, _ ->
            perbaruiKartu()
        }

        etEmail.doOnTextChanged { _, _, _, _ ->
            perbaruiKartu()
        }

        // ================= MUAT DATA =================

        muatProfil()

        // Soal nomor 1
        muatNamaPanggilan()

        // ================= SOAL NOMOR 2 =================
        // Membaca kota yang sudah tersimpan
        muatKota()

        // ================= SOAL NOMOR 3 =================
        // Menghitung setiap kali aplikasi dibuka
        hitungJumlahBuka()
    }

    // =====================================================
    // MENYIMPAN PROFIL
    // =====================================================

    private fun simpanProfil() {

        prefs.edit()
            .putString(
                KEY_NAMA,
                etNama.text.toString().trim()
            )
            .putString(
                KEY_EMAIL,
                etEmail.text.toString().trim()
            )
            .putString(
                KEY_BIO,
                etBio.text.toString().trim()
            )
            .putInt(
                KEY_AVATAR,
                indeksAvatar
            )
            .putBoolean(
                KEY_GELAP,
                swGelap.isChecked
            )
            .apply()

        Toast.makeText(
            this,
            getString(R.string.pesan_tersimpan),
            Toast.LENGTH_SHORT
        ).show()
    }


    // =====================================================
    // SOAL NOMOR 1
    // Menyimpan nama panggilan
    // =====================================================

    private fun simpanNamaPanggilan() {

        val namaPanggilan =
            etNamaPanggilan.text.toString().trim()

        prefs.edit()
            .putString(
                KEY_NAMA_PANGGILAN,
                namaPanggilan
            )
            .apply()

        tvNamaPanggilan.text = namaPanggilan

        Toast.makeText(
            this,
            "Nama panggilan berhasil disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }


    // =====================================================
    // SOAL NOMOR 2
    // Menyimpan Kota
    // =====================================================

    private fun simpanKota() {

        val kota = etKota.text.toString().trim()

        // Menyimpan kota ke SharedPreferences
        prefs.edit()
            .putString(
                KEY_KOTA,
                kota
            )
            .apply()

        // Menampilkan kota langsung setelah disimpan
        tvKota.text = kota

        Toast.makeText(
            this,
            "Kota berhasil disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }

    // =====================================================
    // SOAL NOMOR 3
    // Menghitung jumlah pembukaan aplikasi
    // =====================================================

    private fun hitungJumlahBuka() {
        // Mengambil jumlah buka yang sebelumnya tersimpan
        val jumlahBukaLama = prefs.getInt(
            KEY_JUMLAH_BUKA,
            0
        )
        // Menambahkan 1 setiap kali aplikasi dibuka
        val jumlahBukaBaru = jumlahBukaLama + 1

        // Menyimpan jumlah buka yang baru
        prefs.edit()
            .putInt(
                KEY_JUMLAH_BUKA,
                jumlahBukaBaru
            )
            .apply()
        // Menampilkan jumlah buka ke TextView
        tvJumlahBuka.text =
            "Kamu sudah membuka aplikasi ini $jumlahBukaBaru kali"
    }


    // =====================================================
    // MEMBACA PROFIL
    // =====================================================

    private fun muatProfil() {

        val nama =
            prefs.getString(KEY_NAMA, "") ?: ""

        val email =
            prefs.getString(KEY_EMAIL, "") ?: ""

        val bio =
            prefs.getString(KEY_BIO, "") ?: ""

        indeksAvatar =
            prefs.getInt(KEY_AVATAR, 0)
                .coerceIn(0, DAFTAR_AVATAR.lastIndex)

        val gelap =
            prefs.getBoolean(KEY_GELAP, false)

        etNama.setText(nama)
        etEmail.setText(email)
        etBio.setText(bio)

        ivAvatar.setImageResource(
            DAFTAR_AVATAR[indeksAvatar]
        )

        swGelap.isChecked = gelap

        terapkanModeGelap(gelap)
        perbaruiKartu()
    }


    // =====================================================
    // SOAL NOMOR 1
    // Membaca nama panggilan
    // =====================================================

    private fun muatNamaPanggilan() {

        val namaPanggilan =
            prefs.getString(
                KEY_NAMA_PANGGILAN,
                ""
            ) ?: ""

        // Nama yang tersimpan otomatis muncul
        // setelah aplikasi dibuka kembali
        tvNamaPanggilan.text = namaPanggilan
    }


    // =====================================================
    // SOAL NOMOR 2
    // Membaca Kota
    // =====================================================

    private fun muatKota() {

        // Membaca kota dari SharedPreferences
        val kota =
            prefs.getString(
                KEY_KOTA,
                ""
            ) ?: ""

        // Menampilkan kota yang tersimpan
        tvKota.text = kota
    }


    // =====================================================
    // RESET
    // =====================================================

    private fun hapusSemuaData() {

        prefs.edit()
            .clear()
            .apply()

        indeksAvatar = 0

        Toast.makeText(
            this,
            getString(R.string.pesan_reset),
            Toast.LENGTH_SHORT
        ).show()

        muatProfil()
        muatNamaPanggilan()

        // ================= SOAL NOMOR 2 =================
        // Setelah reset, kota juga dibaca ulang
        muatKota()
    }


    // =====================================================
    // GANTI AVATAR
    // =====================================================

    private fun gantiAvatar() {

        indeksAvatar =
            (indeksAvatar + 1) % DAFTAR_AVATAR.size

        ivAvatar.setImageResource(
            DAFTAR_AVATAR[indeksAvatar]
        )
    }


    // =====================================================
    // UPDATE KARTU
    // =====================================================

    private fun perbaruiKartu() {

        val nama =
            etNama.text.toString().trim()

        val email =
            etEmail.text.toString().trim()

        tvNamaKartu.text =
            nama.ifEmpty {
                getString(R.string.nama_default)
            }

        tvEmailKartu.text =
            email.ifEmpty {
                getString(R.string.email_default)
            }
    }


    // =====================================================
    // MODE GELAP
    // =====================================================

    private fun terapkanModeGelap(aktif: Boolean) {

        val latar =
            if (aktif) "#121212" else "#FFFFFF"

        val warnaNama =
            if (aktif) "#F5F5F5" else "#1B1B1B"

        val warnaEmail =
            if (aktif) "#B0B0B0" else "#6B6B6B"

        layoutUtama.setBackgroundColor(
            Color.parseColor(latar)
        )

        tvNamaKartu.setTextColor(
            Color.parseColor(warnaNama)
        )

        tvEmailKartu.setTextColor(
            Color.parseColor(warnaEmail)
        )
    }
}