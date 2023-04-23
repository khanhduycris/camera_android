package com.mobiai.app.ui.fragment

import SettingFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mobiai.R
import com.mobiai.app.utils.SaveUtils
import com.mobiai.app.utils.ShowTutorialEvents
import com.mobiai.app.utils.listenEvent
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentCameraViewBinding
import java.io.File

class MainFragment() : BaseFragment<FragmentCameraViewBinding>() {

    private var galleryFragment = GalleryFragment()
    private var previewFragment: PreviewFragment? = null
    private var settingFragment = SettingFragment()
    private var isFlashOn = false
    private var isShow = true
    private var fileSave: File? = null

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCameraViewBinding = FragmentCameraViewBinding.inflate(inflater, container, false)

    override fun initView() {
        if (SharedPreferenceUtils.pathImageLatest != null) {
            Glide.with(requireContext()).load(SharedPreferenceUtils.pathImageLatest)
                .into(binding.icAlbum)
        } else {
            binding.icAlbum.setPadding(20, 20, 20, 20)
        }

        if (SharedPreferenceUtils.isShowedTutorialFistOpen) {
            binding.tutorial.tutorialView.visibility = View.VISIBLE
        } else {
            binding.tutorial.tutorialView.visibility = View.GONE
        }
        binding.ctnAlbum.setOnClickListener {
            addFragment(galleryFragment, true)
        }

        binding.ctnFlash.setOnClickListener {
            if (!isFlashOn) {
                isFlashOn = true
                binding.cameraView.setFlash(true)
            } else {
                isFlashOn = false
                binding.cameraView.setFlash(false)

            }
        }

        binding.seekbarBrightNess.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress - 24
                binding.cameraView.setBrightness(value)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.seekbarZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.cameraView.zoom((progress.toFloat()) / 4 - 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.ctnTakePicture.setOnClickListener {
            binding.cameraView.capturePicture()
        }


        binding.ctnHide.setOnClickListener {
            if (isShow) {
                hide()
            } else {
                show()
            }
        }

        binding.ctnSetting.setOnClickListener {
            replaceFragment(settingFragment, true)
        }

        binding.cameraView.setOnPreviewListener {
            createFileSaveInternal()
        }
        handlerEvent()
    }

    private fun show() {
        binding.ctnSetting.visibility = View.VISIBLE
        binding.llBrightNess.visibility = View.VISIBLE
        binding.seekbarZoom.visibility = View.VISIBLE
        binding.imgHide.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_hide
            )
        )
        isShow = true
    }

    private fun hide() {
        binding.ctnSetting.visibility = View.INVISIBLE
        binding.llBrightNess.visibility = View.INVISIBLE
        binding.seekbarZoom.visibility = View.INVISIBLE
        binding.imgHide.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_show
            )
        )
        isShow = false
    }

    override fun onResume() {
        super.onResume()
        //TODO khác take picture
        binding.cameraView.startStream()

    }

    override fun onStop() {
        super.onStop()
        binding.cameraView.closeStream()
    }

    private fun createFileSaveInternal() {
        fileSave = null

        fileSave = SaveUtils.getFileName(requireContext())
        SaveUtils.convertBitmapToFile(binding.cameraView.imageByteArray, fileSave!!)
        previewFragment = fileSave?.absolutePath?.let { PreviewFragment(it) }
        previewFragment?.let { addFragment(it) }
    }

    private fun handlerEvent() {
        addDisposable(listenEvent({
            if (it is ShowTutorialEvents) {
                binding.tutorial.tutorialView.visibility = View.VISIBLE
            }
        }, {}))
    }

}