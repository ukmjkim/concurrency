package com.mjkim.concurrency.knn.benchmark.data;

import lombok.ToString;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ToString
@EqualsAndHashCode
public class Distance implements Comparable<Distance> {
	private int index;
	private double distance;
	
	public int compareTo(Distance other) {
		if (this.distance < other.getDistance()) {
			return -1;
		} else if (this.distance > other.getDistance()) {
			return 1;
		}
		return 0;
	}
}
