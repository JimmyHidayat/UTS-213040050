package com.example.uts_213040050

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "barang_db"
        private const val TABLE_NAME = "barang"
        private const val COLUMN_ID_BARANG = "id_barang"
        private const val COLUMN_NAMA_BARANG = "nama_barang"
        private const val COLUMN_JENIS_BARANG = "jenis_barang"
        private const val COLUMN_QTY = "qty"
        private const val COLUMN_TANGGAL = "tanggal"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID_BARANG TEXT PRIMARY KEY,"
                + "$COLUMN_NAMA_BARANG TEXT,"
                + "$COLUMN_JENIS_BARANG TEXT,"
                + "$COLUMN_QTY INTEGER,"
                + "$COLUMN_TANGGAL TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(model: DBModel): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID_BARANG, model.idBarang)
        values.put(COLUMN_NAMA_BARANG, model.namaBarang)
        values.put(COLUMN_JENIS_BARANG, model.jenisBarang)
        values.put(COLUMN_QTY, model.qty)
        values.put(COLUMN_TANGGAL, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(model.tanggal))

        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getData(): ArrayList<DBModel> {
        val dataList = ArrayList<DBModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var idBarang: String
        var namaBarang: String
        var jenisBarang: String
        var qty: Int
        var tanggal: Date

        if (cursor != null && cursor.moveToFirst()) {
            val idBarangIndex = cursor.getColumnIndex(COLUMN_ID_BARANG)
            val namaBarangIndex = cursor.getColumnIndex(COLUMN_NAMA_BARANG)
            val jenisBarangIndex = cursor.getColumnIndex(COLUMN_JENIS_BARANG)
            val qtyIndex = cursor.getColumnIndex(COLUMN_QTY)
            val tanggalIndex = cursor.getColumnIndex(COLUMN_TANGGAL)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            do {
                idBarang = cursor.getString(idBarangIndex)
                namaBarang = cursor.getString(namaBarangIndex)
                jenisBarang = cursor.getString(jenisBarangIndex)
                qty = cursor.getInt(qtyIndex)
                tanggal = dateFormat.parse(cursor.getString(tanggalIndex))

                dataList.add(DBModel(idBarang, namaBarang, jenisBarang, qty, tanggal))
            } while (cursor.moveToNext())
        }

        cursor?.close()
        db.close()

        return dataList
    }


    fun updateData(model: DBModel): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAMA_BARANG, model.namaBarang)
        values.put(COLUMN_JENIS_BARANG, model.jenisBarang)
        values.put(COLUMN_QTY, model.qty)
        values.put(COLUMN_TANGGAL, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(model.tanggal))

        return db.update(TABLE_NAME, values, "$COLUMN_ID_BARANG = ?", arrayOf(model.idBarang))
    }

    fun deleteData(idBarang: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID_BARANG = ?", arrayOf(idBarang))
    }
}
