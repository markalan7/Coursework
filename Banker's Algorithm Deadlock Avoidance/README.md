# Banker's Algorithm Deadlock Avoidance

This is a resource and deadlock avoidance simulator using Banker's algorithm. This project required multithreading via POSIX threads, requiring me to account for race conditions and synchronization using mutex locks.

**Usage**         
compile:    gcc -o bankers driver.c process.c banker.c -lpthread      
run:        ./bankers -n &lt;number_of_processes&gt; -a &lt;availability_vector&gt;

**Warning**
This was originally compiled using gcc version 3.4.2. 