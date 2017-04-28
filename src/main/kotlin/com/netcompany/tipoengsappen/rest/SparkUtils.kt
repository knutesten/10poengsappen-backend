package com.netcompany.tipoengsappen.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.netcompany.tipoengsappen.model.User
import spark.Filter
import spark.Request
import spark.ResponseTransformer
import spark.Spark.halt

val toJson = ResponseTransformer { obj -> ObjectMapper().writeValueAsString(obj) }


val authenticate = Filter { req, _ ->
    try {
        req.getUser()
    } catch (_: IllegalStateException) {
        halt(401, "You are not welcome here.")
    }
}


fun Request.getUser(): User = this.session().attribute<User>("user")