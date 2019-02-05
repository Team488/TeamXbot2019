package competition.subsystems.pose;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.google.common.cache.Weigher;

import xbot.common.math.XYPair;

public class Obstacle extends Rectangle2D.Double {

    private static final long serialVersionUID = 1L;

    public XYPair topLeft;
    public XYPair topRight;
    public XYPair bottomLeft;
    public XYPair bottomRight;

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

    public XYPair getIntersectionAveragePoint(XYPair start, XYPair end) {
        // test each of the four lines to get any intersection points, then average those points together.
        XYPair topLine = getLineIntersectionPoint(start, end, topLeft, topRight);
        XYPair bottomLine = getLineIntersectionPoint(start, end, bottomLeft, bottomRight);
        XYPair leftLine = getLineIntersectionPoint(start, end, topLeft, bottomLeft);
        XYPair rightLine = getLineIntersectionPoint(start, end, topRight, bottomRight);

        XYPair combinedPoint = topLine.clone().add(bottomLine).add(leftLine).add(rightLine);
        if (combinedPoint.getMagnitude() < 0.01) {
            // No intersection points.
            return null;
        } else {
            return combinedPoint.scale(0.5);
        }
    }

    public XYPair getLineIntersectionPoint(XYPair lineA1, XYPair lineA2, XYPair lineB1, XYPair lineB2) {
        XYPair candidate = new XYPair();

        // quick check to see if the line segments cross
        Line2D.Double l1 = new Line2D.Double(lineA1.x, lineA1.y, lineA2.x, lineA2.y);
        Line2D.Double l2 = new Line2D.Double(lineB1.x, lineB1.y, lineB2.x, lineB2.y);

        if (!l1.intersectsLine(l2)) {
            return candidate;
        }

        double x1 = lineA1.x;
        double y1 = lineA1.y;
        double x2 = lineA2.x;
        double y2 = lineA2.y;

        double x3 = lineB1.x;
        double y3 = lineB1.y;
        double x4 = lineB2.x;
        double y4 = lineB2.y;

        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d != 0) {
            double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

            candidate = new XYPair(xi, yi);
        }
        return candidate;
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