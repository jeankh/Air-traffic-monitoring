package com.example.airtrafficmonitoring

data class FlightData(
    val icao24: String,
    val firstSeen: Long,
    val estDepartureAirport: String,
    val lastSeen: Long,
    val estArrivalAirport: String,
    val callsign: String,
    val estDepartureAirportHorizDistance: Int,
    val estDepartureAirportVertDistance: Int,
    val estArrivalAirportHorizDistance: Int,
    val estArrivalAirportVertDistance: Int,
    val departureAirportCandidatesCount: Int,
    val arrivalAirportCandidatesCount: Int
) {
    // Constructeur secondaire (peut être utilisé pour créer un objet directement dans la classe).
    constructor() : this(
        icao24 = "",
        firstSeen = 0,
        estDepartureAirport = "",
        lastSeen = 0,
        estArrivalAirport = "",
        callsign = "",
        estDepartureAirportHorizDistance = 0,
        estDepartureAirportVertDistance = 0,
        estArrivalAirportHorizDistance = 0,
        estArrivalAirportVertDistance = 0,
        departureAirportCandidatesCount = 0,
        arrivalAirportCandidatesCount = 0
    )
}

