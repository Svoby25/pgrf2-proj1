package utils;

import solid.Vectorizable;

public class Lerp<T extends Vectorizable<T>> {

    public T lerp(T start, T end, double factor) {
        return start.mul(1 - factor).add(end.mul(factor));
    }

    public double t(double minValue, double maxValue, double targetValue) {
        if (minValue > maxValue) {
            double temp = maxValue;
            maxValue = minValue;
            minValue = temp;
        }

        if (targetValue < minValue) {
            return 0.0;
        } else if (targetValue > maxValue) {
            return 1.0;
        } else {
            return (targetValue - minValue) / (maxValue - minValue);
        }
    }
}
