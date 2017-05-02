package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.auth.OpenIdConnectAuth
import com.netcompany.tipoengsappen.dao.UserDao
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Component
import spark.Route
import spark.Spark.get
import spark.Spark.halt
import java.math.BigInteger
import java.security.SecureRandom


@Component
open class AuthService(val openIdConnectAuth: OpenIdConnectAuth,
                       val userDao: UserDao,
                       @Value("\${open-id-connect.logout-url") val logoutUrl: String,
                       @Value("\${server.url}") val serverUrl: String) : SparkService {
    override fun init() {
        get("/api/auth/login") { req, res ->
            val state = BigInteger(130, SecureRandom()).toString(32)
            req.session(true)
            req.session().attribute("state", state)
            req.session().attribute("redirectUrl", serverUrl)
            res.redirect(openIdConnectAuth.createAuthenticationUrl(state))
            res
        }

        get("/api/auth/code") { req, res ->
            if (req.queryParams("state") != req.session().attribute<String>("state")) {
                req.session().invalidate()
                halt(401, "Invalid state parameter.")
            }

            try {
                val email = openIdConnectAuth.exchangeCodeForEmail(req.queryParams("code"))
                req.session().attribute("user", userDao.findByEmail(email))
                res.redirect(req.session().attribute("redirectUrl"))
                res
            } catch (_: DataAccessException) {
                halt(401, "You are not a registered user.")
            }
        }

        get("/api/auth/logout") { req, res ->
            req.session().invalidate()
            res.redirect(logoutUrl)
            res
        }

        get("/api/auth/session", Route { req, _ -> req.session().attribute("user") }, toJson)
    }
}