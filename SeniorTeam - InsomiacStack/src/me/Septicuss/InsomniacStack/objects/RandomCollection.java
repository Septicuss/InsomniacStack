package me.Septicuss.InsomniacStack.objects;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {

	private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
	private final Random random;
	private boolean fullPercentage = false;
	private double total = 0;

	public RandomCollection() {
		this(new Random());
	}

	public RandomCollection(Random random) {
		this.random = random;
	}

	public RandomCollection<E> add(double weight, E result) {

		if (weight <= 0) {
			return this;
		}

		total += weight;
		map.put(total, result);
		return this;

	}

	public void setFullPercentage(boolean fullPercentage) {
		this.fullPercentage = fullPercentage;
	}

	public E next() {

		double multiplier = (fullPercentage ? 100 : total);
		double value = random.nextDouble() * multiplier;

		if (fullPercentage && value >= total) {
			return null;
		}

		return map.higherEntry(value).getValue();

	}

}