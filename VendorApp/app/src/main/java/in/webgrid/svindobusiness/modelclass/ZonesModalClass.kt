package `in`.webgrid.svindobusiness.modelclass

data class ZonesModalClass(
    val error: String,
    val location_zone: List<LocationZoneXX>,
    val location_sub_zone: List<LocationSubZone>
)
data class LocationSubZone(
    val id: String,
    val pin_code: String
)