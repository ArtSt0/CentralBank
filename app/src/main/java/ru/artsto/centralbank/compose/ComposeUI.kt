package ru.artsto.centralbank.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.artsto.centralbank.retrofit.Currency
import ru.artsto.centralbank.retrofit.ExchangeRate

@Composable
fun ListScreen(
    exchangeRate: MutableState<ExchangeRate>,
    onClickUpdate: () -> Unit,
    textTimer: MutableState<String>,
    onDropDownMenuItem1: () -> Unit,
    onDropDownMenuItem2: () -> Unit,
    onValueChange1: () -> Unit,
    onValueChange2: () -> Unit,
    kol1: MutableState<String>,
    kol2: MutableState<String>,
    key1: MutableState<String>,
    key2: MutableState<String>,
) {
    val scaffoldState = rememberScaffoldState()
    val columnWeight1 = 1f

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {Text(text = "Central Bank")},
                actions = {
                    IconButton(onClick = {onClickUpdate()}) {
                        Icon(Icons.Filled.Update, contentDescription = null)
                    }
                }
            )
        }
    ) {paddingValues->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = "авто.обновление через: ${textTimer.value}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )
            }

            //шапка таблицы
            Row(modifier = Modifier
                .background(Color.LightGray)
            ) {
                TableCell(text = "символьное обозначение", weight = columnWeight1, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                TableCell(text = "наименование", weight = columnWeight1, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                TableCell(text = "номинал", weight = columnWeight1, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                TableCell(text = "текущий курс, руб", weight = columnWeight1, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }


            LazyColumn(
                Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                ,
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(3.dp) //интервал между элементами
            ) {
                exchangeRate.value.currency?.let {
                    items(it.toList()){item ->

                        Row {

                            val fontWeight = if (item.second.charCode =="RUB" || item.second.charCode =="USD" || item.second.charCode =="EUR" || item.second.charCode =="CNY"){
                                FontWeight.Bold
                            }else{
                                FontWeight.Normal
                            }

                            TableCell(text = item.second.charCode, weight = columnWeight1, fontWeight = fontWeight)
                            TableCell(text = item.second.name, weight = columnWeight1, fontWeight = fontWeight)
                            TableCell(text = item.second.nominal.toString(), weight = columnWeight1, fontWeight = fontWeight)
                            TableCell(text = item.second.value.toString(), weight = columnWeight1, fontWeight = fontWeight)
                        }
                    }
                }
            }

            if (exchangeRate.value.currency!=null) {

                val exc = exchangeRate.value.currency?.toMutableMap()

                Converter(
                    exc,
                    onDropDownMenuItem1,
                    onDropDownMenuItem2,
                    onValueChange1,
                    onValueChange2,
                    kol1,
                    kol2,
                    key1,
                    key2
                )
            }
        }
    }
}

@Composable
fun Converter(
    exchangeRate: MutableMap<String, Currency>?,
    onDropDownMenuItem1: () -> Unit,
    onDropDownMenuItem2: () -> Unit,
    onValueChange1: () -> Unit,
    onValueChange2: () -> Unit,
    kol1: MutableState<String>,
    kol2: MutableState<String>,
    key1: MutableState<String>,
    key2: MutableState<String>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        //конвертор валют
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "Конвертация валют", fontWeight = FontWeight.Bold)

            exchangeRate?.let {
                RowConverter(kol1, it, key1, onValueChange1, onDropDownMenuItem1)
                RowConverter(kol2, it, key2, onValueChange2, onDropDownMenuItem2)
            }

        }
    }
}

@Composable
fun RowConverter(
    kol: MutableState<String>,
    exchangeRate: MutableMap<String, Currency>,
    key: MutableState<String>,
    onValueChange: () -> Unit,
    onDropDownMenuItem: () -> Unit
){

    //val exchange = exchangeRate.get(key = key.value)
    /*
    var currency by remember {
        mutableStateOf(exchange?.charCode)
    }*/

        val focusManager = LocalFocusManager.current
        Row(
            Modifier.fillMaxWidth()
        ) {

            Column(Modifier.weight(1f)) {
                //сумма в рублях
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
                    ,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    value = kol.value,
                    onValueChange = {
                        if (it.isEmpty()) {
                            kol.value = it
                            onValueChange()
                        } else {
                            if (it.toDoubleOrNull() != null) {
                                kol.value = it
                                onValueChange()
                            }
                        }

                    },
                    label = {
                        Text(text = "количество")
                    },
                    singleLine = true
                )
            }
            Column(Modifier.weight(1f)) {

                var expanded by remember {
                    mutableStateOf(false)
                }
                val icon = if (expanded)
                    Icons.Filled.ArrowDropUp
                else
                    Icons.Filled.ArrowDropDown
                //выбор валюты
                OutlinedTextField(
                    readOnly = true
                    ,
                    value = key.value,
                    onValueChange = {
                        key.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text("валюта")
                    },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded, //открытие/закрытие меню
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    exchangeRate.forEach { itemMap ->
                        DropdownMenuItem(onClick = {
                            key.value = itemMap.value.charCode
                            expanded = false
                            onDropDownMenuItem()
                        }) {
                            Text(text = itemMap.value.charCode)
                        }
                    }
                }
            }
        }
}

@Composable
@Preview(showBackground = true)
fun PreviewListScreen(){

    val currency = Currency(
        id = "R00123",
        numCode = "023",
        charCode = "RUB",
        nominal = 1.0,
        name = "Российский рубль",
        value = 1.0,
        previous = 100.79
    )
    val map:MutableMap<String, Currency> = mutableMapOf("RUB" to currency)
    val state = ExchangeRate(
        currency = map,
        date = "24 марта 2022",
        previousDate = "23 марта 2022",
        previousURL = "",
        timestamp = ""
    )

    val exchangeRate = remember {
        mutableStateOf(state)
    }
    val textTimer = remember {
        mutableStateOf("30")
    }
    val kol1 = remember {
        mutableStateOf("1.0")
    }
    val kol2 = remember {
        mutableStateOf("1.0")
    }
    val key1 = remember {
        mutableStateOf("RUB")
    }
    val key2 = remember {
        mutableStateOf("USD")
    }

    ListScreen(
        exchangeRate = exchangeRate,
        onClickUpdate = {},
        textTimer = textTimer,
        onDropDownMenuItem1 = {},
        onDropDownMenuItem2 = {},
        onValueChange1 = {},
        onValueChange2 = {},
        kol1 = kol1,
        kol2 = kol2,
        key1 = key1,
        key2 = key2
    )
}

@Composable
fun RowScope.TableCell(
    text:String,
    weight: Float,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize:TextUnit = 12.sp
){
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
        ,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight
    )
}