package com.gt22.sgexp.gui.client

import com.gt22.sgexp.R
import gcewing.sg.BaseContainer
import gcewing.sg.BaseGui
import gcewing.sg.SGAddressing
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.GlStateManager.glTexParameteri
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import org.lwjgl.opengl.GL11.*

class AddressPageGui(val stack: ItemStack) : BaseGui.Screen(BaseContainer(256, 208)) {
    private val texture = R.rl("textures/gui/addressPage.png")
    private val glyphs = R.rl("textures/gui/symbols48.png")


    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f)
        val k = (this.width - xSize) / 2
        val l = (this.height - ySize) / 2
        mc.renderEngine.bindTexture(texture)
        drawTexturedModalRect(k, l, 0, 0, xSize, 166)
        GlStateManager.color(1f, 1f, 1f)
        drawAddress(k, l)
    }



    private fun drawAddress(k: Int, l: Int) {
        val address: String? = stack.tagCompound?.getString("address")
        if(address != null) {
            val formatted = SGAddressing.formatAddress(address, "-", "-")
            val addrWidth = fontRendererObj.getStringWidth(formatted)
            fontRendererObj.drawString(formatted, (k + xSize / 2 - addrWidth / 2).toFloat(), (l + 140).toFloat(), 0, false)
            drawGlyphs(k + 125, l - 10, address)
        }
    }

    private val symbolsPerRowInTexture = 10
    private val symbolWidthInTexture = 48
    private val symbolHeightInTexture = 48
    private val symbolTextureWidth = 512
    private val symbolTextureHeight = 256
    private val cellSize = 24

    private fun drawGlyphs(x: Int, y: Int, address: String) {
        val x0 = x - address.length * cellSize / 2
        val y0 = y + ySize / 2 - cellSize / 2
        //      bindSGTexture("symbol_frame.png", 256, 64);
        //      drawTexturedRect(x - frameWidth / 2, y, frameWidth, frameHeight, 0, 0);
        bindTexture(glyphs,
                symbolTextureWidth * cellSize / symbolWidthInTexture,
                symbolTextureHeight * cellSize / symbolHeightInTexture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        val n = address.length
        for (i in 0 until n) {
            val s = SGAddressing.symbolChars.indexOf(address[i])
            val row = s / symbolsPerRowInTexture
            val col = s % symbolsPerRowInTexture
            drawTexturedRect((x0 + i * cellSize).toDouble(), y0.toDouble(), cellSize.toDouble(), cellSize.toDouble(),
                    (col * cellSize).toDouble(), (row * cellSize).toDouble())
        }
    }

}