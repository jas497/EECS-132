rem java build and test

javac Node.java LinkedList.java LinkedListIterator.java
javac Entity.java Network.java PropagationStudy.java
javac -classpath junit-4.11.jar; Entity_Tester.java Network_Tester.java PropagationStudy_Tester.java LinkedList_Tester.java

pause
