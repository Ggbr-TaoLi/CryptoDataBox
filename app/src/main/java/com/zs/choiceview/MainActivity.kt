package com.zs.choiceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.zs.choiceview.adapter.BaseAdapter
import com.zs.choiceview.bean.BaseBean
import com.zs.choiceview.bean.GridItemBean
import com.zs.various.adapter.ChoiceAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.ArrayList
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.zs.choiceview.MainActivity.LIST.filtrate


class MainActivity : AppCompatActivity() {

    var mData: MutableList<GridItemBean> = mutableListOf()
    var mAdapter: ChoiceAdapter? = null
    var initDatas: MutableList<BaseBean> = ArrayList<BaseBean>()
    private var mBaseAdapter: BaseAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    //相似于java的 static
    object LIST{
        var filtrate: LinearLayout? = null
    }
//    private  var recycler_info: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //filtrate
        filtrate = findViewById(R.id.cl_action) as LinearLayout
        //数据初始化
        initData()
        //list数据
        var datas: MutableList<BaseBean> = ArrayList<BaseBean>()
        datas = doJsonReader() //获取数据
        initAllData(datas)
        initDatas = datas
    }
    //初始化所有数据
    fun initAllData( data : MutableList<BaseBean>){
        /***初始化recycle_view**/
        recycle_view.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        recycle_view.layoutManager = mLayoutManager//设置LayoutManager
        recycle_view.itemAnimator = DefaultItemAnimator()
        /***初始化BaseAdapter**/
        mBaseAdapter = BaseAdapter(this, data)
        mBaseAdapter!!.mListener = { _, posion ->
            Log.d("WY+", "按下了$posion")
        }
        /***把recycleview和adapter关联**/
        recycle_view.setAdapter(mBaseAdapter)
        recycle_view.addOnScrollListener(MyRecyclerViewScrollListener())//添加滑动监听
//        recycle_view.setOnTouchListener()
    }
    //初始化筛选列表
    fun initData(){
//        mData.add(GridItemBean(1, "count" , true))
//        mData.add(GridItemBean("1" , "all" , true))
////        mData.add(GridItemBean("2" , "0"))
//        mData.add(GridItemBean("2" , "10000-"))
//        mData.add(GridItemBean("3" , "10000+"))

        mData.add(GridItemBean(2 , "priceChange"))
        mData.add(GridItemBean("5" , "UP"))
        mData.add(GridItemBean("6" , "DOWN"))

        mData.add(GridItemBean(3 , "count"))
        mData.add(GridItemBean("5" , "10000-"))
        mData.add(GridItemBean("6" , "10000+"))
        mAdapter = ChoiceAdapter()
        mAdapter?.initData(mData)
        RecyclerViewUtil.initGrid(this, recycler_grid , mAdapter,4)
        var layoutManager : androidx.recyclerview.widget.GridLayoutManager = recycler_grid?.layoutManager as androidx.recyclerview.widget.GridLayoutManager
        layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                var type = mAdapter?.getItemViewType(position)
                return if (type == 0) 1 else 4
            }
        }
    }
    //获取选择标签
    fun getData(view: View){
//        var data1 = mAdapter?.getMultiChoiceItem(1) //可多选
        var data1 = mAdapter?.getChoiceItem(2)//单选
        var data2 = mAdapter?.getChoiceItem(3)
//        var data3 = mAdapter?.getMultiChoiceItem(3)
        var rdatas: MutableList<BaseBean> = ArrayList<BaseBean>()
        //判断
        if(data1!=null&&data2!=null){
            for ((index,BaseBean) in initDatas.withIndex()){
                var priceChange:Double =BaseBean.priceChange.toDouble()
                var count :Double=BaseBean.count.toDouble()
                if(data1.name == "UP"){
                    if(priceChange>=0){
                        if(data2.name == "10000-"){
                            if(count<=10000){
                                rdatas.add(BaseBean)
                            }
                        }else{
                            if(count>10000){
                                rdatas.add(BaseBean)
                            }
                        }
                    }
                }else{
                    if(priceChange<0){
                        if(data2.name == "10000-"){
                            if(count<=10000){
                                rdatas.add(BaseBean)
                            }
                        }else{
                            if(count>10000){
                                rdatas.add(BaseBean)
                            }
                        }
                    }
                }
            }
        }else if(data1==null&&data2!=null){
            for ((index,BaseBean) in initDatas.withIndex()){
                var count :Double=BaseBean.count.toDouble()
                if(data2.name == "10000-"){
                    if(count<=10000){
                        rdatas.add(BaseBean)
                    }
                }else{
                    if(count>10000){
                        rdatas.add(BaseBean)
                    }
                }
            }
        }else if(data1!=null&&data2==null){
            for ((index,BaseBean) in initDatas.withIndex()){
                var priceChange :Double=BaseBean.priceChange.toDouble()
                if(data1.name == "UP"){
                    if(priceChange>=0){
                        rdatas.add(BaseBean)
                    }
                }else{
                    if(priceChange<0){
                        rdatas.add(BaseBean)
                    }
                }
            }
        }else{
            rdatas.addAll(initDatas)
        }
        initAllData(rdatas)
    }
