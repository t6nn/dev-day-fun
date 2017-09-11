#!/usr/bin/env bats

@test "First task-3 test" {
  expected="$(cat test1.out)"
  run java -cp ../out MissingNumber < test1.in
  [ "$expected" = "${lines[0]}" ]
}
