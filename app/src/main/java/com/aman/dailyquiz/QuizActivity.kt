package com.aman.dailyquiz

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.dailyquiz.databinding.ActivityQuizBinding
import com.aman.dailyquiz.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        var questionModelList:List<QuestionModel> = listOf()
        var time : String = ""
    }
    private lateinit var binding: ActivityQuizBinding
    private var countDownTimer:CountDownTimer?=null
    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
        }

        loadQuestions()
        startTimer()

    }

    private fun startTimer() {

        val totalTimeInMills = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMills, 1000L) {

            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                val mins = seconds/60
                val remainingSeconds = seconds%60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", mins, remainingSeconds)

            }

            override fun onFinish() {
                binding.timerIndicatorTextview.text = "00:00"
                Toast.makeText(this@QuizActivity,"Times Up!",Toast.LENGTH_SHORT).show()
                finishQuiz()
            }

        }.start()

    }

    private fun loadQuestions() {
      selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size){
            countDownTimer?.cancel()
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/${questionModelList.size}"
            questionProgressIndicator.progress = (currentQuestionIndex.toFloat()/ questionModelList.size.toFloat()*100).toInt()

            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]

        }
    }

    private fun finishQuiz() {

        val totalQuestions = questionModelList.size
        val percent = ((score.toFloat() / totalQuestions.toFloat()) * 100 ).toInt()


        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percent
            scoreProgressText.text = "$percent %"

            if(percent > 60){
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text = "Oops! You have failed"
                scoreTitle.setTextColor(Color.RED)
            }

            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener{
                finish()
            }

        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }

    private fun highlightCorrectAnswer() {
        binding.apply {
            when (questionModelList[currentQuestionIndex].correct) {
                btn0.text -> btn0.setBackgroundColor(getColor(R.color.green))
                btn1.text -> btn1.setBackgroundColor(getColor(R.color.green))
                btn2.text -> btn2.setBackgroundColor(getColor(R.color.green))
                btn3.text -> btn3.setBackgroundColor(getColor(R.color.green))
            }
        }
    }

    private fun resetOptions() {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
        selectedAnswer = ""
    }

    override fun onClick(v: View?) {
        if (selectedAnswer.isNotEmpty()) {
            return
        }

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickBtn = v as Button
        selectedAnswer = clickBtn.text.toString()




            if (selectedAnswer == questionModelList[currentQuestionIndex].correct)
            {
                clickBtn.setBackgroundColor(getColor(R.color.green))
                score++

                Log.i("Score of Quiz", score.toString())
            }else {
                clickBtn.setBackgroundColor(getColor(R.color.red))

                highlightCorrectAnswer()
            }

        clickBtn.postDelayed({
            currentQuestionIndex++
            loadQuestions()
            resetOptions()
        },2000)
    }


}