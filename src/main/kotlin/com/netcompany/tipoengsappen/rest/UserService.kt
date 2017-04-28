package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.dao.UserDao
import org.springframework.stereotype.Component
import spark.Route
import spark.Spark.get


@Component
class UserService(val userDao: UserDao) : SparkService {
    override fun init() {
        get("/api/user", Route { req, _ -> req.session().attribute("user") }, toJson)
    }
}