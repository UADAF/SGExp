package com.gt22.sgexp.registry

import com.gt22.sgexp.R
import com.gt22.sgexp.tile.NaquadriaReactorTile
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry

object TileRegistry {

    private fun r(te: Class<out TileEntity>) {
        GameRegistry.registerTileEntity(te, "${R.MODID}_${te.simpleName}")
    }

    fun reg() {
        r(NaquadriaReactorTile::class.java)
    }


}