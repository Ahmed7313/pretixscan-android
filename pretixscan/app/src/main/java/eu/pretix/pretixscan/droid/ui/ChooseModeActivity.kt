package eu.pretix.pretixscan.droid.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import eu.pretix.pretixscan.droid.R
import eu.pretix.pretixscan.droid.databinding.ActivityChooseModeBinding

class ChooseModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_mode)

        binding.btnNext.setOnClickListener {
            when {
                binding.cbSignIn.isChecked -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                binding.cbCheckIn.isChecked -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else -> {
                    Toast.makeText(this, getString(R.string.error_no_option_selected), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
