package com.hjhj.app3_diary_handlersharedpreference

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

//    view를 lazy하게 초기화하는 이유는 MainActivity라는 클래스가 생성될 시점에는 뷰가 아직 그려지지 않아서.
//    onCreate()함수가 실행되는 시점이 뷰가 그려지는 시점이기 때문임. 그러므로 onCreate에서 레이지하게 초기화를 해야함
    private val numberPicker1: NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply{
                this.minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker2: NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply{
                this.minValue = 0
                this.maxValue = 9
            }
    }
    private val numberPicker3: NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply{
                minValue = 0
                maxValue = 9
            }
    }
    private val openButton: AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.openButton)
    }
    private val changePsswordButton: AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }
    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //호출을 해줘야 생성됨
        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {
            if(changePasswordMode){
                Toast.makeText(this, "비밀번호 변경 중입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreferences =getSharedPreferences("passowrd", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //처음에 값이 없다면 000으로 디폴트
            if(passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            }else{
                showErrorAlertDialg()
            }
        }
        changePsswordButton.setOnClickListener {

            val passwordPreferences =getSharedPreferences("passowrd", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
            if(changePasswordMode){
                //번호를 저장하는 기능


                //sharedpreference저장하는 법 두가지:
                //commit: 파일이 다 저장될때까지 uiThread멈추고 기다림
                //apply: 비동기적으로 바로 저장하는 방식

//                passwordPreferences.edit {
//                    val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
//                    putString("password",passwordFromUser)
//                    commit()//이렇게 커밋해도 됨
//                }
                //바로 위랑 똑같은거임 원래 commit=false가 디폴트인데, true로 하려면 따로 써주면 됨
                passwordPreferences.edit(true){
                    putString("password",passwordFromUser)
                }
                changePasswordMode = false //저장끝났으니 다시 처음으로
                changePsswordButton.setBackgroundColor(Color.BLACK)

            }else{
                //처음에 값이 없다면 000으로 디폴트
                if(passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드 입력", Toast.LENGTH_SHORT).show()
                    changePsswordButton.setBackgroundColor(Color.RED)

                }else{
                    showErrorAlertDialg()
                }
            }
        }

    }

    private fun showErrorAlertDialg(){
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다")
            .setPositiveButton("확인"){dialog, which ->
                //누르면 버튼꺼지는거라 다른 기능안넣었음
            }
            .create().show()
    }
}