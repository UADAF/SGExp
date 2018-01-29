package com.gt22.sgexp.gui

import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler


typealias GuiCreator<T> = (ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) -> T

object GuiHandler : IGuiHandler{



    private val guiStorage: MutableList<Pair<GuiCreator<Gui?>, GuiCreator<Container?>>> = mutableListOf()

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return guiStorage[ID].first(ID, player, world, x, y, z)
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return guiStorage[ID].second(ID, player, world, x, y, z)
    }

    fun registerGui(client: GuiCreator<Gui?>, server: GuiCreator<Container?>): Int {
        val id = guiStorage.size
        val pair = Pair(client, server)
        guiStorage.add(pair)
        assert(guiStorage[id] == pair)
        return id
    }

    fun <T> fromSupplier(s: () -> T): GuiCreator<T> = {_, _, _, _, _, _ -> s()}

}