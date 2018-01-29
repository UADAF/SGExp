package com.gt22.sgexp

import com.gt22.sgexp.proxy.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = R.MODID, name = R.NAME, version = R.VERSION, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", modLanguage = "kotlin", dependencies = "required-after:forgelin; required-after:sgcraft; after:OpenComputers")
object SGExp {

    @SidedProxy(clientSide = "com.gt22.sgexp.proxy.ClientProxy", serverSide = "com.gt22.sgexp.proxy.CommonProxy")
    lateinit var proxy: CommonProxy

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        R.log = e.modLog
        proxy.preInit(e)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }


    @Mod.EventHandler
    fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
    }

}