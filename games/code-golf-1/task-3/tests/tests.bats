#!/usr/bin/env bats

cp=../../out/task-3
runcmd="java -cp $cp Task3"

@test "First test" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}
