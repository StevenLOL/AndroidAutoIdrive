package me.hufman.androidautoidrive.phoneui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.earnstone.perf.Registry
import kotlinx.android.synthetic.main.activity_setup.*
import me.hufman.androidautoidrive.BuildConfig
import me.hufman.androidautoidrive.MutableAppSettings
import me.hufman.androidautoidrive.PercentileCounter
import me.hufman.androidautoidrive.R
import me.hufman.androidautoidrive.carapp.music.MusicAppMode
import me.hufman.idriveconnectionkit.android.SecurityService
import java.text.SimpleDateFormat
import java.util.*

class SetupActivity : AppCompatActivity() {
	companion object {
		const val INTENT_REDRAW = "me.hufman.androidautoidrive.SetupActivity.REDRAW"
		fun redraw(context: Context) {
			context.sendBroadcast(Intent(INTENT_REDRAW))
		}
	}

	val redrawListener = object: BroadcastReceiver() {
		override fun onReceive(p0: Context?, p1: Intent?) {
			redraw()
		}
	}

	val handler = Handler()
	val redrawTask: Runnable = Runnable {
		redraw()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_setup)

		btnInstallBmwClassic.setOnClickListener { installBMWClassic() }
		btnInstallMissingBMWClassic.setOnClickListener { installBMWClassic() }
		btnInstallMiniClassic.setOnClickListener { installMiniClassic() }
		btnInstallMissingMiniClassic.setOnClickListener { installMiniClassic() }

		this.registerReceiver(redrawListener, IntentFilter(INTENT_REDRAW))
	}

	override fun onResume() {
		super.onResume()

		redraw()
	}

	override fun onPause() {
		super.onPause()

		handler.removeCallbacks(redrawTask)
	}

	override fun onDestroy() {
		super.onDestroy()
		this.unregisterReceiver(redrawListener)
	}

	fun isConnectedSecurityConnected(): Boolean {
		return SecurityService.isConnected()
	}
	fun isConnectedNewInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			!it.contains("Classic")
		}
	}

	fun isBMWConnectedInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("BMW")
		}
	}
	fun isBMWConnectedClassicInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("BMW") &&
			it.contains("Classic")
		}
	}
	fun isBMWConnectedNewInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("BMW") &&
			!it.contains("Classic")
		}
	}

	fun isMiniConnectedInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("Mini")
		}
	}
	fun isMiniConnectedClassicInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("Mini") &&
			it.contains("Classic")
		}
	}
	fun isMiniConnectedNewInstalled(): Boolean {
		return SecurityService.installedSecurityServices.any {
			it.startsWith("Mini") &&
			!it.contains("Classic")
		}
	}

	fun redraw() {
		paneConnectedBMWNewInstalled.visible = isConnectedNewInstalled() && !isConnectedSecurityConnected() && isBMWConnectedNewInstalled()
		paneConnectedMiniNewInstalled.visible = isConnectedNewInstalled() && !isConnectedSecurityConnected() && !isBMWConnectedNewInstalled() && isMiniConnectedNewInstalled()
		paneBMWMissing.visible = !(isConnectedNewInstalled() && !isConnectedSecurityConnected()) && !isBMWConnectedInstalled()
		paneMiniMissing.visible = !(isConnectedNewInstalled() && !isConnectedSecurityConnected()) && !isMiniConnectedInstalled()
		paneBMWReady.visible = isConnectedSecurityConnected() && isBMWConnectedInstalled()
		paneMiniReady.visible = isConnectedSecurityConnected() && isMiniConnectedInstalled()

		val buildTime = SimpleDateFormat.getDateTimeInstance().format(Date(BuildConfig.BUILD_TIME))
		txtBuildInfo.text = getString(R.string.txt_build_info, BuildConfig.VERSION_NAME, buildTime)

		val latency = Registry.getCounter("Performance", null, "Car Latency")
		paneCarPerformance.visible = latency != null
		if (latency != null && latency is PercentileCounter) {
			txtCarLatency.text = "${latency.name}: $latency"

			val musicAppMode = MusicAppMode(MutableAppSettings(this), latency)
			txtCarLatency.text = "${latency.name}: ${latency.percentile(0.0)}/$latency/${latency.percentile(1.0)}\nWill use audio context: ${musicAppMode.shouldRequestAudioContext()}"
		}

		val carCapabilities = synchronized(DebugStatus.carCapabilities) {
			DebugStatus.carCapabilities.map {
				"${it.key}: ${it.value}"
			}.sorted().joinToString("\n")
		}
		txtCarCapabilities.text = carCapabilities
		paneCarCapabilities.visible = carCapabilities.isNotEmpty()

		handler.removeCallbacks(redrawTask)
		handler.postDelayed(redrawTask, 1000)
	}

	fun installBMWClassic() {
		val intent = Intent(Intent.ACTION_VIEW).apply {
			data = Uri.parse("https://play.google.com/store/apps/details?id=com.bmwgroup.connected.bmw.usa")
		}
		startActivity(intent)
	}

	fun installMiniClassic() {
		val intent = Intent(Intent.ACTION_VIEW).apply {
			data = Uri.parse("https://play.google.com/store/apps/details?id=com.bmwgroup.connected.mini.usa")
		}
		startActivity(intent)
	}
}

var View.visible: Boolean
	get() { return this.visibility == VISIBLE }
	set(value) {this.visibility = if (value) VISIBLE else GONE}