package org.cardboardpowered.impl.inventory;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.cardboardpowered.impl.inventory.CardboardBrewerInventory;
import org.cardboardpowered.impl.inventory.CardboardFurnaceInventory;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockInventoryConverter implements InventoryCreator.InventoryConverter {

    public abstract Inventory getTileEntity();

    @Override
    public CraftInventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(getTileEntity());
    }

    @Override
    public CraftInventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        Inventory inventory = getTileEntity();
        if (inventory instanceof LootableContainerBlockEntity)
            ((LootableContainerBlockEntity) inventory).setCustomName(CraftChatMessage.fromStringOrNull(title));
        return getInventory(inventory);
    }

    public CraftInventory getInventory(Inventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends BlockInventoryConverter {

        @Override
        public Inventory getTileEntity() {
            AbstractFurnaceBlockEntity furnace = new FurnaceBlockEntity();
            furnace.setLocation(CraftServer.server.getWorld(World.OVERWORLD), BlockPos.ORIGIN);
            return furnace;
        }

        @Override
        public CraftInventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            Inventory tileEntity = getTileEntity();
            ((AbstractFurnaceBlockEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public CraftInventory getInventory(Inventory tileEntity) {
            return new CardboardFurnaceInventory((AbstractFurnaceBlockEntity) tileEntity);
        }
    }

    public static class BrewingStand extends BlockInventoryConverter {

        @Override
        public Inventory getTileEntity() {
            return new BrewingStandBlockEntity();
        }

        @Override
        public CraftInventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            Inventory tileEntity = getTileEntity();
            if (tileEntity instanceof BrewingStandBlockEntity)
                ((BrewingStandBlockEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public CraftInventory getInventory(Inventory tileEntity) {
            return new CardboardBrewerInventory(tileEntity);
        }
    }

    public static class Dispenser extends BlockInventoryConverter {
        public Inventory getTileEntity() { return new DispenserBlockEntity(); }
    }

    public static class Dropper extends BlockInventoryConverter {
        @Override
        public Inventory getTileEntity() { return new DropperBlockEntity(); }
    }

    public static class Hopper extends BlockInventoryConverter {
        public Inventory getTileEntity() { return new HopperBlockEntity(); }
    }

    public static class BlastFurnace extends BlockInventoryConverter {
        public Inventory getTileEntity() { return new BlastFurnaceBlockEntity(); }
    }

    public static class Lectern extends BlockInventoryConverter {
        public Inventory getTileEntity() { return new LecternBlockEntity().inventory; }
    }

    public static class Smoker extends BlockInventoryConverter {
        public Inventory getTileEntity() { return new SmokerBlockEntity(); }
    }

}