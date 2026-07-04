# Langton Ant Cellular Automaton – Parallel Implementation Comparison (Java + JavaFX)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-orange)
![Concurrency](https://img.shields.io/badge/Concurrency-Executor%20%7C%20ForkJoin%20%7C%20Sequential-purple)
![Status](https://img.shields.io/badge/Status-Academic%20Project-success)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

# Abstract

This project implements a parallelized simulation of Langton’s Ant cellular automaton in Java using JavaFX. It compares multiple execution strategies including sequential, ExecutorService-based parallelism, and ForkJoinPool-based parallelism. The system is designed to benchmark performance differences across execution models while visualizing the simulation in real time.

The project focuses on:
- Parallel computation strategies in Java
- Concurrency performance benchmarking
- Real-time JavaFX rendering
- Comparative analysis of execution models

# Overview

Langton’s Ant is a two-dimensional Turing machine that exhibits emergent behaviour from simple rules. This project extends the classic simulation by introducing multiple execution strategies:

- Sequential execution (baseline)
- ExecutorService (thread pool with batching and non-batching modes)
- ForkJoinPool (divide-and-conquer parallelism)

A benchmarking system records:

- Total execution time
- Time per step (efficiency)
- Speedup vs baseline
- Throughput (steps per second)

A JavaFX interface provides:

- Real-time simulation rendering
- Mode switching
 -Performance monitoring
 -Automated benchmarking
- CSV export of results

# Author

- James Harris

# Usage

Controls
- Start → Run simulation
- Stop → Pause and record results
- Reset → Reset grid and ants
- Mode Toggle → Switch between:
  - SEQUENTIAL
  - FORKJOIN
  - EXECUTOR_FAST
  - EXECUTOR_SLOW
- Automated Evaluation → Runs full benchmark suite
- Export Results → Saves benchmark data to CSV

# Installation
Prerequisites
- Java 17+
- JavaFX SDK
- Maven

## Clone the Repository
```
git clone https://github.com/your-username/Langton-Ant-Cellular-Automaton-Parallel-Implementation-Comparison-Java-JavaFX.git
cd Langton-Ant-Cellular-Automaton-Parallel-Implementation-Comparison-Java-JavaFX
```

## Run the Project:
```
mvn clean javafx:run
```

If using an IDE such as Intellij:
- Import as Maven Project
- Run Launcher.java class

# Features:
- Multi-strategy simulation engine
- Parallel performance benchmarking
- Thread pool + ForkJoin implementations
- Real-time JavaFX rendering
- Exportable CSV benchmark results
- Configurable grid size, ant count, and step count

# Architecture:
Core components:

SimulationManager → Core simulation state + execution
SimulationStrategy → Strategy pattern for execution models
BenchmarkManager → Stores and exports results
Main_Controller → JavaFX UI controller
Ant → Cellular automaton agent logic

# Performance Metrics:
The system measures:

- Execution time (ms)
- Efficiency (ms per step)
- Speedup (relative to sequential baseline)
- Throughput (steps/sec)

# Known Limitations
- Parallel execution may introduce nondeterministic ordering
- Benchmark accuracy affected by JVM warm-up
- ForkJoin overhead may dominate for small grids
- ExecutorService strategy recreates tasks per step (performance cost)

# Future Improvements
- Persistent worker threads (reduce task overhead)
- GPU-based simulation rendering
- Deterministic parallel execution mode
- Adaptive chunk sizing for workload balancing
- Headless benchmarking mode (no JavaFX overhead)

# License

This project is licensed under the MIT Licence. See the LICENCE file for details.


# Feedback & Contributing

Contributions are welcome. Suggested areas:

Performance improvements
New parallel strategies
Visualization enhancements
Benchmark accuracy improvements
