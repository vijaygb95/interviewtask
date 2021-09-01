# Story list

Components used

1. Mvvm pattern
2. Databinding
3. Live Data
4. Coroutine
5. Room Database
6. Koin

Decisions made

1. Designed the ui for listing the story with search and filter option
2. Listing --> Recyclerview, Search ---> Edittext with textwatcher and Room database data for filter option
3. Filter category option listed from the api document
4. Once get the data from cloud --> Stored to roomdatabase
5. When different filter selected  --> Checked data from roomdb if not there got the data from cloud
6. When particular story selected --> called the api for particular story data
7. Story details showed in alert dialog   (Title and Url)


Libraries Used

1.For RecyclerView
implementation 'androidx.recyclerview:recyclerview:1.1.0'

2. For cardView
implementation 'androidx.cardview:cardview:1.0.0'

3. For MultiDex
implementation 'androidx.multidex:multidex:2.0.1'

4. For Lifecycle components
implementation 'androidx.lifecycle:lifecycle-common:2.2.0'
implementation 'androidx.lifecycle:lifecycle-runtime:2.2.0'
implementation 'android.arch.lifecycle:extensions:2.2.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'

5. For Koin(Dependency injection)
implementation "org.koin:koin-android:2.0.1"
implementation 'org.koin:koin-androidx-viewmodel:2.0.1'
implementation 'org.koin:koin-androidx-scope:2.0.1'

6. For Retrofit (Cloud Api call)
implementation 'com.squareup.retrofit2:retrofit:2.6.0'
implementation 'com.google.code.gson:gson:2.8.5'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
implementation 'com.squareup.okhttp3:logging-interceptor:3.12.0'

7. For Coroutines(For async Api call)
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5'

8. For Room database(Local db)
def room_version = "2.2.5"
implementation "androidx.room:room-runtime:$room_version"
kapt "androidx.room:room-compiler:$room_version"
// optional - Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:$room_version"

9. For SwipeRefresh
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'