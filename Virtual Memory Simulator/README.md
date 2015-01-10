# Virtual Memory Simulator

This is a simulator for virtual memory that tests four different page fault algorithms, optimal, aging, not-recently-used, and random.

usage: python vmsim.py -n <numframes> -a <opt|aging|nru|rand> [-r <refresh>] <tracefile>       
The tracefile argument holds lines of memory addresses followed by either a W or an R for write or read, respectively.           
ex: 2286cd90 R          
It will then create a new entry in the page table by either using a free page or evicting one if memory is full.

I included an example tracefile, swim.trace.

**Requirements**            
Python 2.7