package com.netcompany.tipoengsappen.model

import com.fasterxml.jackson.annotation.JsonProperty

class Points(
        @JsonProperty("giverId")
        val giverId: Int,
        @JsonProperty("receiverId")
        val receiverId: Int,
        @JsonProperty("teamId")
        val teamId: Int,
        @JsonProperty("amount")
        val amount: Int
)


