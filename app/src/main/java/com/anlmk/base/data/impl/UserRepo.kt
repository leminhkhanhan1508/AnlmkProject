package com.anlmk.base.data.impl

import androidx.room.*
import com.anlmk.base.data.room.MyAppDatabase


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String
)

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM user")
    suspend fun getAllUser(): List<User?>

}

class UserDaoImpl(private val myAppDatabase: MyAppDatabase) : UserDao {
    override suspend fun insertUser(user: User) {
        myAppDatabase.userDao().insertUser(user)
    }

    override suspend fun getUserById(userId: Int): User? {
        return myAppDatabase.userDao().getUserById(userId)
    }

    override suspend fun getAllUser(): List<User?> {
        return myAppDatabase.userDao().getAllUser()
    }

    // You can add additional methods or customization specific to UserDaoImpl here
}

