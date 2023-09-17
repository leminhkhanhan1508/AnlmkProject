package com.anlmk.base.data.impl

import android.graphics.Bitmap
import android.util.Log
import androidx.room.*
import com.anlmk.base.data.room.MyAppDatabase

@Entity(tableName = "Mealtime")
data class Mealtime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var sessionId: Int? = null,
    var sessionName: String? = null,
    var dateOfMeal: Long? = 0,
    var timeOfMeal: Long? = 0,
    var foodOfMeal: String? = null,
    var imageOfFood: String? = null,
    var molOfFood: String? = null
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

    @Query("DELETE FROM Mealtime WHERE id = :mealtimeId")
    suspend fun deleteById(mealtimeId: Int)

}

class MealtimeDaoImpl(private val myAppDatabase: MyAppDatabase) : MealtimeDao {
    override suspend fun insertMealtime(mealtime: Mealtime){
        try {
            myAppDatabase.mealtimeDao().insertMealtime(mealtime)
        } catch (e: java.lang.Exception) {
            Log.wtf("Exception", e.message)
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

    override suspend fun deleteById(mealtimeId: Int) {
        return myAppDatabase.mealtimeDao().deleteById(mealtimeId)
    }
}