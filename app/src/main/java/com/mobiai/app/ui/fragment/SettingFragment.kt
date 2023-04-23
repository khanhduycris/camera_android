import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.mobiai.R
import com.mobiai.app.ui.activity.LanguageActivity
import com.mobiai.app.ui.dialog.RateDialog
import com.mobiai.app.utils.RxBus
import com.mobiai.app.utils.ShowTutorialEvents
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun initView() {
        binding.tvNameLanguage.text = getLanguageName()
        binding.imgBack.setOnClickListener {
            closeFragment(this)
        }
        binding.lnLanguage.setOnClickListener {
           LanguageActivity.start(requireContext(), openFromMain = true, clearTask = false)
        }

        binding.lnTutorial.setOnClickListener {
            RxBus.publish(ShowTutorialEvents())
            closeFragment(this)
        }

        binding.lnRateApp.setOnClickListener {
            RateDialog(requireContext()) {
                if (it >= 4) {
                    reviewApp()
                }
                Toast.makeText(requireContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT)
                    .show()
            }.show()
        }

        binding.lnShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my cool app!")
            val chooserIntent = Intent.createChooser(shareIntent, "Share ")
            if (chooserIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooserIntent)
            }
        }

        binding.lnTemsOf.setOnClickListener {

        }

        binding.lnPrivate.setOnClickListener {

        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false)

    private fun reviewApp() {
        val manager: ReviewManager = ReviewManagerFactory.create(requireContext())
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo: ReviewInfo = task.result
                val flow: Task<Void> = manager.launchReviewFlow(requireActivity(), reviewInfo)
                Log.d("ReviewInfo", "" + reviewInfo.toString())
                flow.addOnCompleteListener { task1 ->
                    Log.d("ReviewSucces", "" + task1.toString())
                }
            } else {
                Log.d("ReviewError", "" + task.getException().toString())
            }
        }
    }

    private fun getLanguageName(): String {
        val locale = SharedPreferenceUtils.languageCode

        if (locale == "en"){ return getString(R.string.language_english) }
        else if (locale == "de"){ return getString(R.string.language_germany) }
        else if (locale == "es"){ return getString(R.string.language_spain) }
        else if (locale == "fr"){ return getString(R.string.language_france) }
        else if (locale == "pt"){ return getString(R.string.language_portugal) }
        return getString(R.string.language_english)
    }

}