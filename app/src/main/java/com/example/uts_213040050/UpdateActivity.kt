package com.example.uts_213040050

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        setupDatePicker()

        val idEditText = findViewById<TextInputEditText>(R.id.idbrg)
        val namaEditText = findViewById<TextInputEditText>(R.id.nmbrg)
        val jenisEditText = findViewById<TextInputEditText>(R.id.jnsbrg)
        val qtyEditText = findViewById<TextInputEditText>(R.id.qtybrg)
        val tanggalEditText = findViewById<TextInputEditText>(R.id.tglbrg)
        val updateButton = findViewById<Button>(R.id.btnupdate)

        val idBarang = intent.getStringExtra("ID") ?: ""
        val namaBarang = intent.getStringExtra("Nama") ?: ""
        val jenisBarang = intent.getStringExtra("Jenis") ?: ""
        val qty = intent.getIntExtra("QTY", 0)
        val tanggal = intent.getStringExtra("Tanggal") ?: ""

        idEditText.setText(idBarang)
        namaEditText.setText(namaBarang)
        jenisEditText.setText(jenisBarang)
        qtyEditText.setText(qty.toString())
        tanggalEditText.setText(tanggal)

        updateButton.setOnClickListener {
            val idBarang = idEditText.text.toString()
            val namaBarang = namaEditText.text.toString()
            val jenisBarang = jenisEditText.text.toString()
            val qty = qtyEditText.text.toString().toInt()
            val tanggal = tanggalEditText.text.toString()

            val dbHelper = DBHelper(this)
            val updatedRows = dbHelper.updateData(DBModel(idBarang, namaBarang, jenisBarang, qty, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tanggal)))

            if (updatedRows > 0) {
                Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                // Kirim sinyal ke MainActivity untuk memperbarui tampilan
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDatePicker() {
        val dateEditText = findViewById<TextInputEditText>(R.id.tglbrg)
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
}
