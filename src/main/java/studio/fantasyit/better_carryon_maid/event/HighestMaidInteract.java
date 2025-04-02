package studio.fantasyit.better_carryon_maid.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import studio.fantasyit.better_carryon_maid.BetterCarryonMaid;
import tschipp.carryon.Constants;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BetterCarryonMaid.MODID)
public class HighestMaidInteract {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventInteract(InteractMaidEvent event) {
        if (canCarryGeneral(event.getPlayer(), event.getMaid().getPosition(0))) {
            event.setCanceled(true);
        }
    }

    /**
     * @see <a href="https://github.com/Tschipp/CarryOn/blob/41af4218d3d1ad86f9e9e1d3491529e205cbfcb3/Common/src/main/java/tschipp/carryon/common/carry/PickupHandler.java#L58">CarryOn branch 1.20</a> ,Licensed under LGPL-v3
     * @since Modified, return true if carrying, and supports using in client.
     * @author 小鱼飘飘
     */
    public static boolean canCarryGeneral(Player player, Vec3 pos) {
        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        if (carry.isCarrying()) {
            return true;
        }
        if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
            if (player.position().distanceTo(pos) > Constants.COMMON_CONFIG.settings.maxDistance) {
                return false;
            } else {
                if (!carry.isKeyPressed()) {
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
