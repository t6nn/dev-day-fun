#!/usr/bin/env bats

cp=../../out/task-1
runcmd="java -cp $cp ReverseInteger"

@test "Different types of values" {
  expected="$(cat test1.out)"
  run $runcmd < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Single value" {
  expected="$(cat test2.out)"
  run $runcmd < test2.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Bigger integers" {
  expected="$(cat test3.out)"
  run $runcmd < test3.in
  [ "$expected" = "${lines[0]}" ]
}