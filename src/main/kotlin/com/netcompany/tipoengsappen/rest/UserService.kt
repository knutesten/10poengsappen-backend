package com.netcompany.tipoengsappen.rest

import org.springframework.stereotype.Component
import spark.Route
import spark.Spark.get

@Component
open class UserService() : SparkService {
    override fun init() {
        get("/api/user", Route { req, _ -> req.session().attribute("user") }, toJson)
    }
}