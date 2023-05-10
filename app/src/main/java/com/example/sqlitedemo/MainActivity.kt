package com.example.sqlitedemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName : EditText
    private lateinit var edEmail : EditText
    private lateinit var btnAdd : Button
    private lateinit var btnView : Button
    private lateinit var btnUpdate : Button

    private lateinit var sqlitehelper : SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std:StudentModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()

        sqlitehelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener { getStudent() }
        btnUpdate.setOnClickListener { updateStudent() }
        adapter?.setOnClickListener {
            Toast.makeText(this,"${it.name}",Toast.LENGTH_LONG).show()
            edName.setText(it.name)
            edEmail.setText(it.email)
            std = it
        }
        adapter?.setOnClicDeleteItem {
                deleteStudent(it.id)
        }
    }

    private fun addStudent()
    {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this,"Data tidak boleh kosong",Toast.LENGTH_LONG).show()
        }
        else
        {
            val std = StudentModel(name = name, email = email)
            val status = sqlitehelper.insertsStudent(std)

            if(status > -1)
            {
                Toast.makeText(this,"Data Student Ditambahkan",Toast.LENGTH_LONG).show()
                clearText()
                getStudent()
            }
            else
            {
                Toast.makeText(this,"Data Student Tidak Disimpan",Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getStudent()
    {
     val stdList = sqlitehelper.getAllStudent()
        Log.e("MainActivity","${stdList.size}")
        adapter?.addItems(stdList)
    }

    fun updateStudent()
    {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name == std?.name && email == std?.email)
        {
            Toast.makeText(this,"Data Tidak Terupdate",Toast.LENGTH_SHORT).show()
            return
        }
        if(std == null) return
        val std = StudentModel(id = std!!.id,name = name, email = email)

        val status = sqlitehelper.updateStudent(std)
        if(status > -1)
        {
            Toast.makeText(this,"Update Data",Toast.LENGTH_SHORT).show()
            clearText()
            getStudent()
        }
        else
        {
            Toast.makeText(this,"Gagal Update Data",Toast.LENGTH_SHORT).show()
        }


    }

    private fun deleteStudent(id:Int)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah Anda ingin menghapus data ini?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog,_->
            sqlitehelper.deleteStudentById(id)
            getStudent()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog,_->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun clearText()
    {
        edEmail.setText("")
        edName.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView()
    {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    fun initView()
    {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        recyclerView = findViewById(R.id.recyclerView)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

}