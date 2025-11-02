package com.ke.patientapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ke.patientapp.core.data.local.entities.AssessmentType
import com.ke.patientapp.feature.assessment.AssessmentScreen
import com.ke.patientapp.feature.auth.LoginScreen
import com.ke.patientapp.feature.auth.SignupScreen
import com.ke.patientapp.feature.listing.ListingScreen
import com.ke.patientapp.feature.registration.RegistrationScreen
import com.ke.patientapp.feature.vitals.VitalsScreen
import kotlinx.serialization.Serializable


@Composable
fun PatientAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController, startDestination: Any = LoginRoute
) {
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = startDestination
    ) {
        composable<LoginRoute> {
            LoginScreen(onSignupClick = {
                navController.navigate(SignUpRoute)
            }, onLoggedIn = {
                navController.navigate(RegistrationRoute)
            })
        }

        composable<SignUpRoute> {
            SignupScreen(onSignupSuccess = {
                navController.navigate(LoginRoute)
            })
        }

        composable<RegistrationRoute> {
            RegistrationScreen(onSaved = {
                navController.navigate(VitalsRoute(it))
            })
        }

        composable<VitalsRoute> {
            VitalsScreen(nav = navController)
        }

        composable<AssessmentRoute> {
            AssessmentScreen(onSaveClick = {
                navController.navigate(ListingRoute)
            }, onBackClick = {
                navController.navigateUp()
            })
        }

        composable<ListingRoute> {
            ListingScreen()
        }
    }
}


@Serializable
object LoginRoute

@Serializable
object SignUpRoute

@Serializable
object ListingRoute

@Serializable
object RegistrationRoute

@Serializable
data class VitalsRoute(val id: Long)

@Serializable
data class AssessmentRoute(
    val id: Long,
    val patientName: String,
    val visitDate: String,
    val assessmentType: AssessmentType
)
