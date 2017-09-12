#!/usr/bin/env bats

cp=../../out/task-3
runcmd="java -cp $cp MissingNumber"

@test "Small series" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Long series of equal value" {
  expected="$(cat test2.out)"
  run $runcmd < test2.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Single value" {
  expected="$(cat test3.out)"
  run $runcmd < test3.in
  [ "$expected" = "${lines[0]}" ]
}
