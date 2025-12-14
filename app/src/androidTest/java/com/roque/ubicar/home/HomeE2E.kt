package com.roque.ubicar.home

import android.Manifest
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.maps.MapsInitializer
import com.roque.ubicar.MainActivity
import com.roque.ubicar.feature.home.data.distance.DistanceCalculatorImpl
import com.roque.ubicar.feature.home.domain.usecase.GetPathToCarUseCase
import com.roque.ubicar.feature.home.presentation.HomeScreen
import com.roque.ubicar.feature.home.presentation.HomeViewModel
import com.roque.ubicar.navigation.NavigationRoute
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeE2E {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val locationPermissions = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var homeViewmodel: HomeViewModel
    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        // Initialize Google Maps SDK
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            MapsInitializer.initialize(context)
        }

        val homeRepository = FakeHomeRepository()
        val distanceCalculator = DistanceCalculatorImpl()

        homeViewmodel = HomeViewModel(
            FakeLocationService(),
            homeRepository,
            GetPathToCarUseCase(homeRepository, distanceCalculator)
        )

        composeRule.activity.setContent {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.HomeScreen.route
            ) {
                composable(NavigationRoute.HomeScreen.route) {
                    HomeScreen(homeViewmodel)
                }
            }
        }
    }

    @Test
    fun parkCar() {
        /*var state = homeViewmodel.state
        assert(state.car == null)
        assert(state.route == null)
        assert(state.carStatus == CarStatus.NOT_PARKED)
        composeRule.onNodeWithText("Park Here").assertIsDisplayed().performClick()
        state = homeViewmodel.state
        assert(state.car != null)
        assert(state.route == null)
        assert(state.carStatus == CarStatus.PARKED)
        composeRule.onNodeWithText("Get directions").assertIsDisplayed().performClick()
        state = homeViewmodel.state
        assert(state.car != null)
        assert(state.route != null)
        assert(state.carStatus == CarStatus.SEARCHING)
        composeRule.onNodeWithText("Stop Searching").assertIsDisplayed().performClick()
        state = homeViewmodel.state
        assert(state.car == null)
        assert(state.route == null)
        assert(state.carStatus == CarStatus.NOT_PARKED)*/
    }
}