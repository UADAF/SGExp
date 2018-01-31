package com.gt22.sgexp.item

import com.gt22.sgexp.R
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraft.world.storage.loot.LootTableList
import java.util.*

class ChestPlacer : ItemBase() {


    init {
        creativeTab = R.TAB

    }

    override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<String>?, par4: Boolean) {
        list!!.add(TextFormatting.WHITE.toString() + "Creates a chest with loot")
        list.add(TextFormatting.ITALIC.toString() + "Normally places our custom loot table")
        list.add(TextFormatting.ITALIC.toString() + "Sneaking places a modified loot table")
    }

    override fun onItemUse(stack: ItemStack?, player: EntityPlayer?, world: World?, pos: BlockPos?, hand: EnumHand?, theface: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {

        val iblockstate = Blocks.CHEST.defaultState
        world!!.setBlockState(pos!!, Blocks.CHEST.correctFacing(world, pos, iblockstate), 2)
        val tileentity = world.getTileEntity(pos)

        val random = Random()

        if (tileentity is TileEntityChest) {
            /**Our Custom Loot */
            var location = LootTableList.CHESTS_ABANDONED_MINESHAFT

            if (player!!.isSneaking) {
                /**Our modified Spawn Chest Loot */
                location = LootTableList.CHESTS_STRONGHOLD_LIBRARY
            }

            tileentity.setLootTable(location, random.nextLong())
        }


        return EnumActionResult.SUCCESS
    }

}