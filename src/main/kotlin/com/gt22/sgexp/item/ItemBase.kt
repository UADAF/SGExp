package com.gt22.sgexp.item

import com.gt22.sgexp.R
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


open class ItemBase : Item(), IModelProvider {

    init {
        creativeTab = R.TAB
    }

    fun setName(name: String) {
        unlocalizedName = name
        registryName = R.rl(name)
    }

    @SideOnly(Side.CLIENT)
    override fun setupModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName, "normal"))
    }


}