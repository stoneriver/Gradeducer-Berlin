package com.github.stoneriver.gradeducer;

import java.awt.geom.Point2D;

public class Gradeducer {
	private static int numValue;
	private static Point2D.Double[] rbPoints;
	private static Point2D.Double[] ltPoints;
	private static double maxGrad;

	private static class Line {
		private double getValueAt(double x) {
			double deltaX = x - startingPoint.getX();
			double deltaY = deltaX * grad;
			double value = startingPoint.getY() + deltaY;
			return value;
		}

		private Line(Point2D.Double startingPoint, double grad) {
			this.startingPoint = startingPoint;
			this.grad = grad;
		}

		private Point2D.Double startingPoint;
		private double grad;
	}

	private static int maxRB;
	private static int maxLT;
	private static double minGrad;
	private static int minRB;
	private static int minLT;

	public Gradeducer() {
	}

	public static void main(String[] args) {

		input();
		findMaxGrad();
		findMinGrad();

		System.out.println("MaxGrad=" + maxGrad);
		System.out.println("Connecting: RB=" + maxRB + " LT=" + maxLT + "\n");
		System.out.println("MinGrad=" + minGrad);
		System.out.println("Connecting: LT=" + minLT + " RB=" + minRB);
	}

	private static void findMinGrad() {
		for (int i = 0; i < numValue - 1; i++) {
			for (int j = i + 1; j < numValue; j++) {
				double grad = findGradBetween(ltPoints[i], rbPoints[j]);
				Line line = new Line(ltPoints[i], grad);
				if (inspectLine(line)) {
					if (minGrad > grad) {
						minGrad = grad;
						minLT = i;
						minRB = j;
					}
				}
			}
		}
	}

	private static void findMaxGrad() {
		for (int i = 0; i < numValue - 1; i++) {
			for (int j = i + 1; j < numValue; j++) {
				double grad = findGradBetween(rbPoints[i], ltPoints[j]);
				Line line = new Line(rbPoints[i], grad);
				if (inspectLine(line)) {
					if (maxGrad < grad) {
						maxGrad = grad;
						maxRB = i;
						maxLT = j;
					}
				}
			}
		}
	}

	private static void input() {
		java.util.Scanner in = new java.util.Scanner(System.in);
		numValue = in.nextInt();
		rbPoints = new Point2D.Double[numValue];
		ltPoints = new Point2D.Double[numValue];
		maxGrad = Double.MIN_VALUE;
		minGrad = Double.MAX_VALUE;
		for (int i = 0; i < numValue; i++) {
			double x = in.nextDouble();
			double y = in.nextDouble();
			double uncXHigh = in.nextDouble();
			double uncXLow = in.nextDouble();
			double uncYHigh = in.nextDouble();
			double uncYLow = in.nextDouble();

			rbPoints[i] = new Point2D.Double(x + uncXHigh, y - uncYLow);
			ltPoints[i] = new Point2D.Double(x - uncXLow, y + uncYHigh);
		}
		in.close();
	}

	private static double findGradBetween(Point2D.Double p1, Point2D.Double p2) {
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();
		double grad = deltaY / deltaX;
		return grad;
	}

	private static boolean inspectLine(Line line) {
		for (int i = 0; i < numValue; i++) {
			if (!inspectLineBoxPassage(line, rbPoints[i], ltPoints[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean inspectLineBoxPassage(Line line, Point2D.Double inspectionRBPoint,
			Point2D.Double inspectionLTPoint) {
		boolean RBCheck = lineAbovePoint(line, inspectionRBPoint);
		boolean LTCheck = lineBelowPoint(line, inspectionLTPoint);
		return (RBCheck) && (LTCheck);
	}

	private static boolean lineAbovePoint(Line line, Point2D.Double point) {
		return line.getValueAt(point.getX()) >= point.getY();
	}

	private static boolean lineBelowPoint(Line line, Point2D.Double point) {
		return line.getValueAt(point.getX()) <= point.getY();
	}
}
