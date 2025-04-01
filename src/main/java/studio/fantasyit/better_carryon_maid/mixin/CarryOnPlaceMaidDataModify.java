package studio.fantasyit.better_carryon_maid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import tschipp.carryon.common.carry.PlacementHandler;

@Mixin(PlacementHandler.class)
public class CarryOnPlaceMaidDataModify {
    @ModifyVariable(method = {"tryPlaceEntity", "tryStackEntity"}, at = @At(value = "INVOKE_ASSIGN", target = "Ltschipp/carryon/common/carry/CarryOnData;getEntity(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;", shift = At.Shift.AFTER), remap = false, name = "entity")
    private static Entity modifyData(Entity entity, @Local(argsOnly = true) ServerPlayer player) {
        if (entity instanceof EntityMaid) {
            Entity firstPassenger = player.getFirstPassenger();
            player.ejectPassengers();
            player.connection.send(new ClientboundSetPassengersPacket(player));
            if (firstPassenger != null)
                return firstPassenger;
            return new AreaEffectCloud(player.level(), 0.0, 0.0, 0.0);
        }
        return entity;
    }

    @Redirect(method = {"tryPlaceEntity", "tryStackEntity"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private static boolean noAddingFreshEntity(Level level, Entity maid) {
        if (maid instanceof EntityMaid) {
            return true;
        }
        return level.addFreshEntity(maid);
    }
}
