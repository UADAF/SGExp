package com.gt22.sgexp.registry

import gcewing.sg.SGCraft
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.registry.GameRegistry

object RecipeRegistry {


    fun reg() {
        val swiftnessPotion = i(Items.POTIONITEM).apply { tagCompound = NBTTagCompound().apply { setString("Potion", "long_swiftness") } }
        GameRegistry.addShapedRecipe(i(BlockRegistry.gateAccelerator), "RSO", "CNC", "OSR", 'R', i(SGCraft.sgCoreCrystal), 'O', i(SGCraft.sgControllerCrystal), 'S', swiftnessPotion, 'C', i(Items.CLOCK), 'N', i(SGCraft.naquadahBlock))
        GameRegistry.addShapelessRecipe(i(ItemRegistry.addressPage), i(Items.CLAY_BALL), i(Items.CLAY_BALL), i(Items.CLAY_BALL), i(SGCraft.sgCoreCrystal), i(SGCraft.naquadah))
    }

    private fun i(i: Item) = ItemStack(i)
    private fun i(b: Block) = ItemStack(b)

}