package com.gt22.sgexp.proxy

import com.gt22.sgexp.SGExp
import com.gt22.sgexp.gui.GuiHandler
import com.gt22.sgexp.integration.OCIntegration
import com.gt22.sgexp.model.IModelProvider
import com.gt22.sgexp.registry.BlockRegistry
import com.gt22.sgexp.registry.ItemRegistry
import com.gt22.sgexp.registry.LootRegistry
import com.gt22.sgexp.registry.RecipeRegistry
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

open class CommonProxy {

    open fun preInit(e: FMLPreInitializationEvent) {
        BlockRegistry.reg()
        ItemRegistry.reg()
        if(Loader.isModLoaded("OpenComputers")) OCIntegration.init()
        //MinecraftForge.EVENT_BUS.register(LootRegistry)
    }

    open fun init(e: FMLInitializationEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SGExp, GuiHandler)
        RecipeRegistry.reg()
        //LootRegistry.reg()
    }

    open fun postInit(e: FMLPostInitializationEvent) {

    }

    open fun setupModel(m: IModelProvider) {

    }

}