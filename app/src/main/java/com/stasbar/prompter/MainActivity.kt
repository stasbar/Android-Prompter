/*
 * Copyright (c) 2017 Stanislaw stasbar Baranski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *          __             __
 *    _____/ /_____ ______/ /_  ____ ______
 *   / ___/ __/ __ `/ ___/ __ \/ __ `/ ___/
 *  (__  ) /_/ /_/ (__  ) /_/ / /_/ / /
 * /____/\__/\__,_/____/_.___/\__,_/_/
 *            stachu@stasbar.com
 */

package com.stasbar.prompter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    class Book(var size: Int = 100, var currentPosition: Int = 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Prompter.showWithClick(etPage, supportFragmentManager)

        val book = Book()
        Prompter.showWithClick(tvPage, supportFragmentManager)
                .message("Enter page number")
                .showValue { book.currentPosition.toString() }
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .hintMode() //Current value will be displayed as hint
                .validate("Please enter page in range of [1, ${book.size}]") {
                    it.toInt() in 1..book.size
                }

        val prompter = Prompter.on(tvPage, supportFragmentManager)
                .message("Enter page number")
                .validate("Please enter page in range of [1, ${book.size}]") {
                    it.toInt() in 1..book.size
                }

        container.setOnClickListener { prompter.show() }


    }

    companion object {
        const val TAG = "Prompter"
    }
}
