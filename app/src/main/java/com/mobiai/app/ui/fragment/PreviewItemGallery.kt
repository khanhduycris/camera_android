package com.mobiai.app.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.mobiai.R
import com.mobiai.app.ui.dialog.DeleteDialog
import com.mobiai.app.utils.ReloadAllGallery
import com.mobiai.app.utils.RxBus
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentItemGalleryBinding
import java.io.File

class PreviewItemGallery(val pathImage : String) : BaseFragment<FragmentItemGalleryBinding>() {

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment()
    }

    override fun initView() {
        binding.imgBack.setOnClickListener {
            closeFragment(this)
        }
        binding.imgFlip.setOnClickListener {
            val imgBitmap = (binding.imgPreview.drawable as BitmapDrawable).bitmap
            val matrix = Matrix().apply { postRotate(90f, imgBitmap.width / 2f, imgBitmap.height / 2f) }
            val rotatedBitmap = Bitmap.createBitmap(imgBitmap, 0, 0, imgBitmap.width, imgBitmap.height, matrix, true)
            binding.imgPreview.setImageBitmap(rotatedBitmap)

        }

        binding.imgDelete.setOnClickListener {
            DeleteDialog(requireContext()){
                val file = File(pathImage)
                runBackground({
                    file.delete()
                }, {
                   if(it){
                       Toast.makeText(requireContext(), R.string.delete_file_success, Toast.LENGTH_SHORT).show()
                       closeFragment(this)
                       RxBus.publish(ReloadAllGallery())
                   }
                })
            }.show()
        }

        val imgBitmap = BitmapFactory.decodeFile(pathImage)
        binding.imgPreview.setImageBitmap(imgBitmap)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentItemGalleryBinding {
        return FragmentItemGalleryBinding.inflate(inflater, container, false)
    }
}