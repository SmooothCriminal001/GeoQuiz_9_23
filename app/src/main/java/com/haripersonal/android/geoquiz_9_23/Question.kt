package com.haripersonal.android.geoquiz_9_23

import androidx.annotation.StringRes

data class Question(
    @StringRes val resId: Int,
    val answer: Boolean,
    var userAnswered:Boolean = false,
    var answerMark: Int = 0,
    var hasCheated: Boolean = false
)