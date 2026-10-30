# MiniBank — Object-Oriented Banking Application (CEUE203)

MiniBank is a unified, production-grade console banking application developed in Java (JDK 17+) implementing **Part B of all Practicals (Labs 1 through 14)** in a single cohesive project.

---

## 🏛️ Comprehensive Mapping of Part B Across Practicals

| Practical | Core Requirements & Skills (Part B) | Implemented Classes / Files |
|---|---|---|
| **Practical 1** | Tools setup, `BankInfo` record header, `MenuOption` enum, numbered console menu shell with `Scanner`, switch expressions, invalid choice re-prompting, bank working hours. | [`BankInfo.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/BankInfo.java), [`MenuOption.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/MenuOption.java), [`MiniBank.java`](file:///e:/Java%20Practical/Minibank/src/minibank/MiniBank.java) |
| **Practical 2** | `Customer` class (encapsulation, private static ID generator `generateCustomerId` -> `CUST101`), `Account` base class (`AC0001` auto ID, constructor chaining with `this(ownerName, 0)`), `deposit` and `withdraw` modifying balance without public setter, rejecting negative amounts. | [`Customer.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Customer.java), [`Account.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Account.java) |
| **Practical 3** | Overriding `Object` methods: `toString()`, `equals()` and `hashCode()` comparing accounts by `accountNumber`. Public static nested class `Customer.Address`, `clone()` method implementing `Cloneable`, `instanceof` type checking, multi-line statement formatting. | [`Account.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Account.java), [`Customer.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Customer.java) |
| **Practical 4** | Regular expressions with compiled `Pattern` in `Validator` (`isValidMobile`, `isValidEmail`, `isValidPan`, `isValidIfsc`, `isValidAmount`), `TransactionType` enum, `Command` record, `CommandParser.parse()` using `split()`, `StatementFormatter` using `StringBuilder`. | [`Validator.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/Validator.java), [`Command.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Command.java), [`TransactionType.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/TransactionType.java), [`CommandParser.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/CommandParser.java), [`StatementFormatter.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/StatementFormatter.java) |
| **Practical 5** | Inheritance and Polymorphism: `Account` made abstract with `interestRate()` and `canWithdraw(amount)`. Subclasses `SavingsAccount` (4.0% interest, min balance rule), `CurrentAccount` (0.0% interest, overdraft limit rule), and `FixedDepositAccount` (7.0% interest, locked funds). Subclass constructors calling `super(...)`. Runtime polymorphism on `Account[]` array and pattern matching `instanceof`. | [`Account.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Account.java), [`SavingsAccount.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/SavingsAccount.java), [`CurrentAccount.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/CurrentAccount.java), [`FixedDepositAccount.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/FixedDepositAccount.java) |
| **Practical 6** | Interfaces: `Transactable`, `InterestBearing` with default methods `yearlyInterest()` and `monthlyInterest()`, `@FunctionalInterface WithdrawRule` used with both an anonymous inner class and a lambda expression, `Premium` marker interface, package structure (`model`, `util`, `service`, `exception`, `repository`), static import. | [`Transactable.java`](file:///e:/Java%20Practical/Minibank/src/minibank/interfaces/Transactable.java), [`InterestBearing.java`](file:///e:/Java%20Practical/Minibank/src/minibank/interfaces/InterestBearing.java), [`WithdrawRule.java`](file:///e:/Java%20Practical/Minibank/src/minibank/interfaces/WithdrawRule.java), [`Premium.java`](file:///e:/Java%20Practical/Minibank/src/minibank/interfaces/Premium.java) |
| **Practical 7** | Custom annotations with meta-annotations (`@Target`, `@Retention(RUNTIME)`): `@Id`, `@Positive`, `@MaxLength`. Annotating fields in `Account` and `Customer`. Reflection-based `AnnotationValidator.validate()` inspecting fields at runtime. `@Override` and `@FunctionalInterface`. | [`Id.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/annotation/Id.java), [`Positive.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/annotation/Positive.java), [`MaxLength.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/annotation/MaxLength.java), [`AnnotationValidator.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/AnnotationValidator.java) |
| **Practical 8** | Custom exception hierarchy: `BankException` (checked base), `InsufficientFundsException` (holding `shortfall`), `AccountNotFoundException`, `InvalidAmountException`, `DailyLimitExceededException`. Declaring `throws`, `try-catch-finally` in `transfer()`, try-with-resources using `AutoCloseable BankSession`. | [`BankException.java`](file:///e:/Java%20Practical/Minibank/src/minibank/exception/BankException.java), [`InsufficientFundsException.java`](file:///e:/Java%20Practical/Minibank/src/minibank/exception/InsufficientFundsException.java), [`BankSession.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/BankSession.java) |
| **Practical 9** | Multithreading & Synchronization: `AccountWorker` implementing `Runnable` to test concurrent deposits, `join()` on threads, demonstrating race condition when unsynchronized and fixing it with synchronized methods/blocks, inspecting thread lifecycle and names. | [`AccountWorker.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/AccountWorker.java), [`Account.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Account.java) |
| **Practical 10** | Concurrency engine: `TransactionProcessor` with managed thread pool (`Executors.newFixedThreadPool(4)`), `submit()` and `stop()`. Producer-Consumer coordination with `wait()` and `notifyAll()` in `ProducerConsumerBuffer`. Deadlock simulation and resolution through consistent lock ordering (locking smaller `accountNumber` first). | [`TransactionProcessor.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/TransactionProcessor.java), [`ProducerConsumerBuffer.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/ProducerConsumerBuffer.java) |
| **Practical 11** | Persistence & NIO: `Serializable` implementation with `serialVersionUID`, `StatePersister.save()` / `load()` using `ObjectOutputStream` / `ObjectInputStream`. NIO append-only `TransactionLog.append()` with `Files.write(CREATE, APPEND)`. `ReportGenerator` walking directory with `Files.newDirectoryStream`, reading lines via `BufferedReader`, computing financial totals, reading attributes via `Files.readAttributes`. | [`StatePersister.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/StatePersister.java), [`TransactionLog.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/TransactionLog.java), [`ReportGenerator.java`](file:///e:/Java%20Practical/Minibank/src/minibank/util/ReportGenerator.java) |
| **Practical 12** | Java Collections Framework: `AccountStore` with `HashMap<String, Account>`, `ArrayList<String>` transaction history, `sortedByBalance()` using `Comparator`, natural ordering with `Comparable<Account>` (`compareTo` by account number), thread-safe `ConcurrentHashMap`. | [`AccountStore.java`](file:///e:/Java%20Practical/Minibank/src/minibank/repository/AccountStore.java), [`Account.java`](file:///e:/Java%20Practical/Minibank/src/minibank/model/Account.java) |
| **Practical 13** | Generics and Reflection: Generic `Repository<T>` backing any entity (`Account`, `Customer`), finding key via `@Id` reflection in `save()`, `findById()`, `findAll()`, `delete()`, `@SafeVarargs saveAll(T... items)`. Bounded generic method `<N extends Number> double average(N[] values)`. | [`Repository.java`](file:///e:/Java%20Practical/Minibank/src/minibank/repository/Repository.java) |
| **Practical 14** | Capstone Assembly (Milestone 3): Layered architecture (`BankService` wiring UI -> Service -> Repository -> Concurrency -> NIO Persistence). Automatic data load on startup and save on exit. Concurrent batch processing. NIO report menu option. Unit tests suite. `@Audit` reflective logging. Runnable JAR packaging. | [`BankService.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/BankService.java), [`AuditLogger.java`](file:///e:/Java%20Practical/Minibank/src/minibank/service/AuditLogger.java), [`MiniBankTest.java`](file:///e:/Java%20Practical/Minibank/src/minibank/test/MiniBankTest.java), [`PracticalDemonstrator.java`](file:///e:/Java%20Practical/Minibank/src/minibank/test/PracticalDemonstrator.java) |

---

## 📁 Project Directory Structure

```
Minibank/
├── src/
│   └── minibank/
│       ├── MiniBank.java                    # Application Entry Point & Menu Navigation
│       ├── model/
│       │   ├── BankInfo.java                # Application Header (Java Record)
│       │   ├── MenuOption.java              # Main Menu Enum
│       │   ├── TransactionType.java         # DEPOSIT, WITHDRAW, TRANSFER Enum
│       │   ├── Command.java                 # Command Record
│       │   ├── Account.java                 # Abstract Base Account
│       │   ├── SavingsAccount.java          # Savings Account Implementation
│       │   ├── CurrentAccount.java          # Current Account with Overdraft
│       │   ├── FixedDepositAccount.java     # Fixed Deposit with Lock Rule
│       │   └── Customer.java                # Customer with nested static Address
│       ├── model/annotation/
│       │   ├── Id.java                      # @Id Marker Annotation
│       │   ├── Positive.java                # @Positive Rule Annotation
│       │   ├── MaxLength.java               # @MaxLength String Rule Annotation
│       │   └── Audit.java                   # @Audit Reflective Logging Annotation
│       ├── interfaces/
│       │   ├── Transactable.java            # Transactable Interface
│       │   ├── InterestBearing.java         # Interface with Default Methods
│       │   ├── WithdrawRule.java            # @FunctionalInterface for Lambdas
│       │   └── Premium.java                 # Marker Interface
│       ├── exception/
│       │   ├── BankException.java           # Base Checked Exception
│       │   ├── InsufficientFundsException.java # Exception with shortfall field
│       │   ├── AccountNotFoundException.java
│       │   ├── InvalidAmountException.java
│       │   └── DailyLimitExceededException.java
│       ├── repository/
│       │   ├── Repository.java              # Generic Repository<T> using @Id reflection
│       │   └── AccountStore.java            # HashMap, ConcurrentHashMap, Comparator sorting
│       ├── service/
│       │   ├── BankService.java             # Core Service Layer
│       │   ├── AccountWorker.java           # Runnable for Multithreading
│       │   ├── TransactionProcessor.java    # Managed Thread Pool & Deadlock Fix
│       │   ├── ProducerConsumerBuffer.java  # wait()/notifyAll() Coordination
│       │   ├── BankSession.java             # AutoCloseable for Try-With-Resources
│       │   └── AuditLogger.java             # Reflective Logger for @Audit
│       ├── util/
│       │   ├── Validator.java               # Regex Validation (Mobile, Email, PAN, IFSC)
│       │   ├── AnnotationValidator.java     # Reflection Validator for @Positive/@MaxLength
│       │   ├── CommandParser.java           # Command String Parser
│       │   ├── StatementFormatter.java      # StringBuilder Formatter
│       │   ├── StatePersister.java          # Object Serialization (Save/Load)
│       │   ├── TransactionLog.java          # Modern NIO Append-Only Logger
│       │   └── ReportGenerator.java         # NIO DirectoryStream & Attributes Report
│       └── test/
│           ├── PracticalDemonstrator.java   # Automated Runner for Labs 1 to 14
│           └── MiniBankTest.java            # Unit Test Suite with Assertions
├── bin/                                     # Compiled Bytecode
├── data/                                    # Serialized Accounts Database (accounts.dat)
├── logs/                                    # Audit Logs & Daily Reports
├── manifest.txt                             # JAR Manifest (Main-Class: minibank.MiniBank)
├── minibank.jar                             # Executable Standalone JAR
├── build.bat                                # Automated Build Script
├── run.bat                                  # Application Launcher Script
└── test.bat                                 # Test & Verification Runner Script
```

---

## 🚀 How to Build and Run

### Option 1: Using the Provided Batch Scripts (Windows)
1. **Compile and build JAR:**
   ```cmd
   build.bat
   ```
2. **Run the interactive MiniBank console:**
   ```cmd
   run.bat
   ```
3. **Run the complete Unit Test and Verification Suite (Practicals 1–14):**
   ```cmd
   test.bat
   ```

### Option 2: Using Standard JDK Commands
- **Compile all source files:**
  ```cmd
  javac -d bin -sourcepath src src/minibank/MiniBank.java src/minibank/*/*.java src/minibank/*/*/*.java
  ```
- **Run the Interactive Console:**
  ```cmd
  java -cp bin minibank.MiniBank
  ```
- **Run Automated Demonstration of Practicals 1 to 14:**
  ```cmd
  java -cp bin minibank.MiniBank --demo
  ```
- **Run Unit Tests:**
  ```cmd
  java -cp bin minibank.MiniBank --test
  ```
- **Execute via Runnable JAR:**
  ```cmd
  java -jar minibank.jar
  ```

---

## 🎯 Verification Highlights
- **Encapsulation & Safety**: No public setter for balance. Validates inputs before boundary passage.
- **Polymorphism**: `Account` array resolves interest rates and withdrawal rules dynamically.
- **Modern Concurrency**:
  - `AccountWorker` eliminates race conditions with synchronized critical sections.
  - Managed 4-thread pool via `TransactionProcessor`.
  - Producer-consumer bounded buffer coordinated using `wait()` and `notifyAll()`.
  - Consistent lock ordering (`accountA.compareTo(accountB) < 0`) eliminates deadlock in inter-account transfers.
- **Java NIO & Persistence**:
  - Automatic binary serialization to `data/accounts.dat`.
  - Append-only audit logging to `logs/transactions.log`.
  - Directory stream report generation computing deposit and withdrawal totals.
- **Generics & Reflection**:
  - Generic `Repository<T>` reads `@Id` by reflection and works seamlessly for both `Account` and `Customer`.
  - Reflective `AnnotationValidator` checks `@Positive` and `@MaxLength`.

