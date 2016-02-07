rem java build _all_ and test

javac Employee.java ISupervisor.java NoSuchEmployeeException.java 
javac SalariedEmployee.java HourlyEmployee.java SalesEmployee.java
javac SalariedSupervisor.java HourlySupervisor.java SalesSupervisor.java
javac EmployeeDatabase.java 

javac -classpath junit-4.11.jar;  EmployeeDatabase_Tester.java GeneralEmployee_Tester.java

pause
