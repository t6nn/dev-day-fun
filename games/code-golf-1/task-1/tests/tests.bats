#!/usr/bin/env bats

cp=../../out/task-1
runcmd="java -cp $cp ReverseInteger"

@test "First test" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}
