package com.gt22.sgexp.registry

import com.gt22.sgexp.SGExp
import com.gt22.sgexp.block.BlockBase
import com.gt22.sgexp.block.GateAccelerator
import com.gt22.sgexp.block.NaquadriaReactor
import com.gt22.sgexp.model.IModelProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.registry.GameRegistry

object BlockRegistry {

    lateinit var gateAccelerator: GateAccelerator
    lateinit var nqaReactor: NaquadriaReactor

    fun reg() {
        val r = GameRegistry.findRegistry(Block::class.java)
        BlockRegistry::class.java.declaredFields.filter { field -> Block::class.java.isAssignableFrom(field.type) && !ItemBlock::class.java.isAssignableFrom(field.type) }.forEach { field ->
            val i: Block = field.type.newInstance() as Block
            (i as? BlockBase)?.setName(field.name)

            r.register(i)
            field.set(this, i)
            if(i is IModelProvider) SGExp.proxy.setupModel(i)
        }
    }

}