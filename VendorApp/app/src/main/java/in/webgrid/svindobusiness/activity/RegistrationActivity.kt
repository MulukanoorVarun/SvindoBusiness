package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import`in`.webgrid.svindobusiness.databinding.DouctmentVerificationScreenBinding

@SuppressLint("StaticFieldLeak")
lateinit var douctmentverifybinding: DouctmentVerificationScreenBinding
class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        douctmentverifybinding = DouctmentVerificationScreenBinding.inflate(layoutInflater)
        setContentView(douctmentverifybinding.root)

    }
}