#!/usr/bin/env bats

cp=../../out/task-2
runcmd="java -cp $cp FindMatrixSum"

@test "First test" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Second test" {
  expected="$(cat test2.out)"
  run $runcmd < test2.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Third test" {
  expected="$(cat test3.out)"
  run $runcmd < test3.in
  [ "$expected" = "${lines[0]}" ]
}
