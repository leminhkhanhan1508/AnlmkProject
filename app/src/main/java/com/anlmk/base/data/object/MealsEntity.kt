package com.anlmk.base.data.`object`

import com.anlmk.base.data.impl.Mealtime

class MealsEntity {
    private var icon: Int = 0
    private var title: String? = null
    private var descript: String? = null
    var destiny: String? = null
    private var isHightLight: Boolean = false
    private var typeLayout: Int = 0
    private var counter: Int = 0
    private var dataToolTip: String? = null
    var dateOfMeal:String?=null
    var mealBreakfast: Mealtime? = null
    var mealLunch: Mealtime? = null
    var mealDinner: Mealtime? = null

    fun isHighLight(): Boolean {
        return isHightLight
    }

    fun setHighLight(hightLight: Boolean): MealsEntity {
        isHightLight = hightLight
        return this
    }

    fun getTypeLayout(): Int {
        return typeLayout
    }

    fun setTypeLayout(typeLayout: Int): MealsEntity {
        this.typeLayout = typeLayout
        return this
    }


    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?): MealsEntity {
        this.title = title
        return this
    }

    fun getIcon(): Int {
        return icon
    }

    fun setIcon(icon: Int): MealsEntity {
        this.icon = icon
        return this
    }

    fun getDescript(): String? {
        return descript
    }

    fun setDescript(descript: String?): MealsEntity {
        this.descript = descript
        return this
    }


    fun getCounter(): Int {
        return counter
    }


    fun setCounter(counter: Int): MealsEntity {
        this.counter = counter
        return this
    }

    fun setDestiny(destiny: String): MealsEntity {
        this.destiny = destiny
        return this
    }


    constructor(title: String, descript: String?) {
        this.title = title
        this.descript = descript
    }

    constructor() {}

    fun setDataToolTip(dataToolTip: String): MealsEntity {
        this.dataToolTip = dataToolTip
        return this
    }
    fun getDataToolTip():String?{
        return dataToolTip
    }
}