#!/usr/bin/env bats

cp=../../out/task-2
runcmd="java -cp $cp FindConnectedBlocks"

@test "First test" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}
