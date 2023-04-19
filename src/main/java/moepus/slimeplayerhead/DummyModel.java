package moepus.slimeplayerhead;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DummyModel extends Model {
    static public DummyModel instance = new DummyModel();

    public DummyModel() {
        super(RenderType::entityCutoutNoCull);
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay,
            float pRed, float pGreen, float pBlue, float pAlpha) {
    }
};