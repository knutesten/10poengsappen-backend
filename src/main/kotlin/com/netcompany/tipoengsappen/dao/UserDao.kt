package com.netcompany.tipoengsappen.dao

import com.netcompany.tipoengsappen.model.User
import com.netcompany.tipoengsappen.model.UserWithPoints
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Timestamp


@Component
class UserDao(val namedTemplate: NamedParameterJdbcTemplate) {
    val userRowMapper = RowMapper { rs, _ ->
        User(rs.getInt("id"), rs.getString("email"), rs.getString("name"))
    }

    val userWithPointsRowMapper = RowMapper { rs, _ ->
        UserWithPoints(rs.getInt("id"), rs.getString("email"), rs.getString("name"), rs.getInt("points"))
    }

    fun findById(id: Int): User {
        return namedTemplate.queryForObject(
                "SELECT * FROM user WHERE id = :id",
                MapSqlParameterSource().addValue("id", id),
                userRowMapper)
    }

    fun findByEmail(email: String): User {
        return namedTemplate.queryForObject(
                "SELECT * FROM user WHERE email = :email",
                MapSqlParameterSource().addValue("email", email),
                userRowMapper)
    }

    fun allUsersWithPointsForTeam(teamId: Int, userId: Int, from: Timestamp?, to: Timestamp?): Array<UserWithPoints> {
        return namedTemplate.query("" +
                "SELECT u.id, u.email, u.name, sum(p.amount) AS points " +
                "FROM user u " +
                "JOIN user_team ut ON u.id = ut.user_id AND ut.team_id = :team_id " +
                "LEFT JOIN points p ON p.receiver_id = u.id " +
                (if (from != null) "    AND p.created_at >= :from " else "") +
                (if (to != null) "      AND p.created_at <= :to " else "") +
                "WHERE exists (SELECT NULL FROM user_team WHERE user_id = :user_id AND team_id = ut.team_id)" +
                "GROUP BY u.id, u.email, u.name",
                MapSqlParameterSource()
                        .addValue("team_id", teamId)
                        .addValue("user_id", userId)
                        .addValue("from", from)
                        .addValue("to", to),
                userWithPointsRowMapper).toTypedArray()
    }
}