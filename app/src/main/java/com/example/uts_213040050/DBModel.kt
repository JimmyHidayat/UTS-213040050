package com.example.uts_213040050
import java.util.Date

data class DBModel(
    var idBarang: String,
    var namaBarang: String,
    var jenisBarang: String,
    var qty: Int,
    var tanggal: Date
)
