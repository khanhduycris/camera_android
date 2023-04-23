package com.mobiai.app.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.exoplayer2.c.i
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.mobiai.app.adapter.ImageAdapter
import com.mobiai.app.model.Image
import com.mobiai.app.utils.ReloadAllGallery
import com.mobiai.app.utils.listenEvent
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentGalleryBinding
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private var images: ArrayList<Image> = ArrayList()
    private var imageAdapter: ImageAdapter? = null
    private var clickTime = System.currentTimeMillis()
    override fun initView() {
        binding.imgBack.setOnClickListener {
            closeFragment(this)
        }
        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rcv.layoutManager = layoutManager
        imageAdapter =
            ImageAdapter(requireContext(), images, object : ImageAdapter.OnItemImageClicked {
                override fun onItemClicked(image: String) {
                    if(System.currentTimeMillis() - clickTime < 1000){
                        addFragment(PreviewItemGallery(image))
                        clickTime = System.currentTimeMillis()
                    }
                }
            })
        binding.rcv.adapter = imageAdapter

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    readSdCard()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                }
            })
            .check()

        binding.takePicture.setOnClickListener {
            closeFragment()
        }
        handlerEvent()

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)

    private fun readSdCard() {
        runBackground({
            queryImage()
        }, {
            if (it.isNotEmpty()) {
                binding.rcv.visibility = View.VISIBLE
                binding.ctnNoImage.visibility = View.GONE
                imageAdapter?.setItems(it)
            } else {
                SharedPreferenceUtils.pathImageLatest = null
                binding.rcv.visibility = View.GONE
                binding.ctnNoImage.visibility = View.VISIBLE
            }
        })
    }

    private fun queryImage(): ArrayList<Image> {
        val listImage = ArrayList<Image>()
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "/Magnifier"
        ).toString()
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
        files.let { listFile ->
            listFile.sortByDescending { it.lastModified() }
            for (i in listFile.indices) {
                val pathFile = "$path/${files?.get(i)?.name}"
                val image = Image(pathFile)
                listImage.add(image)
            }
        }
        return listImage
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment()
    }

    private fun handlerEvent() {
        addDisposable(listenEvent({
            if (it is ReloadAllGallery) {
                readSdCard()
            }
        }, {}))
    }

}