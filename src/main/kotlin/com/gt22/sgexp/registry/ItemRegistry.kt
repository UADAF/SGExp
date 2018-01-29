package com.gt22.sgexp.registry

import com.gt22.sgexp.R
import com.gt22.sgexp.SGExp
import com.gt22.sgexp.item.AddressPage
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.registry.IForgeRegistry
import java.lang.reflect.Field

object ItemRegistry {

    lateinit var addressPage: AddressPage

    fun reg() {
        val r = GameRegistry.findRegistry(Item::class.java)
        ItemRegistry::class.java.declaredFields.filter { Item::class.java.isAssignableFrom(it.type) }.forEach {
            if (ItemBlock::class.java.isAssignableFrom(it.type)) {
                regItemBlock(it, r)
            } else {
                regItem(it, r)
            }
        }
    }

    private fun regItem(field: Field, r: IForgeRegistry<Item>) {
        val i: Item = field.type.newInstance() as Item
        r.register(i)
        field.set(this, i)
        if (i is IModelProvider) SGExp.proxy.setupModel(i)
    }

    private fun regItemBlock(field: Field, r: IForgeRegistry<Item>) {
        try {
            val block: Field = BlockRegistry::class.java.getDeclaredField(field.name)
            val ibc: Class<out ItemBlock> = field.type.asSubclass(ItemBlock::class.java)
            block.isAccessible = true
            val ib: ItemBlock = ibc.getDeclaredConstructor(Block::class.java).newInstance(block.get(BlockRegistry) as Block)
            r.register(ib)
            field.set(this, ib)
            if (ib is IModelProvider) SGExp.proxy.setupModel(ib)
        } catch (e: NoSuchFieldException) {
            R.log.warn("Block ${field.name} not found! Unable to register ItemBlock")
        }

    }

}