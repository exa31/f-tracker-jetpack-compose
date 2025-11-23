package cloud.eka_dev.ftracker.data.remote.dto

data class BaseResponse<T>(
    val message: String,
    val success: Boolean,
    val statusCode: Int,
    val data: T
)


//"statusCode": 200,
//"message": "Login successful",
//"data": {
//    "accessToken": "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImJsb29kc3VrZXIxOEBnbWFpbC5jb20iLCJuYW1lIjoiTW9oLiBFa2EgU3lhZnJpbm8gTmF6aGlmYW4iLCJ0eXBlIjoiYWNjZXNzIiwiaWRfdXNlciI6IjY2ZWJiNGU5N2IyOTBkYWNjY2RhOTlkMSIsImlhdCI6MTc1NzQzNDMzNCwiZXhwIjoxNzU3NDM3OTM0fQ.2Fm-EGOZJhY42MG-c0OBl_d2LJKJStj3FJ7ZpFBxiEbsYG0HXXSVIhdBboMjBtoF",
//    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImJsb29kc3VrZXIxOEBnbWFpbC5jb20iLCJuYW1lIjoiTW9oLiBFa2EgU3lhZnJpbm8gTmF6aGlmYW4iLCJ0eXBlIjoicmVmcmVzaCIsImlkX3VzZXIiOiI2NmViYjRlOTdiMjkwZGFjY2NkYTk5ZDEiLCJpYXQiOjE3NTc0MzQzMzQsImV4cCI6MTc2MDAyNjMzNH0.rYec7HxaL65wcY_9IYV0z7gSzCFW_BxsjIyZSEkEbGo"
//},
//"success": true