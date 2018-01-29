package com.gt22.sgexp.coremod

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

class SGExpCoreMod : IFMLLoadingPlugin {

    override fun getASMTransformerClass(): Array<String> = arrayOf(ClassTransformer::class.java.name)

    override fun getSetupClass(): String? = null

    override fun injectData(data: MutableMap<String, Any>?) {

    }

    override fun getAccessTransformerClass(): String? = null

    override fun getModContainerClass(): String? = null

}