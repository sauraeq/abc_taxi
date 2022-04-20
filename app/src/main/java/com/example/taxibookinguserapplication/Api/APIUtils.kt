package com.example.taxibookinguserapplication.Api


class APIUtils {

    companion object {

        private val BASE_URL = "http://demo.equalinfotech.com/abc/api/"

        fun getServiceAPI(): APIConfiguration? {
            return APIClient.getApiClient(BASE_URL)!!.create(APIConfiguration::class.java)

        }
    }


}