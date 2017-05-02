package com.netcompany.tipoengsappen.rest

import com.netcompany.tipoengsappen.dao.PointsDao
import com.netcompany.tipoengsappen.model.Points
import org.springframework.stereotype.Component
import spark.Spark.*

@Component
open class PointsService(val pointsDao: PointsDao) : SparkService {
    override fun init() {
        before("/api/points", authenticate)
        post("/api/points") { req, res ->
            val points = req.bodyAsObject<Points>()

            if (points.giverId != req.getUser().id)
                halt(400, "You can only give points from yourself.")

            if (points.giverId == points.receiverId)
                halt(400, "You can not give points to yourself.")

            pointsDao.insert(points)
            res.status(204)
            res
        }

        delete("/api/points/undo") { req, res ->
            pointsDao.undo(req.getUser().id)
            res.status(204)
            res
        }
    }
}