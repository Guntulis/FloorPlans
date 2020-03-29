package com.planner.floorplans.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.planner.floorplans.R
import com.planner.floorplans.data.api.Resource.Complete
import com.planner.floorplans.data.api.Resource.Empty
import com.planner.floorplans.data.api.Resource.Loading
import com.planner.floorplans.databinding.MainFragmentBinding
import com.planner.floorplans.util.observeIt
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = DEFAULT_SCALE_FACTOR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = MainFragmentBinding.inflate(inflater, container, false).root
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)
            true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                Log.d(TAG, "onSingleTapUp")
                viewModel.displayNextProject()
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
                Log.d(TAG, "onLongPress")
                viewModel.startAgain()
            }
        })
        scaleGestureDetector = ScaleGestureDetector(context, object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                Log.d(TAG, "scaleFactor = $scaleFactor")
                floorPlan.setScaleFactor(scaleFactor)
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.projectIdList.observeIt(this) { listState ->
            when (listState) {
                is Complete -> {
                    viewModel.loadVisibleProject()
                }
            }
        }

        viewModel.visibleProject.observeIt(this) { visibleProjectState ->
            when (visibleProjectState) {
                is Complete -> {
                    val projectResponse = visibleProjectState.value.items?.first()
                    currentProjectId.text = getString(R.string.current_project_name, projectResponse?.name)
                    val project = projectResponse?.data
                    floorPlan.setProject(project, scaleFactor)
                    mainProgressBar.visibility = INVISIBLE
                    errorMessage.visibility = INVISIBLE
                    viewModel.loadNextProject()
                }
                is Loading -> {
                    mainProgressBar.visibility = VISIBLE
                    errorMessage.visibility = INVISIBLE
                }
                is Error -> {
                    mainProgressBar.visibility = INVISIBLE
                    errorMessage.visibility = VISIBLE
                }
            }
        }

        viewModel.nextProject.observeIt(this) { nextProjectState ->
            when (nextProjectState) {
                is Complete -> {
                    val nextProjectName = nextProjectState.value.items?.first()?.name
                    nextProjectId.text = getString(R.string.next_project_name, nextProjectName)
                    smallProgressBar.visibility = INVISIBLE
                }
                is Loading -> {
                    smallProgressBar.visibility = VISIBLE
                }
                is Error -> {
                    smallProgressBar.visibility = INVISIBLE
                }
                is Empty -> {
                    nextProjectId.text = getString(R.string.no_more_projects)
                    smallProgressBar.visibility = INVISIBLE
                }
            }
        }
    }

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        const val DEFAULT_SCALE_FACTOR = 1f
        fun newInstance(): MainFragment = MainFragment()
    }
}
