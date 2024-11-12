package com.snapco.techlife.ui.view.fragment.reels

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.snapco.techlife.databinding.FragmentAddReelBinding // Update the import for ViewBinding

class AddReelFragment : Fragment() {
    lateinit var binding: FragmentAddReelBinding
    private var selectedVideoUri : Uri? = null
    lateinit var videoLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentAddReelBinding.inflate(inflater, container, false)
        videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                selectedVideoUri = result.data?.data
//                Toast.makeText(requireContext(),"Got Video" + selectedVideoUri.toString(),Toast.LENGTH_SHORT).show()
                showPostView();

            }
        }
        binding.uploadView.setOnClickListener{
            checkPermissionAndOpenVideoPicker()
        }

        binding.submitPostBtn.setOnClickListener{
            postVideo()
        }
        return binding.root
    }
    private fun postVideo(){
        if (binding.postCaptionInput.text.toString().isEmpty()){
            binding.postCaptionInput.setError("Write something")
            return;
        }
        setInProgress(true);
        selectedVideoUri?.apply {
            //store in cloud storage
            val videoRef = FirebaseStorage.getInstance()
                .reference
                .child("video/"+ this.lastPathSegment)
            videoRef.putFile(this)
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        //video model store in firestore
                        postToFirestore(downloadUrl.toString())
                    }
                }
        }
    }

    private fun postToFirestore(url: String){

    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.submitPostBtn.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.submitPostBtn.visibility = View.VISIBLE
        }
    }

    private fun showPostView(){
        binding.postView.visibility =View.VISIBLE
        binding.uploadView.visibility = View.GONE
    }
    fun checkPermissionAndOpenVideoPicker(){
        var readExternalVideo : String = ""
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            readExternalVideo = android.Manifest.permission.READ_MEDIA_VIDEO
        }else{
            readExternalVideo = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if(ContextCompat.checkSelfPermission(requireActivity(),readExternalVideo) == PackageManager.PERMISSION_GRANTED){
            openVideoPicker()
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(readExternalVideo),
                100
            )
        }
    }

    private fun openVideoPicker(){
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }

}
