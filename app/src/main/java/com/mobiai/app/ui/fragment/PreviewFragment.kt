package com.mobiai.app.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.mobiai.R
import com.mobiai.app.extension.Permission
import com.mobiai.app.ui.dialog.AllowPermissionDialog
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseActivity
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentPreviewBinding
import java.io.File

open class PreviewFragment(private val pathImagePreview: String) :
    BaseFragment<FragmentPreviewBinding>() {
    private val galleryFragment = GalleryFragment()
    private val homeFragment = MainFragment()

    override fun initView() {
        if (SharedPreferenceUtils.pathImageLatest != null) {
            Glide.with(requireContext()).load(SharedPreferenceUtils.pathImageLatest).into(binding.imgAlbum)
        } else {
            binding.imgAlbum.setPadding(32, 32, 32, 32)
        }
        binding.imgBack.setOnClickListener {
            closeFragment(this)
        }

        binding.imgAlbum.setOnClickListener {
            addFragment(galleryFragment, true)
        }
        binding.tvRetake.setOnClickListener {
            addFragment(homeFragment, true)
        }

        binding.imgFlip.setOnClickListener {
            val imgBitmap = (binding.imgPreview.drawable as BitmapDrawable).bitmap
            val matrix = Matrix().apply { postRotate(90f, imgBitmap.width / 2f, imgBitmap.height / 2f) }
            val rotatedBitmap = Bitmap.createBitmap(imgBitmap, 0, 0, imgBitmap.width, imgBitmap.height, matrix, true)
            binding.imgPreview.setImageBitmap(rotatedBitmap)

        }

        binding.buttonSave.setOnClickListener {
            if (!Permission.hasWriteStoragePermission(requireContext())) {
                showPermissionDialog()
            } else {
                saveImageToExternal()
            }
        }
        val imgBitmap = BitmapFactory.decodeFile(pathImagePreview)
        binding.imgPreview.setImageBitmap(imgBitmap)
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPreviewBinding = FragmentPreviewBinding.inflate(inflater, container, false)

    private fun showPermissionDialog() {
        AllowPermissionDialog(requireContext()) {
            resultLauncher.launch(PERMISSIONS_STORAGE)
        }.show()
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            val writeExternalRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (writeExternalRationale) {
                if (isGranted) {
                    saveImageToExternal()
                } else {
                    AllowPermissionDialog(requireContext(), true) {
                        gotoSetting()
                        isGoToSetting = true
                    }.show()
                }
            } else {
                if (isGranted) {
                    saveImageToExternal()
                } else {
                    AllowPermissionDialog(requireContext(), true) {
                        gotoSetting()
                        isGoToSetting = true
                    }.show()
                }
            }

        }

    private fun saveImageToExternal() {
        if (!requireActivity().isDestroyed || !requireActivity().isFinishing) {
            if (saveAsync.state == Thread.State.NEW) {
                showLoading()
                saveAsync.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGoToSetting && Permission.hasWriteStoragePermission(requireContext())) {
            isGoToSetting = false
            saveImageToExternal()
        }
    }

    private val saveAsync: Thread = object : Thread() {

        override fun run() {
            val pictureFileDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "/Magnifier"
            )
            if (pictureFileDir.exists() || pictureFileDir.mkdirs()) {
                val pictureFile =
                    File(pictureFileDir, "Magnifier_" + System.currentTimeMillis() + ".png")
                try {
                    if (!pictureFile.exists()) {
                        pictureFile.createNewFile()
                    }
                    val pathImageSave: String = pathImagePreview
                    File(pathImageSave).copyTo(pictureFile)
                    requireContext().sendBroadcast(
                        Intent(
                            "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                            Uri.fromFile(pictureFile)
                        )
                    )
                    (requireActivity() as BaseActivity<*>).runOnUiThread {
                        hideLoading()
                        SharedPreferenceUtils.pathImageLatest = pictureFile.absolutePath
                        replaceFragment(PreviewAfterSaveFragment(pictureFile.absolutePath), true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
    }


    fun File.copyTo(file: File) {
        inputStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}