package moepus.slimeplayerhead;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.TinkerHooks;
import slimeknights.tconstruct.library.modifiers.hook.KeybindInteractModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GlowerModifier extends NoLevelsModifier implements KeybindInteractModifierHook {
  @Override
  public boolean startInteract(IToolStackView tool, ModifierEntry modifier, Player player, EquipmentSlot slot, TooltipKey keyModifier) {
    if (!player.isShiftKeyDown() && !player.hasEffect(MobEffects.GLOWING)) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
        return true;
    }
    return false;
  }

  @Override
  protected void registerHooks(Builder hookBuilder) {
    super.registerHooks(hookBuilder);
    hookBuilder.addHook(this, TinkerHooks.ARMOR_INTERACT);
  }
}