//重置清空选择
    fun backData(view: View){
        mAdapter?.clearChoice()
        initAllData(initDatas)
    }
//读取json数据
    fun doJsonReader(): MutableList<BaseBean> {
        var d: MutableList<BaseBean> = ArrayList<BaseBean>()
        try {
            val url = "assets/datas.json"
            val isr = InputStreamReader(this.javaClass.classLoader.getResourceAsStream(url), "UTF-8")
            //从assets获取json文件
            val bfr = BufferedReader(isr)
            var line: String?
            val stringBuilder = StringBuilder()
            while (bfr.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            } //将JSON数据转化为字符串
            val root = JSONObject(stringBuilder.toString())
            val array = root.getJSONArray("infoBox")
            for (i in 0 until array.length()) {
                val stud = array.getJSONObject(i) //用于获取解析数据
                val st = StringBuffer() //用于拼接字符串
                st.append("priceChange : " + stud.getString("priceChange") + ",\n")
                st.append("priceChangePercent : " + stud.getString("priceChangePercent") + ",\n")
                st.append("weightedAvgPrice : " + stud.getString("weightedAvgPrice") + ",\n")
                st.append("prevClosePrice : " + stud.getString("prevClosePrice") + ",\n")
                st.append("lastPrice : " + stud.getString("lastPrice") + ",\n")
                st.append("lastQty : " + stud.getString("lastQty") + ",\n")
                st.append("bidPrice : " + stud.getString("bidPrice") + ",\n")
                st.append("bidQty : " + stud.getString("bidQty") + ",\n")
                st.append("askPrice : " + stud.getString("askPrice") + ",\n")
                st.append("askQty : " + stud.getString("askQty") + ",\n")
                st.append("openPrice : " + stud.getString("openPrice") + ",\n")
                st.append("highPrice : " + stud.getString("highPrice") + ",\n")
                st.append("lowPrice : " + stud.getString("lowPrice") + ",\n")
                st.append("volume : " + stud.getString("volume") + ",\n")
                st.append("quoteVolume : " + stud.getString("quoteVolume") + ",\n")
                st.append("openTime : " + stud.getString("openTime") + ",\n")
                st.append("closeTime : " + stud.getString("closeTime") + ",\n")
                st.append("firstId : " + stud.getString("firstId") + ",\n")
                st.append("lastId : " + stud.getString("lastId") + ",")
                st.append("count : " + stud.getString("count"))
                var temp = st.toString()
                temp = temp.replace(',', ' ')
                d.add(BaseBean(stud.getString("symbol"),stud.getString("priceChange"),stud.getString("count"),
                    temp,"http://img4.imgtn.bdimg.com/it/u=1758683838,870080854&fm=26&gp=0.jpg"))
                bfr.close()
                isr.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return d
    }

    //滑动监听
    private class MyRecyclerViewScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val manager = recyclerView.layoutManager as LinearLayoutManager?
            val lastVisibleItem =
                manager!!.findLastCompletelyVisibleItemPosition() //获取最后一个完全显示的ItemPosition
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                System.out.println(lastVisibleItem)
                // 判断是否滚动到顶部
                if (lastVisibleItem <= 5) {
                    filtrate?.setAnimation(AnimationUtil.moveToViewLocation())
                    filtrate?.setVisibility(View.VISIBLE)
                }
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) { //拖动中
                if ( filtrate?.getVisibility() === View.VISIBLE) {
//                    val linearParams = filtrate!!.getLayoutParams()
//                    linearParams?.height = 100
                  filtrate?.setAnimation(AnimationUtil.moveToViewBottom())
                    filtrate?.setVisibility(View.GONE)
                }
            }
        }
    }

}
