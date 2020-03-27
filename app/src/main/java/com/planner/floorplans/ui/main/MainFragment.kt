package com.planner.floorplans.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.planner.floorplans.data.api.Resource.Complete
import com.planner.floorplans.data.api.Resource.Loading
import com.planner.floorplans.databinding.MainFragmentBinding
import com.planner.floorplans.util.observeIt
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    currentProjectId.text = projectResponse?.name
                    val project = projectResponse?.data
                    val groundColor = parseColor(project?.ground?.color)
                    floorPlan.setGroundColor(groundColor ?: Color.WHITE)
                    floorPlan.setGroundDimensions(project?.width ?: 0f, project?.height ?: 0f)
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
                    nextProjectId.text = nextProjectState.value.items?.first()?.name
                    smallProgressBar.visibility = INVISIBLE
                }
                is Loading -> {
                    smallProgressBar.visibility = VISIBLE
                }
                is Error -> {
                    smallProgressBar.visibility = INVISIBLE
                }
            }
        }
    }

    private fun parseColor(color: String?): Int? {
        return color?.let { value ->
            return try {
                Color.parseColor(value)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Failed to parse color $color", e)
                null
            } catch (e: StringIndexOutOfBoundsException) {
                Log.e(TAG, "Failed to parse color $color", e)
                null
            }
        }
    }

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun newInstance(): MainFragment = MainFragment()
    }
}
