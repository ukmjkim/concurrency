package com.mjkim.concurrency.knn.data;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
@ToString
@EqualsAndHashCode
public class Distance implements Comparable<Distance> {
	private int index;
	private double distance;

	@Override
	public int compareTo(Distance other) {
		if (this.distance < other.getDistance()) {
			return -1;
		} else if (this.distance > other.getDistance()) {
			return 1;
		}
		return 0;
	}
}
