package com.example.fitnessfinal

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "currentMacros")
class CurrentDataManager(context: Context){

    private val dataStore = context.dataStore

    companion object{
        val currentCalsKey = doublePreferencesKey("CURRENT_CALS_KEY")
        val currentProteinsKey = doublePreferencesKey("CURRENT_PROTEINS_KEY")
        val currentFatsKey = doublePreferencesKey("CURRENT_FATS_KEY")
        val currentCarbsKey = doublePreferencesKey("CURRENT_CARBS_KEY")
    }

    suspend fun storeCurrents(cals: Double,proteins: Double,fats: Double,carbs: Double){
        dataStore.edit{pref->
            pref[currentCalsKey] = cals
            pref[currentProteinsKey] = proteins
            pref[currentFatsKey] = fats
            pref[currentCarbsKey] = carbs
        }
    }
    val currentCalsFlow: Flow<Double> = dataStore.data.map{ pref->
       pref[currentCalsKey] ?: 0.0
    }
    val currentProteinsFlow: Flow<Double> = dataStore.data.map{ pref->
        pref[currentProteinsKey] ?: 0.0
    }
    val currentFatsFlow: Flow<Double> = dataStore.data.map{ pref->
        pref[currentFatsKey] ?: 0.0
    }
    val currentCarbsFlow: Flow<Double> = dataStore.data.map{ pref->
        pref[currentCarbsKey] ?: 0.0
    }

}