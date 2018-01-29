package com.gt22.sgexp.item

import com.gt22.sgexp.R
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.item.Item
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import jline.console.KeyMap.meta
import net.minecraftforge.client.model.ModelLoader



open class ItemBase(name: String) : Item(), IModelProvider {

    init {
        unlocalizedName = name
        registryName = R.rl(name)
        creativeTab = R.TAB
    }

    override fun setupModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName, "normal"))
    }


}