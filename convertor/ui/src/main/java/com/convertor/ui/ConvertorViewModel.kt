package com.convertor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertor.data.ApiResponse
import com.convertor.data.ExchangeRateUseCase
import com.convertor.data.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class  ConvertorViewModel @Inject constructor (val exchangeRateUseCase: ExchangeRateUseCase) : ViewModel() {

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private var _errorString = MutableLiveData("")
    val errorString: LiveData<String> get() = _errorString

    private var _currencies = MutableLiveData<List<String>>()
    val currencies: LiveData<List<String>> get() = _currencies

    private var _exchangeRateMap: Map<String,Double> = emptyMap()
    val exchangeRateMap: Map<String,Double> get() = _exchangeRateMap

    private var _convertedListData = MutableLiveData<List<Currency>>()
    val convertedListData: LiveData<List<Currency>> get() = _convertedListData

    private var _currencyAmount = MutableLiveData(Double.NaN)
    val currencyAmount: LiveData<Double> get() = _currencyAmount

    private var _selectedCurrencyIndex = MutableLiveData(0)
    val selectedCurrencyIndex: LiveData<Int> get() = _selectedCurrencyIndex


    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            exchangeRateUseCase.getExchangeRates().collectLatest { response ->

                when(response) {
                    is ApiResponse.Success ->  {
                        _loading.postValue(false)
                        _currencies.postValue(listOf("Select")+ response.data.rates.keys.toList())
                        _exchangeRateMap = (response.data.rates)
                    }
                    is ApiResponse.Error ->  {
                        _loading.postValue(false)
                        _errorString.postValue(response.error)
                    }
                    is ApiResponse.Loading -> {
                        _loading.postValue(true)
                    }

                    else -> {}
                }
            }

        }
    }

    fun setSelectedCurrencyIndex(index: Int) {
        _selectedCurrencyIndex.value = index
        computeConversionIfPossible()
    }


    fun setAmount(amount: String) {
        try {
            _currencyAmount.value = amount.toDouble()
        }
        catch(e: Exception) {
            _currencyAmount.value = Double.NaN
        }
        computeConversionIfPossible()
    }

    fun computeConversionIfPossible() {
        val selectedIndex = selectedCurrencyIndex.value ?: 0
        if(currencyAmount.value?.isNaN() == false && selectedIndex !=0) {
            currencies.value?.let {
                val selectedCurrency = it.get(selectedIndex)
                if (exchangeRateMap.containsKey(selectedCurrency) && exchangeRateMap.get(
                        selectedCurrency
                    ) != 0.00
                ) {
                    exchangeRateMap.get(selectedCurrency)?.let { selectedCurrencyValue ->
                        currencyAmount.value?.let { amount ->
                            computeConversion(selectedCurrencyValue, amount)
                        }
                    }
                }
            }
        }
        else {
            _convertedListData.value = emptyList()
        }
    }

    fun computeConversion(selectedCurrencyValue: Double, amount: Double) {
        val convertedValues: MutableList<Currency> = mutableListOf()
        exchangeRateMap.forEach { entry ->
            convertedValues.add(
                Currency(
                    currency = entry.key,
                    value = (amount*entry.value) / selectedCurrencyValue
                )
            )
        }
        _convertedListData.value = convertedValues
    }
}