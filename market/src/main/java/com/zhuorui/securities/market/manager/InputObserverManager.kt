package com.zhuorui.securities.market.manager

import com.zhuorui.securities.market.model.OnNotifyObserver
import com.zhuorui.securities.market.model.OnSubject
import com.zhuorui.securities.market.model.OnSubject.list
import com.zhuorui.securities.market.model.SearchStokcInfoEnum

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/8
 * Desc:
 */
open class InputObserverManager : OnSubject<OnNotifyObserver>{
    override fun notifyAllObservers(str: String, infoEnum: SearchStokcInfoEnum?) {
        for(obs in list){
            obs.updateInput(str,infoEnum)
        }
    }

    override fun registerObserver(obs: OnNotifyObserver?) {
        list.add(obs)
    }

    override fun removeObserver(obs: OnNotifyObserver?) {
         list.remove(obs)
    }

    }

