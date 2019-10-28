LIBPATH   = /eecs/dept/course/2019-20/F/4411/bufmgr/lib/bufmgrAssign.jar
CLASSPATH = ../:$(LIBPATH):
JAVAC     = /eecs/local/bin/javac -classpath $(CLASSPATH)
JAVA      = /eecs/local/bin/java  -classpath $(CLASSPATH)

PROGS = BMTest

all: $(PROGS)

# test buffer manager

BMTest:BMTest.java
	$(JAVAC) TestDriver.java BMTest.java

bmtest: BMTest
	$(JAVA) tests.BMTest


xx:
	jar tf $(LIBPATH)

clean:
	\rm -f *.class *~ \#* core TRACE
