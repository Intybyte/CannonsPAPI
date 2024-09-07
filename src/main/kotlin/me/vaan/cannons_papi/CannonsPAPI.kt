package me.vaan.cannons_papi

import at.pavlov.cannons.cannon.Cannon
import at.pavlov.cannons.cannon.CannonManager
import at.pavlov.cannons.container.ItemHolder
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class CannonsPAPI : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "cannons"
    }

    override fun getAuthor(): String {
        return "Vaan1310"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun getRequiredPlugin(): String {
        return "Cannons"
    }

    override fun canRegister(): Boolean {
        return Bukkit.getPluginManager().getPlugin(requiredPlugin) != null
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        player ?: return null
        val cannonList = CannonManager.getCannonList().values

        if(cannonList.isEmpty()) return null
        var operatedCannon: Cannon? = null
        for (cannon in cannonList) {
            if (cannon.cannonOperator == player.uniqueId && cannon.isMasterCannon) {
                operatedCannon = cannon
                break
            }
        }

        operatedCannon ?: return null

        when(params) {
            "name" -> return operatedCannon.cannonName
            "design" -> return operatedCannon.designID
            "horizontal_angle" -> return operatedCannon.horizontalAngle.toString()
            "vertical_angle" -> return operatedCannon.verticalAngle.toString()
            "temperature" -> return operatedCannon.ambientTemperature.toString()
            "loaded_gunpowder" -> return operatedCannon.loadedGunpowder.toString()
            "loaded_projectile" -> {
                val stack: ItemHolder
                try {
                    stack = operatedCannon.loadedProjectile.loadingItem
                } catch (e: Exception) {
                    return "None"
                }

                if (stack.hasDisplayName()) {
                    return stack.displayName
                }

                val materialName = stack.toString().lowercase().replace(":", "")
                println(materialName)
                var normalName = ""

                for (word in materialName.split('_')) {
                    //capitalize first letter
                    normalName += word.replaceFirstChar { it.titlecase(Locale.getDefault())  } + " "
                }

                return normalName
            }
        }

        return null
    }

}
