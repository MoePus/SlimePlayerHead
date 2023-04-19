package moepus.slimeplayerhead;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class SlimePlayerHeadWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public SkullModel model;

    public SlimePlayerHeadWithoutLevelRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher,
            EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        model = new SkullModel(pEntityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack,
            MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Item item = pStack.getItem();
        if (item instanceof SlimePlayerHeadHelmet) {
            GameProfile gameprofile = null;
            if (pStack.hasTag()) {
                CompoundTag compoundtag = pStack.getTag();
                if (compoundtag.contains("SkullOwner", 10)) {
                    gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
                } else if (compoundtag.contains("SkullOwner", 8)
                        && !StringUtils.isBlank(compoundtag.getString("SkullOwner"))) {
                    gameprofile = new GameProfile((UUID) null, compoundtag.getString("SkullOwner"));
                    compoundtag.remove("SkullOwner");
                    SkullBlockEntity.updateGameprofile(gameprofile, (p_172560_) -> {
                        compoundtag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_172560_));
                    });
                }
            }

            RenderType rendertype = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, gameprofile);
            SkullBlockRenderer.renderSkull((Direction) null, 180.0F, 0.0F, pPoseStack, pBuffer, pPackedLight,
                    model, rendertype);
        }
    }

}
