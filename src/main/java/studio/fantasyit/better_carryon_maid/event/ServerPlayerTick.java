package studio.fantasyit.better_carryon_maid.event;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import studio.fantasyit.better_carryon_maid.BetterCarryonMaid;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BetterCarryonMaid.MODID)
public class ServerPlayerTick {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT)
            return;
        if (event.phase == TickEvent.Phase.END) {
            CarryOnData data = CarryOnDataManager.getCarryData(event.player);
            if (data.isCarrying()) {
                Entity carried = data.getEntity(event.player.level());
                Entity passenger = event.player.getFirstPassenger();
                if (carried instanceof EntityMaid && !(passenger instanceof EntityMaid)) {
                    data.clear();
                    CarryOnDataManager.setCarryData(event.player, data);
                }
            }
        }
    }
}
