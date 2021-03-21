package com.hjhj.app3_diary_handlersharedpreference

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity(){

    private val handler = Handler(Looper.getMainLooper())//루퍼란걸 넣는데, 메인루퍼를 넣어서 MainThread에 연결된 핸들러가 생긴거임

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)

        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail",""))

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit{
                putString("detail", diaryEditText.text.toString())
            }
            Log.d("tag","save!!: ${diaryEditText.text.toString()}")
        }

         diaryEditText.addTextChangedListener{
             //handler란... 새로운 쓰레드에서는 uiThread(메인쓰레드)가 아니니까 메인쓰레드와 연결해주는 애임
             //즉 runonUiThread()에서 쓰이는게 핸들러임
             handler.removeCallbacks(runnable)//addTextChangedListener함수안에 들어왓을때, 이전에 펜딩되어있는 애들이 있다면 삭제
             //post, send등등 기능이있는데, 그중 postdelayed는 몇초 이후에 함수를 실행하는 애
             handler.postDelayed(runnable, 500)

             Log.d("tag","textchagned:: $it")
         }
    }
}