package com.example.sqlitedemo

import java.util.*

data class StudentModel (
    val id: Int = getAutoId(),
    val name: String="",
    val email: String=""
        ){
  companion object{
      fun getAutoId():Int
      {
          val random = Random()
          return random.nextInt(100)
      }
  }
}