package com.example.uts_213040050

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var dbAdapter: DBAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupDatePicker()
        setupDatabase()
        loadRecyclerViewData()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.listView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            insertData()
        }
    }

    private fun setupDatePicker() {
        val dateEditText = findViewById<TextInputEditText>(R.id.tgl_barang)
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateEditText.setText(dateFormat.format(calendar.time))
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupDatabase() {
        dbHelper = DBHelper(this)
    }

    private fun insertData() {
        val idBarangEditText = findViewById<TextInputEditText>(R.id.idbarang)
        val namaBarangEditText = findViewById<TextInputEditText>(R.id.namabarang)
        val jenisBarangEditText = findViewById<TextInputEditText>(R.id.jenisbarang)
        val qtyEditText = findViewById<TextInputEditText>(R.id.qty)
        val tanggalEditText = findViewById<TextInputEditText>(R.id.tgl_barang)

        val idBarang = idBarangEditText.text.toString()
        val namaBarang = namaBarangEditText.text.toString()
        val jenisBarang = jenisBarangEditText.text.toString()
        val qtyText = qtyEditText.text.toString()
        val tanggalText = tanggalEditText.text.toString()

        // Validasi untuk memastikan semua field telah diisi
        if (idBarang.isEmpty() || namaBarang.isEmpty() || jenisBarang.isEmpty() || qtyText.isEmpty() || tanggalText.isEmpty()) {
            // Menampilkan pesan toast jika ada field yang kosong
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val qty = qtyText.toInt()
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tanggalText)

        val model = DBModel(idBarang, namaBarang, jenisBarang, qty, tanggal)
        dbHelper.addData(model)

        // Menampilkan pesan toast jika data berhasil ditambahkan
        Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

        // Memuat kembali data RecyclerView
        loadRecyclerViewData()

        // Mengosongkan field-field form setelah data berhasil dimasukkan
        idBarangEditText.text = null
        namaBarangEditText.text = null
        jenisBarangEditText.text = null
        qtyEditText.text = null
        tanggalEditText.text = null
    }

    private fun loadRecyclerViewData() {
        val dataList = dbHelper.getData()
        dbAdapter = DBAdapter(this, dataList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = dbAdapter
        }
    }


}
