package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.auth.OpenIdConnectAuth
import org.springframework.stereotype.Component
import spark.Spark.get
import spark.Spark.halt
import java.math.BigInteger
import java.security.SecureRandom


@Component
class UserService(val openIdConnectAuth: OpenIdConnectAuth) : SparkService {
    override fun init() {
        get("/api/auth/login") { req, res ->
            val state = BigInteger(130, SecureRandom()).toString(32)
            req.session(true)
            req.session().attribute("state", state)
            res.redirect(openIdConnectAuth.createAuthenticationUrl(state))
            res
        }

        get("/api/auth/code") { req, res ->
            if (req.queryParams("state") != req.session().attribute<String>("state")) {
                req.session().invalidate()
                halt(401, "Invalid state parameter.")
            }

            val email = openIdConnectAuth.exchangeCodeForEmail(req.queryParams("code"))

            req.session().attribute("email", email)

            res.redirect("/api/auth/user")
            res
        }

        get("/api/auth/logout") { req, res ->
            req.session().invalidate()
            res.status(204)
            res
        }

        get("/api/auth/user") { req, _ -> req.session().attribute("email") }
    }
}