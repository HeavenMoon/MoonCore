package fr.heavenmoon.core.common.utils.math;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class MathUtils {

    public static Random random = new Random();

    public static double averageDouble(final List<Double> list) {
        Double add = 0.0;
        for (final Double listlist : list) {
            add += listlist;
        }
        return add / list.size();
    }

    public static boolean isPair(int i) {
        return i % 2 == 0;
    }

    public static boolean isEntier(double d) {
        int i = 0;
        i = (int) d;
        return i == d;
    }

    public static boolean isPositif(double d) {
        return d > 0.0D;
    }

    public static double round(double number, double numberAfterVirgule) {
        return (int) (number * Math.pow(10.0D, numberAfterVirgule) + 0.5D)
                / Math.pow(10.0D, numberAfterVirgule);
    }

    public static double doubleRandomInclusive(double max, double min) {
        if (max < min) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        double r = Math.random();
        if (r < 0.5D) {
            return (1.0D - Math.random()) * (max - min) + min;
        }
        return Math.random() * (max - min) + min;
    }

    public static final float sin(float radians) {
        return Sin.table[((int) (radians * 2607.5945F) & 0x3FFF)];
    }

    public static final float cos(float radians) {
        return Sin.table[((int) ((radians + 1.5707964F) * 2607.5945F) & 0x3FFF)];
    }

    public static final float sinDeg(float degrees) {
        return Sin.table[((int) (degrees * 45.511112F) & 0x3FFF)];
    }

    public static final float cosDeg(float degrees) {
        return Sin.table[((int) ((degrees + 90.0F) * 45.511112F) & 0x3FFF)];
    }

    public static boolean isInteger(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (Exception localException) {
        }
        return false;
    }

    public static boolean isDouble(Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        } catch (Exception localException) {
        }
        return false;
    }

    public static final int random(int range) {
        return random.nextInt(range + 1);
    }

    public static final int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    public static final boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static final boolean randomBoolean(float chance) {
        return random() < chance;
    }

    public static final float random() {
        return random.nextFloat();
    }

    public static final float random(float range) {
        return random.nextFloat() * range;
    }

    public static final float random(float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }

    public static boolean isPowerOfTwo(int value) {
        return (value != 0) && ((value & value - 1) == 0);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static short clamp(short value, short min, short max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static int floor(float x) {
        return (int) (x + 16384.0D) - 16384;
    }

    public static int floorPositive(float x) {
        return (int) x;
    }

    public static int ceil(float x) {
        return (int) (x + 16384.999999999996D) - 16384;
    }

    public static int ceilPositive(float x) {
        return (int) (x + 0.9999999D);
    }

    public static int round(float x) {
        return (int) (x + 16384.5D) - 16384;
    }

    public static int roundPositive(float x) {
        return (int) (x + 0.5F);
    }

    public static boolean isZero(float value) {
        return Math.abs(value) <= 1.0E-6F;
    }

    public static boolean isZero(float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) <= 1.0E-6F;
    }

    public static boolean isEqual(float a, float b, float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static final Vector rotateAroundAxisZ(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public static final Vector rotateVector(Vector v, double angleX,
                                            double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public static final double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public static Vector getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;

        return new Vector(x, y, z).normalize();
    }

    public static Vector getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vector(x, 0.0D, z);
    }

    public static Material getRandomMaterial(Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2.0D * 3.141592653589793D;
    }

    public static double randomDouble(double min, double max) {
        return Math.random() < 0.5D ? (1.0D - Math.random()) * (max - min)
                + min : Math.random() * (max - min) + min;
    }

    public static float randomRangeFloat(float min, float max) {
        return (float) (Math.random() < 0.5D ? (1.0D - Math.random())
                * (max - min) + min : Math.random() * (max - min) + min);
    }

    public static byte randomByte(int max) {
        return (byte) random.nextInt(max + 1);
    }

    public static int randomRangeInt(int min, int max) {
        return (int) (Math.random() < 0.5D ? (1.0D - Math.random())
                * (max - min) + min : Math.random() * (max - min) + min);
    }

    public static double offset(Entity a, Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double trim(final int degree, final double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = format + "#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d));
    }

    public static int r(final int i) {
        return MathUtils.random.nextInt(i);
    }

    public static double abs(final double a) {
        return (a <= 0.0) ? (0.0 - a) : a;
    }

    public static String ArrayToString(final String[] list) {
        String string = "";
        for (final String key : list) {
            string = string + key + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String ArrayToString(final List<String> list) {
        String string = "";
        for (final String key : list) {
            string = string + key + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String[] StringToArray(final String string, final String split) {
        return string.split(split);
    }

    public static double offset2d(final Entity a, final Entity b) {
        return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(final Location a, final Location b) {
        return offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(final Vector a, final Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }

    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }

    public static String serializeLocation(final Location location) {
        final int X = (int) location.getX();
        final int Y = (int) location.getY();
        final int Z = (int) location.getZ();
        final int P = (int) location.getPitch();
        final int Yaw = (int) location.getYaw();
        return location.getWorld().getName() + ","
                + X + "," + Y + "," + Z + "," + P + "," + Yaw;
    }

    public static Location deserializeLocation(final String string) {
        final String[] parts = string.split(",");
        final World world = Bukkit.getServer().getWorld(parts[0]);
        final Double LX = Double.parseDouble(parts[1]);
        final Double LY = Double.parseDouble(parts[2]);
        final Double LZ = Double.parseDouble(parts[3]);
        final Float P = Float.parseFloat(parts[4]);
        final Float Y = Float.parseFloat(parts[5]);
        final Location result = new Location(world, LX, LY,
                LZ);
        result.setPitch(P);
        result.setYaw(Y);
        return result;
    }

    public static long averageLong(final List<Long> list) {
        long add = 0L;
        for (final Long listlist : list) {
            add += listlist;
        }
        return add / list.size();
    }

    private static class Sin {
        static final float[] table = new float[63];

        static {
            for (int i = 0; i < 16384; i++) {
                table[i] = ((float) Math
                        .sin((i + 0.5F) / 16384.0F * 6.2831855F));
            }
            for (int i = 0; i < 360; i += 90) {
                table[((int) (i * 45.511112F) & 0x3FFF)] = ((float) Math
                        .sin(i * 0.017453292F));
            }
        }
    }

}
