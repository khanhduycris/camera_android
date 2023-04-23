package com.mobiai.app.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mobiai.R
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentPreviewBinding

class PreviewAfterSaveFragment(val pathImage: String) : BaseFragment<FragmentPreviewBinding>() {

    override fun initView() {
        Toast.makeText(requireContext(), getString(R.string.save_sucess), Toast.LENGTH_SHORT).show()
        binding.imgAlbum.setOnClickListener {
            addFragment(GalleryFragment(), true)
        }

        binding.buttonSave.visibility = View.GONE
        binding.imgBack.setOnClickListener {
            closeFragment(this)
        }
        if (SharedPreferenceUtils.pathImageLatest != null) {
            Glide.with(requireContext()).load(pathImage).into(binding.imgAlbum)
        } else {
            binding.imgAlbum.setPadding(20, 20, 20, 20)
        }
        binding.imgFlip.setOnClickListener {
            val imgBitmap = (binding.imgPreview.drawable as BitmapDrawable).bitmap
            val matrix = Matrix().apply { postRotate(90f, imgBitmap.width / 2f, imgBitmap.height / 2f) }
            val rotatedBitmap = Bitmap.createBitmap(imgBitmap, 0, 0, imgBitmap.width, imgBitmap.height, matrix, true)
            binding.imgPreview.setImageBitmap(rotatedBitmap)

        }

        val imgBitmap = BitmapFactory.decodeFile(pathImage)
        binding.imgPreview.setImageBitmap(imgBitmap)
        binding.tvRetake.setOnClickListener {
            closeFragment()
        }

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPreviewBinding {
        return FragmentPreviewBinding.inflate(inflater, container, false)
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment()
    }
}