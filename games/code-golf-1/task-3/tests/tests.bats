#!/usr/bin/env bats

cp=../../out/task-3
runcmd="java -cp $cp MissingNumber"

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
