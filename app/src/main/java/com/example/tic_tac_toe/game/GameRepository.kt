package com.example.tic_tac_toe.game

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_preferences")

class GameRepository(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val USER_WINS_KEY = intPreferencesKey("user_wins")
    }

    val userWins: Flow<Int> = dataStore.data.map {
        it[USER_WINS_KEY] ?: 0
    }

    suspend fun incrementUserWins() {
        dataStore.edit {
            val currentWins = it[USER_WINS_KEY] ?: 0
            it[USER_WINS_KEY] = currentWins + 1
        }
    }
}
