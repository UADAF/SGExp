package com.gt22.sgexp.coremod

import com.gt22.sgexp.registry.BlockRegistry
import gcewing.sg.SGBaseTE
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.Opcodes
import jdk.internal.org.objectweb.asm.tree.*
import net.minecraft.launchwrapper.IClassTransformer

class ClassTransformer : IClassTransformer {
    override fun transform(name: String, transformedName: String, basicClass: ByteArray): ByteArray {
        return if(name == "gcewing.sg.SGBaseTE")
            patchSGBase(name, basicClass)
        else if(name == "gcewing.sg.SGInterfaceTE")
            patchSGInterface(name, basicClass)
        else basicClass
    }

    private fun patchSGBase(name: String, data: ByteArray): ByteArray {
        val node = ClassNode()
        val reader = ClassReader(data)
        reader.accept(node, 0)
        node.methods.find { it.name == "startDiallingToAngle" }?.let {
            for(i in it.instructions) {
                if(i.opcode == Opcodes.BIPUSH) {
                    val load = VarInsnNode(Opcodes.ALOAD, 0)
                    it.instructions.insert(i, load)
                    it.instructions.insert(load, MethodInsnNode(Opcodes.INVOKESTATIC, "com/gt22/sgexp/coremod/Methods", "getAcceleratedDialingTime", "(ILgcewing/sg/SGBaseTE;)I"))
                    return@let
                }
            }
        }
        node.methods.find { it.name == "connect"}?.to(0)?.let(this::modifyEnergy)
        node.methods.find { it.name == "finishDiallingAddress"}?.to(0)?.let(this::modifyEnergy)
        node.methods.find { it.name == "tickEnergyUsage"}?.to(0)?.let(this::modifyEnergy)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
        node.accept(writer)
        return writer.toByteArray()
    }

    private fun patchSGInterface(name: String, data: ByteArray): ByteArray {
        val node = ClassNode()
        val reader = ClassReader(data)
        reader.accept(node, 0)
        node.methods.find { it.name == "ciEnergyToDial"}?.to(2)?.let(this::modifyEnergy)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
        node.accept(writer)
        return writer.toByteArray()
    }

    private fun modifyEnergy(m: Pair<MethodNode, Int>) {
        val it = m.first
        for(i in it.instructions) {
            if(i.opcode == Opcodes.DMUL) {
                val load = VarInsnNode(Opcodes.ALOAD, m.second)
                it.instructions.insert(i, load)
                it.instructions.insert(load, MethodInsnNode(Opcodes.INVOKESTATIC, "com/gt22/sgexp/coremod/Methods", "getAcceleratedEnergyUse", "(DLgcewing/sg/SGBaseTE;)D"))
                break
            }
        }
    }

    companion object {
        fun getAcceleratedDialingTime(defaultTime: Int, te: SGBaseTE): Int {
            var time = defaultTime
            val t = te.localToGlobalTransformation()
            for (i in -2..2) {
                val bp = t.p(i.toDouble(), -1.0, 0.0).blockPos()
                val block = te.world.getBlockState(bp).block
                if(block == BlockRegistry.gateAccelerator) {
                    time = ((time + 1) / 2) //0..5: 40 20 10 5 3 2 1
                }
            }
            return time
        }

        fun getAcceleratedEnergyUse(te: SGBaseTE, energy: Double): Double {
            var eng = energy
            val t = te.localToGlobalTransformation()
            for (i in -2..2) {
                val bp = t.p(i.toDouble(), -1.0, 0.0).blockPos()
                val block = te.world.getBlockState(bp).block
                if(block == BlockRegistry.gateAccelerator) {
                    eng *= 4
                }
            }
            return eng
        }
    }



}