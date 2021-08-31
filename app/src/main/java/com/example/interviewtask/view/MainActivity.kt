package com.example.interviewtask.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.app.washeruser.repository.Status
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityMainBinding
import com.example.interviewtask.model.StoreDetailsModel
import com.example.interviewtask.repository.StoryRepository
import com.example.interviewtask.utility.Constants
import com.example.interviewtask.utility.network.Resource
import com.example.interviewtask.view.adapter.StoriesAdapter
import com.example.interviewtask.viewmodel.StoryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var currentFilter: Int = Constants.NewStories
    private lateinit var adapter: StoriesAdapter
    private var dataList: ArrayList<String> = ArrayList()
    private val storyViewModel by viewModel<StoryViewModel>()
    protected lateinit var binding: ActivityMainBinding
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = StoriesAdapter(dataList, object : CommonCallback {
            override fun onclickStory(id: String) {
                callStoryDetailsApi(id)
            }
        })
        binding.adapter = adapter

        initListeners()
//        setupObservers(Constants.NewStories)
        checkAndUpdateData(Constants.NewStories)
    }

    private fun callStoryDetailsApi(id: String) {
        storyViewModel.getStoreDetails(Constants.StoryDetails, id)
            .observe(this, androidx.lifecycle.Observer {
                it?.let { resource ->

                    when (resource.status) {
                        Status.SUCCESS -> {
                            loadingProgress.visibility = View.GONE
                            showStoryDetails(resource)
                        }
                        Status.ERROR -> {
                            loadingProgress.visibility = View.GONE
                        }
                        Status.LOADING -> {
                            loadingProgress.visibility = View.VISIBLE
                        }

                    }

                }
            })
    }

    private fun showStoryDetails(resource: Resource<StoreDetailsModel>) {


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.story_details, null)
        dialogBuilder.setView(dialogView)

        val llTitle = dialogView.findViewById<View>(R.id.llTitle) as LinearLayout
        val llUrl = dialogView.findViewById<View>(R.id.llUrl) as LinearLayout
        val tvTitle = dialogView.findViewById<View>(R.id.tvTitle) as TextView
        val tvUrl = dialogView.findViewById<View>(R.id.tvUrl) as TextView

        resource.data?.let { storeDetails ->
            if (!TextUtils.isEmpty(storeDetails.title)) {
                llTitle.visibility = View.VISIBLE
                tvTitle.setText(storeDetails.title)
            } else {
                llTitle.visibility = View.GONE
            }
            if (!TextUtils.isEmpty(storeDetails.url)) {
                tvUrl.setText(storeDetails.url)
                llUrl.visibility = View.VISIBLE
            } else {
                llUrl.visibility = View.GONE
            }
        }
        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }


    private fun initListeners() {
        binding.pullToRefresh.setOnRefreshListener {
            setupObservers(Constants.NewStories)
        }
        binding.searchEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(searchString: Editable?) {
                StoryRepository.getDatas(this@MainActivity, currentFilter)
                    ?.observe(this@MainActivity, androidx.lifecycle.Observer {
                        it?.let { resource ->
                            updateList(resource.storylist, searchString.toString())
                        }
                    })

            }

        })
    }


    private fun checkAndUpdateData(type: Int) {

        var isEntered = false
        StoryRepository.getDatas(this, type)?.let {
            it.observe(this, androidx.lifecycle.Observer {
                it?.let { resource ->
                    if (!resource.storylist.isEmpty()) {
                        isEntered = true
                        updateList(resource.storylist, "")
                    }
                }
            })
        }
        if (!isEntered && isNetworkConnected()) {
            setupObservers(type)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }


    private fun setupObservers(type: Int) {
        if (isNetworkConnected())
            storyViewModel.getData(type).observe(this, androidx.lifecycle.Observer {
                it?.let { resource ->
                    loadData(resource, type, "")
                }
            })

    }

    private fun loadData(resource: Resource<List<String>>, type: Int, searchString: String) {
        when (resource.status) {
            Status.SUCCESS -> {
                binding.recycleView.visibility = View.VISIBLE
                binding.noDataFound.visibility = View.GONE
                binding.pullToRefresh.isRefreshing = false
                loading = true;
                loadingProgress.visibility = View.GONE
                resource.data?.let { users ->
                    updateList(users, searchString)
                    if (TextUtils.isEmpty(searchString))
                        insertData(this, users, type)
                }


            }
            Status.ERROR -> {
                binding.recycleView.visibility = View.GONE
                binding.noDataFound.visibility = View.VISIBLE
                binding.pullToRefresh.isRefreshing = false
                loadingProgress.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.recycleView.visibility = View.GONE
                binding.noDataFound.visibility = View.VISIBLE
                if (!binding.pullToRefresh.isRefreshing)
                    loadingProgress.visibility = View.VISIBLE
            }
        }

    }

    fun insertData(context: Context, dataList: List<String>, type: Int) {
        StoryRepository.insertData(context, type, dataList)
    }

    private fun updateList(storyList: List<String>, searchString: String) {

        if (storyList.isEmpty()) {
            binding.noDataFound.visibility = View.VISIBLE
            binding.recycleView.visibility = View.GONE
        } else {
            binding.noDataFound.visibility = View.GONE
            binding.recycleView.visibility = View.VISIBLE
            adapter.apply {
                dataList.clear()

                if (!TextUtils.isEmpty(searchString)) {
                    dataList.addAll(storyList.filter { it.contains(searchString) })
                } else {
                    dataList.addAll(storyList)
                }
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        @BindingAdapter("bind_adapters")
        @JvmStatic
        fun loadUsers(recyclerView: RecyclerView, adapter: Any?) {
            when (adapter) {
                is StoriesAdapter -> {
                    (Objects.requireNonNull<RecyclerView.ItemAnimator>(recyclerView.getItemAnimator()) as SimpleItemAnimator).supportsChangeAnimations =
                        false
                    recyclerView.adapter = adapter as RecyclerView.Adapter<*>?
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun openOptionsMenus(view: View) {
        val popup = PopupMenu(this@MainActivity, view)
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu())
        popup.setOnMenuItemClickListener { item ->
            val id: Int = item.getItemId()
            return@setOnMenuItemClickListener when (id) {
                R.id.new_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_new_stories))
                    currentFilter = Constants.NewStories
                    checkAndUpdateData(Constants.NewStories)
                    Toast.makeText(applicationContext, "NewStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.top_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_top_stories))
                    currentFilter = Constants.TopStories
                    checkAndUpdateData(Constants.TopStories)
                    Toast.makeText(applicationContext, "TopStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.best_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_best_stories))
                    currentFilter = Constants.BestStories
                    checkAndUpdateData(Constants.BestStories)
                    Toast.makeText(applicationContext, "BestStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.ask_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_ask_stories))
                    currentFilter = Constants.AskStories
                    checkAndUpdateData(Constants.AskStories)
                    Toast.makeText(applicationContext, "AskStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.show_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_show_stories))
                    currentFilter = Constants.ShowStories
                    checkAndUpdateData(Constants.ShowStories)
                    Toast.makeText(applicationContext, "ShowStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.job_stories -> {
                    binding.selectedStory.setText(getString(R.string.filter_job_stories))
                    currentFilter = Constants.JobStories
                    checkAndUpdateData(Constants.JobStories)
                    Toast.makeText(applicationContext, "JobStories Selected", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                else -> false
                //            else -> super.onOptionsItemSelected(item)
            }
        }
        popup.show();

    }

}