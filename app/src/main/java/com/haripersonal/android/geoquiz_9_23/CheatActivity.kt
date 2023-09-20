package com.haripersonal.android.geoquiz_9_23

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haripersonal.android.geoquiz_9_23.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER = "com.haripersonal.android.geoquiz_9_23.answerExtra"
private const val EXTRA_ANSWER_SHOWN = "com.haripersonal.android.geoquiz_9_23.answerIsShown"
class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private var answer = false

    companion object{
        fun newIntent(packageContext: Context, answer: Boolean): Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER, answer)
            }
        }

        fun getCheatResult(resultIntent: Intent): Boolean{
            return resultIntent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answer = intent.getBooleanExtra(EXTRA_ANSWER, false)

        binding.showAnswerButton.setOnClickListener {
            val answerToShow = if(answer) R.string.true_button else R.string.false_button
            binding.answerTextView.setText(answerToShow)
            setAnswerShownResult(true)
        }
    }

    fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }

        setResult(Activity.RESULT_OK, data)
    }
}