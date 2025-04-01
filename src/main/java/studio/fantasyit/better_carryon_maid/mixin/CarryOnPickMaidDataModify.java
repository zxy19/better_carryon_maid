package studio.fantasyit.better_carryon_maid.mixin;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.PickupHandler;

import java.util.function.Function;

@Mixin(PickupHandler.class)
public class CarryOnPickMaidDataModify {
    @ModifyVariable(method = "tryPickupEntity", at = @At(value = "INVOKE", target = "tschipp.carryon.common.carry.CarryOnData.setEntity(Lnet/minecraft/world/entity/Entity;)V", shift = At.Shift.BEFORE), name = "entity", remap = false)
    private static Entity modifyData(Entity entity, @Local(argsOnly = true) ServerPlayer player) {
        if (entity instanceof EntityMaid maid) {
            if (maid.startRiding(player)) {
                if (maid.isHomeModeEnable()) {
                    maid.setHomeModeEnable(false);
                }
                InitTrigger.MAID_EVENT.trigger(player, TriggerType.PICKUP_MAID);
                player.connection.send(new ClientboundSetPassengersPacket(player));
                return new EntityMaid(entity.level());
            } else {
                return null;
            }
        }
        return entity;
    }

    @Redirect(method = "tryPickupEntity", at = @At(value = "INVOKE", target = "Ltschipp/carryon/common/carry/CarryOnData;setEntity(Lnet/minecraft/world/entity/Entity;)V"), remap = false)
    private static void noHandleNullEntity(CarryOnData t, Entity entity) {
        if (entity == null) {
            return;
        }
        t.setEntity(entity);
    }

    @Inject(method = "tryPickupEntity", at = @At(value = "INVOKE", target = "net.minecraft.world.entity.Entity.remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V", shift = At.Shift.BEFORE), cancellable = true)
    private static void noContinueForNull(ServerPlayer player, Entity entity, Function<Entity, Boolean> pickupCallback, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) {
            cir.setReturnValue(false);
        }
    }
}
