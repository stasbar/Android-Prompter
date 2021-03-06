/*
 * Copyright (c) 2017. Stanislaw stasbar Baranski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.stasbar.prompter

import android.content.Context
import android.text.InputType
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager

/**
 * Created by stasbar on 31.10.2017
 */
abstract class Builder(val fragmentManager: FragmentManager) {
    protected lateinit var dialog: Prompter
    protected var currentValue: String? = null
    protected var message: String? = null
    protected var hintMode = false
    protected var inputType: Int = InputType.TYPE_NULL
    protected var validator: ((String) -> Boolean) = { true }
    protected var showValueResolver: (() -> String)? = null
    protected var title: String? = null
    protected var failMessage: String? = null
    protected var animateOnFail: Boolean = true
    protected val onValueChangedListeners: ArrayList<OnChangeListener> = ArrayList()
    protected var allowEmpty = false
    abstract fun getContext() : Context

    open fun title(@StringRes stringRes: Int) = apply {
        this.title = getContext().getString(stringRes)
    }

    open fun title(title: String) = apply {
        this.title = title
    }

    open fun message(message: String) = apply {
        this.message = message
    }

    open fun message(@StringRes stringRes: Int) = apply {
        this.message = getContext().getString(stringRes)
    }


    open fun inputType(inputType: Int) = apply {
        this.inputType = inputType
    }

    open fun hintMode() = apply { this.hintMode = true }

    open fun allowEmpty() = apply { this.allowEmpty = true }

    open fun showValue(valueResolver: () -> String) = apply {
        this.showValueResolver = valueResolver
    }

    open fun currentValue(currentValue: String) = apply {
        this.currentValue = currentValue
    }

    open fun addOnValueChangeListener(onValueChanged: (String) -> Unit) = apply {
        onValueChangedListeners.add(object : OnChangeListener {
            override fun onChangeListener(newValue: String) {
                onValueChanged(newValue)
            }
        })
    }

    open fun setOnValueChangeListener(onValueChanged: (String) -> Unit) = apply {
        onValueChangedListeners.clear()
        onValueChangedListeners.add(object : OnChangeListener {
            override fun onChangeListener(newValue: String) {
                onValueChanged(newValue)
            }
        })
    }


    open fun validate(failMessage: String, validator: (String) -> Boolean) = apply {
        this.validator = validator
        this.failMessage = failMessage
    }

    open fun validate(validator: (String) -> Boolean) = apply {
        this.validator = validator
    }


    protected open fun show() {
        val inputType = if (inputType == InputType.TYPE_NULL) figureDefaultInputType() else this.inputType
        val currentValue: String = when{
            this.showValueResolver != null -> showValueResolver!!()
            currentValue != null -> this.currentValue!!
            else -> figureCurrentValue()
        }
        val title: String = if (title == null) getContext().getString(R.string.enter_new_value) else title!!
        val failMessage: String = if (failMessage == null) getContext().getString(R.string.invalid_value) else failMessage!!

        dialog = Prompter.newInstance(inputType = inputType
                , title = title
                , message = message
                , currentValue = currentValue
                , hintMode = hintMode
                , onValueChangedListeners = onValueChangedListeners
                , validator = validator
                , animateOnFail = animateOnFail
                , allowEmpty = allowEmpty
                , failMessage = failMessage
        )

        dialog.show(fragmentManager,"prompter")

    }


    protected abstract fun figureCurrentValue(): String
    protected abstract fun figureDefaultInputType(): Int

}