package com.haripersonal.android.geoquiz_9_23

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuestionViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(){

	private val questionBank = listOf(
		Question(R.string.question_australia, true),
		Question(R.string.question_oceans, true),
		Question(R.string.question_mideast, false),
		Question(R.string.question_africa, false),
		Question(R.string.question_americas, true),
		Question(R.string.question_asia, true)
	)

	private var currentIndex
		get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
		set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

	private var resultPercent: Double? = null

    init {

    }

	val currentQuestion: Question
		get() = questionBank[currentIndex]

	var cheated: Boolean
		get() = currentQuestion.hasCheated
		set(value){
			currentQuestion.hasCheated = value
		}

	fun moveToNext(){
		currentIndex = (currentIndex + 1) % (questionBank.size)
	}

	fun moveToPrev(){
		val prevIndex = currentIndex - 1

		currentIndex = if(prevIndex < 0) (questionBank.size - 1) else prevIndex
	}

	fun isAnswerCorrect(response: Boolean): Boolean{
		val question = questionBank[currentIndex]
		val result = response == question.answer

		question.answerMark = if(result) 1 else 0
		question.userAnswered = true
		return result
	}

	fun getResult(): Double?{
		if(resultPercent == null){
			val allQuestionsAnswered = questionBank.all { it.userAnswered }

			if(allQuestionsAnswered){
				val total = questionBank.filter { !it.hasCheated }.sumOf { it.answerMark }
				val decimalFormat = DecimalFormat("#.##")
				decimalFormat.roundingMode = RoundingMode.HALF_UP
				val percentRaw = ((100.0 / questionBank.size) * total)
				resultPercent = decimalFormat.format(percentRaw).toDouble()
				return resultPercent
			}
		}

		return null
	}
}