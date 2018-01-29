package com.gt22.sgexp.proxy

import com.gt22.sgexp.integration.OCIntegration
import com.gt22.sgexp.model.IModelProvider
import com.gt22.sgexp.registry.BlockRegistry
import com.gt22.sgexp.registry.ItemRegistry
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

open class CommonProxy {

    open fun preInit(e: FMLPreInitializationEvent) {
        BlockRegistry.reg()
        ItemRegistry.reg()
        if(Loader.isModLoaded("OpenComputers")) OCIntegration.init()
    }

    open fun init(e: FMLInitializationEvent) {

    }

    open fun postInit(e: FMLPostInitializationEvent) {

    }

    open fun setupModel(m: IModelProvider) {

    }

}