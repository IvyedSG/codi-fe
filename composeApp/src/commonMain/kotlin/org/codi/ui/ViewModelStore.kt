package org.codi.ui

import org.codi.features.home.HomeViewModel
import org.codi.features.history.HistoryViewModel
import org.codi.features.profile.ProfileViewModel
import org.codi.features.promos.PromoViewModel

/**
 * Store centralizado de ViewModels para compartir entre tabs
 * y evitar recrearlos en cada navegación
 */
object ViewModelStore {
    private var _homeViewModel: HomeViewModel? = null
    private var _historyViewModel: HistoryViewModel? = null
    private var _profileViewModel: ProfileViewModel? = null
    private var _promoViewModel: PromoViewModel? = null

    /**
     * Obtiene o crea la instancia compartida de HomeViewModel
     */
    fun getHomeViewModel(): HomeViewModel {
        if (_homeViewModel == null) {
            _homeViewModel = HomeViewModel()
        }
        return _homeViewModel!!
    }

    /**
     * Obtiene o crea la instancia compartida de HistoryViewModel
     */
    fun getHistoryViewModel(): HistoryViewModel {
        if (_historyViewModel == null) {
            _historyViewModel = HistoryViewModel()
        }
        return _historyViewModel!!
    }

    /**
     * Obtiene o crea la instancia compartida de ProfileViewModel
     */
    fun getProfileViewModel(): ProfileViewModel {
        if (_profileViewModel == null) {
            _profileViewModel = ProfileViewModel()
        }
        return _profileViewModel!!
    }

    /**
     * Obtiene o crea la instancia compartida de PromoViewModel
     */
    fun getPromoViewModel(): PromoViewModel {
        if (_promoViewModel == null) {
            _promoViewModel = PromoViewModel()
        }
        return _promoViewModel!!
    }

    /**
     * Limpia todos los ViewModels y sus cachés (útil al hacer logout)
     */
    fun clear() {
        _homeViewModel?.clearCache()
        _historyViewModel?.clearCache()
        _profileViewModel?.clearCache()
        _promoViewModel?.clearCache()

        _homeViewModel = null
        _historyViewModel = null
        _profileViewModel = null
        _promoViewModel = null
    }
}

