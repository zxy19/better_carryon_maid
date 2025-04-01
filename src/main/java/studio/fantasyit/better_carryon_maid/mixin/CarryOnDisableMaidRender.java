package studio.fantasyit.better_carryon_maid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tschipp.carryon.client.render.CarriedObjectRender;
import tschipp.carryon.client.render.CarryRenderHelper;

@Mixin(CarriedObjectRender.class)
public class CarryOnDisableMaidRender {
    @ModifyVariable(method = "drawFirstPersonEntity", at = @At(value = "INVOKE_ASSIGN", target = "tschipp.carryon.client.render.CarryRenderHelper.getRenderEntity(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/Entity;"), remap = false, name = "entity")
    private static Entity renderEntity(Entity entity) {
        if (entity instanceof EntityMaid)
            return null;

        return entity;
    }

    @ModifyVariable(method = "drawThirdPerson", at = @At(value = "INVOKE_ASSIGN", target = "tschipp.carryon.client.render.CarryRenderHelper.getRenderEntity(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/Entity;"), remap = false, name = "entity")
    private static Entity renderEntityTp(Entity entity) {
        if (entity instanceof EntityMaid)
            return null;

        return entity;
    }
}
