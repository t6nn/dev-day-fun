#!/usr/bin/env bats

@test "Simple test case" {
  expected="$(cat test1.out)"
  run java -cp ../out MissingNumber < test1.in
  [ "$expected" = "${lines[0]}" ]
}

@test "Many values" {
  expected="$(cat test2.out)"
  run java -cp ../out MissingNumber < test2.in
  [ "$expected" = "${lines[0]}" ]
}
