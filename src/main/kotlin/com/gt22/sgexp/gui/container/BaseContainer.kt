//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.10 - Generic inventory container
//
//------------------------------------------------------------------------------------------------

package com.gt22.sgexp.gui.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IContainerListener
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

open class BaseContainer(internal var xSize: Int, internal var ySize: Int) : Container() {
    internal lateinit var playerSlotRange: SlotRange

    protected fun addPlayerSlots(player: EntityPlayer, x: Int = (xSize - 160) / 2, y: Int = ySize - 82) {
        playerSlotRange = SlotRange()
        val inventory = player.inventory
        for (row in 0..2)
            for (col in 0..8)
                this.addSlotToContainer(Slot(inventory, col + row * 9 + 9, x + col * 18, y + row * 18))
        for (col in 0..8)
            this.addSlotToContainer(Slot(inventory, col, x + col * 18, y + 58))
        playerSlotRange.end()
    }

    protected fun addSlots(inventory: IInventory, x: Int, y: Int, numRows: Int): SlotRange {
        return addSlots(inventory, 0, inventory.sizeInventory, x, y, numRows)
    }

    protected fun addSlots(inventory: IInventory, x: Int, y: Int, numRows: Int, slotClass: Class<*>): SlotRange {
        return addSlots(inventory, 0, inventory.sizeInventory, x, y, numRows, slotClass)
    }

    protected fun addSlots(inventory: IInventory, firstSlot: Int, numSlots: Int, x: Int, y: Int, numRows: Int,
                 slotClass: Class<*> = Slot::class.java): SlotRange {
        val range = SlotRange()
        try {
            val slotCon = slotClass.getConstructor(IInventory::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            val numCols = (numSlots + numRows - 1) / numRows
            for (i in 0 until numSlots) {
                val row = i / numCols
                val col = i % numCols
                addSlotToContainer(slotCon.newInstance(inventory, firstSlot + i, x + col * 18, y + row * 18) as Slot)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        range.end()
        return range
    }

    override fun canInteractWith(var1: EntityPlayer): Boolean {
        return true
    }

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        listeners.forEach { sendStateTo(it) }
    }

    // To enable shift-clicking, check validitity of items here and call
    // mergeItemStack as appropriate.
    override fun transferStackInSlot(player: EntityPlayer?, index: Int): ItemStack? {
        var result: ItemStack? = null
        val slot = inventorySlots[index]

        if (slot != null && slot.hasStack) {
            val stack = slot.stack
            val destRange = transferSlotRange(index, stack)
            if (destRange != null) {
                result = stack!!.copy()
                if (!mergeItemStackIntoRange(stack, destRange))
                    return null
                if (stack.stackSize == 0)
                    slot.putStack(null)
                else
                    slot.onSlotChanged()
            }
        }
        return result
    }

    protected fun mergeItemStackIntoRange(stack: ItemStack, range: SlotRange): Boolean {
        return mergeItemStack(stack, range.firstSlot, range.numSlots, range.reverseMerge)
    }

    // Return the range of slots into which the given stack should be moved by
    // a shift-click.
    protected fun transferSlotRange(srcSlotIndex: Int, stack: ItemStack?): SlotRange? {
        return null
    }

    internal fun sendStateTo(listener: IContainerListener) {}

    override fun updateProgressBar(i: Int, value: Int) {}

    inner class SlotRange {
        var firstSlot: Int = 0
        var numSlots: Int = 0
        var reverseMerge: Boolean = false

        init {
            firstSlot = inventorySlots.size
        }

        fun end() {
            numSlots = inventorySlots.size - firstSlot
        }

        operator fun contains(slot: Int): Boolean {
            return slot >= firstSlot && slot < firstSlot + numSlots
        }
    }

}
