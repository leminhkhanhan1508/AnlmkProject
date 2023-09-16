package com.anlmk.base.data.`object`

enum class Session(private val value: Int) {
    Morning(1),
    Afternoon(2),
    Night(3);

    companion object {
        fun getTypeSession(type: Int?): Session {
            return when (type) {
                Morning.value -> Morning
                Afternoon.value -> Afternoon
                Night.value -> Night
                else -> Morning
            }
        }
        fun getValueSession(session: Session):Int{
            return when (session) {
                Morning -> Morning.value
                Afternoon -> Afternoon.value
                Night -> Night.value
            }
        }
    }
}