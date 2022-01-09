package com.zs.choiceview

import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object AnimationUtil {
    private val TAG = AnimationUtil::class.java.simpleName

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    fun moveToViewBottom(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, -1.0f
        )
        mHiddenAction.setDuration(500)
        return mHiddenAction
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    fun moveToViewLocation(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.setDuration(500)
        return mHiddenAction
    }
}