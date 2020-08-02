package com.example.examplemod.entity.changed;

import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HardStray extends StrayEntity {
    public static final String name = "hard_stray";
    private final RangedBowAttackGoal<AbstractSkeletonEntity> aiArrow = new RangedBowAttackGoal<>(this, 1.0D, 20, 20.0F);
    private final MeleeAttackGoal aiAttack = new MeleeAttackGoal(this, 1.3D, false) {
        //Reset task when interrupted
        public void resetTask() {
            super.resetTask();
            HardStray.this.setAggroed(false);
        }

        //Start doing task
        public void startExecuting() {
            super.startExecuting();
            HardStray.this.setAggroed(true);
        }
    };

    public HardStray(EntityType<? extends StrayEntity> type, World world) {
        super(ModEntities.HARD_STRAY, world);
    }

    @Override
    public void setCombatTask() {
        if(!(this.aiArrow == null) && !(this.aiAttack == null)) {
            if (this.world != null && !this.world.isRemote) {
                this.goalSelector.removeGoal(this.aiAttack);
                this.goalSelector.removeGoal(this.aiArrow);
                ItemStack itemstack = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW));
                if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                    int i = 20;
                    if (this.world.getDifficulty() != Difficulty.HARD) {
                        i = 20;
                    }

                    this.aiArrow.setAttackCooldown(i);
                    this.goalSelector.addGoal(4, this.aiArrow);
                } else {
                    this.goalSelector.addGoal(4, this.aiAttack);
                }
            }
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.world != null && !this.world.isRemote) {
            ItemStack heldItem = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW));
            if (heldItem.getItem() == Items.BOW) {
                LivingEntity target = this.getAttackTarget();
                if (target != null) {
                    double flatDist = this.getDistanceSq(target);
                    if (flatDist <= 8.0D){
                        this.goalSelector.removeGoal(this.aiArrow);
                        this.goalSelector.addGoal(4, this.aiAttack);
                    } else {
                        this.goalSelector.removeGoal(this.aiAttack);
                        this.goalSelector.addGoal(4, this.aiArrow);
                    }
                }
            }
        }
    }
    @Nonnull
    @Override
    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.fireArrow(arrowStack, distanceFactor);
        if (abstractarrowentity instanceof ArrowEntity) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.SLOWNESS, 600,1));
        }

        return abstractarrowentity;
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(4.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.5D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0f);
    }

}
