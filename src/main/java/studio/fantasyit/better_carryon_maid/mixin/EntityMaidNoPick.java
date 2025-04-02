package studio.fantasyit.better_carryon_maid.mixin;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityMaid.class)
public abstract class EntityMaidNoPick extends TamableAnimal {

    protected EntityMaidNoPick(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    @Override
    public boolean isPickable() {
        if (getVehicle() instanceof Player)
            return false;
        return super.isPickable();
    }
}
