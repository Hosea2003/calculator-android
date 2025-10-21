package com.rindra.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private lateinit var gridButtons: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvExpression = findViewById(R.id.tv_expression)
        tvResult = findViewById(R.id.tv_result)
        gridButtons = findViewById(R.id.grid_buttons)

        for(i in 0 until gridButtons.childCount){
            val view = gridButtons.getChildAt(i)
            if(view is Button){
                view.setOnClickListener { this.handleButtonClick(view.text.toString()) }
            }
        }
    }

    private fun handleButtonClick(value:String){
        when(value){
            "AC"->{
                tvExpression.text=""
                tvResult.text=""
            }
            "⌫"->{
                val current = tvResult.text.toString()
                if(current.isNotEmpty()){
                    tvResult.text = current.dropLast(1)
                }
            }
            "="->{
                evaluateExpression()
            }
            else->{
                tvResult.append(value)
            }
        }
    }

    private fun evaluateExpression(){
        val expression = tvResult.text.toString()
        if(expression.isEmpty()) return

        try{
            val safeExpression = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")

            println(safeExpression)

            val engine = ExpressionBuilder(safeExpression).build().evaluate()
            tvExpression.text = expression
            tvResult.text = formatResult(engine.toString())
        }
        catch (e: Exception){
            tvResult.text="Syntax error"
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatResult(result: String): String {
        return try {
            val num = result.toDouble()
            if (num == num.toInt().toDouble()) num.toInt().toString()
            else String.format("%.4f", num).trimEnd('0').trimEnd('.')
        } catch (e: Exception) {
            result
        }
    }
}