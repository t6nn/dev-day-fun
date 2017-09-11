#!/usr/bin/env bash

projectdir=$PWD
builddir=$projectdir/out
srcdir=$projectdir/src
testdir=$projectdir/tests
bats_cmd=$projectdir/bats/bats

mkdir -p $builddir/task-1 $builddir/task-2 $builddir/task-3

cd $srcdir

javac -d $builddir/task-1 ReverseInteger.java
javac -d $builddir/task-2 FindMatrixSum.java
javac -d $builddir/task-3 MissingNumber.java

cd $testdir/task-1 && $bats_cmd tests.bats
cd $testdir/task-2 && $bats_cmd tests.bats
cd $testdir/task-3 && $bats_cmd tests.bats