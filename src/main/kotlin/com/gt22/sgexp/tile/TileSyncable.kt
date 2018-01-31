package com.gt22.sgexp.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Created by mrAppleXZ on 11.04.17 20:28.
 */
open class TileSyncable : TileEntity() {
    @SideOnly(Side.CLIENT)
    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        super.onDataPacket(net, pkt)
        readFromNBT(pkt.nbtCompound)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(getPos(), 1, updateTag)
    }

    fun sendUpdatesToClients() {
        val ibs = world.getBlockState(getPos())
        world.notifyBlockUpdate(getPos(), ibs, ibs, 2)
    }
}
