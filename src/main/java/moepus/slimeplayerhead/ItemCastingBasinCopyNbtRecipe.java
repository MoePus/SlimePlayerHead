package moepus.slimeplayerhead;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;

public class ItemCastingBasinCopyNbtRecipe extends ItemCastingRecipe {

    public ItemCastingBasinCopyNbtRecipe(ResourceLocation id, String group, Ingredient cast, FluidIngredient fluid,
            ItemOutput result, int coolingTime, boolean consumed, boolean switchSlots) {
        super(TinkerRecipeTypes.CASTING_BASIN.get(), id, group, cast, fluid, result, coolingTime, consumed, switchSlots);
        slimeplayerhead.LOGGER
                .info(String.format("ItemCastingBasinCopyNbtRecipe invoked: %s, %s, %s", cast.getItems()[0].toString(),
                        fluid.toString(), result.get().getItem().toString()));
    }

    @Override
    public ItemStack assemble(ICastingContainer inv) {
        ItemStack result = getResultItem().copy();
        if (inv.getStack().getItem() instanceof PlayerHeadItem) {
            result.setTag(inv.getStack().getTag());
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return slimeplayerhead.basinCopyNBTRecipeSerializer.get();
    }
}
