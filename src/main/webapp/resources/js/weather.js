const currentLocationWeather = document.querySelector(".currentLocationWeather")
const currentLocationWeeklyWeather = document.querySelector(".currentLocationWeeklyWeather")
const currentHourWeather = document.querySelector(".currentHourWeather")
const city = document.querySelector("#city")
const sessionLatitude = document.querySelector("#latitude")
const sessionLongitude = document.querySelector("#longitude")
const KELVIN_OFFSET = 273.15
const today = new Date().getDate()
let sessionLatitudeValue = sessionLatitude ? parseFloat(sessionLatitude.getAttribute("data-latitude")) : null
let sessionLongitudeValue = sessionLongitude ? parseFloat(sessionLongitude.getAttribute("data-longitude")) : null

// 현위치 정보 가져오기
document.querySelector("#currentLocationBtn").addEventListener('click', function(){
  if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition((position) => {
      const locationObj = weatherLocation(position)
      getWeatherInfo(locationObj.latitude, locationObj.longitude)
    })
  } else {
    console.log("현재 위치 사용 불가능")
  }
})

// 위도, 경도 가져오기
const weatherLocation = (position) => {
  const locationObj = {
    latitude: position.coords.latitude,
    longitude: position.coords.longitude
  }
  return locationObj
}

// 세션에 저장된 현위치 위도, 경도가 없는 경우 서울(기본) 위도, 경도
const weatherDefaultLocation = () => {
  const SEOUL_LATITUDE = 37.5665
  const SEOUL_LONGITUDE = 126.9780
  const defaultLocationObj = {
    latitude: SEOUL_LATITUDE,
    longitude: SEOUL_LONGITUDE
  }
  return defaultLocationObj
}

// 날씨 아이콘, 날씨 문자열 생성
const getWeatherIconAndText = (main) => {
  let icon = ''
  let weather = ''
  
  switch(main){
    case 'Clear':
      icon = '<i class="bi bi-brightness-high"></i>'
      weather = '맑음'
      break;
    case 'Wind':
      icon = '<i class="bi bi-wind"></i>'
      weather = '바람'
      break;
    case 'Clouds':
      icon = '<i class="bi bi-clouds"></i>'
      weather = '흐림'
      break;
    case 'Rain':
      icon = '<i class="bi bi-cloud-rain-fill"></i>'
      weather = '비'
      break;
    case 'Snow':
      icon = '<i class="bi bi-snow2"></i>'
      weather = '눈'
      break;
    default:
      icon = '<p>icon err</p>'
  }
  return {icon, weather}
}

// dt_txt 문자열 분해
const parseDateTime = (dt_txt) => {
  // 날짜, 시간 분리
  const [dataPart, timePart] = dt_txt.split(" ") // 2001-01-01 21:00:00

  // 날짜 분해
  const [year, month, day] = dataPart.split("-")
  
  // 시간 분해
  const [hour, minute, second] = timePart.split(":")
  
  return {year, month, day, hour}
}

// 서버에서 날씨 정보 가져오기
const getWeatherInfo = (latitude, longitude) => {
  // 세션에 저장된 위도, 경도가 없는 경우 서울로 지정
  if (!(latitude) || !(longitude)) {
    const defaultLocation  = weatherDefaultLocation()
    latitude = defaultLocation.latitude;
    longitude = defaultLocation.longitude;
  }

  // 오늘 날씨
  $.ajax({
    type: "get",
    url: "/weather/getCurrentWeather",
    data: {latitude, longitude},
    success: function(currentWeatherDto){
      let output = updateCurrentWeatherInfo(currentWeatherDto)
      currentLocationWeather.innerHTML = output
    },
    error: function(error){
      console.error("날씨 정보 가져오기 실패", error)
    }
  })

  // 일주일 날씨
  $.ajax({
    type: "get",
    url: "/weather/getWeeklyWeather",
    data: {latitude, longitude},
    success: function(weeklyWeatherDto){
      let weeklyOutput = updateWeeklyWeatherInfo(weeklyWeatherDto)
      currentLocationWeeklyWeather.innerHTML = weeklyOutput
      
      let hourlyOutput = hourlyWeather(weeklyWeatherDto) // 시간별 오늘 날씨 
      currentHourWeather.innerHTML = hourlyOutput
      
      let cityNameOutput = updateCityName(weeklyWeatherDto) // 도시 이름 업데이트
      city.innerHTML = cityNameOutput 
    },
    error: function(){
      console.error("일주일 날씨 정보 가져오기 실패", error)
    }
  })
}

