package moepus.slimeplayerhead;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimePlayerHeadLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends CustomHeadLayer<T, M> {
    public SkullModel model;

    public SlimePlayerHeadLayer(RenderLayerParent<T, M> owner, EntityModelSet ems) {
        super(owner, ems);
        model = new SkullModel(ems.bakeLayer(ModelLayers.PLAYER_HEAD));
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity,
            float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw,
            float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!itemstack.isEmpty()) {
            Item item = itemstack.getItem();
            this.getParentModel().getHead().translateAndRotate(pMatrixStack);
            if (item instanceof SlimePlayerHeadHelmet) {
                pMatrixStack.scale(1.1875F, -1.1875F, -1.1875F);
                if (pLivingEntity instanceof Villager || pLivingEntity instanceof ZombieVillager) {
                    pMatrixStack.translate(0.0D, 0.0625D, 0.0D);
                }

                GameProfile gameprofile = null;
                if (itemstack.hasTag()) {
                    CompoundTag compoundtag = itemstack.getTag();
                    if (compoundtag.contains("SkullOwner", 10)) {
                        gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
                    }
                }

                pMatrixStack.translate(-0.5D, 0.0D, -0.5D);
                RenderType rendertype = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, gameprofile);
                
                SkullBlockRenderer.renderSkull((Direction)null, 180.0F, pLimbSwing, pMatrixStack, pBuffer, pPackedLight, model, rendertype);
            }
        }
    }
}
