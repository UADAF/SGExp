package com.gt22.sgexp.tile

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

class NaquadriaReactorTile : TileSyncable() {


    private val inventory: IItemHandler = object : ItemStackHandler(9) {

        override fun onContentsChanged(slot: Int) {
            markDirty()
            sendUpdatesToClients()
        }

    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return when(capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> true
            else -> super.hasCapability(capability, facing)
        }
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T {
        return when(capability) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
            else -> super.getCapability(capability, facing)
        }
    }



}
