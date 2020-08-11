package com.example.rateapisample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager

/**
 * @author Nav Singh
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var manager: ReviewManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //rate this app
        //initialize ReviewManager
        //for testing use FakeReviewManager instance, only use it for testing purpose
        manager = when (BuildConfig.DEBUG) {
            true -> {
                FakeReviewManager(this)
            }

            false -> {
                ReviewManagerFactory.create(applicationContext)
            }

        }

        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { req ->
            when {
                req.isSuccessful -> {
                    // We got the ReviewInfo object
                    val reviewInfo = request.result
                    val flow = manager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Toast.makeText(this, "Review process finished", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    // There was some problem, continue regardless of the result.
                }
            }
        }
    }
}
