package com.example.sqlitedemo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class SQLiteHelper (context:Context):SQLiteOpenHelper(context,DATABASENAME,null,DATABASEVERSION) {

    companion object{
        private const val DATABASEVERSION = 1
        private const val DATABASENAME = "student.db"
        private const val TBL_NAME = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(db: SQLiteDatabase?) {
      val creatTableStudent = ("CREATE TABLE " + TBL_NAME +"( " +
              ID + " INTEGER PRIMARY KEY, "+ NAME+ " TEXT,"+ EMAIL + ")")
        db?.execSQL(creatTableStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_NAME")
        onCreate(db)
    }

    fun insertsStudent(std:StudentModel):Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(EMAIL,std.email)

        val success = db.insert(TBL_NAME,null,contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllStudent():ArrayList<StudentModel>
    {
        val stdList:ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT *FROM $TBL_NAME"
        val db = this.readableDatabase

        val cursor:Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String
        var email:String

        if(cursor.moveToFirst())
        {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))

                val std = StudentModel(id = id,name = name,email = email)
                stdList.add(std)
            }while (cursor.moveToNext())
        }
        return stdList
    }

    fun updateStudent(std:StudentModel):Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(EMAIL,std.email)

        val success = db.update(TBL_NAME, contentValues,"id="+std.id,null)
        db.close()
        return success

    }

    fun deleteStudentById(id:Int):Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,id)
        val success = db.delete(TBL_NAME,"id=$id",null )
        db.close()
        return success
    }
}