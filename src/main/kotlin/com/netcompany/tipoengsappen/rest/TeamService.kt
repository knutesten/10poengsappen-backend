package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.dao.TeamDao
import com.netcompany.tipoengsappen.dao.UserDao
import org.springframework.stereotype.Component
import spark.Route
import spark.Spark.before
import spark.Spark.get


@Component
class TeamService(val teamDao: TeamDao, val userDao: UserDao) : SparkService {

    override fun init() {
        before("/api/teams", authenticate)
        before("/api/teams/*", authenticate)

        get("/api/teams", Route { req, _ ->
            val user = req.getUser()
            teamDao.allForUser(user.id)
        }, toJson)

        get("/api/teams/:id", Route { req, _ ->
            userDao.allUsersWithPointsForTeam(req.params(":id").toInt(), req.getUser().id)
        }, toJson)
    }
}