// 오늘 날씨 정보 화면에 업데이트
const updateCurrentWeatherInfo = (currentWeatherDto) => {
  const main = currentWeatherDto.weather[0].main // 주요 날씨
  //const description = weatherDto.weather[0].description // 주요 날씨 상세
  const feels_like = (currentWeatherDto.main.feels_like - KELVIN_OFFSET).toFixed(1) // 체감온도
  const temp = (currentWeatherDto.main.temp - KELVIN_OFFSET).toFixed(1)
  const temp_max = (currentWeatherDto.main.temp_max - KELVIN_OFFSET).toFixed(1)
  const temp_min = (currentWeatherDto.main.temp_min - KELVIN_OFFSET).toFixed(1)
  const {icon, weather}= getWeatherIconAndText(main) // weather: 맑음, 바람, 흐림, 비, 눈
  let output = ''
  output +=
    `
        <p class="high-low-temperature"><span id="temp_min">${temp_min}°</span> / <span id="temp_max">${temp_max}°</span></p>
        <p></p>
        <p><h2 class="currentTemperature">${temp}°</h2></p>
        <p>${icon} ${weather}</p>
        <p>체감 ${feels_like}°</p>
    `
  return output
}

// 일주일 날씨 정보 화면에 업데이트
const updateWeeklyWeatherInfo = (weeklyWeatherDto) => {
  const otherdayDto = weeklyWeatherDto.list.filter((item) => parseDateTime(item.dt_txt).day != today)
  
  let output = ''
  let lastDate = ''

  otherdayDto.forEach((item) => {
    const main = item.weather[0].main // weeklyWeatherDto.list[0].weather[0].main
    const feels_like = (item.main.feels_like - KELVIN_OFFSET).toFixed(1) 
    const temp = (item.main.temp - KELVIN_OFFSET).toFixed(1)
    const temp_max = (item.main.temp_max - KELVIN_OFFSET).toFixed(1)
    const temp_min = (item.main.temp_min - KELVIN_OFFSET).toFixed(1)
    const dt_txt = (item.dt_txt)

    let dateTime = parseDateTime(dt_txt)
    const currentDate = `${dateTime.month}${dateTime.day}`
    const {icon, weather}= getWeatherIconAndText(main)

    // 날짜가 이전과 다르다면 divider 클래스 추가
    let dividerClass = ''
    if (lastDate != '' && lastDate != currentDate) {
      dividerClass = 'date-divider'
    }
    lastDate = currentDate

    output +=
      `
        <div class="weekly-weather-row ${dividerClass}">
          <span class="date-text">${dateTime.month} / ${dateTime.day}</span>
          <span>${icon}</span>
          <span id="hour">${dateTime.hour}시</span>
          <span>${temp}°</span>
          <span>${weather}</span>
          <span>체감 ${feels_like}°</span>
          <span>
            <span id="temp_min">${temp_min}°</span> / <span id="temp_max">${temp_max}°</span>
          </span>
        </div>
      `
    })
  return output
}

// 시간별 오늘 날씨
const hourlyWeather = (weeklyWeatherDto) => {
  const hourlyWeaterDto = weeklyWeatherDto.list.filter((item) => parseDateTime(item.dt_txt).day == today)
  let output = ''
  hourlyWeaterDto.forEach((item) => {
    const main = item.weather[0].main
    const temp = (item.main.temp - KELVIN_OFFSET).toFixed(1)
    const temp_max = (item.main.temp_max - KELVIN_OFFSET).toFixed(1)
    const temp_min = (item.main.temp_min - KELVIN_OFFSET).toFixed(1)
    const dt_txt = (item.dt_txt)
    let dateTime = parseDateTime(dt_txt)
    const {icon, weather}= getWeatherIconAndText(main)
    output += 
      `
        <div class="weather-row">
          <div class="weather-cell" id="hour">${dateTime.hour}시</div>
          <div class="weather-cell">${temp}°</div>
          <div class="weather-cell">${icon}</div>
          <div class="weather-cell">${weather}</div><br>
        </div>
      `
  })
  return output
}

// 도시 이름 업데이트
const updateCityName = (weeklyWeatherDto) => {
  const cityName = weeklyWeatherDto.city.name
  return `${cityName}`
}

// 페이지 로드 시 함수 호출
getWeatherInfo(sessionLatitudeValue, sessionLongitudeValue)