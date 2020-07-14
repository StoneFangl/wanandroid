package com.xiaojianjun.wanandroid.ui.base

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.login.LoginActivity
import com.xiaojianjun.wanandroid.util.core.ActivityManager
import com.xiaojianjun.wanandroid.util.core.bus.Bus
import com.xiaojianjun.wanandroid.util.core.bus.USER_LOGIN_STATE_CHANGED

abstract class BaseVmActivity<VM : BaseViewModel> : BaseActivity() {

    protected open lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        observe()
        initView()
        initData()
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }


    /**
     * 获取ViewModel的class
     */
    protected abstract fun viewModelClass(): Class<VM>

    /**
     * 订阅，LiveData、Bus
     */
    open fun observe() {
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(this, Observer {
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityManager.start(LoginActivity::class.java)
            }
        })
    }

    /**
     * 数据初始化相关
     */
    open fun initView() {
        // Override if need
    }

    /**
     * 懒加载数据
     */
    open fun initData() {
        // Override if need
    }

    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (isLogin()) {
            then?.invoke()
            true
        } else {
            ActivityManager.start(LoginActivity::class.java)
            false
        }
    }

}
