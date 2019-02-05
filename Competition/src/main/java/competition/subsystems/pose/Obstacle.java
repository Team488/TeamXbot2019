package competition.subsystems.pose;

import java.awt.geom.Rectangle2D;

import com.google.common.cache.Weigher;

import xbot.common.math.XYPair;

public class Obstacle extends Rectangle2D.Double {

    private static final long serialVersionUID = 1L;

    XYPair topLeft;
    XYPair topRight;
    XYPair bottomLeft;
    XYPair bottomRight;

    public boolean topLeftAvailable = true;
    public boolean topRightAvailable = true;
    public boolean bottomLeftAvailable = true;
    public boolean bottomRightAvailable = true;

    public boolean defaultTopLeft = true;
    public boolean defaultTopRight = true;
    public boolean defaultBottomLeft = true;
    public boolean defaultBottomRight = true;

    public String name;

    
    public Obstacle(double x, double y, double width, double height, String name) {
        super(x-width/2, y-height/2, width, height);
        this.name = name;
        // Put the points sliiiiightly outside the bounding box, so we don't collide with the true corners later.
        topLeft = new XYPair(x-width/2*1.01, y+height/2*1.01);
        topRight = new XYPair(x+width/2*1.01, y+height/2*1.01);
        bottomLeft = new XYPair(x-width/2*1.01, y-height/2*1.01);
        bottomRight = new XYPair(x+width/2*1.01, y-height/2*1.01);

        resetCorners();
    }

    public void resetCorners() {
        topLeftAvailable = defaultTopLeft;
        topRightAvailable = defaultTopRight;
        bottomLeftAvailable = defaultBottomLeft;
        bottomRightAvailable = defaultBottomRight;
    }

    public double getDistanceToCenter(XYPair other) {
        return new XYPair(this.getCenterX(), this.getCenterY()).getDistanceToPoint(other);
    }

    public XYPair getClosestCornerToPoint(XYPair other) {
        XYPair candidate = new XYPair();
        double minimumDistance = 100000;
        
        if (topLeftAvailable) {
            double distance = topLeft.getDistanceToPoint(other);
            if (distance < minimumDistance) {
                candidate = topLeft;
                minimumDistance = distance;
            }
        }

        if (topRightAvailable) {
            double distance = topRight.getDistanceToPoint(other);
            if (distance < minimumDistance) {
                candidate = topRight;
                minimumDistance = distance;
            }
        }

        if (bottomLeftAvailable) {
            double distance = bottomLeft.getDistanceToPoint(other);
            if (distance < minimumDistance) {
                candidate = bottomLeft;
                minimumDistance = distance;
            }
        }

        if (bottomRightAvailable) {
            double distance = bottomRight.getDistanceToPoint(other);
            if (distance < minimumDistance) {
                candidate = bottomRight;
                minimumDistance = distance;
            }
        }

        if (candidate == topLeft) {
            topLeftAvailable = false;
        }

        if (candidate == topRight) {
            topRightAvailable = false;
        }

        if (candidate == bottomLeft) {
            bottomLeftAvailable = false;
        }

        if (candidate == bottomRight) {
            bottomRightAvailable = false;
        }

        return candidate;
    }

}