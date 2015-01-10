"""
Mark Silvis
Virtual memory simulator
"""


# imports
import sys
import os.path
import random
from collections import deque

# classes
class Memory:
    """ Physical memory (RAM) """
    def __init__(self, frames):
        self.frametable = [None]*frames     # frame table
        self.count = 0                      # frame count
        random.seed()
    def add(self, page):
        """ Add frame to memory """
        self.frametable[page.framenum] = page
        page.valid = 1
        page.referenced = 1
        self.count += 1
        try:
            for i in page.age:
                i = 0
        except:
            pass
    def fault(self, algorithm, page, frames, linenum):
        """ Memory is full and a page must be evicted """
        global writes
        if algorithm == 'rand':
            """ random eviction algorithm """
            index = random.randrange(0, frames)
            evicted = self.frametable[index]
            page.framenum = index
        elif algorithm == 'opt':
            """ optimal eviction algorithm """
            furthest = linenum
            for frame in self.frametable:
                if frame != None and len(frame.opt)>0:
                    evicted = frame
                    if frame.opt[0] <= linenum:
                        frame.opt.popleft()
                    elif frame.opt[0] > furthest:
                        furthest = frame.opt.popleft()
                    else:
                        continue
            page.framenum = evicted.framenum
        elif algorithm == 'aging':
            """ aging eviction algorithm """
            minage = float('inf')
            evicted = None
            for frame in self.frametable:
                age = ''.join(map(str, frame.age))
                age = int(age)
                if age < minage:
                    minage = age
                    evicted = frame
            page.framenum = evicted.framenum
        elif algorithm == 'nru':
            """ not recently used eviction algorithm """
            evicted = None
            for frame in self.frametable:
                if frame.referenced == 0:
                    if frame.dirty == 0:
                        evicted = frame
                        break
                    evicted = frame
            # no unreferenced frames - evict random
            if evicted == None:
                algorithm = 'rand'
                ret = self.fault(algorithm, page, frames, linenum)
                algorithm = 'nru'
                return ret
            page.framenum = evicted.framenum
        if evicted.dirty == 1:
            writes += 1
            ret = 'dirty'
        else:
            ret = 'clean'
        evicted.evict()
        self.add(page)
        self.count -= 1
        return ret

class PageTable:
    """ Virtual memory (Page Table) """
    def __init__(self):
        self.table = {}             # page table
    def add(self, page):
        """ add page to page table """
        self.table[page.pagenum] = page
        
class Page:
    """ An entry in the page table """
    def __init__(self, pagenum, mode):
        self.pagenum = pagenum
        self.framenum = None
        if mode == 'W':
            self.dirty = 1
        else:
            self.dirty = 0
        self.referenced = 1
        self.valid = 0
    def evict(self):
        """ reset page variables after eviction """
        self.framenum = None
        self.referenced = 0
        self.valid = 0
    def optList(self):
        """ if opt algorithm is chosen, create a deque to store all locations of memory reference """
        self.opt = deque()
    def aging(self):
        """ if aging algorithm is chosen, create a deque to store the referenced bits on refresh """
        # initialize age deque to size 8 filled with 0's
        self.age = deque()
        i = 0
        while i < 8:
            self.age.append(0)
            i += 1


# set up
# opt/rand
if len(sys.argv) == 6:
    if sys.argv[1] == '-n' and sys.argv[3] == '-a' and sys.argv[5] != '-r' and (sys.argv[4] == 'opt' or sys.argv[4] == 'rand'):
        frames = sys.argv[2]
        algorithm = sys.argv[4]
        tracefile = sys.argv[5]
    # usage error
    else:
        sys.exit("Usage: python vmsim.py -n <numframes> -a <opt|aging|nru|rand> [-r <refresh>] <tracefile>\nrefresh required for nru and aging algorithms")
# aging/nru
elif len(sys.argv) == 8:
    if sys.argv[1] == '-n' and sys.argv[3] == '-a' and sys.argv[5] == '-r' and (sys.argv[4] == 'aging' or sys.argv[4] == 'nru'):
        frames = sys.argv[2]
        algorithm = sys.argv[4]
        refresh = int(sys.argv[6])
        tracefile = sys.argv[7]
    # usage error
    else:
        sys.exit("Usage: python vmsim.py -n <numframes> -a <opt|aging|nru|rand> [-r <refresh>] <tracefile>\nrefresh required for nru and aging algorithms")
# usage error
else:
    sys.exit("Usage: python vmsim.py -n <numframes> -a <opt|aging|nru|rand> [-r <refresh>] <tracefile>\nrefresh required for nru and aging algorithms")


# variables
frames = int(frames)
accesses = 0
faults = 0
global writes
writes = 0


# check if file exists
if(not os.path.isfile(tracefile)):
    sys.exit("File not found")

trace = open(tracefile, 'r')
memory = Memory(frames)
pagetable = PageTable()

# preprocess file for optimal algorithm
if algorithm == 'opt':
    linenum = 1
    for line in trace:
        split = line.split(' ')
        addr = split[0]
        addr = addr[:5]
        mode = split[1][:1]
        # already exists in pagetable
        if addr in pagetable.table:
            currpage = pagetable.table[addr]
            currpage.opt.append(linenum)
        # not in page table, must be added
        else:
            currpage = Page(addr, mode)
            currpage.optList()
            currpage.opt.append(linenum)
            pagetable.add(currpage)
        linenum +=1
    # return file pointer to beginning
    trace.seek(0)


# execute simulation
linenum = 1
for line in trace:
    # reset referenced bit for nru and aging
    if algorithm == 'nru' or algorithm == 'aging':
        if (linenum % refresh) == 0:
            for frame in memory.frametable:
                if not frame == None:
                    if algorithm == 'aging':
                        # append referenced bit to beginning
                        frame.age.appendleft(frame.referenced)
                        frame.age.pop()
                    frame.referenced = 0
    accesses += 1
    split = line.split(' ')
    addr = split[0]
    addr = addr[:5]
    mode = split[1][:1]
    # check if page exists in page table
    if addr in pagetable.table:
        currpage = pagetable.table[addr]
        if mode == 'W':
            currpage.dirty = 1
        else:
            currpage.dirty = 0
        # check if page exists in physical memory
        if currpage.valid == 1:
            # hit
            currpage.referenced = 1
            #print 'Hit'
        else:
            # check if memory is full
            if memory.count == frames:
                # page fault with eviction
                faults += 1
                #print 'Page fault - evict '
                memory.fault(algorithm, currpage, frames, linenum)
            else:
                # page fault without eviction (page already exists, but isn't in frame)
                # should only happen when opt page replacement algorithm is chosen
                currpage.framenum = memory.count
                memory.add(currpage)
                faults += 1
                #print 'Page fault - no eviction'
    # page not in table
    else:
        # create page
        newpage = Page(addr, mode)
        if algorithm == 'aging':
            newpage.aging()
        # check if memory is full
        if memory.count == frames:
            # page fault with eviction
            faults += 1
            #print 'Page fault - evict '
            memory.fault(algorithm, newpage, frames, linenum)
        else:
            # page fault without eviction (new page)
            newpage.framenum = memory.count
            pagetable.add(newpage)
            memory.add(newpage)
            faults += 1
            #print 'Page fault - no eviction'

    linenum += 1

# print stats
print ''
print "Number of frames:\t" + str(frames)
print "Total memory accesses:\t" + str(accesses)
print "Total page faults:\t" + str(faults)
print "Total writes to disk:\t" + str(writes)

sys.exit(0)