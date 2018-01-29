package com.gt22.sgexp.registry

import com.gt22.sgexp.SGExp
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.registry.GameRegistry

object BlockRegistry {


    fun reg() {
        val r = GameRegistry.findRegistry(Block::class.java)
        BlockRegistry::class.java.declaredFields.filter { field -> Block::class.java.isAssignableFrom(field.type) && !ItemBlock::class.java.isAssignableFrom(field.type) }.forEach { field ->
            val i: Block = field.type.newInstance() as Block
            r.register(i)
            field.set(this, i)
            if(i is IModelProvider) SGExp.proxy.setupModel(i)
        }
    }

}