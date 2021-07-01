package com.srp.ewayspanel.ui.landing

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.srp.eways.base.BaseViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.util.Utils
import com.srp.ewayspanel.BuildConfig
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import java.io.File


class UpdateFragment : NavigationMemberFragment<BaseViewModel>() {

    private lateinit var mProgressBar: ProgressBar
    private lateinit var mProgressText: AppCompatTextView

    private lateinit var updateUrl: String
    private var downloadId: Int = 0

    companion object {
        @JvmStatic
        fun newInstance(): UpdateFragment = UpdateFragment()

        private const val OLD_VERSION_KEY = "old_version_key"
    }

    fun setUpdateUrl(url: String) {
        updateUrl = url
    }

    override fun acquireViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_update
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mProgressBar = view.findViewById(R.id.progressBar)
        mProgressText = view.findViewById(R.id.progress_text)

        val cache = DI.getPreferenceCache()
        cache.put(OLD_VERSION_KEY, BuildConfig.VERSION_CODE)

        downloadId = PRDownloader.download(updateUrl, getRootDirPath(), "ewyas.apk")
                .build()
                .setOnStartOrResumeListener {
                    mProgressBar.isIndeterminate = false
                }
                .setOnPauseListener { }
                .setOnCancelListener {
                    mProgressBar.progress = 0
                    mProgressText.text = ""
                    mProgressBar.isIndeterminate = false
                }
                .setOnProgressListener { progress ->
                    val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                    mProgressBar.progress = progressPercent.toInt()
                    mProgressText.text = getProgressDisplayLine(progressPercent)
                    mProgressBar.isIndeterminate = false
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        getInstallIntent()
                        onBackPressed()
                    }

                    override fun onError(error: Error) {
                        Toast.makeText(context, getString(R.string.network_error_undefined), Toast.LENGTH_SHORT).show()
                        mProgressText.text = ""
                        mProgressBar.progress = 0
                        mProgressBar.isIndeterminate = false

                    }
                })
    }

    private fun getProgressDisplayLine(percent: Long): String? {
        return "% " + Utils.toPersianNumber(percent)
    }

    private fun getRootDirPath(): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(context!!.applicationContext,
                    null)[0]
            file.absolutePath
        } else {
            context!!.applicationContext.filesDir.absolutePath
        }
    }

    private fun getInstallIntent() {
        val file = File(getRootDirPath(), "/ewyas.apk")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fileUri = FileProvider.getUriForFile(context!!, context!!.packageName + ".fileprovider", file)
            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity!!.startActivity(Intent.createChooser(intent, "asd"))
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity!!.startActivity(intent)
        }
    }

    override fun onDetach() {
        PRDownloader.pause(downloadId)
        PRDownloader.cancel(downloadId)
        super.onDetach()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            PRDownloader.pause(downloadId)
            PRDownloader.cancel(downloadId)
        }
    }

}