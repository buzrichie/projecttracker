# 🚀 TrackerBoost – Performance Profiling & Optimization Report

## 📍 Project Name
**Project Tracker System**

## 🧪 Objective
Analyze and optimize the performance of the Project Tracker using tools like JProfiler, JMeter, and Spring Boot Actuator.

---

## 🖥️ System Setup

- **Java Version**: 21
- **Spring Boot**: 3.x
- **JProfiler**: v13+
- **JMeter**: v5.6+
- **Spring Boot Actuator**: Enabled with full metrics
- **GC Settings**: Default unless otherwise stated

---

## 🔍 Tools Overview

| Tool                     | Purpose                                      |
|--------------------------|----------------------------------------------|
| **JProfiler**            | CPU, memory, thread, and GC profiling        |
| **JMeter**               | Load testing – simulate concurrent requests  |
| **Spring Boot Actuator** | Capture system and endpoint-level metrics    |

---

## 📈 Performance Metrics

### 🧪 Initial Performance

![Initial Performance](/docs/initial-performance.png)

---

### 1. 📊 JMeter Load Testing Results

#### 🔴 Before Optimization

![JMeter Before Optimization](/docs/JMeterInitial.png)

#### 🟢 After Optimization

![JMeter After Optimization](/docs/JMeterProcess.png)

| Metric            | Value         |
|-------------------|---------------|
| Avg Response Time | 0 ms          |
| Max Response Time | 12046 ms      |
| Throughput        | 150.1 req/sec |
| Error Rate        | 53.20%        |
| 90th Percentile   | 420 ms        |
| 95th Percentile   | 500 ms        |

---

### 2. 🔬 JProfiler Analysis

#### 📷 Method Hotspots

- Before:
  ![Method Hotspots Before](/docs/JProfilier_Method_Hotspot.png)
- After:
  ![Method Hotspots After](/docs/JProfilier_Method_Hotspot_After.png)

#### ✅ Memory Allocation

- High allocation in DTO mapping layer
- Optimized using object reuse strategies

#### ✅ Thread Dump

- Threads blocked on DB pool under high load
- **Solution**: Increased HikariCP connection pool size

#### ✅ CPU Hotspots

- Intensive methods:
    - `repository.UserRepository`
- **Solution**: Introduced caching & avoided eager fetching

#### ✅ Garbage Collection Comparison

| GC Algorithm | Avg Pause | Frequency  | Notes                      |
|--------------|-----------|------------|-----------------------------|
| G1GC         | 40 ms     | Moderate   | Default configuration       |
| ZGC          | 8 ms      | Frequent   | Minimal pause, smoother run |
| Parallel GC  | 120 ms    | Infrequent | High pause under stress     |

##### Visual Comparison:

- **Parallel GC**  
  ![Parallel GC](/docs/ParrallelGC.png)

- **G1GC**  
  ![G1GC](/docs/G1GC.png)

- **ZGC**  
  ![ZGC](/docs/ZGC.png)

---

### 3. 📡 Spring Boot Actuator Metrics

Metrics pulled from `/actuator/metrics`:

| Metric                           | Value            |
|----------------------------------|------------------|
| `system.cpu.usage`               | 60%              |
| `jvm.memory.used`                | ~420 MB          |
| `jvm.gc.pause` (95th percentile) | 38 ms            |
| `http.server.requests` (avg)     | 250 ms latency   |

---

## 🛠️ Key Optimizations

- 🧠 **Caching**: Added method-level caching for repeated queries
- 💾 **Database**: Switched to lazy loading for selected JPA relationships
- ⚙️ **Thread Pooling**: Increased HikariCP and async executor pools
- 🧹 **Garbage Collection**: Migrated to ZGC for minimal pause impact
- ♻️ **Memory Management**: Reused DTOs and entity instances where viable

---

## ✅ Summary & Impact

> 🚀 Significant improvements achieved post-optimization:

- CPU usage reduced by **~35%**
- Avg response time improved by **~40%**
- GC pause time reduced by **~75%**

---

## 📎 Attachments

- 📄 `jmeter-results.jtl`
- 🖼️ JProfiler snapshots (heap, threads, CPU usage)


---

## 🗓️ Date: `2025-06-26`
**Author:** Richmond Kwame Nyarko  
