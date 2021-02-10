package com.example.remotepaging.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.remotepaging.databinding.AddUserBinding
import com.example.remotepaging.viewmodel.AddUserViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private val REQUEST_CODE = 200
    private val REQUEST_CODE_OPEN_DOCUMENT_TREE = 400
    private val TAG : String = AddUserFragment::class.java.simpleName

    private val addUserViewmodel: AddUserViewmodel by viewModels()

    var binding : AddUserBinding? = null

    var imagePath : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddUserBinding.inflate(layoutInflater)
        binding?.btnSave?.setOnClickListener {
            //capturePhoto()
            saveData()

        }
        binding?.imgAvatar?.setOnClickListener {
            if (isStoragePermissionGranted()){
                capturePhoto()
            }else{
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_OPEN_DOCUMENT_TREE) }
            }

        }
        return binding?.root
    }

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){

            val bitmap  = data.extras?.get("data") as? Bitmap
            val filePath = bitmap?.let { saveImage(it) }
            Log.d(TAG, filePath!!)
            binding?.imgAvatar?.setImageBitmap(bitmap)
            saveTempBitmap(bitmap)

        } else if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE){
            capturePhoto()
        }
    }


    fun saveTempBitmap(bitmap: Bitmap) {
        if (isExternalStorageWritable()) {
            imagePath = saveImage(bitmap)
        } else {
            //prompt the user or do something
        }
    }

    private fun saveImage(finalBitmap: Bitmap) : String{
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "test_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun saveData(){
        val fname = binding?.editFirstName?.text.toString()
        val lname = binding?.editLastName?.text.toString()
        val email = binding?.editEmail?.text.toString()


        when {
            TextUtils.isEmpty(imagePath) -> {
                Toast.makeText(context, "Please select Image", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(fname) -> {
                Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(lname) -> {
                Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show()
            }
            !addUserViewmodel.isValidEmail(email) -> {
                Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
            }
            else -> {
                addUserViewmodel.saveData(imagePath!!, email, fname, lname)
                activity?.onBackPressed()
            }
        }


    }


    fun  requestStoragepermission(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
        startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT_TREE)
    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            capturePhoto()
        }
    }

    fun isStoragePermissionGranted() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return if (activity?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted");
                true;
            } else {


                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_OPEN_DOCUMENT_TREE
                    )
                };
                false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }



}