package com.anlmk.base.data.impl

import android.graphics.Bitmap
import android.util.Log
import androidx.room.*
import com.anlmk.base.data.room.MyAppDatabase

@Entity(tableName = "Mealtime")
data class Mealtime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var sessionId: Int?,
    var sessionName: String?,
    var dateOfMeal: Long?,
    var timeOfMeal: Long?,
    var foodOfMeal:String?,
    var imageOfFood: ByteArray?,
    var molOfFood: String?
)

@Dao
interface MealtimeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMealtime(mealtime: Mealtime)

    @Query("SELECT * FROM Mealtime WHERE id = :id")
    suspend fun getMealtimeById(id: Int): Mealtime?

    @Query("SELECT * FROM Mealtime WHERE dateOfMeal = :dateOfMeal")
    suspend fun getMealtimeByDate(dateOfMeal: Long?): List<Mealtime?>?

    @Query("SELECT * FROM Mealtime")
    suspend fun getAllMealtime(): List<Mealtime?>?

    @Update
    suspend fun updateMealtime(mealtime: Mealtime)

}

class MealtimeDaoImpl(private val myAppDatabase: MyAppDatabase) : MealtimeDao {
    override suspend fun insertMealtime(mealtime: Mealtime){
        try {
            myAppDatabase.mealtimeDao().insertMealtime(mealtime)
        } catch (e: java.lang.Exception) {
            Log.wtf("KHANHANDEBUG", e.message)
        }
    }

    override suspend fun getMealtimeById(id: Int): Mealtime? {
        return myAppDatabase.mealtimeDao().getMealtimeById(id)
    }

    override suspend fun getMealtimeByDate(dateOfMeal: Long?): List<Mealtime?>? {
        return myAppDatabase.mealtimeDao().getMealtimeByDate(dateOfMeal)
    }

    override suspend fun getAllMealtime(): List<Mealtime?> {
        return myAppDatabase.mealtimeDao().getAllMealtime() ?: arrayListOf()
    }

    override suspend fun updateMealtime(mealtime: Mealtime) {
        return myAppDatabase.mealtimeDao().updateMealtime(mealtime)
    }
}