/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Создаем ViewModel при первом создании фрагмента.
    // Если фрагмент создается заново, он получает тот же экземпляр GameViewModel, созданный
    // первый фрагмент

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container,false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment",
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameViewModel = viewModel

        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Укажите представление фрагмента как владельца жизненного цикла привязки.
        // Это используется для того, чтобы привязка могла наблюдать обновления LiveData
        binding.lifecycleOwner = viewLifecycleOwner

// Настраиваем прослушиватель кликов для кнопок «Отправить» и «Пропустить».
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI

//          binding.score.text = getString(R.string.score, viewModel.score.value)
//    binding.wordCount.text = getString(
//        R.string.word_count, viewModel.currentWordCount.value, MAX_NO_OF_WORDS)
        // Observe the currentScrambledWord LiveData.
// Observe the scrambledCharArray LiveData, passing in the LifecycleOwner and the observer.

//        viewModel.score.observe(viewLifecycleOwner,
//            { newScore ->
//                binding.score.text = getString(R.string.score, newScore)
//            })
//        viewModel.currentWordCount.observe(viewLifecycleOwner,
//            { newWordCount ->
//                binding.wordCount.text =
//                    getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
//            })
    }

//     Проверяет слово пользователя и соответственно обновляет счет.
//     Отображает следующее зашифрованное слово.
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
    if (viewModel.isUserWordCorrect(playerWord)) {
        setErrorTextField(false)
        if (!viewModel.nextWord()) {
            showFinalScoreDialog()
        }
    } else {
        setErrorTextField(true)
    }
    }

//     Пропускает текущее слово без изменения оценки.
//     Увеличивает количество слов.
    private fun onSkipWord() {
        if (viewModel.nextWord()) {

            setErrorTextField(false)

        } else {
            showFinalScoreDialog()
        }
    }

//    Получает случайное слово для списка слов и перемешивает буквы в нем.
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

//    Повторно инициализирует данные в ViewModel и обновляет представления новыми данными, чтобы
//     перезапустить игру.
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)

    }

    private fun exitGame() {
        activity?.finish()
    }

//     Sets and resets the text field error status.
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }


//Создает и показывает AlertDialog с окончательной оценкой.
    private fun showFinalScoreDialog() {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.congratulations))
        .setMessage(getString(R.string.you_scored, viewModel.score.value))
        .setCancelable(false)
        .setNegativeButton(getString(R.string.exit)) { _, _ ->
            exitGame()
        }
        .setPositiveButton(getString(R.string.play_again)) { _, _ ->
            restartGame()
        }
        .show()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }
//    Skips the current word without changing the score.
}
