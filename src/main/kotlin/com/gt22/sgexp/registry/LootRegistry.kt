package com.gt22.sgexp.registry

import com.gt22.sgexp.R
import net.minecraft.util.ResourceLocation
import net.minecraft.world.storage.loot.*
import net.minecraftforge.event.LootTableLoadEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


object LootRegistry {
    private var tables: MutableList<String> = mutableListOf(
            "abandoned_mineshaft",
            "desert_pyramid",
            "end_city_treasure",
            "jungle_temple",
            "simple_dungeon",
            "stronghold_corridor",
            "stronghold_crossing",
            "stronghold_library",
            "village_blacksmith"
    )

    //@SubscribeEvent
    fun tableLoad(e: LootTableLoadEvent) {
        var table = ""
        for (t in tables) {
            if (e.name.resourcePath == "chests/$t") {
                e.table.addPool(getAdditive("chests/$t"))
                table = t
                break
            }
        }
        if (!table.isEmpty()) {
            tables.remove(table)
        }
    }

    fun reg() {
        for(t in tables) {
            LootTableList.register(R.rl("chests/$t"))
        }
    }


    private fun getAdditive(entryName: String): LootPool {
        return LootPool(arrayOf<LootEntry>(getAdditiveEntry(entryName, 1)), arrayOfNulls(0), RandomValueRange(1f), RandomValueRange(0f, 1f), "Additive_pool")
    }

    /** Make sure to setup your resource location accordingly  */
    private fun getAdditiveEntry(name: String, weight: Int): LootEntryTable {
        return LootEntryTable(R.rl(name), weight, 0, arrayOfNulls(0), "Additive_entry")
    }

}