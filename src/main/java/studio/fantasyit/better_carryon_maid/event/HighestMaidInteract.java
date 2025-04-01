package studio.fantasyit.better_carryon_maid.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import studio.fantasyit.better_carryon_maid.BetterCarryonMaid;
import tschipp.carryon.Constants;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;
import tschipp.carryon.common.carry.PickupHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BetterCarryonMaid.MODID, value = Dist.CLIENT)
public class HighestMaidInteract {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderLevelLast(InteractMaidEvent event) {
        if (canCarryGeneral(event.getPlayer(), event.getMaid().getPosition(0))) {
            event.setCanceled(true);
        }
    }

    public static boolean canCarryGeneral(Player player, Vec3 pos) {
        if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
            if (player.position().distanceTo(pos) > Constants.COMMON_CONFIG.settings.maxDistance) {
                return false;
            } else {
                CarryOnData carry = CarryOnDataManager.getCarryData(player);
                if (carry.isCarrying()) {
                    return false;
                } else if (!carry.isKeyPressed()) {
                    return false;
                } else if (player.tickCount == carry.getTick()) {
                    return false;
                } else if (player instanceof ServerPlayer sp) {
                    return sp.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && sp.gameMode.getGameModeForPlayer() != GameType.ADVENTURE;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
