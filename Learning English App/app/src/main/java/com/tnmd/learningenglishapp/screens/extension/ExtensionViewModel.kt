package com.tnmd.learningenglishapp.screens.extension

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tnmd.learningenglishapp.model.Edit
import com.tnmd.learningenglishapp.model.EditResponse
import com.tnmd.learningenglishapp.model.Schedule
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.ScheduleService
import com.tnmd.learningenglishapp.model.service.WordsService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.screens.chatgpt.ApiService
import com.tnmd.learningenglishapp.screens.chatgpt.OpenAIRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

import javax.inject.Inject

@HiltViewModel
class ExtensionViewModel @Inject
constructor(
    private val wordsService: WordsService,
    logService: LogService,
    private val scheduleService: ScheduleService
) :
    LearningEnglishAppViewModel(logService) {

    private val _uiState = MutableStateFlow(ExtensionUiState())
    val uiState: StateFlow<ExtensionUiState> = _uiState.asStateFlow()
    val words = mutableStateListOf<Words>()

    // Khai báo biến để lưu kết quả từ SAPLING API
    private val _editResponse = MutableStateFlow<EditResponse?>(null)
    val editResponse: StateFlow<EditResponse?> = _editResponse.asStateFlow()

    // MutableState để kiểm soát việc hiển thị hộp thoại lỗi
    private val _showErrorsDialog = mutableStateOf(false)
    val showErrorsDialog: State<Boolean> = _showErrorsDialog

    // Danh sách lỗi
    private val _errorList = mutableStateListOf<Edit>()
    val errorList: List<Edit> = _errorList

    // Biến để kiểm tra trạng thái xử lý
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _imageAI = mutableStateOf<String?>(null)
    val imageAI: State<String?> = _imageAI


    init {
        launchCatching {
            scheduleService.schedule.collect { scheduleList ->
                newDayUserChoosen(scheduleList)
            }
        }
        loadData()
    }

    private fun newDayUserChoosen(scheduleList: List<Schedule>) {
        Log.d("checkScheduleScreen3", scheduleList.toString())
        val updatedUiState = _uiState.value.copy(dayUserChoosen = scheduleList)
        _uiState.value = updatedUiState
        Log.d("checkScheduleScreen3", _uiState.value.dayUserChoosen.toString())
    }
    // Hàm để đặt trạng thái xử lý
    private fun setIsLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun showErrorsDialog(errors: List<Edit>) {
        // Cập nhật danh sách lỗi và hiển thị hộp thoại
        _errorList.clear()
        _errorList.addAll(errors)
        _showErrorsDialog.value = true
    }

    // Hàm để đóng hộp thoại lỗi
    fun closeErrorsDialog() {
        _showErrorsDialog.value = false
    }

    fun loadData() {
        _uiState.value = ExtensionUiState(currentPage = 0)
    }

    fun changeTab(tab: Int) {
        _uiState.update { currentState ->
            currentState.copy(currentPage = tab)
        }
    }

    fun updateUserCheckGrammar(string: String) {
        _uiState.update { currentState ->
            currentState.copy(userStringGues = string)
        }
    }

    fun submitUserStringToSapling() {
        val url = "https://api.sapling.ai/api/v1/edits"
        val key = "3VFWH4CP23X5EK02LUW2AUXMW9ZDCXT0"
        setIsLoading(true)

        val params =
            "{\"key\":\"$key\", \"text\":\" ${_uiState.value.userStringGues}\", \"session_id\":\"Test Document UUID\"}"
        println(params)

        val client = OkHttpClient()

        val requestBody = params.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code ${response.code}")
                }

                println("Server: ${response.header("Server")}")
                println("Date: ${response.header("Date")}")
                println("Vary: ${response.header("Vary")}")

                val responseBody = response.body?.string()

                if (!responseBody.isNullOrEmpty()) {
                    // Chuyển đổi chuỗi JSON thành đối tượng Kotlin
                    val gson = Gson()
                    val editResponse = gson.fromJson(responseBody, EditResponse::class.java)

                    // Lưu kết quả vào biến _editResponse
                    _editResponse.value = editResponse
                    // Đặt trạng thái xử lý là false khi hoàn thành xử lý
                    setIsLoading(false)
                } else {
                    // Xử lý trường hợp responseBody là null hoặc rỗng
                    Log.d("responseBody", "Response body is null or empty")
                }
            } catch (e: Exception) {
                // Xử lý lỗi tại đây
                setIsLoading(false)
                e.printStackTrace()
            }
        }

    }

    val messages = mutableStateListOf<Message>()
    val isWaitingForResponse = mutableStateOf(false)
    fun sendMessage(text: String, isUser: Boolean = true) {
        messages.add(Message(text, "user"))
        if (isUser) {
            viewModelScope.launch {
                val response =
                    ApiService.openAIApi.generateResponse(OpenAIRequestBody(messages = messages))
                messages.add(response.choices.first().message)

                // Sau khi nhận được phản hồi, cập nhật isWaitingForResponse thành false
                isWaitingForResponse.value = false
            }
        }
    }

    fun wordsSearchChange(string: String) {
        _uiState.update { currentState ->
            currentState.copy(
                wordUserSearch = string,
                hasWordSearch = false,
                wordSearchResult = Words()
            )
        }
    }

    fun searchSubmit() {
        Log.d("ExtensionViewModel", _uiState.value.wordUserSearch)

        var result = false
        try {
            setIsLoading(true)
            launchCatching {
                val foundWords = wordsService.findAWordsWithName(_uiState.value.wordUserSearch)
                _uiState.update { currentState ->
                    val foundWords = wordsService.findAWordsWithName(_uiState.value.wordUserSearch)
                    val updatedWordSearchResult = foundWords
                        ?: Words() // Tạo một đối tượng Words mặc định nếu foundWords là null
                    currentState.copy(wordSearchResult = updatedWordSearchResult)
                }
            }
        } catch (e: Exception) {
            // Xử lý lỗi ở đây
            e.printStackTrace()
            // Ví dụ: Hiển thị thông báo lỗi cho người dùng
        } finally {
            setIsLoading(false)
        }
        if (_uiState.value.wordSearchResult != null) {
            _uiState.update { currentState ->
                currentState.copy(hasWordSearch = true)
            }
        }
        Log.d("ExtensionViewModel", result.toString())
    }

    fun updateEdit(isChecked: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isEditSchedule = isChecked)
        }
    }

    fun updateTempSchedule(schedule: Schedule){
        _uiState.update { currentState ->
            currentState.copy(tempSchedule = schedule)
        }
    }

    fun updateDayUserChoosen(dateInput: String) {
        val dateExists = _uiState.value.dayUserChoosen.any { it.date == dateInput }

        if (dateExists == false) {
            Log.d("dateExists", dateExists.toString())
            updateTempSchedule(Schedule(date = dateInput))
            launchCatching {
                val id = scheduleService.newSchedule(_uiState.value.tempSchedule)
                // Tìm phần tử có date bằng dateInput và cập nhật id
                val updatedDayUserChoosen = _uiState.value.dayUserChoosen.map {
                    if (it.date == dateInput) {
                        it.copy(id = id) // Cập nhật id ở đây
                    } else {
                        it
                    }
                }
                _uiState.update { currentState ->
                    currentState.copy(dayUserChoosen = updatedDayUserChoosen)
                }
            }
        } else {
            updateTempSchedule(Schedule(date = dateInput))
            val dateToRemove = _uiState.value.dayUserChoosen.firstOrNull { it.date.equals(dateInput) }
            Log.d("dateExists", dateToRemove.toString())
            Log.d("dateExists", dateInput.toString() + " | check dateInput")
            Log.d("dateExists", _uiState.value.dayUserChoosen.toString() + " | check dateInput")
            if (dateToRemove != null) {
                Log.d("dateExists", dateToRemove.toString())
                launchCatching { scheduleService.removeSchedule(dateToRemove) }
            }
        }

        Log.d("dateExists", dateExists.toString() + " - check")
        _uiState.update { currentState ->
            val currentList = currentState.dayUserChoosen
            val updatedList = if (currentList.any { it.date == dateInput }) {
                currentList.filterNot { it.date == dateInput }
            } else {
                currentList + Schedule(date = dateInput)
            }
            currentState.copy(dayUserChoosen = updatedList)
        }

    }

    fun textToImage(inputString: String) {
        _uiState.update { currentState ->
            currentState.copy(isOpenDialog = true)
        }
        _imageAI.value = null

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val unsplashApiKey = "yNsL2oK20qhiRDCBuGJ4PKbqDxp3JJMFJ2TuiOlGqE8" // Thay YOUR_UNSPLASH_API_KEY bằng API key của bạn
                val unsplashUrl = "https://api.unsplash.com/photos/random/?query=${inputString}&client_id=${unsplashApiKey}"

                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(unsplashUrl)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody)

                    val imageUrl = json.getString("urls")
                    val myImage = getFullImageUrl(imageUrl)

                    withContext(Dispatchers.Main) {
                        _imageAI.value = myImage
                    }
                } else {
                    withContext(Dispatchers.Main) {}
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {}
            }
        }
    }

    fun updateIsOpenDialog(){
        _uiState.update { currentState ->
            currentState.copy(isOpenDialog = false)
        }
    }

}

data class Message(val content: String, val role: String) {
    val isUser: Boolean
        get() = role == "user"
}

fun getFullImageUrl(jsonString: String): String? {
    try {
        val jsonObject = JSONObject(jsonString)
        return jsonObject.optString("full")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


