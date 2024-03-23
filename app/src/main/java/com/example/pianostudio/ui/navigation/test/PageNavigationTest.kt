//package com.example.pianostudio.ui.navigation
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.AnimatedContentScope
//import androidx.compose.animation.AnimatedContentTransitionScope
//import androidx.compose.animation.ContentTransform
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.togetherWith
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.Immutable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.ProvidableCompositionLocal
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.staticCompositionLocalOf
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import com.example.pianostudio.ui.random.UpdateEffect
//
//
//@Composable
//fun PageNavigationRoot(
//    startingRoute: String,
//    transitionSpec: MyTransition = defaultTransition,
//    content: @Composable () -> Unit
//) {
//    val fullPath = remember {
//        mutableStateOf(startingRoute.split('/'))
//    }
//
//    val route = remember(fullPath.value) {
//        NavigationRoute(
//            thisPageName = "root",
//            remainingRoute = fullPath.value
//        )
//    }
//
//    val preferences = remember(transitionSpec) {
//        NavigationPreferences(
//            transitionSpec = transitionSpec
//        )
//    }
//
//    CompositionLocalProvider(
//        localNavigationPreferences provides preferences,
//        localNavigationRoute provides route,
//        localRootPath provides fullPath,
//        content = content
//    )
//}
//
//@Composable
//fun PageSwitcher(
//    modifier: Modifier = Modifier,
//    contentAlignment: Alignment = Alignment.Center,
//    transitionSpec: MyTransition? = null,
//    builder: DefineNewPagesScope.() -> Unit,
//) {
//    val thePages = remember(builder) {
//        DefineNewPagesScope().apply { builder() }.pages
//    }
//
//    val currentPreferences = localNavigationPreferences.currentOrThrow
//    val newPreferences = remember(currentPreferences, transitionSpec) {
//        NavigationPreferences(
//             transitionSpec = transitionSpec ?: currentPreferences.transitionSpec
//        )
//    }
//
//    val currentRoute = localNavigationRoute.currentOrThrow
//    val newRoute = remember {
//        val route = currentRoute.remainingRoute
//        if (route.isEmpty()) error("No page specified!")
//        val newPageName = route[0]
//        val newRemainingRoute =
//            if (route.size <= 1) emptyList()
//            else route.subList(1, route.size)
//
//        val theNewRoute = NavigationRoute(
//            thisPageName = newPageName,
//            remainingRoute = newRemainingRoute
//        )
//
//        mutableStateOf(theNewRoute)
//    }
//
//    LaunchedEffect(newRoute, currentRoute) {
//        val route = currentRoute.remainingRoute
//        if (route.isEmpty()) error("No page specified!")
//        val newPageName = route[0]
//        val newRemainingRoute =
//            if (route.size <= 1) emptyList()
//            else route.subList(1, route.size)
//
//        newRoute.value = NavigationRoute(
//            thisPageName = newPageName,
//            remainingRoute = newRemainingRoute
//        )
//    }
//
//    val selectedPageWithRoute = remember(thePages, newRoute, newRoute.value.thisPageName) {
//        val pageName = newRoute.value.thisPageName
//        val content: PageContent = thePages[pageName]
//            ?: (error("No page declared with the name '${pageName}'!") as PageContent)
//        PageWithRoute(
//            pageContent = content,
//            localRoute = newRoute
//        )
//    }
//
//    CompositionLocalProvider(
//        localNavigationPreferences provides newPreferences
//    ) {
//        updateTransition(selectedPageWithRoute, label = "").AnimatedContent(
//            modifier = modifier,
//            contentAlignment = contentAlignment,
//            transitionSpec = newPreferences.transitionSpec,
//            contentKey = { newRoute.value.thisPageName }
//        ) { thisPageWithRoute ->
//            CompositionLocalProvider(
//                localNavigationRoute provides thisPageWithRoute.localRoute.value,
//            ) {
//                thisPageWithRoute.pageContent.invoke(this)
//            }
//        }
//    }
//}
//
//@Immutable
//class PageWithRoute(
//    val pageContent: PageContent,
//    val localRoute: MutableState<NavigationRoute>
//)
//
//// #################################################################################################
//
//@Composable
//fun rememberLocalPageNavigator(): PageNavigator {
//    val localRoute = localNavigationRoute.currentOrThrow
//    val rootPath = localRootPath.currentOrThrow
//
//    return remember(localRoute, rootPath) {
//        PageNavigator(
//            thisPageName = localRoute.thisPageName,
//            remainingRoute = localRoute.remainingRoute,
//            _rootPath = rootPath
//        )
//    }
//}
//
//class PageNavigator(
//    private val thisPageName: String,
//    private val remainingRoute: List<String>,
//    private val _rootPath: MutableState<List<String>>
//) {
//    val fullPath: List<String>
//        get() = _rootPath.value
//    val isLeafPage = remainingRoute.isEmpty()
//    val nextPage = if (isLeafPage) "" else remainingRoute[0]
//
//    fun navigateTo(route: String) {
//        _rootPath.value = route.split('/')
//    }
//}
//
//// #################################################################################################
//
//class DefineNewPagesScope {
//    val pages = mutableMapOf<String, PageContent>()
//
//    fun page(name: String, content: PageContent) {
//        pages[name] = content
//    }
//}
//
//typealias PageContent = @Composable AnimatedContentScope.() -> Unit
//
//// #################################################################################################
//
//private val localRootPath: ProvidableCompositionLocal<MutableState<List<String>>?> =
//    staticCompositionLocalOf { null }
//
//private val localNavigationPreferences: ProvidableCompositionLocal<NavigationPreferences?> =
//    staticCompositionLocalOf { null }
//
//private val localNavigationRoute: ProvidableCompositionLocal<NavigationRoute?> =
//    staticCompositionLocalOf { null }
//
//private val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
//    @Composable get() = current ?: error("CompositionLocal is null")
//
//private class NavigationPreferences(
//    val transitionSpec: MyTransition,
//)
//
//class NavigationRoute(
//    val thisPageName: String,
//    val remainingRoute: List<String>
//)
//
//// #################################################################################################
//
//typealias MyTransition = AnimatedContentTransitionScope<PageWithRoute>.() -> ContentTransform
//
//private val defaultTransition: MyTransition = {
//    fadeIn(animationSpec = tween(300)) togetherWith
//            fadeOut(animationSpec = tween(300))
//}