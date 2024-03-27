package com.example.uts_213040050

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class DBAdapter(private val context: Context, private val dataList: ArrayList<DBModel>) : RecyclerView.Adapter<DBAdapter.ViewHolder>() {
    private val dbHelper: DBHelper = DBHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvID: TextView = itemView.findViewById(R.id.tvID)
        private val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        private val tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        private val tvQTY: TextView = itemView.findViewById(R.id.tvQTY)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)

        fun bindData(data: DBModel) {
            tvID.text = "ID: ${data.idBarang}"
            tvNama.text = "Nama: ${data.namaBarang}"
            tvJenis.text = "Jenis: ${data.jenisBarang}"
            tvQTY.text = "QTY: ${data.qty.toString()}"
            tvTanggal.text = "Tanggal: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data.tanggal)}"

            // Menambahkan OnClickListener untuk menampilkan MaterialAlertDialog saat cardview diklik
            itemView.setOnClickListener {
                showAlertDialog(data)
            }
        }

        private fun showAlertDialog(data: DBModel) {
            MaterialAlertDialogBuilder(itemView.context)
                .setTitle("ID:  ${data.idBarang}")
                .setMessage("Nama: ${data.namaBarang}\nJenis: ${data.jenisBarang}\nQTY: ${data.qty}\nTanggal: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(data.tanggal)}")
                .setPositiveButton("Update") { _, _ ->
                    // Kode untuk meng-handle update data
                    val intent = Intent(itemView.context, UpdateActivity::class.java).apply {
                        putExtra("ID", data.idBarang)
                        putExtra("Nama", data.namaBarang)
                        putExtra("Jenis", data.jenisBarang)
                        putExtra("QTY", data.qty)
                        putExtra("Tanggal", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data.tanggal))
                    }
                    itemView.context.startActivity(intent)
                }
                .setNegativeButton("Delete") { _, _ ->
                    MaterialAlertDialogBuilder(itemView.context)
                        .setTitle("Hapus Data")
                        .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                        .setPositiveButton("Ya") { _, _ ->
                            // Kode untuk menghapus data dari database
                            val deletedRows = dbHelper.deleteData(data.idBarang)
                            if (deletedRows > 0) {
                                Toast.makeText(itemView.context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                                // Memperbarui tampilan setelah menghapus data
                                dataList.remove(data)
                                notifyDataSetChanged()
                            } else {
                                Toast.makeText(itemView.context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
