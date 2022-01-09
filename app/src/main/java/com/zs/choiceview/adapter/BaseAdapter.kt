package com.zs.choiceview.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zs.choiceview.R
import com.zs.choiceview.bean.BaseBean
import kotlinx.android.synthetic.main.item_cardview.view.*

class BaseAdapter(var context: Context, private var mData: List<BaseBean>) : RecyclerView.Adapter<BaseAdapter.ItemViewHolder>() {

    var mListener: ((View, Int) -> Unit)? = null
    /**
     * 把数组的数据放到控件里
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        Log.d("WY+", "=onBindViewHolder=")
        val bean = mData!![position]
        holder.mTitle.text = bean.symbol
        holder.mDesc.text = bean.allInfo
//        加载图片到mNewsImg，这里省略
        Glide.with(context).load(bean.imgsrc).crossFade().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        Log.d("WY+", "=getItemCount=")
        return mData.size
    }

    /**
     * 可视界面中，加载一个项就调用一次，滑动看见多一个item就会调用一次
     * @param holder
     * @param position
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BaseAdapter.ItemViewHolder {
        Log.d("WY+", "=getItemCount=")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ItemViewHolder(v)
    }

    inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var mTitle: TextView = v.tvTitle//大字标题 控件绑定
        var mDesc: TextView = v.tvDesc//小字内容
        var imageView: ImageView = v.ivNews//显示recycleview中的图片

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListener?.invoke(view, this.adapterPosition)//回调给mainactivity
        }
    }
}
