package com.midina.core_data

import com.midina.core_data.api.CoreApiInterface
import com.midina.core_data.data.leaguepref.LeaguePreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreRepository @Inject constructor(private val coreApiInterface: CoreApiInterface) {
    suspend fun getLeaguePreferences(): LeaguePreferences =
        coreApiInterface.getLeaguePreferences()
}
