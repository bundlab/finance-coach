# AI-Powered Personal Finance Coach

A lightweight, terminal-based Java application designed to help users manage their personal finances using the **50/30/20 budgeting rule**. The application provides smart financial tracking, automated categorization, and real-time AI-style coaching.

---

## Features

- Categorized tracking of Income, Needs, Wants, and Savings
- Real-time financial analysis and coaching based on the 50/30/20 rule
- Persistent storage of all transactions using CSV
- Clean, interactive terminal interface
- Built with professional project structure using Maven

---

## Tech Stack

- **Language**: Java 17+
- **Build Tool**: Apache Maven
- **Storage**: Flat-file CSV
- **Environment**: Linux / macOS / Windows (Terminal)

---

## 📁 Project Structure
```text
finance-coach/
├── src/main/java/com/financecoach/
│   ├── Main.java
│   ├── engine/          # Business logic & AI analysis
│   └── model/           # Data models (Transaction, Budget, etc.)
├── src/main/resources/
│   └── finance_data.csv     # Persistent storage
├── pom.xml
├── README.md
└── CONTRIBUTING.md
```
---

## Installation & Setup

### Prerequisites

Ensure you have **OpenJDK 17** (or higher) and **Maven** installed.

On Ubuntu/Debian:
```bash
sudo apt update
sudo apt install openjdk-17-jdk 

# Clone and Run
git clone https://github.com/bundlab/finance-coach.git
cd finance-coach

# Build the project
mvn clean compile

# Run the application
mvn exec:java