package com.zhuorui.securities.market.manager

import com.zhuorui.securities.market.model.OnNotifyObserver
import com.zhuorui.securities.market.model.OnSubject
import com.zhuorui.securities.market.model.OnSubject.list

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/8
 * Desc:
 */
open class InputObserverManager : OnSubject<OnNotifyObserver>{
    override fun registerObserver(obs: OnNotifyObserver?) {
        list.add(obs)
    }

    override fun removeObserver(obs: OnNotifyObserver?) {
       list.remove(obs)
    }

    override fun notifyAllObservers(str :String) {
        for(obs in list){
            obs.updateInput(str)
        }
    }

}