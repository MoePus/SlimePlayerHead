package moepus.slimeplayerhead;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.client.event.EntityRenderersEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(slimeplayerhead.MOD_ID)
public class slimeplayerhead {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "slimeph";

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MOD_ID);

    public static final RegistryObject<Item> SLIME_PLAYER_HEAD_ITEM = ITEMS.register("slime_player_head",
            () -> new SlimePlayerHeadHelmet());

    public static final RegistryObject<ItemCastingRecipe.Serializer<ItemCastingBasinCopyNbtRecipe>> basinCopyNBTRecipeSerializer = RECIPE_SERIALIZERS
            .register("casting_basin_copy_nbt",
                    () -> new ItemCastingRecipe.Serializer<>(ItemCastingBasinCopyNbtRecipe::new));

    public static final StaticModifier<GlowerModifier> glower = MODIFIERS.register("glower",
            GlowerModifier::new);

    public slimeplayerhead() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        MODIFIERS.register(eventBus);

        eventBus.register(this);
    }

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
    public class WorldClientEvents {
        static public SlimePlayerHeadWithoutLevelRenderer blockEntityRenderer;

        @SubscribeEvent
        public static void construct(EntityRenderersEvent.AddLayers event) {
            blockEntityRenderer = new SlimePlayerHeadWithoutLevelRenderer(
                    Minecraft.getInstance().getBlockEntityRenderDispatcher(), event.getEntityModels());
            addLayerToHumanoid(event, EntityType.ARMOR_STAND);

            addLayerToPlayerSkin(event, "default");
            addLayerToPlayerSkin(event, "slim");

            var dummy = EntityType.byString("dummmmmmy:target_dummy");
            if (dummy.isPresent()) {
                addLayerToHumanoid(event, (EntityType<Mob>) dummy.get());
            }
        }

        private static <E extends LivingEntity, M extends HumanoidModel<E>> void addLayerToHumanoid(
                EntityRenderersEvent.AddLayers event, EntityType<E> entityType) {
            LivingEntityRenderer<E, M> render = event.getRenderer(entityType);
            render.addLayer(new SlimePlayerHeadLayer<E, M>(render, event.getEntityModels()));
        }

        private static <E extends Player, M extends HumanoidModel<E>> void addLayerToPlayerSkin(
                EntityRenderersEvent.AddLayers event, String skinName) {
            LivingEntityRenderer<E, M> render = event.getSkin(skinName);
            render.addLayer(new SlimePlayerHeadLayer<E, M>(render, event.getEntityModels()));
        }
    }
}
