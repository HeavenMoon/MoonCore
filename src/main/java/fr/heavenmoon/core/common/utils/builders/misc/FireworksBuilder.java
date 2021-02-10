package fr.heavenmoon.core.common.utils.builders.misc;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Fallancy
 * @project Fallancy (Rapini)
 * @date 19.01.2019
 */
public class FireworksBuilder {

    private Location location;
    private Color color;
    private FireworkEffect.Type type;
    private boolean kill;
    private Integer power;
    private List<FireworkEffect> effects;

    public FireworksBuilder(Location loc) {
        this.location = null;
        this.color = null;
        this.type = null;
        this.kill = false;
        this.power = 1;
        this.effects = null;
        this.setLoc(loc);
    }

    public Location getLoc() {
        return this.location;
    }

    public FireworksBuilder setLoc(Location loc) {
        this.location = loc;
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public FireworksBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public FireworkEffect.Type getType() {
        return this.type;
    }

    public FireworksBuilder setType(FireworkEffect.Type type) {
        this.type = type;
        return this;
    }

    public boolean isKill() {
        return this.kill;
    }

    public FireworksBuilder setKill(boolean kill) {
        this.kill = kill;
        return this;
    }

    public Integer getPower() {
        return this.power;
    }

    public FireworksBuilder setPower(int power) {
        this.power = power;
        return this;
    }

    public List<FireworkEffect> getEffects() {
        return this.effects;
    }

    public FireworksBuilder addEffect(FireworkEffect effect) {
        this.effects.add(effect);
        return this;
    }

    public FireworksBuilder setEffect(FireworkEffect... effects) {
        this.effects = Arrays.asList(effects);
        return this;
    }

    public Firework build() {
        Firework firework = (Firework) this.getLoc().getWorld().spawnEntity(this.getLoc(), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        Random r = new Random();
        if (this.effects == null) {
            FireworkEffect effect = FireworkEffect.builder()
                    .flicker(r.nextBoolean()).withColor(this.getColor())
                    .withFade(this.getColor()).with(this.getType())
                    .trail(r.nextBoolean()).build();
            this.addEffect(effect);
            firework.setFireworkMeta(meta);
        }
        if (this.effects != null || this.power != null) {
            if (this.effects != null) meta.addEffects(this.getEffects());
            if (this.power != null) meta.setPower(this.getPower());
        }
        firework.setFireworkMeta(meta);
        return firework;
    }

    public static class RandomBuilder {
        private static Color getColor(int i) {
            Color c = null;
            if (i == 1) c = Color.AQUA;
            if (i == 2) c = Color.BLACK;
            if (i == 3) c = Color.BLUE;
            if (i == 4) c = Color.FUCHSIA;
            if (i == 5) c = Color.GRAY;
            if (i == 6) c = Color.GREEN;
            if (i == 7) c = Color.LIME;
            if (i == 8) c = Color.MAROON;
            if (i == 9) c = Color.NAVY;
            if (i == 10) c = Color.OLIVE;
            if (i == 11) c = Color.ORANGE;
            if (i == 12) c = Color.PURPLE;
            if (i == 13) c = Color.RED;
            if (i == 14) c = Color.SILVER;
            if (i == 15) c = Color.TEAL;
            if (i == 16) c = Color.WHITE;
            if (i == 17) c = Color.YELLOW;
            return c;
        }

        public static void Firework(Entity e) {
            World w = e.getWorld();
            Firework fw = (Firework) w.spawnEntity(e.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            Random r = new Random();
            int rt = r.nextInt(5) + 1;

            FireworkEffect.Type type = FireworkEffect.Type.BALL;
            if (rt == 1) type = FireworkEffect.Type.BALL;
            if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
            if (rt == 3) type = FireworkEffect.Type.BURST;
            if (rt == 4) type = FireworkEffect.Type.CREEPER;
            if (rt == 5) type = FireworkEffect.Type.STAR;
            int r1i = r.nextInt(17) + 1;
            int r2i = r.nextInt(17) + 1;
            Color c1 = getColor(r1i);
            Color c2 = getColor(r2i);
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
            fwm.addEffect(effect);
            int rp = r.nextInt(2) + 1;
            fwm.setPower(rp);
            fw.setFireworkMeta(fwm);
        }
    }
}
