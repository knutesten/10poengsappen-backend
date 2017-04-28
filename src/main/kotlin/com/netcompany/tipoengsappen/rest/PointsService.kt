package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.dao.PointsDao
import com.netcompany.tipoengsappen.model.Points
import org.springframework.stereotype.Component
import spark.Spark.*

@Component
open class PointsService(val pointsDao: PointsDao) : SparkService {
    override fun init() {
        before("/api/points", authenticate)
        put("/api/points", {req, res ->
            val points = req.bodyAsObject<Points>()

            if (points.giverId != req.getUser().id)
                halt(400, "You can only give points from yourself.")

            pointsDao.insert(points)
            res.status(204)
            res
        })
    }
}