package `in`.webgrid.svindobusiness.modelclass


data class AddPrintingProduct(
    val colour_data: List<ColourData>,
    val fabric_data: List<FabricData>,
    val pattern_data: List<PatternData>,
    val prices_data: List<PricesData>,
    val sizes_data: List<SizesData>,
    val addons_data: List<AddonsData>
)
data class ColourData(
    val name: String,
    val code: String
)
data class FabricData(
    val name: String,
    val description: String
)
data class PatternData(
    val name: String,
    val description: String
)
data class PricesData(
    val quantity: String,
    val sale_price: String,
    val piece_price: String
)
data class SizesData(
    val size: String,
    val size_from: String,
    val size_to: String
)
data class AddonsData(val id: String, )