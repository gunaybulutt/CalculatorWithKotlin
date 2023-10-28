package com.gunay.calculatorwithkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.gunay.calculatorwithkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun numberAction(view: View){


        if (view is Button){

            if (view.text == "."){
                if (canAddDecimal){
                    binding.workingsTV.append(view.text)
                    canAddDecimal = false
                }
            }else{
                binding.workingsTV.append(view.text)

            }
            canAddOperation = true
        }

    }

    fun operationAction(view: View){
        if (view is Button && canAddOperation){
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }

    }

    fun backSpaceAction(view: View) {

      val length = binding.workingsTV.length()
        if (length > 0){
            val updatedText = binding.workingsTV.text.subSequence(0, length - 1)
            binding.workingsTV.text = updatedText
        }

    }

    fun allClearAction(view: View) {

        binding.workingsTV.text=""
        binding.resultsTV.text=""

    }


    fun equalsAction(view: View) {
        binding.resultsTV.text = calculateResult()
    }

    private fun calculateResult(): String {
        val digitsOperators = digitOperators()
        if (digitsOperators.isEmpty()){
            return ""
        }

        val timeDivision = timesDivisionCalculate(digitsOperators)
        if(timeDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timeDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if (operator == '+'){
                    result += nextDigit
                }
                if (operator == '-'){
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')){
            list = calcTimesDive(list)
        }

        return list
    }

    private fun calcTimesDive(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float

                when(operator){

                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1

                    }

                    else->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }

            if (i > restartIndex){
                newList.add(passedList[i])
            }
        }
        return  newList
    }

    private fun digitOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.workingsTV.text){
            if (character.isDigit() || character == '.'){
                currentDigit += character
            }else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return  list
    }
}























