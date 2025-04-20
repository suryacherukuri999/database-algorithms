# Database Algorithms Implementation

This repository implements two core database‐management algorithms in Java:

1. **B+ Tree**  
   - Configurable order  
   - Bulk‐loaded (dense) and incremental (sparse) construction  
   - Search, range query, insert, delete  

2. **Hash‐Based Natural Join**  
   - Simulated external memory: unlimited virtual disk, 15‑block main memory, 8 tuples per block  
   - Two‑pass hash join + one‑pass optimization  
   - Disk I/O counting  

## Requirements
- Java 11 or higher  
- Maven 3.6 or higher  

## Installation

1. **Clone the repository**  
   ```
   git clone <repository-url>
   cd database-algorithms
   ```

2. **Build with Maven**
   ```
   mvn clean package
   ```
   This produces `target/database-algorithms-1.0-SNAPSHOT.jar`.

## Usage

Run the packaged JAR:
```
java -jar target/database-algorithms-1.0-SNAPSHOT.jar
```

You'll see a menu:
1. Run B+ Tree implementation tests
2. Run Hash Join implementation tests
3. Exit

## Testing

### 1. B+ Tree Tests (Option 1)
- **Data Generation**: 10,000 unique integer keys in [100,000, 200,000].
- **Tree Construction**:
  - Dense B+ tree (order 13 & 24)
  - Sparse B+ tree (order 13 & 24)
- **Operations**:
  - 2 insertions on each dense tree
  - 2 deletions on each sparse tree
  - 5 mixed insert/delete on all trees
  - 5 search + range queries on all trees
- Output shows node states before/after each change.

### 2. Hash Join Tests (Option 2)
- **Relation S(B,C)**: 5,000 tuples (B ∈ [10,000, 50,000]).
- **Scenario 5.1**: R with 1,000 tuples sampled from S.B → two‑pass join → 20 random results.
- **Scenario 5.2**: R with 1,200 tuples (B ∈ [20,000, 30,000]) → two‑pass join → all results.
- **Metrics**: disk I/O counted via VirtualDisk.readBlock/writeBlock.

## Project Structure

```
database-algorithms/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/dbproject/
                ├── Main.java
                ├── btree/
                │   ├── BPlusTree.java
                │   ├── BTreeTester.java
                │   ├── InternalNode.java
                │   ├── LeafNode.java
                │   ├── Node.java
                │   ├── TreeBuilder.java
                └── join/
                    ├── DiskBlock.java
                    ├── HashFunction.java
                    ├── HashJoin.java
                    ├── JoinTester.java
                    ├── JoinTuple.java
                    ├── MainMemory.java
                    ├── RelationGenerator.java
                    ├── Tuple.java
                    └── VirtualDisk.java
```