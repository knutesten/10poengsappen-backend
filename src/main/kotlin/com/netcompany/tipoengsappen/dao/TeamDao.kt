package com.netcompany.tipoengsappen.dao

import com.netcompany.tipoengsappen.model.Team
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class TeamDao(val namedTemplate: NamedParameterJdbcTemplate) {
    val rowMapper = RowMapper { rs, _ -> Team(rs.getInt("id"), rs.getString("name")) }

    fun allForUser(userId: Int): Array<Team> {
        return namedTemplate.query("" +
                "SELECT t.* " +
                "FROM team t " +
                "JOIN user_team ut ON ut.team_id = t.id " +
                "WHERE ut.user_id = :user_id",
                MapSqlParameterSource().addValue("user_id", userId),
                rowMapper).toTypedArray()
    }
}
