package com.example.pianostudio.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun PageNavigationRoot(
    startingRoute: String,
    transitionSpec: myTransition = defaultTransition,
    content: @Composable () -> Unit
) {
    val fullPath = remember(startingRoute) {
        mutableStateOf(startingRoute.split('/'))
    }

    val rootNavContext = remember(fullPath) {
        RootNavContext(fullPath)
    }

    val scopedNavContext = remember(fullPath.value, rootNavContext, transitionSpec) {
        ScopedNavContext(
            thisPageName = "root",
            localRoute = fullPath.value,
            transitionSpec = transitionSpec
        )
    }

    CompositionLocalProvider(
        localScopedNavContext provides scopedNavContext,
        localRootNavContext provides rootNavContext,
        content = content
    )
}

@Composable
fun PageSwitcher(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    transitionSpec: myTransition? = null,
    builder: DefineNewPagesScope.() -> Unit,
) {
    val thisNavContext = localScopedNavContext.currentOrThrow

    val defineNewPagesScope = remember(builder) {
        DefineNewPagesScope().apply { builder() }
    }

    val thisTransitionSpec = remember(thisNavContext, transitionSpec) {
        transitionSpec ?: thisNavContext.transitionSpec
    }

    val selectedPage = remember(defineNewPagesScope, thisNavContext, thisTransitionSpec) {
        val route = thisNavContext.localRoute
        if (route.isEmpty()) error("No page specified!")
        val selectedPageName = route[0]
        val selectedPageContent = defineNewPagesScope.pages[selectedPageName]
            ?: error("No page declared with that name!")

        PageWithContext(
            pageContent = selectedPageContent,
            navContext = ScopedNavContext(
                thisPageName = selectedPageName,
                localRoute = if (route.size <= 1) emptyList() else
                    route.subList(1, route.size),
                transitionSpec = thisTransitionSpec,
            )
        )
    }

    LaunchedEffect(selectedPage) {
        selectedPage.pageContent.onOpened.invoke()
    }

    updateTransition(selectedPage, label = "").AnimatedContent(
        modifier = modifier,
        contentAlignment = contentAlignment,
        contentKey = { it.navContext.thisPageName },
        transitionSpec = thisTransitionSpec
    ) {
        CompositionLocalProvider(
            localScopedNavContext provides it.navContext
        ) {
            it.pageContent.content.invoke(this)
        }
    }
}

// #################################################################################################

@Composable
fun rememberLocalPageNavigator(): PageNavigator {
    val scopedNavContext = localScopedNavContext.currentOrThrow
    val rootNavContext = localRootNavContext.currentOrThrow
    return remember(scopedNavContext, rootNavContext) {
        PageNavigator(scopedNavContext, rootNavContext)
    }
}

class PageNavigator(
    scopedNavContext: ScopedNavContext,
    private val rootNavContext: RootNavContext
) {
    val rootPath: List<String>
        get() = rootNavContext.rootPath.value

    val thisPageName = scopedNavContext.thisPageName

    val isLeafPage = scopedNavContext.localRoute.isEmpty()

    val nextPage = if(isLeafPage) "" else scopedNavContext.localRoute[0]

    val localRoute = scopedNavContext.localRoute

    fun navigateTo(route: String) {
        rootNavContext.rootPath.value = route.split('/')
    }
}

// #################################################################################################

class DefineNewPagesScope {
    val pages = mutableMapOf<String, Page>()

    fun page(
        name: String,
        onOpened: () -> Unit = {},
        content: @Composable AnimatedContentScope.() -> Unit,
    ) {
        val newPageScope = Page(onOpened = onOpened, content = content)
        pages[name] = newPageScope
    }
}

class Page(
    val onOpened: () -> Unit = {},
    val content: @Composable AnimatedContentScope.() -> Unit,
)

class PageWithContext(
    val pageContent: Page,
    val navContext: ScopedNavContext
)

// #################################################################################################

private val localScopedNavContext: ProvidableCompositionLocal<ScopedNavContext?> =
    staticCompositionLocalOf { null }

private val localRootNavContext: ProvidableCompositionLocal<RootNavContext?> =
    staticCompositionLocalOf { null }

private val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    get() = current ?: error("CompositionLocal is null")

class ScopedNavContext(
    val thisPageName: String,
    val localRoute: List<String>,
    val transitionSpec: myTransition
)

class RootNavContext(
    val rootPath: MutableState<List<String>>,
)

// #################################################################################################

typealias myTransition = AnimatedContentTransitionScope<PageWithContext>.() -> ContentTransform

private val defaultTransition: myTransition = {
    fadeIn(animationSpec = tween(300)) togetherWith
            fadeOut(animationSpec = tween(300))
}