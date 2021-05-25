package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

//ViewModel, содержащий данные приложения и методы для обработки данных.
class GameViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
//    val currentScrambledWord: LiveData<String>
//        get() = _currentScrambledWord

    // Список слов, используемых в игре
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

//    Обновляет currentWord и currentScrambledWord следующим словом.
    private fun getNextWord() {
    currentWord = allWordsList.random()
    val tempWord = currentWord.toCharArray()
    tempWord.shuffle()
    while (tempWord.toString().equals(currentWord, false)) {
        tempWord.shuffle()
    }
    if (wordsList.contains(currentWord)) {
        getNextWord()
    } else {
        _currentScrambledWord.value = String(tempWord)
        _currentWordCount.value = (_currentWordCount.value)?.inc()
        wordsList.add(currentWord)
    }
}
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }
//    Возвращает истину, если текущее количество слов меньше MAX_NO_OF_WORDS
    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

//    Повторно инициализирует данные игры, чтобы перезапустить игру.
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

//Увеличивает счет игры, если слово игрока верно.
    private fun increaseScore() {
    _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

//    Возвращает истину, если слово игрока верное.
//     Соответственно увеличивает балл
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }
}
//android:text="@string/word_count"
//android:text="@string/score"