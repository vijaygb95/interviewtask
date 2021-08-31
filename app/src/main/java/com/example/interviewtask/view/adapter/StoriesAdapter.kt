package com.example.interviewtask.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtask.baseclass.BaseAdapter
import com.example.interviewtask.databinding.StoriesAdapterBinding

import com.example.interviewtask.R
import com.example.interviewtask.view.CommonCallback


class StoriesAdapter(val documentModel: ArrayList<String>, var commonCallback: CommonCallback) :
    BaseAdapter<String>(documentModel) {


    override fun onBindViewHolderBase(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ViewHolder).getBinding()
        binding.data = documentModel[position]
        if (documentModel.isEmpty()) {
            return
        }
        binding.cvParent.setOnClickListener {
            commonCallback.onclickStory(documentModel[position])
        }
    }


    override fun onCreateViewHolderBase(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.stories_adapter, parent, false)
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var databding: StoriesAdapterBinding? = null

        init {
            databding = DataBindingUtil.bind<ViewDataBinding>(itemView) as StoriesAdapterBinding
        }

        fun getBinding(): StoriesAdapterBinding {
            return databding!!
        }

    }
}