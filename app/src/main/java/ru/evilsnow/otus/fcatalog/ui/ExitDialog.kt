package ru.evilsnow.otus.fcatalog.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import ru.evilsnow.otus.fcatalog.R
import android.view.Gravity
import android.view.ViewGroup



class ExitDialog(context: Context, private val confirmCallback: () -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_exit)

        window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }

        findViewById<Button>(R.id.exit_positive_button).setOnClickListener { confirmCallback.invoke() }
        findViewById<Button>(R.id.exit_negative_button).setOnClickListener { this.hide() }
    }

}