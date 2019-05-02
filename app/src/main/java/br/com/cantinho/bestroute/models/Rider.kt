package br.com.cantinho.bestroute.models

import android.location.Location

class Rider {

    var name:String? = null
    var description:String? = null
    var image:Int? = null
    var power:Double = 10.0
    var location: Location? = null
    var isCatch:Boolean? =  false
    constructor(name:String, description:String, image:Int, power:Double = 10.0, latitude:Double, longitude:Double) {
        this.name = name
        this.description = description
        this.image = image
        this.power = power
        location = Location(name)
        location!!.latitude = latitude
        location!!.longitude = longitude
        this.isCatch = false
    }

}