package com.planner.floorplans.ui.main

import android.os.Bundle
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
import kotlinx.android.synthetic.main.main_fragment.currentProjectId
import kotlinx.android.synthetic.main.main_fragment.errorMessage
import kotlinx.android.synthetic.main.main_fragment.progressBar
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
        viewModel.visibleProject.observeIt(this) { visibleProjectState ->
            when (visibleProjectState) {
                is Complete -> {
                    currentProjectId.text = visibleProjectState.value.storage
                    progressBar.visibility = INVISIBLE
                    errorMessage.visibility = INVISIBLE
                }
                is Loading -> {
                    progressBar.visibility = VISIBLE
                    errorMessage.visibility = INVISIBLE
                }
                is Error -> {
                    progressBar.visibility = INVISIBLE
                    errorMessage.visibility = VISIBLE
                }
            }
        }
    }
}
