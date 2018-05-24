package com.shiyanqi.todo.activity

import android.os.Bundle
import com.shiyanqi.todo.R
import com.shiyanqi.todo.base.BaseActivity
import android.content.Intent
import com.shiyanqi.todo.App
import com.shiyanqi.todo.constants.ConstantValues
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_index.*
import java.util.concurrent.TimeUnit


class IndexActivity : BaseActivity() {

    var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.mAppStatus = ConstantValues.STATUS_ONLINE
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.activity_index

    override fun setUpView() = Unit

    override fun setUpData() = doSomeThing()

    private fun doSomeThing() {
        val count = ConstantValues.INDEX_COUNT_DOWN_SECOND
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .map { aLong -> count - aLong }
                .observeOn(AndroidSchedulers.mainThread(), false, 100)
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(s: Disposable?) {
                        mDisposable = s
                    }

                    override fun onNext(aLong: Long?) {
                        tv_count_down.text = "${aLong}秒"
                    }

                    override fun onComplete() {
                        mDisposable?.dispose()
                        startActivity(Intent(this@IndexActivity, MainActivity::class.java))
                        finish()
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })
    }

}


