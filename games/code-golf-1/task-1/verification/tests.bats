#!/usr/bin/env bats

@test "First test" {
  expected="$(cat test1.out)"
  run java -cp ../out ReverseInteger < test1.in
  [ "$expected" = "${lines[0]}" ]
}
