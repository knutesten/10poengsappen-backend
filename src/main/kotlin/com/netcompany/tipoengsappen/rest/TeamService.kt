package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.dao.TeamDao
import com.netcompany.tipoengsappen.dao.UserDao
import org.springframework.stereotype.Component
import spark.Route
import spark.Spark.*


@Component
open class TeamService(val teamDao: TeamDao, val userDao: UserDao) : SparkService {

    override fun init() {
        path("/api/teams") {
            before("", authenticate)
            before("/*", authenticate)

            get("", Route { req, _ ->
                val user = req.getUser()
                teamDao.allForUser(user.id)
            }, toJson)

            get("/:id", Route { req, _ ->
                userDao.allUsersWithPointsForTeam(req.params(":id").toInt(), req.getUser().id)
            }, toJson)
        }
    }
}


