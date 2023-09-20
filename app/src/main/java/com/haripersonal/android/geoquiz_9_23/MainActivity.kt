package com.haripersonal.android.geoquiz_9_23

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.haripersonal.android.geoquiz_9_23.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuestionViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
    	ActivityResultContracts.StartActivityForResult()
    ) { result ->
    	if(result.resultCode == Activity.RESULT_OK){
            val hasCheated = result.data?.let { CheatActivity.getCheatResult(it) } ?: false

            if(hasCheated){
                quizViewModel.cheated = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            moveToNext()
        }

        binding.prevButton.setOnClickListener {
            moveToPrev()
        }

        binding.questionTextView.setOnClickListener {
            moveToNext()
        }

        binding.cheatButton.setOnClickListener {
            val intentToLaunch = CheatActivity.newIntent(this@MainActivity, quizViewModel.currentQuestion.answer)
            cheatLauncher.launch(intentToLaunch)
        }

        updateQuestion()
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }


    fun reAssessAnswerButtons(){
        binding.trueButton.isEnabled = !quizViewModel.currentQuestion.userAnswered
        binding.falseButton.isEnabled = !quizViewModel.currentQuestion.userAnswered
    }

    fun updateQuestion(){
        binding.questionTextView.setText(quizViewModel.currentQuestion.resId)
        reAssessAnswerButtons()
    }

    fun moveToNext(){
        quizViewModel.moveToNext()
        updateQuestion()
    }

    fun moveToPrev(){
        quizViewModel.moveToPrev()
        updateQuestion()
    }

    fun checkAnswer(response: Boolean){
        val isAnswerCorrect = quizViewModel.isAnswerCorrect(response)

        val toastMessageResId = when{
            quizViewModel.cheated   -> R.string.judgment_toast
            isAnswerCorrect         -> R.string.correct_toast
            else                    -> R.string.incorrect_toast
        }

        reAssessAnswerButtons()
        showToast(toastMessageResId)

        val totalResult = quizViewModel.getResult()
        totalResult?.let { showToast("You have got $it percent!") }
    }
}