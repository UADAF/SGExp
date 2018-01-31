package com.gt22.sgexp.block

import com.gt22.sgexp.R
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


open class BlockBase(mat: Material) : Block(mat), IModelProvider {

    init {
        @Suppress("LeakingThis")
        setCreativeTab(R.TAB)
    }

    internal fun setName(name: String) {
        registryName = R.rl(name)
        unlocalizedName = name
    }

    @SideOnly(Side.CLIENT)
    override fun setupModels() {
        val bld = StateMap.Builder()
        for (prop in getBlockState().properties) {
            bld.ignore(prop)
        }
        ModelLoader.setCustomStateMapper(this, bld.build())
    }

}