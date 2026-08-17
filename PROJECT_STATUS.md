# Software DNA — Project Status

## Current Date
2026-08-17

---

# 1. PROJECT GOAL

Software DNA analyzes a software repository and builds a
persistent, queryable structural representation of the codebase.

Current target language:
- Java

Current parser:
- JavaParser

---

# 2. ROADMAP

## Phase 1 — MVP
Status: COMPLETED

- [x] Repository scanning
- [x] Java parsing
- [x] Package extraction
- [x] Class extraction
- [x] Interface extraction
- [x] Enum extraction
- [x] Record extraction
- [x] Method extraction
- [x] Constructor extraction
- [x] Field extraction
- [x] Import extraction
- [x] Annotation extraction

---

## Phase 2 — Repository Understanding
Status: COMPLETED

### Entity / Relationship Extraction
- [x] Entity IDs
- [x] Entity references
- [x] Relationship extraction
- [x] Type resolution
- [x] Dependency extraction
- [x] Method call extraction
- [x] Inheritance extraction
- [x] Interface implementation extraction

### Knowledge Graph
- [x] GraphNode
- [x] GraphEdge
- [x] KnowledgeGraph
- [x] KnowledgeGraphBuilder
- [x] NodeBuilder
- [x] EdgeBuilder

### Graph Relationships
- [x] DECLARES
- [x] HAS_METHOD
- [x] HAS_FIELD
- [x] HAS_CONSTRUCTOR
- [x] DEPENDS_ON
- [x] CALLS
- [x] EXTENDS
- [x] IMPLEMENTS

### Graph Query Engine
- [x] Dependencies
- [x] Dependents
- [x] Callees
- [x] Callers
- [x] Subclasses
- [x] Superclass
- [x] Implementations
- [x] Implemented interfaces

### Impact Analysis
- [x] Direct dependency impact
- [x] Method-call impact
- [x] Containment-aware impact

### Code Metrics
- [x] LOC
- [x] Parameter count
- [x] Local variables
- [x] Method calls
- [x] Object creations
- [x] Return statements
- [x] Cyclomatic complexity
- [x] Nesting depth
- [x] Loop count
- [x] Conditional count

### Architecture Metrics
- [x] Fan-In
- [x] Fan-Out
- [x] CBO
- [x] DIT
- [x] NOC
- [x] RFC

---

## Phase 3 — Architecture Recovery
Status: NOT STARTED

- [ ] Layer detection
- [ ] MVC detection
- [ ] Architecture style classification
- [ ] Architecture violation detection

---

## Phase 4 — Knowledge Graph
Status: NOT STARTED

- [ ] Neo4j setup
- [ ] Neo4j schema
- [ ] Graph export
- [ ] Persistent graph storage
- [ ] Cypher queries
- [ ] Replace / complement in-memory graph

---

## Phase 5 — LLM Integration
Status: NOT STARTED

- [ ] Natural language query
- [ ] Query → graph traversal
- [ ] Subgraph retrieval
- [ ] LLM explanation
- [ ] Query validation
- [ ] Hallucination evaluation

---

## Phase 6 — Visualization
Status: NOT STARTED

- [ ] Graph visualization
- [ ] Class diagram
- [ ] Dependency graph
- [ ] Call graph
- [ ] Architecture visualization
- [ ] Complexity heatmap

---

## Phase 7 — Incremental Updates
Status: NOT STARTED

- [ ] Git diff detection
- [ ] Changed entity detection
- [ ] Reverse dependency traversal
- [ ] Partial graph recomputation
- [ ] Incremental Neo4j update

---

# 3. CURRENT VERIFIED OUTPUT

Repository:

sample_projects

Files parsed:

22

Knowledge Graph:

Nodes: 45
Edges: 51

---

# 4. VERIFIED GRAPH QUERIES

StudentService dependencies:

Student
Course
Teacher

Student dependents:

StudentService

Student.study() callees:

Teacher.teach()

Teacher.teach() callers:

Student.study()

Animal subclasses:

Mammal

Mammal superclass:

Animal

Report implementations:

Printable

Printable implementations:

Report

---

# 5. VERIFIED IMPACT ANALYSIS

Changing Student:

StudentService

Changing Teacher.teach():

Student.study()

Containment-aware impact of Teacher:

Teacher.teach()
Student
StudentService
Student.study()

---

# 6. VERIFIED ARCHITECTURE METRICS

StudentService:

Fan-Out: 3
Fan-In: 0
CBO: 3
DIT: 0
NOC: 0
RFC: 1

Teacher:

Fan-Out: 0
Fan-In: 2
CBO: 0
DIT: 0
NOC: 0
RFC: 0

---

# 7. IMPORTANT EXISTING CLASSES

Knowledge graph:

- KnowledgeGraph
- GraphNode
- GraphEdge
- KnowledgeGraphBuilder
- NodeBuilder
- EdgeBuilder

Queries:

- KnowledgeGraphQuery

Impact analysis:

- ImpactAnalyzer

Application:

- ParserApplication

---

# 8. CURRENT ARCHITECTURE

Repository
    ↓
RepositoryParser
    ↓
RepositoryModel
    ↓
RepositoryAnalyzer
    ↓
KnowledgeGraphBuilder
    ↓
NodeBuilder + EdgeBuilder
    ↓
KnowledgeGraph
    ↓
KnowledgeGraphQuery
    ↓
ImpactAnalyzer

---

# 9. CURRENT NEXT TASK

NEXT FEATURE:

Neo4j integration

First tasks:

1. Add Neo4j dependency
2. Configure Neo4j connection
3. Design Neo4j schema
4. Create Neo4j exporter
5. Export existing 45 nodes
6. Export existing 51 edges
7. Verify Neo4j graph
8. Recreate graph queries using Cypher

---

# 10. DO NOT REDO

Do NOT recreate:

- Repository parser
- Entity extraction
- Relationship extraction
- KnowledgeGraph
- GraphNode
- GraphEdge
- NodeBuilder
- EdgeBuilder
- Graph queries
- ImpactAnalyzer
- Code metrics
- Architecture metrics

These are already implemented and verified.

---

# 11. RULE FOR FUTURE DEVELOPMENT

Before implementing a feature:

1. Check PROJECT_STATUS.md
2. Check existing source files
3. Check current output
4. Only implement missing functionality
5. Do not create duplicate analyzers/classes
6. Update PROJECT_STATUS.md after completion