/**
 * Represents some generic methods to handle most of the complicated
 * math for you.
 * 
 * You shouldn't modify this file.
 * 
 * @author Braedon Wooding
 */
public final class MathsHelper {
    public static final double RADIUS_OF_JUPITER = 69_911;
    public static final int CLOCKWISE = -1;
    public static final int ANTI_CLOCKWISE = 1;

    /**
     * Determine the distance between a satellite and another satellite.
     */
    public static double getDistance(double satelliteHeight, Angle satelliteAngle, double otherHeight,
            Angle otherAngle) {
        // convert to euclidean
        double satX = Math.cos(satelliteAngle.toRadians()) * satelliteHeight,
                satY = Math.sin(satelliteAngle.toRadians()) * satelliteHeight;
        double otherX = Math.cos(otherAngle.toRadians()) * otherHeight,
                otherY = Math.sin(otherAngle.toRadians()) * otherHeight;

        // find length of line between euclidean points
        double length = Math.sqrt((satX - otherX) * (satX - otherX) + (satY - otherY) * (satY - otherY));
        return length;
    }

    /**
     * Determine the distance between a satellite and a device.
     */
    public static double getDistance(double satelliteHeight, Angle satelliteAngle, Angle deviceAngle) {
        return getDistance(satelliteHeight, satelliteAngle, RADIUS_OF_JUPITER, deviceAngle);
    }

    /**
     * Determine if a satellite is visible to a device.
     */
    public static boolean isVisible(double satelliteHeight, Angle satelliteAngle, Angle deviceAngle) {
        return isVisible(satelliteHeight, satelliteAngle, RADIUS_OF_JUPITER + 50, deviceAngle);
    }

    /**
     * Determine if a satellite is visible to another satellite.
     */
    public static boolean isVisible(double satelliteHeight, Angle satelliteAngle, double otherHeight,
            Angle otherAngle) {
        // convert to euclidean
        double satX = Math.cos(satelliteAngle.toRadians()) * satelliteHeight,
                satY = Math.sin(satelliteAngle.toRadians()) * satelliteHeight;
        double otherX = Math.cos(otherAngle.toRadians()) * otherHeight,
                otherY = Math.sin(otherAngle.toRadians()) * otherHeight;

        // renaming variables to match equations
        double ax = satX, ay = satY;
        double bx = otherX, by = otherY;

        // t^2 component == (bx - ax)^2 + (by - ay)^2
        double a = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);

        // t component == 2[ax(bx - ax) + ay(by - ay)]
        double b = 2 * (ax * (bx - ax) + ay * (by - ay));

        // t^0 component == ax^2 + ay^2 - RADIUS_OF_JUPITER^2
        double c = ax * ax + ay * ay - RADIUS_OF_JUPITER * RADIUS_OF_JUPITER;

        // det = b^2 - 4ac
        double det = b * b - 4 * a * c;

        // non-real t
        if (det <= 0)
            return true;

        // calculate 2 possible t's
        double sqrtDet = Math.sqrt(det);

        // (-b + sqrtDet)/2a
        double tPos = (-b + sqrtDet) / (2 * a);
        double tNeg = (-b - sqrtDet) / (2 * a);

        // in our specific case we are going to only allow t \in [0, 1]
        // because we are okay with it being the tangent, just not going *through*
        return !((0 <= tPos && tPos <= 1) || (0 <= tNeg && tNeg <= 1));
    }
}