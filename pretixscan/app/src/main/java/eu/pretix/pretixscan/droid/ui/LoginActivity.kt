package eu.pretix.pretixscan.droid.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import eu.pretix.libpretixsync.api.PretixApi
import eu.pretix.pretixscan.droid.AndroidHttpClientFactory
import eu.pretix.pretixscan.droid.AppConfig
import eu.pretix.pretixscan.droid.BuildConfig
import eu.pretix.pretixscan.droid.PretixScan
import eu.pretix.pretixscan.droid.R
import eu.pretix.pretixscan.droid.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val conf = AppConfig(this)
        val api = PretixApi.fromConfig(conf, AndroidHttpClientFactory(application as PretixScan))
        val body = JSONObject().apply {
            put("hardware_brand", Build.BRAND)
            put("hardware_model", Build.MODEL)
            put("os_name", (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.BASE_OS else "").ifEmpty { "Android" })
            put("os_version", Build.VERSION.RELEASE)
            put("software_brand", "pretixSCAN Android")
            put("software_version", BuildConfig.VERSION_NAME)
            put("user", binding.etUser.text.toString())
            put("password", binding.etPassword.text.toString())
        }

        val pd = ProgressDialog(this).apply {
            isIndeterminate = true
            setMessage(getString(R.string.progress_registering))
            setCancelable(false)
            show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.postResource(api.apiURL("device/user-login"), body)
                runOnUiThread {
                    pd.dismiss()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    pd.dismiss()
                    Toast.makeText(this@LoginActivity, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
