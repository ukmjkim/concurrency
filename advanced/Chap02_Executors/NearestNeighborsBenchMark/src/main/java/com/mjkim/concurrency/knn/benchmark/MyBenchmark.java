/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mjkim.concurrency.knn.benchmark;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.mjkim.concurrency.knn.benchmark.config.Task;
import com.mjkim.concurrency.knn.benchmark.data.Entity;
import com.mjkim.concurrency.knn.benchmark.loader.BankMarketingTextLoader;
import com.mjkim.concurrency.knn.benchmark.loader.ILoader;
import com.mjkim.concurrency.knn.benchmark.main.App;

@State(Scope.Benchmark)
public class MyBenchmark {
	@Param({ "10", "30" })
	public int size;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	// @Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void ParallelGroupSortMain() {
		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		Task task = new Task();
		task.setTitle("ParallelGroupSort");
		task.setThreshold(size);
		task.setParallel(true);
		task.setGroup(true);
		task.setSort(true);

		try {
			App.process(task, train, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	// @Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void ParallelGroupNonSortMain() {
		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		Task task = new Task();
		task.setTitle("ParallelGroupNonSort");
		task.setThreshold(size);
		task.setParallel(true);
		task.setGroup(true);
		task.setSort(false);

		try {
			App.process(task, train, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	// @Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void ParallelIndividualSortMain() {
		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		Task task = new Task();
		task.setTitle("ParallelIndividualSort");
		task.setThreshold(size);
		task.setParallel(true);
		task.setGroup(false);
		task.setSort(true);

		try {
			App.process(task, train, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	// @Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void ParallelIndividualNonSortMain() {
		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		Task task = new Task();
		task.setTitle("ParallelIndividualNonSort");
		task.setThreshold(size);
		task.setParallel(true);
		task.setGroup(false);
		task.setSort(false);

		try {
			App.process(task, train, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	// @Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void serialMain() {
		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		Task task = new Task();
		task.setTitle("Serial");
		task.setThreshold(size);
		task.setParallel(false);
		task.setGroup(false);
		task.setSort(false);

		try {
			App.process(task, train, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
