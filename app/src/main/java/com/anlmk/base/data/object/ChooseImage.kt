package com.anlmk.base.data.`object`

enum class ChooseImage(private val value: Int) {
    Camera(1),
    Album(2);

    companion object {
        fun getTypeChooseImage(type: Int?): ChooseImage {
            return when (type) {
                Camera.value -> Camera
                Album.value -> Album
                else -> Album
            }
        }
        fun getValueChooseImage(chooseImage: ChooseImage):Int{
            return when (chooseImage) {
                Camera -> Camera.value
                Album -> Album.value
            }
        }
    }
}