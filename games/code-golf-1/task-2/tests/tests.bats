#!/usr/bin/env bats

cp=../../out/task-2
runcmd="java -cp $cp FindMatrixSum"

@test "Small matrix" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Bigger values, sum equal to divisor" {
  expected="$(cat test2.out)"
  run $runcmd < test2.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Single cell" {
  expected="$(cat test3.out)"
  run $runcmd < test3.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Single column" {
  expected="$(cat test4.out)"
  run $runcmd < test4.in
  [ "$expected" = "${lines[0]}" ]
}
