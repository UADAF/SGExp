package com.gt22.sgexp.item

import com.gt22.sgexp.R
import com.gt22.sgexp.SGExp
import com.gt22.sgexp.gui.GuiHandler
import com.gt22.sgexp.gui.client.AddressPageGui
import gcewing.sg.BaseOrientation
import gcewing.sg.DHDTE
import gcewing.sg.SGBaseTE
import gcewing.sg.SGCraft
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

fun BlockPos.MutableBlockPos.madd(x: Int = 0, y: Int = 0, z: Int = 0): BlockPos.MutableBlockPos {
    setPos(getX() + x, getY() + y, getZ() + z)
    return this
}

class AddressPage : ItemBase() {
    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack?> {
        if(!world.isRemote && stack.tagCompound?.hasKey("address") != true) {
            initAddress(stack, world, player)
        }
        if(world.isRemote) Minecraft.getMinecraft().displayGuiScreen(AddressPageGui(stack))
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }

    private fun initAddress(stack: ItemStack, world: World, player: EntityPlayer) {
        val nether: World? = world.minecraftServer?.worldServerForDimension(-1)
        if (nether != null) {
            val gate = buildGate(nether)
            if (gate != null) {
                val addr = gate.getHomeAddress()
                player.addChatComponentMessage(TextComponentString(addr))
                stack.tagCompound = NBTTagCompound().apply { setString("address", addr) }
            } else {
                player.addChatComponentMessage(TextComponentString("Something wrong with gate"))
            }
        } else {
            player.addChatComponentMessage(TextComponentString("Unable to find nether"))
        }
    }

    private fun place(curBlock: BlockPos.MutableBlockPos, world: World, vararg row: IBlockState?) {
        for(b in row) {
            curBlock.madd(x = 1)
            if(b != null) {
                world.setBlockState(curBlock, b)
            } else {
                world.setBlockToAir(curBlock)
            }
        }
        curBlock.madd(x = -row.size)
    }

    @Suppress("DEPRECATION")
    private fun buildGate(world: World, basePos: BlockPos = BlockPos(0, 0, 0)): SGBaseTE? {
        touchChunk(world)
        val curBlock = BlockPos.MutableBlockPos(basePos)
        while(world.getBlockState(curBlock).block != Blocks.AIR) {
            curBlock.madd(y = 1)
        }
        R.log.info(curBlock)
        val gatePos = curBlock.madd(y = 1).toImmutable()
        curBlock.madd(x = -3) //Shift to beginning
        val ring = SGCraft.sgRingBlock.defaultState
        val chevron = SGCraft.sgRingBlock.getStateFromMeta(1)


        //From bottom to top:
        //CRCRC
        //RAAAR
        //CAAAC
        //RAAAR
        //CRBRC
        //C - chevron, R - ring, B - base, A - air
        place(curBlock, world, chevron, ring, SGCraft.sgBaseBlock.getStateFromMeta(2), ring, chevron)
        place(curBlock.move(EnumFacing.UP), world, ring, null, null, null, ring)
        place(curBlock.move(EnumFacing.UP), world, chevron, null, null, null, chevron)
        place(curBlock.move(EnumFacing.UP), world, ring, null, null, null, ring)
        place(curBlock.move(EnumFacing.UP), world, chevron, ring, chevron, ring, chevron)
        buildPlatform(world, gatePos)
        return tuneGate(world, gatePos)
    }

    private fun touchChunk(world: World) = world.chunkProvider.provideChunk(0, 0)


    private fun buildPlatform(world: World, gatePos: BlockPos) {

        val brick = Blocks.STONEBRICK.defaultState
        val stairs = Blocks.STONE_BRICK_STAIRS.defaultState.withRotation(Rotation.CLOCKWISE_180)

        @Suppress("UNCHECKED_CAST")
        val dhd = SGCraft.sgControllerBlock.defaultState.withProperty<EnumFacing, EnumFacing>(BaseOrientation.Orient4WaysByState.FACING as IProperty<EnumFacing>, EnumFacing.SOUTH)
        val curBlock = BlockPos.MutableBlockPos(gatePos)
        curBlock.madd(x = -3, y = -1, z = 3)
        for(i in 1..8) place(curBlock.move(EnumFacing.NORTH), world, brick, brick, brick, brick, brick)
        place(curBlock.move(EnumFacing.NORTH), world, stairs, stairs, stairs, stairs, stairs)
        curBlock.madd(y = 1, z = 9)
        for(i in 1..2) place(curBlock.move(EnumFacing.NORTH), world, brick, brick, brick, brick, brick)
        curBlock.move(EnumFacing.NORTH) //Skip gate
        for(i in 1..2) place(curBlock.move(EnumFacing.NORTH), world, brick, brick, brick, brick, brick)
        place(curBlock.move(EnumFacing.NORTH), world, stairs, stairs, stairs, stairs, stairs)
        place(curBlock.move(EnumFacing.NORTH), world, null, null, null, null, null)
        place(curBlock.move(EnumFacing.NORTH), world, null, null, dhd, null, null)
        place(curBlock.move(EnumFacing.NORTH), world, null, null, null, null, null)
        curBlock.madd(y = 1, z = 9)
        for(i in 1..4) {
            print(curBlock.subtract(gatePos))
            for(j in 1..2) place(curBlock.move(EnumFacing.NORTH), world, null, null, null, null, null)
            curBlock.move(EnumFacing.NORTH) //Skip gate
            for(j in 1..2) place(curBlock.move(EnumFacing.NORTH), world, null, null, null, null, null)
            curBlock.madd(y = 1, z = 9)
        }
    }

    private fun tuneGate(world: World, gatePos: BlockPos): SGBaseTE? {
        val gate = world.getTileEntity(gatePos)
        if(gate == null || gate !is SGBaseTE) {
            R.log.warn("Unable to find gate that was just placed at $gatePos")
        } else {
            gate.update() //Ensure gate gets address
            gate.applyChevronUpgrade(ItemStack(SGCraft.sgChevronUpgrade), null)
            for(i in 0 until gate.sizeInventory) {
                gate.setInventorySlotContents(i, ItemStack(Blocks.STONEBRICK))
            }
        }
        val dhdPos = gatePos.north(5)
        val dhd = world.getTileEntity(dhdPos)
        if(dhd == null || dhd !is DHDTE) {
            R.log.warn("Unable to find DHD that was just placed at $dhdPos")
        } else {
            dhd.setInventorySlotContents(0, ItemStack(SGCraft.naquadah))
            dhd.drawEnergy(1.0) //Draw some energy, so naquadah can't be taken out
        }
        return gate as? SGBaseTE
    }

}