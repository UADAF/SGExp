package com.gt22.sgexp

import codechicken.lib.gui.SimpleCreativeTab
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.Logger

object R {


    const  val MODID = "sgexploration"
    const val NAME = "Stargate Exploration"
    const val MC_VER = "1.10.2"
    const val MOD_VER = "1.0"
    const val VERSION = "mc-${MC_VER}_$MOD_VER"
    val TAB: CreativeTabs = SimpleCreativeTab(MODID, "sgexploration:addressPage")

    lateinit var log: Logger

    fun rl(s: String): ResourceLocation {
        return ResourceLocation(MODID, s)
    }

}