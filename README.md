1. Project Overview and Problem Statement

The Personal Finance Tracker is a Java desktop application designed to solve the real-world problem where many individuals struggle to understand where their money goes each month. Without an organized record of income and expenses, tracking spending habits and planning budgets becomes difficult.
This application provides an intuitive Graphical User Interface (GUI) for daily use, allowing users to effectively manage their finances.

Core Features 
•	Transaction Recording: Record income and expenses.
•	Categorization: Categorize transactions (e.g., Groceries, Salary).
•	Automatic Calculation: Automatically calculate totals (Income, Expense, Net Balance).
•	Data Visualization: Visualize spending breakdown using a pie chart.
•	Data Persistence: Persistent storage using file serialization.
•	Reporting: Generate a downloadable CSV report for detailed analysis.


2. System Design and Architecture
The system is designed with Object-Oriented Principles (OOP) and a modular approach, separating responsibilities into distinct classes.

2.1 Main Classes and Responsibilities 
Class	Primary Responsibility	OOP/Design Focus
Transaction	Stores transaction details (amount, category, date, type).	Data Storage, Encapsulation
FinanceManager	The business logic core. Calculates totals, net balance, and manages category breakdowns.	Calculations, Algorithmic Logic
DataManager	Handles all file operations: saving/loading (.ser) and exporting the CSV report.	Data Persistence, File I/O
FinanceTrackerGUI	The main Swing interface, managing user interactions and display.	Presentation Layer
PieChartPanel	Renders the graphical representation of expense breakdown.	Data Visualization
	
2.2 Data Flow 
When a user adds a new transaction, the data follows a clear sequential path:
1.	Input: User adds a transaction in the GUI.
2.	Processing: The transaction is passed to the FinanceManager. The FinanceManager appends it to an internal list, updates totals, and updates category breakdowns.
3.	Persistence: The transaction is persisted using the DataManager.
4.	Output: The GUI refreshes the display and pie chart.
Data flow direction: User Interface → FinanceManager → DataManager → Saved File (.ser).

3. Setup and User Guide
   
3.1 Requirements and Execution
•	Requirements: Java Development Kit (JDK) 8 or newer is required to compile and run the application.
•	Execution: The main entry point is the FinanceTrackerGUI class. The source code is provided in a zipped file labeled: "GroupNumber PersonalFinanceTracker.zip".

3.2 GUI Layout 
The interface is divided into four main areas:
Panel	Content
Top Panel	Add Transaction form (Description, Amount, Type, Category).
Center Panel	JTable displaying all transactions.
Bottom Panel	Summary totals (Income, Expense, Net Balance).
Right Panel	Pie chart and action buttons (Delete, Export CSV).

3.3 How to Use the Application
Action	Steps	Result
Add Transaction	1. Navigate to the Top Panel form. 2. Enter Description and Amount. 3. Select Type (Income/Expense) and Category. 4. Submit the transaction.	The transaction appears in the Center Panel table, and totals in the Bottom Panel update.
View Summary	Look at the Bottom Panel.	Displays current Income, Expense, and Net Balance.
Analyze Spending	View the Pie Chart in the Right Panel.	Visualizes the expense breakdown by category.
Delete Transaction	1. Select the desired row in the Center Panel JTable. 2. Click the Delete button in the Right Panel.	The row is removed, and all totals are recalculated and updated.
Export Data	Click the Export CSV button in the Right Panel.	A CSV file containing all transaction data is exported for external analysis.
