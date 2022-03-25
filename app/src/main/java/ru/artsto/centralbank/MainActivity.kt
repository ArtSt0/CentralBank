package ru.artsto.centralbank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.artsto.centralbank.compose.ListScreen
import ru.artsto.centralbank.ui.theme.CentralBankTheme
import ru.artsto.centralbank.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private val vmMainView: MainViewModel by viewModels()
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        ViewModelProvider(this, factory)[MainViewModel::class.java]

        setContent {
            CentralBankTheme {
                ListScreen(
                    vmMainView.exchangeRate,
                    vmMainView::onClickUpdate,
                    vmMainView.textTimer,
                    vmMainView::onDropDownMenu1,
                    vmMainView::onDropDownMenu2,
                    vmMainView::onValueChange1,
                    vmMainView::onValueChange2,
                    vmMainView.kol1,
                    vmMainView.kol2,
                    vmMainView.key1,
                    vmMainView.key2,
                )
            }
        }
    }
}