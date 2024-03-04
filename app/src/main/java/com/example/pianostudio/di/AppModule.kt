package com.example.pianostudio.di

import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.midi_io.KeyboardInput
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single {
        KeyboardInput(get())
    }

    viewModel {
        MainViewModel(get())
    }

}