package moepus.slimeplayerhead;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolStatProviders;
import slimeknights.tconstruct.library.tools.item.ModifiableArmorItem;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.ArmorSlotType;
import net.minecraftforge.client.IItemRenderProperties;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import moepus.slimeplayerhead.slimeplayerhead.WorldClientEvents;

public class SlimePlayerHeadHelmet extends ModifiableArmorItem {
    public static final ModifiableArmorMaterial armor_mat = ModifiableArmorMaterial
            .builder(new ResourceLocation("slimeph", "slime_player_head"), ArmorSlotType.HELMET)
            .setStatsProvider(ToolStatProviders.NO_PARTS)
            .setSoundEvent(Sounds.EQUIP_TRAVELERS.getSound())
            .build();
    public static final Properties prop = new Item.Properties().tab(TinkerTools.TAB_TOOLS).rarity(Rarity.EPIC);

    public SlimePlayerHeadHelmet() {
        super(armor_mat, ArmorSlotType.HELMET, prop);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Nonnull
            public Model getBaseArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot,
                    HumanoidModel<?> _default) {
                return DummyModel.instance;
            }

            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return WorldClientEvents.blockEntityRenderer;
            }
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "textures/entity/steve.png";
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag pCompoundTag) {
        super.verifyTagAfterLoad(pCompoundTag);
        if (pCompoundTag.contains("SkullOwner", 8) && !StringUtils.isBlank(pCompoundTag.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID) null, pCompoundTag.getString("SkullOwner"));
            SkullBlockEntity.updateGameprofile(gameprofile, (p_151177_) -> {
                pCompoundTag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_151177_));
            });
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag compoundtag = stack.getTag();
            if (compoundtag.contains("SkullOwner"))
            {
                return Rarity.EPIC;
            }
        }
        return Rarity.RARE;
    }
}