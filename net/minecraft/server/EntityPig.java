package net.minecraft.server;

public class EntityPig extends EntityAnimal {

    public EntityPig(World world) {
        super(world);
        this.texture = "/mob/pig.png";
        this.a(0.9F, 0.9F);
        this.getNavigation().a(true);
        float f = 0.25F;

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, f));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 0.25F, Item.WHEAT.id, false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 0.28F));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, f));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    public boolean aV() {
        return true;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.hasSaddle());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSaddle(nbttagcompound.getBoolean("Saddle"));
    }

    protected String aQ() {
        return "mob.pig";
    }

    protected String aR() {
        return "mob.pig";
    }

    protected String aS() {
        return "mob.pigdeath";
    }

    public boolean c(EntityHuman entityhuman) {
        if (super.c(entityhuman)) {
            return true;
        } else if (this.hasSaddle() && !this.world.isStatic && (this.passenger == null || this.passenger == entityhuman)) {
            entityhuman.mount(this);
            return true;
        } else {
            return false;
        }
    }

    protected int getLootId() {
        return this.isBurning() ? Item.GRILLED_PORK.id : Item.PORK.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);

        for (int k = 0; k < j; ++k) {
            if (this.isBurning()) {
                this.b(Item.GRILLED_PORK.id, 1);
            } else {
                this.b(Item.PORK.id, 1);
            }
        }
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddle(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
    }

    public void a(EntityLightning entitylightning) {
        if (!this.world.isStatic) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);

            entitypigzombie.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.world.addEntity(entitypigzombie);
            this.die();
        }
    }

    protected void a(float f) {
        super.a(f);
        if (f > 5.0F && this.passenger instanceof EntityHuman) {
            ((EntityHuman) this.passenger).a((Statistic) AchievementList.u);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityPig(this.world);
    }
}
