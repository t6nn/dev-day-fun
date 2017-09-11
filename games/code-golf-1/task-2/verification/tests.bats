#!/usr/bin/env bats

@test "First task-2 test" {
  expected="$(cat test1.out)"
  run java -cp ../out FindMatrixSum < test1.in
  [ "$expected" = "${lines[0]}" ]
}
