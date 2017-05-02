package com.netcompany.tipoengsappen.dao

import com.netcompany.tipoengsappen.model.Points
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
open class PointsDao(val namedTemplate: NamedParameterJdbcTemplate) {
    fun insert(points: Points) {
        if (points.amount < 1 || points.amount > 10)
            throw IllegalArgumentException("The amount of points given must be between 1 and 10")

        namedTemplate.update("" +
                "INSERT INTO points (created_at, receiver_id, giver_id, team_id, amount) " +
                "VALUES (:now, :receiver_id, :giver_id, :team_id, :amount)",
                MapSqlParameterSource()
                        .addValue("now", Timestamp.from(Instant.now()))
                        .addValue("receiver_id", points.receiverId)
                        .addValue("giver_id", points.giverId)
                        .addValue("team_id", points.teamId)
                        .addValue("amount", points.amount))
    }

    fun undo(userId: Int) {
        namedTemplate.update("" +
                "DELETE" +
                "FROM points " +
                "WHERE id = (SELECT id " +
                "            FROM points" +
                "            WHERE created_at > :one_minute_ago" +
                "                  AND giver_id = :user_id " +
                "            ORDER BY id DESC" +
                "            LIMIT 1)",
                MapSqlParameterSource()
                        .addValue("one_minute_ago", Timestamp.from(Instant.now().minus(1, ChronoUnit.MINUTES)))
                        .addValue("user_id", userId))
    }
}